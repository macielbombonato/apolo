package br.apolo.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.apolo.data.model.User;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

	User findUserByEmail(String email);
	
}
