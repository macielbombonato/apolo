package apolo.business.service;

import apolo.data.model.Tenant;
import apolo.data.model.UserCustomField;

import org.springframework.data.domain.Page;

public interface UserCustomFieldService extends BaseService<UserCustomField> {
	
	final int PAGE_SIZE = 20;

	Page<UserCustomField> search(Tenant tenant, Integer pageNumber, String param);
	
}