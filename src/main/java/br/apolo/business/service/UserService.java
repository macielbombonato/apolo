package br.apolo.business.service;

import java.util.Collection;

import org.springframework.security.core.userdetails.UserDetailsService;

import br.apolo.data.model.User;

public interface UserService extends UserDetailsService {

	Collection<User> list();

	User find(Long id);

	User findByLogin(String login);

	User getAuthenticatedUser();
}