package br.apolo.data.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import br.apolo.data.enums.UserStatus;
import br.apolo.data.model.User;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

	User findUserByEmail(String email);
	
	Page<User> findByNameLikeOrEmailLikeAndStatusOrderByNameAsc(@Param("name") String name, @Param("email") String email, @Param("status") UserStatus status, Pageable page);
	
	Page<User> findByStatus(UserStatus status, Pageable page);
	
	Page<User> findByStatusNot(UserStatus status, Pageable page);
	
	User findByStatus(UserStatus status);
	
}
