package br.apolo.data.repository.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.apolo.data.model.BaseEntity;
import br.apolo.data.repository.BaseRepository;

@SuppressWarnings("rawtypes")
public abstract class BaseRepotitoryImpl<E extends BaseEntity> implements BaseRepository<E> {
	
	@PersistenceContext
	protected EntityManager em;
	
}
