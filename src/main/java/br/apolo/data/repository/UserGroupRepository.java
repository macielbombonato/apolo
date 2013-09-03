package br.apolo.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.apolo.data.model.UserGroup;

public interface UserGroupRepository extends JpaRepository<UserGroup, Long>, UserGroupRepositoryCustom {

	UserGroup findByName(String name);
	
}
