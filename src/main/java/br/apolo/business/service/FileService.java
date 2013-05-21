package br.apolo.business.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import br.apolo.business.model.FileContent;
import br.apolo.data.model.BaseEntity;

public interface FileService<E extends BaseEntity> {
	
	String uploadFile(E entity, FileContent file, InputStream inputStream);
	
	String extractFileExtension(String fileName);
	
	void copyFile(File sourceFile, File destFile) throws IOException;
	
	void copyDirectory(File sourceLocation , File targetLocation) throws IOException;
	
	void zipFolder(String directory) throws IOException;
	
	void convertTextFileToHtmlFile(E entity, String sourceName, String destName);
	
	void delete(File directory) throws IOException;
	
	void convertMSOfficeFilesToPDF(E entity, String sourceName);
	
	int convertPDFToImages(E entity);
	
}
