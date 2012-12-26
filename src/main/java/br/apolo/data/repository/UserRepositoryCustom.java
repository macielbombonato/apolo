package br.apolo.data.repository;

import java.util.List;

import br.apolo.data.model.User;


public interface UserRepositoryCustom {

	public List<User> search(String param);

}
