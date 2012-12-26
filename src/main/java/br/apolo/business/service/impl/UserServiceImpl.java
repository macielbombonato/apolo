package br.apolo.business.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.apolo.business.model.SearchResult;
import br.apolo.business.service.UserService;
import br.apolo.data.model.User;
import br.apolo.data.model.UserGroup;
import br.apolo.data.repository.UserRepository;
import br.apolo.security.CurrentUser;
import br.apolo.security.UserPermission;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Override
	public List<User> list() {
		return (List<User>) userRepository.findAll();
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
		return save(entity, false);
	}

	@Override
	@Transactional
	public User save(User user, boolean changePassword) {
		if (changePassword) {
			Md5PasswordEncoder encoder = new Md5PasswordEncoder();
			user.setPassword(encoder.encodePassword(user.getPassword(), null));			
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
		
		result.setResults(userRepository.search(param));
		
		return result;
	}
}
