package apolo.business.service.impl;

import apolo.business.model.FileContent;
import apolo.business.model.InstallFormModel;
import apolo.business.service.EmailService;
import apolo.business.service.FileService;
import apolo.business.service.TenantService;
import apolo.business.service.UserService;
import apolo.common.config.model.ApplicationProperties;
import apolo.common.exception.AccessDeniedException;
import apolo.common.exception.BusinessException;
import apolo.common.util.ApoloCrypt;
import apolo.common.util.MessageBundle;
import apolo.data.enums.Skin;
import apolo.data.enums.Status;
import apolo.data.enums.UserStatus;
import apolo.data.model.Tenant;
import apolo.data.model.User;
import apolo.data.model.UserCustomFieldValue;
import apolo.data.model.UserGroup;
import apolo.data.repository.UserGroupRepository;
import apolo.data.repository.UserRepository;
import apolo.security.CurrentUser;
import apolo.security.UserPermission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.*;

@Service("userService")
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {

	private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

	@Inject
	private UserRepository userRepository;

	@Inject
	private UserGroupRepository userGroupRepository;

	@Inject
	private FileService<User> fileService;

	@Inject
	private ApplicationProperties applicationProperties;

	@Inject
	private TenantService tenantService;

	@Inject
	private ApoloCrypt apoloCrypt;

	@Inject
	@Named("smtpEmailService")
	private EmailService emailService;

	public List<User> list(Tenant tenant) {
		PageRequest request = new PageRequest(1, PAGE_SIZE, Sort.Direction.ASC, "name");

		Page<User> result = userRepository.findByTenantAndUserStatusNotOrderByNameAsc(
				tenant,
				UserStatus.LOCKED,
				request
		);

		return result.getContent();
	}

	public Page<User> list(Tenant tenant, Integer pageNumber) {
		if (pageNumber < 1) {
			pageNumber = 1;
		}

		PageRequest request = new PageRequest(pageNumber - 1, PAGE_SIZE, Sort.Direction.ASC, "name");

		Page<User> result = userRepository.findByTenantAndUserStatusNotOrderByNameAsc(
				tenant,
				UserStatus.LOCKED,
				request
		);

		return result;
	}

	public Page<User> listAll(Integer pageNumber) {
		if (pageNumber < 1) {
			pageNumber = 1;
		}

		PageRequest request = new PageRequest(pageNumber - 1, PAGE_SIZE, Sort.Direction.ASC, "name");

		Page<User> result = userRepository.findAll(
				request
			);

		return result;
	}

	public User find(Long id) {
		return userRepository.findOne(id);
	}

	@Override
	public int increaseSignInCounter(User user, String userIPAddress) {
		int result = 0;

		if (user != null) {
			if (user.getSignInCount() != null) {
				user.setSignInCount(user.getSignInCount() + 1);
			} else {
				user.setSignInCount(1);
			}

			user.setLastSignInAt(user.getCurrentSignInAt());
			user.setLastSignInIp(user.getCurrentSignInIp());

			user.setCurrentSignInAt(new Date());
			user.setCurrentSignInIp(userIPAddress);

			result = user.getSignInCount();

			if (user.getResetPasswordToken() != null) {
				user.setResetPasswordToken(null);
				user.setResetPasswordSentAt(null);
			}

			this.save(user);
		}

		return result;
	}

	@Override
	public long count() {
		long result = 0L;
		if (getAuthenticatedUser() != null
				&& getAuthenticatedUser().getPermissions().contains(UserPermission.ADMIN)) {
			result = userRepository.count();
		}

		return result;
	}

	@Override
	public long countByTenant(Tenant tenant) {
		long result = 0L;
		if (getAuthenticatedUser() != null
				&& getAuthenticatedUser().getPermissions().contains(UserPermission.ADMIN)) {
			result = userRepository.countByTenant(tenant);
		}

		return result;
	}

	@Override
	public void generateResetPasswordToken(String serverUrl, String email) {
		if (email != null
				&& !"".equals(email)) {
			User user = this.findByLogin(email);

			if (user != null) {
				try {
					String token = apoloCrypt.encode(
							user.getEmail(),
							user.getDbTenant().getName() + user.getName(),
							applicationProperties.getIvKey()
					);

					user.setResetPasswordToken(token);
					user.setResetPasswordSentAt(new Date());

					user = this.save(user);

					emailService.sendAsync(
							user.getDbTenant(),
							user.getDbTenant().getName(),
							user.getDbTenant().getEmailFrom(),
							user.getName(),
							user.getEmail(),
							MessageBundle.getMessageBundle("user.forgot-password.email.subject"),
							this.buildResetPasswordMessage(
									serverUrl + "reset-password/",
									user
							).toString()
					);
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
		}
	}

	@Override
	public User findByToken(String token) {
		User user = null;

		user = userRepository.findByResetPasswordToken(token);

		if (user != null
				&& user.getResetPasswordSentAt() != null) {
			Calendar now = GregorianCalendar.getInstance();
			now.setTime(new Date());

			Calendar resetAt = GregorianCalendar.getInstance();
			resetAt.setTime(user.getResetPasswordSentAt());
			resetAt.add(Calendar.MINUTE, 60);

			if (resetAt.before(now)) {
				user.setResetPasswordToken(null);
				user.setResetPasswordSentAt(null);

				this.save(user);

				user = null;
			}
		} else {
			user = null;
		}

		return user;
	}

	public User find(Tenant tenant, Long id) {
		return userRepository.findByTenantAndId(tenant, id);
	}

	public User findByLogin(String login) {
		return userRepository.findUserByEmail(login);
	}

	public User loadByUsernameAndPassword(String username, String password) {
		User user = null;
		try {
			user = userRepository.findByEmailAndPassword(
					username,
					apoloCrypt.encode(password)
			);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			String message = MessageBundle.getMessageBundle("user.msg.password.encrypt.error");
			throw new BusinessException(message);
		}

		if (user == null) {
			String message = MessageBundle.getMessageBundle("user.msg.no.data.found");
			throw new UsernameNotFoundException(message);
		}

		return user;
	}

	@Transactional
	public User save(User entity) {
		if (entity != null) {
			entity.setUpdatedBy(getAuthenticatedUser());
			entity.setUpdatedAt(new Date());
		}

		return save(null, entity, false, null);
	}

	@Transactional
	public User save(String serverUrl, User user, boolean changePassword, FileContent file) {
		if (UserStatus.ADMIN.equals(user.getStatus())
				&& !user.getPermissions().contains(UserPermission.ADMIN)) {
			String message = MessageBundle.getMessageBundle("user.edit.msg.error.admin.permission");
			throw new BusinessException(message);
		}

		boolean sendEmail = false;

		if (user.getId() == null) {
			try {
				String token = apoloCrypt.encode(
						user.getEmail(),
						user.getDbTenant().getName() + user.getName(),
						applicationProperties.getIvKey()
				);

				user.setResetPasswordToken(token);
				user.setResetPasswordSentAt(new Date());

				sendEmail = true;
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}

		if (changePassword) {
			Md5PasswordEncoder encoder = new Md5PasswordEncoder();
			try {
				user.setPassword(
						apoloCrypt.encode(user.getPassword())
				);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				String message = MessageBundle.getMessageBundle("user.msg.password.encrypt.error");
				throw new BusinessException(message);
			}
		} else {
			User dbUser = this.find(user.getId());
			user.setPassword(dbUser.getPassword());
		}

		if (file != null) {
			if (file != null
					&& file.getFile() != null
					&& file.getFile().getOriginalFilename() != null
					&& !file.getFile().getOriginalFilename().isEmpty()) {

				if (user.getId() == null) {
					userRepository.saveAndFlush(user);
				}

				user.setAvatarOriginalName(file.getFile().getOriginalFilename());
				try {
					user.setAvatarFileName(
							fileService.uploadFile(
									user.getTenant(),
									user,
									file,
									"picture",
									file.getFile().getInputStream()
							)
					);

					user.setAvatarFileSize(file.getFile().getSize());
					user.setAvatarContentType(file.getFile().getContentType());
					user.setAvatarUpdatedAt(new Date());
				} catch (IOException e) {
					String message = MessageBundle.getMessageBundle("commons.errorUploadingFile");
					throw new BusinessException(message);
				}
			}
		}

		if (user.getCustomFields() != null && !user.getCustomFields().isEmpty()) {
			for (UserCustomFieldValue field : user.getCustomFields()) {
				field.setUser(user);
			}
		}

		if (UserStatus.LOCKED.equals(user.getStatus())) {
			user.setEnabled(false);
		} else {
			user.setEnabled(true);
		}

		user = userRepository.save(user);

		if (sendEmail) {
			try {
				emailService.sendAsync(
						user.getDbTenant(),
						user.getDbTenant().getName(),
						user.getDbTenant().getEmailFrom(),
						user.getName(),
						user.getEmail(),
						MessageBundle.getMessageBundle("user.new.email.subject"),
						this.buildCreateUserMessage(
								serverUrl + "reset-password/",
								user
						).toString()
				);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}

		return user;
	}

	@Transactional
	public void remove(User user) {
		if (!user.getStatus().isChangeable()) {
			String message = MessageBundle.getMessageBundle("user.remove.msg.error.admin");
			throw new BusinessException(message);
		}

		userRepository.delete(user);
	}

	public Collection<GrantedAuthority> loadUserAuthorities(User user) {
		Collection<GrantedAuthority> result = new ArrayList<GrantedAuthority>();

		if (user != null
				&& user.getGroups() != null
				&& !user.getGroups().isEmpty()) {
			for (UserGroup group : user.getGroups()) {
				if (group != null
						&& (group.getStatus().equals(UserStatus.ADMIN) || group.getStatus().equals(UserStatus.ACTIVE))
						&& group.getPermissions() != null
						&& !group.getPermissions().isEmpty()) {
					for (UserPermission permission : group.getPermissions()) {
						result.add(new SimpleGrantedAuthority(permission.getAttribute()));
					}
				}
			}

		}

		return result;
	}

	public Page<User> search(Tenant tenant, Integer pageNumber, String param) {
		if (pageNumber < 1) {
			pageNumber = 1;
		}

		PageRequest request = new PageRequest(pageNumber - 1, PAGE_SIZE, Sort.Direction.ASC, "name");

		if (param != null) {
			param = "%" + param + "%";
		}

		Page<User> result = userRepository.findByTenantAndNameLikeOrEmailLikeOrderByNameAsc(
				tenant,
				param,
				param,
				request
		);

		return result;
	}

	public User lock(User user) {
		if (!user.getStatus().isChangeable()) {
			String message = MessageBundle.getMessageBundle("error.403.4");
			throw new AccessDeniedException(4, message);
		} else if(getAuthenticatedUser().equals(user)) {
			String message = MessageBundle.getMessageBundle("error.403.5");
			throw new AccessDeniedException(5, message);
		}

		User dbUser = this.find(user.getId());
		user.setPassword(dbUser.getPassword());
		user.setStatus(UserStatus.LOCKED);
		user.setEnabled(false);

		return userRepository.save(user);
	}

	public User unlock(User user) {
		if (!user.getStatus().isChangeable()) {
			String message = MessageBundle.getMessageBundle("error.403.4");
			throw new AccessDeniedException(4, message);
		}

		User dbUser = this.find(user.getId());
		user.setPassword(dbUser.getPassword());
		user.setStatus(UserStatus.ACTIVE);
		user.setEnabled(true);

		return userRepository.save(user);
	}

	public Page<User> listLocked(Tenant tenant, Integer pageNumber) {
		if (pageNumber < 1) {
			pageNumber = 1;
		}

		PageRequest request = new PageRequest(pageNumber - 1, PAGE_SIZE, Sort.Direction.ASC, "name");

		return userRepository.findByTenantAndUserStatus(tenant, UserStatus.LOCKED, request);
	}

	public User getSystemAdministrator() {
		User user = null;

		List<User> userList = userRepository.findByUserStatus(UserStatus.ADMIN);

		if (userList != null && !userList.isEmpty()) {
			user = userList.get(0);
		}

		return user;
	}

	@Transactional
	public boolean systemSetup(String serverUrl, InstallFormModel formModel, FileContent file) {
		boolean result = false;

		Collection<GrantedAuthority> authorities = loadUserAuthorities(formModel.getUser());

		new CurrentUser(
				1L,
				formModel.getUser().getEmail(),
				formModel.getUser().getPassword().toLowerCase(),
				formModel.getUser(),
				authorities
		);

		Tenant tenant = tenantService.findByUrl(applicationProperties.getDefaultTenant());

		if (tenant == null) {
			tenant = new Tenant();

			tenant.setName(applicationProperties.getDefaultTenant());
			tenant.setLogoHeight(20);
			tenant.setLogoWidth(15);
			tenant.setUrl(applicationProperties.getDefaultTenant());
			tenant.setStatus(Status.ACTIVE);

			tenant.setSkin(Skin.SKIN_DEFAULT);

			tenant.setEmailFrom(applicationProperties.getEmailFrom());
			tenant.setEmailUsername(applicationProperties.getEmailUsername());
			tenant.setEmailPassword(applicationProperties.getEmailPassword());
			tenant.setSmtpHost(applicationProperties.getSmtpHost());
			tenant.setSmtpPort(applicationProperties.getSmtpPort());
			tenant.setUseTLS(applicationProperties.getUseTLS());

			formModel.getUser().setTenant(tenant);

			tenantService.save(tenant);
		}


		if (formModel.getUser() != null) {
			// Create system administrator
			User dbUser = null;
			if (formModel.getUser().getEmail() != null && !"".equals(formModel.getUser().getEmail())) {
				dbUser = findByLogin(formModel.getUser().getEmail());
			}

			if (dbUser == null) {
				UserGroup adminUserGroup = null;

				// Create admin group
				try {
					adminUserGroup = userGroupRepository.findByName(MessageBundle.getMessageBundle("user.permission.ADMIN"));
				} catch (Throwable e) {
					/*
					 * If is the really first time that you are using application, this line will throw a error
					 * In other cases, this error can´t occur
					 */
					LOG.error(e.getMessage(), e);
				}

				if (adminUserGroup == null
						|| (adminUserGroup.getPermissions() != null
						&& !adminUserGroup.getPermissions().contains(UserPermission.ADMIN))
						) {
					adminUserGroup = new UserGroup();

					adminUserGroup.setName(MessageBundle.getMessageBundle("user.permission.ADMIN"));
					adminUserGroup.setStatus(UserStatus.ADMIN);

					Set<UserPermission> perms = new HashSet<UserPermission>();
					perms.add(UserPermission.ADMIN);

					adminUserGroup.setPermissions(perms);

					adminUserGroup.setCreatedBy(formModel.getUser());
					adminUserGroup.setCreatedAt(new Date());
					adminUserGroup.setTenant(tenant);

					adminUserGroup = userGroupRepository.save(adminUserGroup);
				}

				// Admin groups
				Set<UserGroup> adminGroups = new HashSet<UserGroup>();
				adminGroups.add(adminUserGroup);

				formModel.getUser().setGroups(adminGroups);

				formModel.getUser().setStatus(UserStatus.ADMIN);
				formModel.getUser().setEnabled(true);

				formModel.getUser().setCreatedBy(formModel.getUser());
				formModel.getUser().setCreatedAt(new Date());
				
				/*
				 * Save system administrator and get your ID
				 */
				dbUser = this.save(serverUrl, formModel.getUser(), true, file);
				
				/*
				 * Save again to tell to the system that the administrator create yourself
				 */
				formModel.getUser().setCreatedBy(dbUser);

				formModel.getUser().setTenant(tenant);

				this.save(serverUrl, dbUser, false, null);
				
				/*
				 * Save the group again to associate administrator user as owner 
				 */
				adminUserGroup.setCreatedBy(dbUser);
				userGroupRepository.save(adminUserGroup);

				result = true;
			} else {
				dbUser.setStatus(UserStatus.ADMIN);
				dbUser.setPassword(formModel.getUser().getPassword());

				this.save(serverUrl, dbUser, true, file);

				result = true;
			}

			// insert createdby in tenant
			tenant.setCreatedBy(dbUser);
			tenant.setCreatedAt(new Date());

			tenantService.save(tenant);
		}

		return result;
	}

	private StringBuilder buildResetPasswordMessage(String url, User user) {
		StringBuilder result = new StringBuilder();

		result.append("<html>");
		result.append("<body>");

		result.append("<h1>");
		result.append(MessageBundle.getMessageBundle("user.forgot-password.email.subject"));
		result.append("</h1>");

		result.append("<p>");
		result.append(MessageBundle.getMessageBundle("user.forgot-password.email.message"));
		result.append("</p>");

		result.append("<p>");

		result.append("<a href=\"" + url + user.getResetPasswordToken() + "\" >");
		result.append(url + user.getResetPasswordToken());
		result.append("</a>");

		result.append("</p>");

		result.append("<p>");
		result.append(MessageBundle.getMessageBundle("user.forgot-password.email.footer"));
		result.append("</p>");

		result.append("</body>");
		result.append("</html>");

		return result;
	}

	private StringBuilder buildCreateUserMessage(String url, User user) {
		StringBuilder result = new StringBuilder();

		result.append("<html>");
		result.append("<body>");

		result.append("<h1>");
		result.append(MessageBundle.getMessageBundle("user.new.email.subject"));
		result.append("</h1>");

		result.append("<p>");
		result.append(MessageBundle.getMessageBundle("user.new.email.message", user.getName()));
		result.append("</p>");

		result.append("<p>");

		result.append("<a href=\"" + url + user.getResetPasswordToken() + "\" >");
		result.append(url + user.getResetPasswordToken());
		result.append("</a>");

		result.append("</p>");

		result.append("<p>");
		result.append(MessageBundle.getMessageBundle("user.new.email.footer"));
		result.append("</p>");

		result.append("</body>");
		result.append("</html>");

		return result;
	}
}
