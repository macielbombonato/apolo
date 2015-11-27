package apolo.data.repository;

import apolo.data.enums.UserStatus;
import apolo.data.model.Tenant;
import apolo.data.model.User;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

	List<User> findUserByEmail(String email);

	User findUserByTenantAndEmail(Tenant tenant, String email);

	Page<User> findByTenantAndUserStatusNotOrderByNameAsc(
			@Param("tenant") Tenant tenant,
			@Param("status") UserStatus userStatus,
			Pageable page
	);

	@Query("select u from User u where u.tenant = ?1 and (u.name like ?2 or u.email like ?3) order by name asc)")
	Page<User> findByTenantAndNameLikeOrEmailLikeOrderByNameAsc(
			Tenant tenant,
			String name,
			String email,
			Pageable page
	);

	Page<User> findByTenantAndNameLikeOrEmailLikeAndUserStatusOrderByNameAsc(
			@Param("tenant") Tenant tenant,
			@Param("name") String name,
			@Param("email") String email,
			@Param("status") UserStatus userStatus,
			Pageable page
	);

	Page<User> findByTenantAndNameLikeOrEmailLikeAndUserStatusNotOrderByNameAsc(
			@Param("tenant") Tenant tenant,
			@Param("name") String name,
			@Param("email") String email,
			@Param("status") UserStatus userStatus,
			Pageable page
	);

	Page<User> findByTenantAndUserStatus(
			Tenant tenant,
			UserStatus userStatus,
			Pageable page
	);

	Page<User> findByUserStatusNot(
			UserStatus userStatus,
			Pageable page
	);

	Page<User> findAll(
			Pageable page
	);

	List<User> findByUserStatus(UserStatus userStatus);

	User findByTenantAndEmailAndPassword(Tenant tenant, String email, String password);

	List<User> findByEmail(String email);

	User findByResetPasswordToken(String token);

	User findByTenantAndId(Tenant tenant, Long id);

	long count();

	long countByTenant(Tenant tenant);

}
