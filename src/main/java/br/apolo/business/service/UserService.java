package br.apolo.business.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import br.apolo.data.model.User;

public interface UserService extends UserDetailsService {

	List<User> list();

	User find(Long id);

	User findByLogin(String login);

	User save(User user);
	
	void remove(User user);
}