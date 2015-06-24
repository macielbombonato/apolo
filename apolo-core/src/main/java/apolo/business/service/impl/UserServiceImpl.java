package apolo.business.service.impl;

import apolo.business.model.FileContent;
import apolo.business.model.InstallFormModel;
import apolo.business.service.FileService;
import apolo.business.service.TenantService;
import apolo.business.service.UserService;
import apolo.common.config.model.ApplicationProperties;
import apolo.common.exception.AccessDeniedException;
import apolo.common.exception.BusinessException;
import apolo.common.util.MessageBundle;
import apolo.data.enums.Spinner;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

@Service("userService")
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserGroupRepository userGroupRepository;
	
	@Autowired
	private FileService<User> fileService;
	
	@Autowired
	private ApplicationProperties applicationProperties;
	
	@Autowired
	private TenantService tenantService;
	
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

	public User find(Long id) {
		return userRepository.findOne(id);
	}

	@Override
	public int increaseSignInCounter(Long id, String userIPAddress) {
		int result = 0;
		User user = this.find(id);

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

			user.setDisableAuditLog(true);

			result = user.getSignInCount();

			this.save(user);
		}

		return result;
	}

	public User find(Tenant tenant, Long id) {
		return userRepository.findByTenantAndId(tenant, id);
	}

	public User findByLogin(String login) {
		return userRepository.findUserByEmail(login);
	}

	public User loadByUsernameAndPassword(String username, String password) {
		Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		User user = userRepository.findByEmailAndPassword(username, encoder.encodePassword(password, null));
		
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
		
		return save(entity, false, null);
	}

	@Transactional
	public User save(User user, boolean changePassword, FileContent file) {
		if (UserStatus.ADMIN.equals(user.getStatus()) 
				&& !user.getPermissions().contains(UserPermission.ADMIN)) {
			String message = MessageBundle.getMessageBundle("user.edit.msg.error.admin.permission");
			throw new BusinessException(message);
		}
		
		if (changePassword) {
			Md5PasswordEncoder encoder = new Md5PasswordEncoder();
			user.setPassword(encoder.encodePassword(user.getPassword(), null));			
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
		
		return userRepository.saveAndFlush(user);
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
			
			result.add(new SimpleGrantedAuthority(UserPermission.AFTER_AUTH_USER.getAttribute()));
			
			user.getPermissions().add(UserPermission.AFTER_AUTH_USER);
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
	public boolean systemSetup(InstallFormModel formModel, FileContent file) {
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
			tenant.setSpinner(Spinner.COGS);
			tenant.setStatus(Status.ACTIVE);

			tenant.setEmailFrom(applicationProperties.getEmailFrom());
			tenant.setEmailPassword(applicationProperties.getEmailPassword());
			tenant.setSmtpHost(applicationProperties.getSmtpHost());
			tenant.setSmtpPort(applicationProperties.getSmtpPort());
			tenant.setUseTLS(applicationProperties.getUseTLS());

			tenant.setGoogleAdClient(applicationProperties.getGoogleAdClient());
			tenant.setGoogleAdSlotOne(applicationProperties.getGoogleAdSlotOne());
			tenant.setGoogleAdSlotTwo(applicationProperties.getGoogleAdSlotTwo());
			tenant.setGoogleAdSlotThree(applicationProperties.getGoogleAdSlotThree());
			tenant.setGoogleAnalyticsUserAccount(applicationProperties.getGoogleAnalyticsUserAccount());

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
				dbUser = this.save(formModel.getUser(), true, file);
				
				/*
				 * Save again to tell to the system that the administrator create yourself
				 */
				formModel.getUser().setCreatedBy(dbUser);
				
				formModel.getUser().setTenant(tenant);
				
				this.save(dbUser, false, null);
				
				/*
				 * Save the group again to associate administrator user as owner 
				 */
				adminUserGroup.setCreatedBy(dbUser);
				userGroupRepository.save(adminUserGroup);
				
				result = true;
			} else {
				dbUser.setStatus(UserStatus.ADMIN);
				dbUser.setPassword(formModel.getUser().getPassword());
				
				this.save(dbUser, true, file);
				
				result = true;
			}
			
			// insert createdby in tenant
			tenant.setCreatedBy(dbUser);
			tenant.setCreatedAt(new Date());
			
			tenantService.save(tenant);
		}
		
		return result;
	}
}
