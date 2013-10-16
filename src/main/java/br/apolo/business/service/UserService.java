package br.apolo.business.service;

import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;

import br.apolo.business.model.FileContent;
import br.apolo.business.model.InstallFormModel;
import br.apolo.data.model.User;

public interface UserService extends UserDetailsService, BaseService<User> {

	final int PAGE_SIZE = 10;

	/**
	 * Find user by email address
	 * @param login
	 * @return User
	 */
	User findByLogin(String login);

	/**
	 * Save user
	 * @param user
	 * @param changePassword
	 * @param file
	 * @return User
	 */
	User save(User user, boolean changePassword, FileContent file);
	
	/**
	 * Search user
	 * @param pageNumber
	 * @param param
	 * @return Page<User>
	 */
	Page<User> search(Integer pageNumber, String param);
	
	/**
	 * Lock the user access
	 * @param user
	 * @return User
	 */
	User lock(User user);
	
	/**
	 * Unlock the user access
	 * @param user
	 * @return User
	 */
	User unlock(User user);
	
	/**
	 * List locked users
	 * @param pageNumber
	 * @return Page<User>
	 */
	Page<User> listLocked(Integer pageNumber);
	
	/**
	 * Get the user administrator
	 * @return User
	 */
	User getSystemAdministrator();
	
	/**
	 * System setup. True if setup occurs without errors
	 * @param formModel
	 * @param file
	 * @return boolean
	 */
	boolean systemSetup(InstallFormModel formModel, FileContent file);
}