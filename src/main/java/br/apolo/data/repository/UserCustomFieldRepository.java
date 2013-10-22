package br.apolo.data.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import br.apolo.data.model.UserCustomField;

public interface UserCustomFieldRepository extends JpaRepository<UserCustomField, Long>, UserCustomFieldRepositoryCustom {

	Page<UserCustomField> findByNameLikeOrderByNameAsc(@Param("name") String name, Pageable page);
	
}
