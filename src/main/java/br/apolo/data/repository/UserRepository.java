package br.apolo.data.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import br.apolo.data.enums.Status;
import br.apolo.data.model.User;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

	User findUserByEmail(String email);
	
	Page<User> findByNameLikeOrEmailLikeAndStatusOrderByNameAsc(@Param("name") String name, @Param("email") String email, @Param("status") Status status, Pageable page);
	
	Page<User> findByNameLikeOrEmailLikeAndStatusNotOrderByNameAsc(@Param("name") String name, @Param("email") String email, @Param("status") Status status, Pageable page);
	
	Page<User> findByStatus(Status status, Pageable page);
	
	Page<User> findByStatusNot(Status status, Pageable page);
	
	List<User> findByStatus(Status status);
	
}
