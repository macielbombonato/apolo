package apolo.business.service.impl;

import apolo.business.service.ConfigurationService;
import apolo.data.model.Configuration;
import apolo.data.model.Tenant;
import apolo.data.repository.ConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service("configurationService")
public class ConfigurationServiceImpl extends BaseServiceImpl<Configuration> implements ConfigurationService {

	@Autowired
	private ConfigurationRepository configurationRepository;
	
	public List<Configuration> list(Tenant tenant) {
		List<Configuration> result = configurationRepository.findByTenant(tenant);
		
		return result;
	}
	
	public Page<Configuration> list(Tenant tenant, Integer pageNumber) {
		if (pageNumber < 1) {
			pageNumber = 1;
		}
		
		PageRequest request = new PageRequest(pageNumber - 1, PAGE_SIZE, Sort.Direction.ASC, "field");
		
		Page<Configuration> result = configurationRepository.findByTenant(tenant, request);
		
		return result;
	}

	public Configuration find(Tenant tenant, Long id) {
		return null; // TODO configurationRepository.findByTenant(tenant, id);
	}

	@Transactional
	public Configuration save(Configuration configuration) {
		if (configuration != null) {
			configuration.setLastUpdatedBy(getAuthenticatedUser());
			configuration.setLastUpdateDate(new Date());
		}
		
		return configurationRepository.saveAndFlush(configuration);
	}

	@Transactional
	public void remove(Configuration configuration) {
		configurationRepository.delete(configuration);
	}

	public Configuration find(Tenant tenant) {
		return null;//TODO configurationRepository.findByTenant(tenant);
	}
}
