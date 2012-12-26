package br.apolo.data.repository;

import org.springframework.data.repository.CrudRepository;

import br.apolo.data.model.User;

public interface UserRepository extends CrudRepository<User, Long>,
		UserRepositoryCustom {

	User findUserByLogin(String login);

}
