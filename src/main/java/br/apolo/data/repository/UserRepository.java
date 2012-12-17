package br.apolo.data.repository;

import java.util.List;

import br.apolo.data.model.User;

public interface UserRepository extends JpaRepository<User> {
	
	User findByLogin(String login);
	
	List<User> search(String param);

}
