package br.apolo.data.repository.factory;

import br.apolo.data.repository.ApoloRepository;
import br.apolo.data.repository.impl.ApoloRepositoryImpl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.persistence.EntityManager;

import java.io.Serializable;

/**
 * This class is used by Spring-Data to create the {@link ApoloRepository} instance and manage it.
 * User: rmberne
 * Date: 28/11/13
 */
public class ApoloRepositoryFactoryBean<R extends JpaRepository<T, I>, T, I extends Serializable> extends JpaRepositoryFactoryBean<R, T, I> {

    @SuppressWarnings("rawtypes")
	protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
        return new ApoloRepositoryFactory(entityManager);
    }

    private static class ApoloRepositoryFactory<T, I extends Serializable> extends JpaRepositoryFactory {

        private EntityManager entityManager;

        public ApoloRepositoryFactory(EntityManager entityManager) {
            super(entityManager);

            this.entityManager = entityManager;
        }

        @SuppressWarnings("unchecked")
		protected Object getTargetRepository(RepositoryMetadata metadata) {
            return new ApoloRepositoryImpl<T, I>((Class<T>) metadata.getDomainType(), entityManager);
        }

        protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
            // The RepositoryMetadata can be safely ignored, it is used by JpaRepositoryFactory
            // to check for QueryDslJpaRepository's witch is out of scope.
            return ApoloRepository.class;
        }
    }
}
