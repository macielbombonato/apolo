package apolo.business.service.impl;

import apolo.business.service.EmailService;
import apolo.business.service.FileService;
import apolo.business.service.TenantService;
import apolo.business.service.UserService;
import apolo.common.MessageBuilder;
import apolo.common.config.model.ApplicationProperties;
import apolo.common.exception.AccessDeniedException;
import apolo.common.exception.BusinessException;
import apolo.common.util.ApoloCrypt;
import apolo.common.util.MessageBundle;
import apolo.data.enums.Status;
import apolo.data.enums.UserStatus;
import apolo.data.model.PermissionGroup;
import apolo.data.model.Tenant;
import apolo.data.model.User;
import apolo.data.model.UserCustomFieldValue;
import apolo.data.repository.PermissionGroupRepository;
import apolo.data.repository.UserRepository;
import apolo.security.CurrentUser;
import apolo.security.Permission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

@Service("userService")
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {

	private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

	@Inject
	private UserRepository userRepository;

	@Inject
	private PermissionGroupRepository permissionGroupRepository;

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

	@Inject
	private MessageBuilder messageBuilder;

	public List<User> list(Tenant tenant) {
		PageRequest request = new PageRequest(1, PAGE_SIZE, Sort.Direction.ASC, "name");

		Page<User> result = userRepository.findByTenantAndStatusNotOrderByNameAsc(
				tenant,
				UserStatus.LOCKED,
				request
		);

		return result.getContent();
	}

	public Page<User> list(Tenant tenant, Integer pageNumber) {
		if (pageNumber == null || pageNumber < 1) {
			pageNumber = 1;
		}

		PageRequest request = new PageRequest(pageNumber - 1, PAGE_SIZE, Sort.Direction.ASC, "name");

		Page<User> result = userRepository.findByTenantAndStatusNotOrderByNameAsc(
				tenant,
				UserStatus.LOCKED,
				request
		);

		return result;
	}

	public Page<User> listAll(Integer pageNumber) {
		if (pageNumber == null || pageNumber < 1) {
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
	public int increaseSignInCounter(User user, String sessionId, String userIPAddress) {
		int result = 0;

		if (user != null) {
			if (user.getSignInCount() != null) {
				user.setSignInCount(user.getSignInCount() + 1);
			} else {
				user.setSignInCount(1);
			}

			String token = null;

			try {
				token = apoloCrypt.encode(
                        user.getEmail() + new Date().getTime(),
                        user.getTenant().getName() + user.getName(),
                        applicationProperties.getIvKey()
                );
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}

			user.setToken(token);
			user.setSessionId(sessionId);

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
				&& getAuthenticatedUser().getPermissions().contains(Permission.ADMIN)) {
			result = userRepository.count();
		}

		return result;
	}

	@Override
	public long countByTenant(Tenant tenant) {
		long result = 0L;
		if (getAuthenticatedUser() != null
				&& getAuthenticatedUser().getPermissions().contains(Permission.ADMIN)) {
			result = userRepository.countByTenant(tenant);
		}

		return result;
	}

	@Override
	public void generateResetPasswordToken(String serverUrl, String email) {
		if (email != null
				&& !"".equals(email)) {
			List<User> userList = this.findByLogin(email);

			for(User user : userList) {
				if (user != null) {
					try {
						String token = apoloCrypt.encode(
								user.getEmail(),
								user.getTenant().getName() + user.getName(),
								applicationProperties.getIvKey()
						);

						user.setResetPasswordToken(token);
						user.setResetPasswordSentAt(new Date());

						user = this.save(user);

						emailService.sendAsync(
								user.getTenant(),
								user.getTenant().getName(),
								user.getTenant().getEmailFrom(),
								user.getName(),
								user.getEmail(),
								MessageBundle.getMessageBundle("user.forgot-password.email.subject"),
								messageBuilder.buildResetPasswordMessage(
										serverUrl + "reset-password/",
										MessageBundle.getMessageBundle("user.forgot-password.email.subject"),
										MessageBundle.getMessageBundle("user.forgot-password.email.message"),
										user.getResetPasswordToken(),
										MessageBundle.getMessageBundle("user.forgot-password.email.footer")
								)
						);
					} catch (Exception e) {
						log.error(e.getMessage(), e);
					}
				}
			}
		}
	}

	@Override
	public User findByResetToken(String token) {
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

	@Override
	public User findByToken(
			String token,
			String sessionId
	) {
		User user = null;

		user = userRepository.findByTokenAndSessionId(token, sessionId);

		return user;
	}

	@Override
	public User findBySession(
			String sessionId
	) {
		User user = null;

		user = userRepository.findBySessionId(sessionId);

		return user;
	}

	public User find(Tenant tenant, Long id) {
		return userRepository.findByTenantAndId(tenant, id);
	}

	public User findByLogin(Tenant tenant, String login) {
		return userRepository.findUserByTenantAndEmail(tenant, login);
	}

	public List<User> findByLogin(String login) {
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
		return save(null, entity, false);
	}

	@Transactional
	public User save(String serverUrl, User user, boolean changePassword) {
		if (user != null) {
			user.setUpdatedBy(getAuthenticatedUser());
			user.setUpdatedAt(new Date());
		}

		if (UserStatus.ADMIN.equals(user.getStatus())
				&& !user.getPermissions().contains(Permission.ADMIN)) {
			String message = MessageBundle.getMessageBundle("user.edit.msg.error.admin.permission");
			throw new BusinessException(message);
		}

		boolean sendEmail = false;

		if (user.getId() == null) {
			try {
				String token = apoloCrypt.encode(
						user.getEmail() + new Date().getTime(),
						user.getTenant().getName() + user.getName(),
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

		user = userRepository.saveAndFlush(user);

		if (sendEmail) {
			try {
				emailService.sendAsync(
						user.getTenant(),
						user.getTenant().getName(),
						user.getTenant().getEmailFrom(),
						user.getName(),
						user.getEmail(),
						MessageBundle.getMessageBundle("user.new.email.subject"),
						messageBuilder.buildCreateUserMessage(
								serverUrl,
								MessageBundle.getMessageBundle("user.new.email.subject"),
								MessageBundle.getMessageBundle("user.new.email.message", user.getName()),
								user.getResetPasswordToken() + "reset-password/",
								MessageBundle.getMessageBundle("user.new.email.footer")
						)
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
			for (PermissionGroup group : user.getGroups()) {
				if (group != null
						&& (group.getStatus().equals(UserStatus.ADMIN) || group.getStatus().equals(UserStatus.ACTIVE))
						&& group.getPermissions() != null
						&& !group.getPermissions().isEmpty()) {
					for (Permission permission : group.getPermissions()) {
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

		return userRepository.findByTenantAndStatus(tenant, UserStatus.LOCKED, request);
	}

	public User getSystemAdministrator() {
		User user = null;

		List<User> userList = userRepository.findByStatus(UserStatus.ADMIN);

		if (userList != null && !userList.isEmpty()) {
			user = userList.get(0);
		}

		return user;
	}

	@Transactional
	public boolean systemSetup(String serverUrl, User user) {
		boolean result = false;

		Collection<GrantedAuthority> authorities = loadUserAuthorities(user);

		new CurrentUser(
				1L,
				user.getEmail(),
				user.getPassword(),
				user,
				authorities
		);

		Tenant tenant = tenantService.findByUrl(applicationProperties.getDefaultTenant());

		if (tenant == null) {
			tenant = new Tenant();

			tenant.setName(applicationProperties.getDefaultTenant());
			tenant.setUrl(applicationProperties.getDefaultTenant());
			tenant.setStatus(Status.ACTIVE);

			tenant.setEmailFrom(applicationProperties.getEmailFrom());
			tenant.setEmailUsername(applicationProperties.getEmailUsername());
			tenant.setEmailPassword(applicationProperties.getEmailPassword());
			tenant.setSmtpHost(applicationProperties.getSmtpHost());
			tenant.setSmtpPort(applicationProperties.getSmtpPort());
			tenant.setUseTLS(applicationProperties.getUseTLS());

			user.setTenant(tenant);

			tenantService.save(tenant);
		}


		if (user != null) {
			// Create system administrator
			User dbUser = null;
			if (user.getEmail() != null
					&& !"".equals(user.getEmail())) {
				List<User> dbUserList = findByLogin(user.getEmail());

				if (dbUserList != null
						&& !dbUserList.isEmpty()){
					dbUser = dbUserList.get(0);
				}
			}

			if (dbUser == null) {
				PermissionGroup adminPermissionGroup = null;

				// Create admin group
				try {
					adminPermissionGroup = permissionGroupRepository.findByName(MessageBundle.getMessageBundle("user.permission.ADMIN"));
				} catch (Throwable e) {
					/*
					 * If is the really first time that you are using application, this line will throw a error
					 * In other cases, this error can´t occur
					 */
					LOG.error(e.getMessage(), e);
				}

				if (adminPermissionGroup == null
						|| (adminPermissionGroup.getPermissions() != null
						&& !adminPermissionGroup.getPermissions().contains(Permission.ADMIN))
						) {
					adminPermissionGroup = new PermissionGroup();

					adminPermissionGroup.setName(MessageBundle.getMessageBundle("user.permission.ADMIN"));
					adminPermissionGroup.setStatus(UserStatus.ADMIN);

					Set<Permission> perms = new HashSet<Permission>();
					perms.add(Permission.ADMIN);

					adminPermissionGroup.setPermissions(perms);

					adminPermissionGroup.setCreatedBy(user);
					adminPermissionGroup.setCreatedAt(new Date());

					adminPermissionGroup = permissionGroupRepository.save(adminPermissionGroup);
				}

				// Admin groups
				Set<PermissionGroup> adminGroups = new HashSet<PermissionGroup>();
				adminGroups.add(adminPermissionGroup);

				user.setGroups(adminGroups);

				user.setStatus(UserStatus.ADMIN);
				user.setEnabled(true);

				user.setCreatedBy(user);
				user.setCreatedAt(new Date());

				/*
				 * Save system administrator and get your ID
				 */
				dbUser = this.save(serverUrl, user, true);

				/*
				 * Save again to tell to the system that the administrator create yourself
				 */
				user.setCreatedBy(dbUser);

				user.setTenant(tenant);

				this.save(serverUrl, dbUser, false);

				/*
				 * Save the group again to associate administrator user as owner
				 */
				adminPermissionGroup.setCreatedBy(dbUser);
				permissionGroupRepository.save(adminPermissionGroup);

				result = true;
			} else {
				dbUser.setStatus(UserStatus.ADMIN);
				dbUser.setPassword(user.getPassword());

				this.save(serverUrl, dbUser, true);

				result = true;
			}

			// insert createdby in tenant
			tenant.setCreatedBy(dbUser);
			tenant.setCreatedAt(new Date());

			tenantService.save(tenant);
		}

		return result;
	}

	public void reconstructAuthenticatedUser(User user) {
		boolean isLoged = true;

		if (user == null) {
			user = new User();
			isLoged = false;
		}

		Collection<GrantedAuthority> authorities =
				loadUserAuthorities(
						getAuthenticatedUser()
				);

		Authentication newAuth = new CurrentUser(
				user.getId(),
				user.getEmail(),
				user.getPassword(),
				user,
				authorities
		);

		SecurityContextHolder.getContext().setAuthentication(newAuth);

		if (!isLoged) {
			SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
		}
	}
}
