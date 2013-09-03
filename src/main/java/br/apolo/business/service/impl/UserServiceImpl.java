package br.apolo.business.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.metamodel.SingularAttribute;

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
import br.apolo.business.model.SearchResult;
import br.apolo.business.service.FileService;
import br.apolo.business.service.UserService;
import br.apolo.common.exception.GenericException;
import br.apolo.common.util.MessageBundle;
import br.apolo.data.enums.UserPermission;
import br.apolo.data.model.User;
import br.apolo.data.model.UserGroup;
import br.apolo.data.model.User_;
import br.apolo.data.repository.UserRepository;
import br.apolo.security.CurrentUser;

@Service("userService")
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {

	@Autowired
	UserRepository userRepository;
	
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
		
		return userRepository.findAll(request);
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
					userRepository.save(user);	
				}
				
				user.setPictureOriginalName(file.getFile().getOriginalFilename());
				try {
					user.setPictureGeneratedName(fileService.uploadFile(user, file, file.getFile().getInputStream()));
				} catch (IOException e) {
					String message = MessageBundle.getMessageBundle("commons.errorUploadingFile");
					throw new GenericException(message);
				}
			}
		}
		
		return userRepository.save(user);
	}
	
	@Override
	@Transactional
	public void remove(User user) {
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
	public SearchResult<User> search(String param) {
		SearchResult<User> result = new SearchResult<User>();
		
		List<SingularAttribute<User, String>> fields = new ArrayList<SingularAttribute<User,String>>();
		fields.add(User_.email);
		fields.add(User_.name);
		
		result.setResults(userRepository.search(param, fields));
		
		return result;
	}
}
