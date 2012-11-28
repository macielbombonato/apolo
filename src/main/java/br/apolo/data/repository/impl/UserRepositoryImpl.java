package br.apolo.data.repository.impl;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import br.apolo.data.model.User;
import br.apolo.data.repository.UserRepository;

@Repository(value = "userRepository")
public class UserRepositoryImpl extends JpaRepositoryImpl<User> implements UserRepository {

	public User findByLogin(String login) {

		// Validation included to prevent unnecessary data base access
		if (login == null || login.isEmpty() || "anonymousUser".equals(login)) {
			throw new UsernameNotFoundException(login);
		}

		try {
			User u = createNamedQuery("User.findByLogin").setParameter("login", login).getSingleResult();

			return u;
		} catch (Exception e) {
			throw new UsernameNotFoundException(login);
		}

	}
}
