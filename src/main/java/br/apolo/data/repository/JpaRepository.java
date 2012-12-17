package br.apolo.data.repository;

import java.util.List;

import br.apolo.data.model.BaseEntity;

public interface JpaRepository<E extends BaseEntity> {
	E find(Long id);

	E saveOrUpdate(E entity);

	void remove(E entity);

	List<E> findAll();
	
	List<E> search(String param);
}
