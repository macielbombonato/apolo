package br.apolo.business.service;

import org.springframework.data.domain.Page;

import br.apolo.data.model.UserCustomField;

public interface UserCustomFieldService extends BaseService<UserCustomField> {
	
	final int PAGE_SIZE = 20;

	Page<UserCustomField> search(Integer pageNumber, String param);
	
}