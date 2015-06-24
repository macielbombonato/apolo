package apolo.business.service;

import apolo.business.model.FileContent;
import apolo.business.model.InstallFormModel;
import apolo.data.model.Tenant;
import apolo.data.model.User;
import org.springframework.data.domain.Page;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface UserService extends BaseService<User> {

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
	 * @param tenant
	 * @param pageNumber
	 * @param param
	 * @return Page<User>
	 */
	Page<User> search(Tenant tenant, Integer pageNumber, String param);
	
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
	 * @param tenant
	 * @param pageNumber
	 * @return Page<User>
	 */
	Page<User> listLocked(Tenant tenant, Integer pageNumber);
	
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
	
	/**
	 * Load user authorities to check permissions
	 * @param user
	 * @return Collection<GrantedAuthority>
	 */
	Collection<GrantedAuthority> loadUserAuthorities(User user);
	
	/**
	 * Load user by username and password
	 * @param username
	 * @param password
	 * @return User
	 */
	User loadByUsernameAndPassword(String username, String password);
	
	/**
	 * Alternative to find an user without use tenant 
	 * @param id
	 * @return User
	 */
	User find(Long id);

	/**
	 * Increase user sign in counter
	 * @param id
	 */
	int increaseSignInCounter(Long id, String userIPAddress);
}