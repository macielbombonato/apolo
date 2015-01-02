package apolo.data.repository.impl;

import apolo.data.model.BaseEntity;
import apolo.data.repository.BaseRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@SuppressWarnings("rawtypes")
public abstract class BaseRepotitoryImpl<E extends BaseEntity> implements BaseRepository<E> {
	
	@PersistenceContext
	protected EntityManager em;
	
}
