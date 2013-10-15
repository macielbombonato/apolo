package br.apolo.business.service;

import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;

import br.apolo.business.model.FileContent;
import br.apolo.business.model.InstallFormModel;
import br.apolo.data.model.User;

public interface UserService extends UserDetailsService, BaseService<User> {
	
	final int PAGE_SIZE = 10;

	User findByLogin(String login);

	User save(User user, boolean changePassword, FileContent file);
	
	Page<User> search(Integer pageNumber, String param);
	
	User lock(User user);
	
	User unlock(User user);
	
	Page<User> listLocked(Integer pageNumber);
	
	User getSystemAdministrator();
	
	boolean systemSetup(InstallFormModel formModel, FileContent file);
}