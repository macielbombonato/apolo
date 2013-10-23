package br.apolo.business.service.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.apolo.business.service.UserCustomFieldService;
import br.apolo.common.exception.AccessDeniedException;
import br.apolo.data.model.UserCustomField;
import br.apolo.data.model.UserCustomFieldOption;
import br.apolo.data.repository.UserCustomFieldRepository;

@Service("userCustomFieldService")
public class UserCustomFieldServiceImpl extends BaseServiceImpl<UserCustomField> implements UserCustomFieldService {

	@Autowired
	private UserCustomFieldRepository userCustomFieldRepository;
	
	@Override
	public List<UserCustomField> list() {
		return userCustomFieldRepository.findAll();
	}
	
	@Override
	public Page<UserCustomField> list(Integer pageNumber) {
		if (pageNumber < 1) {
			pageNumber = 1;
		}
		
		PageRequest request = new PageRequest(pageNumber - 1, PAGE_SIZE);
		
		return userCustomFieldRepository.findAll(request);
	}

	@Override
	public UserCustomField find(Long id) {
		return userCustomFieldRepository.findOne(id);
	}

	@Override
	@Transactional
	public UserCustomField save(UserCustomField userCustomField) throws AccessDeniedException {
		if (userCustomField != null) {
			userCustomField.setLastUpdatedBy(getAuthenticatedUser());
			userCustomField.setLastUpdateDate(new Date());
			
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

	@Override
	@Transactional
	public void remove(UserCustomField userCustomField) throws AccessDeniedException {
		userCustomFieldRepository.delete(userCustomField);
	}

	@Override
	public Page<UserCustomField> search(Integer pageNumber, String param) {
		if (pageNumber < 1) {
			pageNumber = 1;
		}
		
		PageRequest request = new PageRequest(pageNumber - 1, PAGE_SIZE, Sort.Direction.ASC, "name");
		
		if (param != null) {
			param = "%" + param + "%";	
		}
		
		return userCustomFieldRepository.findByNameLikeOrderByNameAsc(param, request);
	}
}
