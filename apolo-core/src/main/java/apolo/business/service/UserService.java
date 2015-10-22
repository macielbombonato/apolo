package apolo.business.service;

import apolo.business.model.FileContent;
import apolo.business.model.InstallFormModel;
import apolo.business.service.base.BaseService;
import apolo.data.model.Tenant;
import apolo.data.model.User;
import org.springframework.data.domain.Page;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface UserService extends BaseService<User> {

	final int PAGE_SIZE = 10;

	User findByLogin(String login);

	User save(String serverUrl, User user, boolean changePassword, FileContent file);
	
	Page<User> search(Tenant tenant, Integer pageNumber, String param);
	
	User lock(User user);
	
	User unlock(User user);
	
	Page<User> listLocked(Tenant tenant, Integer pageNumber);
	
	User getSystemAdministrator();
	
	boolean systemSetup(String serverUrl, InstallFormModel formModel, FileContent file);
	
	Collection<GrantedAuthority> loadUserAuthorities(User user);
	
	User loadByUsernameAndPassword(String username, String password);
	
	User find(Long id);

	int increaseSignInCounter(User user, String userIPAddress);

	long count();

	long countByTenant(Tenant tenant);

	void generateResetPasswordToken(String serverUrl, String email);

	User findByToken(String token);

	Page<User> listAll(Integer pageNumber);
}