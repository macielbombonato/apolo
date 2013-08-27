package br.apolo.business.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import br.apolo.business.model.FileContent;
import br.apolo.data.model.User;

public interface UserService extends UserDetailsService, BaseService<User> {

	User findByLogin(String login);

	User save(User user, boolean changePassword, FileContent file);
}