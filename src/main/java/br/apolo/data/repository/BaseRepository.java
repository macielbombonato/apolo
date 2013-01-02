package br.apolo.data.repository;

import java.util.List;

import javax.persistence.metamodel.SingularAttribute;

import br.apolo.data.model.BaseEntity;

public interface BaseRepository<E extends BaseEntity> {
	
	List<E> search(String param, List<SingularAttribute<E, String>> fields);

}
