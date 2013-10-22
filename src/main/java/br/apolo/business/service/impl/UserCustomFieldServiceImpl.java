package br.apolo.business.service.impl;

import java.util.Date;
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
		userCustomField.setLastUpdatedBy(getAuthenticatedUser());
		userCustomField.setLastUpdateDate(new Date());
		
		return userCustomFieldRepository.save(userCustomField);
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
