package apolo.business.service.impl;

import apolo.business.service.UserCustomFieldService;
import apolo.data.model.Tenant;
import apolo.data.model.UserCustomField;
import apolo.data.model.UserCustomFieldOption;
import apolo.data.repository.UserCustomFieldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Service("userCustomFieldService")
public class UserCustomFieldServiceImpl extends BaseServiceImpl<UserCustomField> implements UserCustomFieldService {

	@Autowired
	private UserCustomFieldRepository userCustomFieldRepository;
	
	public List<UserCustomField> list(Tenant tenant) {
		List<UserCustomField> result = userCustomFieldRepository.findByTenantOrderByNameAsc(tenant);
		
		return result;
	}
	
	public Page<UserCustomField> list(Tenant tenant, Integer pageNumber) {
		if (pageNumber < 1) {
			pageNumber = 1;
		}
		
		PageRequest request = new PageRequest(pageNumber - 1, PAGE_SIZE, Sort.Direction.ASC, "name");
		
		Page<UserCustomField> result = userCustomFieldRepository.findByTenantOrderByNameAsc(tenant, request);
		
		return result;
	}

	public UserCustomField find(Tenant tenant, Long id) {
		return userCustomFieldRepository.findByTenantAndId(tenant, id);
	}

	@Transactional
	public UserCustomField save(UserCustomField userCustomField) {
		if (userCustomField != null) {
			userCustomField.setUpdatedBy(getAuthenticatedUser());
			userCustomField.setUpdatedAt(new Date());
			
			if (userCustomField.getOptionsStringList() != null 
					&& !userCustomField.getOptionsStringList().isEmpty()) {
				if (userCustomField.getOptions() == null) {
					userCustomField.setOptions(new HashSet<UserCustomFieldOption>());
				} else {
					userCustomField.getOptions().clear();
				}
				
				UserCustomFieldOption option = null;
				for (String optionString : userCustomField.getOptionsStringList()) {
					option = new UserCustomFieldOption();
					option.setUserCustomField(userCustomField);
					option.setValue(optionString);
					
					userCustomField.getOptions().add(option);
				}
			} else {
				userCustomField.setOptions(new HashSet<UserCustomFieldOption>());
			}
		}
		
		
		return userCustomFieldRepository.saveAndFlush(userCustomField);
	}

	@Transactional
	public void remove(UserCustomField userCustomField) {
		userCustomFieldRepository.delete(userCustomField);
	}

	public Page<UserCustomField> search(Tenant tenant, Integer pageNumber, String param) {
		if (pageNumber < 1) {
			pageNumber = 1;
		}
		
		PageRequest request = new PageRequest(pageNumber - 1, PAGE_SIZE, Sort.Direction.ASC, "name");
		
		if (param != null) {
			param = "%" + param + "%";	
		}
		
		return userCustomFieldRepository.findByTenantAndNameLikeOrderByNameAsc(tenant, param, request);
	}
}
