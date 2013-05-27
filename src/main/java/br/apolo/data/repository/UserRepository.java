package br.apolo.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import br.apolo.data.model.User;

public interface UserRepository extends PagingAndSortingRepository<User, Long>, UserRepositoryCustom {

	User findUserByEmail(String email);
	
	@Query("FROM User e ORDER BY e.name")
	List<User> findAllUsers();

}
