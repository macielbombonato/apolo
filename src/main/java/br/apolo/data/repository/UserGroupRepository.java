package br.apolo.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.apolo.data.model.UserGroup;

public interface UserGroupRepository extends JpaRepository<UserGroup, Long>, UserGroupRepositoryCustom {

	@Query("FROM UserGroup e ORDER BY e.name")
	List<UserGroup> findAllUserGroups();
	
	UserGroup findByName(String name);
	
}
