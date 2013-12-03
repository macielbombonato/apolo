package br.apolo.business.service;

import java.util.List;

import org.springframework.data.domain.Page;

import br.apolo.data.model.BaseEntity;
import br.apolo.data.model.User;

@SuppressWarnings("rawtypes")
public interface BaseService<E extends BaseEntity> {
	
	E find(Long id);
	
	E save(E entity);
	
	void remove(E entity);
	
	User getAuthenticatedUser();
	
	List<E> list();
	
	Page<E> list(Integer pageNumber);
	
}
