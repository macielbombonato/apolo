package br.apolo.data.repository;

import org.springframework.data.repository.CrudRepository;

import br.apolo.data.model.UserGroup;

public interface UserGroupRepository extends CrudRepository<UserGroup, Long>,
		UserGroupRepositoryCustom {

}
