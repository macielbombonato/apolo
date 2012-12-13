package br.apolo.business.service;

import java.util.List;

import br.apolo.data.model.BaseEntity;

public interface BaseService<E extends BaseEntity> {
	
	List<E> list();
	
	E find(Long id);
	
	E save(E entity);
	
	void remove(E entity);

}
