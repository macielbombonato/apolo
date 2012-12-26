package br.apolo.data.repository;

import java.util.List;

import br.apolo.data.model.UserGroup;

public interface UserGroupRepositoryCustom {

	List<UserGroup> search(String param);

}
