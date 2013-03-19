package br.apolo.business.service;

import java.io.InputStream;

import br.apolo.business.model.FileContent;
import br.apolo.data.model.BaseEntity;

public interface FileService<E extends BaseEntity> {
	
	String uploadFile(E entity, FileContent file, InputStream inputStream);
	
	String extractFileExtension(String fileName);
	
}
