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
import apolo.data.enums.Language;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HapoloSet;
import java.util.List;
import java.util.Set;

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
	
	@Override
	public List<User> list(Tenant tenant) {
		PageRequest request = new PageRequest(1, PAGE_SIZE, Sort.Direction.ASC, "name");
		
		Page<User> result = userRepository.findByTenantAndUserStatusNotOrderByNameAsc(
				tenant, 
				UserStatus.LOCKED, 
				request
			);
		
		return result.getContent();
	}
	
	@Override
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

	@Override
	public User find(Long id) {
		return userRepository.findOne(id);
	}
	
	@Override
	public User find(Tenant tenant, Long id) {
		return userRepository.findByTenantAndId(tenant, id);
	}

	@Override
	public User findByLogin(String login) {
		return userRepository.findUserByEmail(login);
	}

	@Override
	public User loadByUsernameAndPassword(String username, String password) {
		Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		User user = userRepository.findByEmailAndPassword(username, encoder.encodePassword(password, null));
		
		if (user == null) {
			String message = MessageBundle.getMessageBundle("user.msg.no.data.found");
			throw new UsernameNotFoundException(message);
		}
				
		return user;
	}
	
	@Override
	@Transactional
	public User save(User entity) {
		if (entity != null) {
			entity.setLastUpdatedBy(getAuthenticatedUser());
			entity.setLastUpdateDate(new Date());			
		}
		
		return save(entity, false, null);
	}

	@Override
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
				
				user.setPictureOriginalName(file.getFile().getOriginalFilename());
				try {
					user.setPictureGeneratedName(
							fileService.uploadFile(
									user.getTenant(), 
									user, 
									file, 
									file.getFile().getInputStream()
								)
						);
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
		
		return userRepository.saveAndFlush(user);
	}
	
	@Override
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

	@Override
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

	@Override
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
		
		return userRepository.save(user);
	}

	@Override
	public User unlock(User user) {
		if (!user.getStatus().isChangeable()) {
			String message = MessageBundle.getMessageBundle("error.403.4");
			throw new AccessDeniedException(4, message);
		}
		
		User dbUser = this.find(user.getId());
		user.setPassword(dbUser.getPassword());
		user.setStatus(UserStatus.ACTIVE);
		
		return userRepository.save(user);
	}
	
	@Override
	public Page<User> listLocked(Tenant tenant, Integer pageNumber) {
		if (pageNumber < 1) {
			pageNumber = 1;
		}
		
		PageRequest request = new PageRequest(pageNumber - 1, PAGE_SIZE, Sort.Direction.ASC, "name");
		
		return userRepository.findByTenantAndUserStatus(tenant, UserStatus.LOCKED, request);
	}

	@Override
	public User getSystemAdministrator() {
		User user = null;
		
		List<User> userList = userRepository.findByUserStatus(UserStatus.ADMIN);
		
		if (userList != null && !userList.isEmpty()) {
			user = userList.get(0);
		}
		
		return user;
	}

	@Override
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
			tenant.setLogoWidth(80);
			tenant.setUrl(applicationProperties.getDefaultTenant());
			tenant.setSkin(Skin.SKIN_5);
			tenant.setLanguage(Language.BR);
			tenant.setStatus(Status.ACTIVE);
			
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
					
					Set<UserPermission> perms = new HapoloSet<UserPermission>();
					perms.add(UserPermission.ADMIN);
					
					adminUserGroup.setPermissions(perms);
					
					adminUserGroup.setCreatedBy(formModel.getUser());
					adminUserGroup.setCreationDate(new Date());
					adminUserGroup.setTenant(tenant);
					
					adminUserGroup = userGroupRepository.save(adminUserGroup);
				}
				
				// Admin groups
				Set<UserGroup> adminGroups = new HapoloSet<UserGroup>();
				adminGroups.add(adminUserGroup);
				
				formModel.getUser().setGroups(adminGroups);
				
				formModel.getUser().setStatus(UserStatus.ADMIN);
				
				formModel.getUser().setCreatedBy(formModel.getUser());
				formModel.getUser().setCreationDate(new Date());
				
				formModel.getUser().setLanguage(tenant.getLanguage());
				
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
			tenant.setCreationDate(new Date());
			
			tenantService.save(tenant);
		}
		
		return result;
	}
}
