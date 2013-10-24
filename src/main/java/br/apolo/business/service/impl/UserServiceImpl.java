package br.apolo.business.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.apolo.business.model.FileContent;
import br.apolo.business.model.InstallFormModel;
import br.apolo.business.service.FileService;
import br.apolo.business.service.UserService;
import br.apolo.common.exception.BusinessException;
import br.apolo.common.util.MessageBundle;
import br.apolo.data.enums.Status;
import br.apolo.data.enums.UserPermission;
import br.apolo.data.model.User;
import br.apolo.data.model.UserCustomFieldValue;
import br.apolo.data.model.UserGroup;
import br.apolo.data.repository.UserGroupRepository;
import br.apolo.data.repository.UserRepository;
import br.apolo.security.CurrentUser;

@Service("userService")
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserGroupRepository userGroupRepository;
	
	@Autowired
	private FileService<User> fileService;

	@Override
	public List<User> list() {
		return userRepository.findAll(new Sort(new Sort.Order(Sort.Direction.ASC, "name")));
	}
	
	@Override
	public Page<User> list(Integer pageNumber) {
		if (pageNumber < 1) {
			pageNumber = 1;
		}
		
		PageRequest request = new PageRequest(pageNumber - 1, PAGE_SIZE, Sort.Direction.ASC, "name");
		
		return userRepository.findByStatusNot(Status.LOCKED, request);
	}

	@Override
	public User find(Long id) {
		return userRepository.findOne(id);
	}

	@Override
	public User findByLogin(String login) {
		return userRepository.findUserByEmail(login);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User u = userRepository.findUserByEmail(username);
		
		if (Status.LOCKED.equals(u.getStatus())) {
			String message = MessageBundle.getMessageBundle("user.auth.msg.error.locked");
			throw new BusinessException(message);
		}
		
		Collection<GrantedAuthority> authorities = loadUserAuthorities(u);
		return new CurrentUser(u.getId(), u.getEmail(), u.getPassword().toLowerCase(), u, authorities);
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
		if (Status.ADMIN.equals(user.getStatus()) 
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
					user.setPictureGeneratedName(fileService.uploadFile(user, file, file.getFile().getInputStream()));
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

	private Collection<GrantedAuthority> loadUserAuthorities(User u) {
		Collection<GrantedAuthority> result = new ArrayList<GrantedAuthority>();

		for (UserGroup group : u.getGroups()) {
			for (UserPermission permission : group.getPermissions()) {
				result.add(new SimpleGrantedAuthority(permission.getAttribute()));
			}
		}

		return result;
	}

	@Override
	public Page<User> search(Integer pageNumber, String param) {
		if (pageNumber < 1) {
			pageNumber = 1;
		}
		
		PageRequest request = new PageRequest(pageNumber - 1, PAGE_SIZE, Sort.Direction.ASC, "name");
		
		if (param != null) {
			param = "%" + param + "%";	
		}
		
		return userRepository.findByNameLikeOrEmailLikeAndStatusNotOrderByNameAsc(param, param, Status.LOCKED, request);
	}

	@Override
	public User lock(User user) {
		if (!user.getStatus().isChangeable()) {
			String message = MessageBundle.getMessageBundle("user.status.msg.error.admin");
			throw new BusinessException(message);
		} else if(getAuthenticatedUser().equals(user)) {
			String message = MessageBundle.getMessageBundle("user.status.msg.error.sameuser");
			throw new BusinessException(message);
		}
		
		User dbUser = this.find(user.getId());
		user.setPassword(dbUser.getPassword());
		user.setStatus(Status.LOCKED);
		
		return userRepository.save(user);
	}

	@Override
	public User unlock(User user) {
		if (!user.getStatus().isChangeable()) {
			String message = MessageBundle.getMessageBundle("user.status.msg.error.admin");
			throw new BusinessException(message);
		}
		
		User dbUser = this.find(user.getId());
		user.setPassword(dbUser.getPassword());
		user.setStatus(Status.ACTIVE);
		
		return userRepository.save(user);
	}
	
	@Override
	public Page<User> listLocked(Integer pageNumber) {
		if (pageNumber < 1) {
			pageNumber = 1;
		}
		
		PageRequest request = new PageRequest(pageNumber - 1, PAGE_SIZE, Sort.Direction.ASC, "name");
		
		return userRepository.findByStatus(Status.LOCKED, request);
	}

	@Override
	public User getSystemAdministrator() {
		User user = null;
		
		List<User> userList = userRepository.findByStatus(Status.ADMIN);
		
		if (userList != null && !userList.isEmpty()) {
			user = userList.get(0);
		}
		
		return user;
	}

	@Override
	@Transactional
	public boolean systemSetup(InstallFormModel formModel, FileContent file) {
		boolean result = false;
		
		if (formModel.getUser() != null) {
			User dbUser = null;
			if (formModel.getUser().getEmail() != null && !"".equals(formModel.getUser().getEmail())) {
				dbUser = findByLogin(formModel.getUser().getEmail());	
			}
			
			if (dbUser == null) {
				UserGroup userGroup = null;
				
				try {
					userGroup = userGroupRepository.findOne(1L);
				} catch (Throwable e) {
					/*
					 * If is the really first time that you are using application, this line will throw a error
					 * In other cases, this error can´t occur
					 */
					log.error(e.getMessage(), e);
				}
				
				if (userGroup == null 
						|| (userGroup.getPermissions() != null 
						&& !userGroup.getPermissions().contains(UserPermission.ADMIN))
						) {
					userGroup = new UserGroup();
					
					userGroup.setName(MessageBundle.getMessageBundle("user.permission.ADMIN"));
					userGroup.setStatus(Status.ADMIN);
					
					Set<UserPermission> perms = new HashSet<UserPermission>();
					perms.add(UserPermission.ADMIN);
					
					userGroup.setPermissions(perms);
					
					userGroup.setCreatedBy(formModel.getUser());
					userGroup.setCreationDate(new Date());
					
					userGroup = userGroupRepository.save(userGroup);
				}
				
				Set<UserGroup> groups = new HashSet<UserGroup>();
				groups.add(userGroup);
				
				formModel.getUser().setGroups(groups);
				
				formModel.getUser().setStatus(Status.ADMIN);
				
				formModel.getUser().setCreatedBy(formModel.getUser());
				formModel.getUser().setCreationDate(new Date());
				
				/*
				 * Save system administrator and get your ID
				 */
				dbUser = this.save(formModel.getUser(), true, file);	
				
				/*
				 * Save again to tell to the system that the administrator create yourself
				 */
				formModel.getUser().setCreatedBy(dbUser);
				this.save(dbUser, false, null);
				
				/*
				 * Save again the course to atribute administrator 
				 */
				userGroup.setCreatedBy(dbUser);
				userGroupRepository.save(userGroup);
				
				result = true;
			} else {
				dbUser.setStatus(Status.ADMIN);
				dbUser.setPassword(formModel.getUser().getPassword());
				
				this.save(dbUser, true, file);
				
				result = true;
			}
		}
		
		return result;
	}
}
