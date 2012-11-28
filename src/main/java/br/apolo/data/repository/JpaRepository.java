package br.apolo.data.repository;

import java.util.List;

import br.apolo.data.model.BaseEntity;

public interface JpaRepository<T extends BaseEntity> {
	T find(Long id);

	T saveOrUpdate(T entity);

	void remove(T entity);

	List<T> findAll();
}
