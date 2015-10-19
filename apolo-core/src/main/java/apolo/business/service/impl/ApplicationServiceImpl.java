package apolo.business.service.impl;

import apolo.business.service.ApplicationService;
import apolo.common.util.ApoloCrypt;
import apolo.data.enums.Status;
import apolo.data.model.Application;
import apolo.data.model.Tenant;
import apolo.data.repository.ApplicationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

@Service("applicationService")
public class ApplicationServiceImpl extends BaseServiceImpl<Application> implements ApplicationService {

	private static final Logger log = LoggerFactory.getLogger(ApplicationServiceImpl.class);

	@Inject
	private ApplicationRepository applicationRepository;

	@Inject
	private ApoloCrypt apoloCrypt;
	
	public Page<Application> list(Tenant tenant, Integer pageNumber) {
		if (pageNumber < 1) {
			pageNumber = 1;
		}
		
		PageRequest request = new PageRequest(pageNumber - 1, PAGE_SIZE, Sort.Direction.ASC, "name");
		
		Page<Application> result = null;
		
		result = applicationRepository.findByTenant(tenant, request);

		return result;
	}

	public Application find(Long id) {
		return applicationRepository.findOne(id);
	}

	public Application find(Tenant tenant, Long id) {
		return applicationRepository.findOne(id);
	}

	@Transactional
	public Application save(Application entity) {

		if (entity != null
				&& entity.getId() == null) {
			entity.setCreatedBy(getAuthenticatedUser());
			entity.setCreatedAt(new Date());

			entity.setStatus(Status.ACTIVE);
		}

		entity.setUpdatedBy(getAuthenticatedUser());
		entity.setUpdatedAt(new Date());

		if (entity != null
				&& (entity.getApplicationKey() == null
						|| "".equals(entity.getApplicationKey()))
				) {
			try {
				String key = entity.getName() + entity.getTenant().getName() + (new Date()).getTime();

				if (key != null
						&& key.length() > 16) {
					key = key.substring(key.length() - 16, key.length() - 1);
				}

				entity.setApplicationKey(
                        apoloCrypt.encode(
                                key,
                                entity.getName(),
                                entity.getTenant().getName()
                        )
                );
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}

		return applicationRepository.saveAndFlush(entity);
	}

	@Transactional
	public void remove(Application entity) {
		applicationRepository.delete(entity);
	}

	@Override
	public List<Application> list(Tenant tenant) {
		return null;
	}

	public Page<Application> search(Tenant tenant, Integer pageNumber, String param) {
		if (pageNumber < 1) {
			pageNumber = 1;
		}
		
		PageRequest request = new PageRequest(pageNumber - 1, PAGE_SIZE, Sort.Direction.ASC, "name");
		
		if (param != null) {
			param = "%" + param + "%";	
		}
		
		Page<Application> result = applicationRepository.findByTenantAndNameLikeOrderByNameAsc(tenant, param, request);
		
		return result;
	}

	@Transactional
	public Application lock(Application entity) {
		entity.setStatus(Status.LOCKED);
		return applicationRepository.save(entity);
	}

	@Transactional
	public Application unlock(Application entity) {
		entity.setStatus(Status.ACTIVE);
		return applicationRepository.save(entity);
	}

	@Override
	public Application find(String key) {
		return applicationRepository.findByApplicationKey(key);
	}

}
