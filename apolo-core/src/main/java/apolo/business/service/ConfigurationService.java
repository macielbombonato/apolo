package apolo.business.service;

import apolo.data.model.Configuration;
import apolo.data.model.Tenant;

public interface ConfigurationService extends BaseService<Configuration> {
	
	final int PAGE_SIZE = 100;
	
	Configuration find(Tenant tenant);

}