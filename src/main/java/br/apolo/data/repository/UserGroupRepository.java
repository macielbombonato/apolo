package br.apolo.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import br.apolo.data.model.UserGroup;

public interface UserGroupRepository extends PagingAndSortingRepository<UserGroup, Long>, UserGroupRepositoryCustom {

	@Query("FROM UserGroup e ORDER BY e.name")
	List<UserGroup> findAllUserGroups();
	
}
