package br.apolo.data.repository;

import br.apolo.data.model.User;

public interface UserRepository extends JpaRepository<User> {
	
	User findByLogin(String login);

}
