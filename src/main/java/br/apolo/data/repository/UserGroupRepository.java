package br.apolo.data.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import br.apolo.data.model.UserGroup;

public interface UserGroupRepository extends JpaRepository<UserGroup, Long>, UserGroupRepositoryCustom {

	UserGroup findByName(String name);
	
	Page<UserGroup> findByNameLikeOrderByNameAsc(@Param("name") String name, Pageable page);
	
}
