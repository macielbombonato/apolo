package apolo.business.service;

import apolo.business.model.FileContent;
import apolo.data.model.base.BaseEntity;
import apolo.data.model.Tenant;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@SuppressWarnings("rawtypes")
public interface FileService<E extends BaseEntity> {
	
	String uploadFile(Tenant tenant, E entity, FileContent file, String filename, InputStream inputStream);
	
	String extractFileExtension(String fileName);
	
	void copyFile(File sourceFile, File destFile) throws IOException;
	
	void copyDirectory(File sourceLocation , File targetLocation) throws IOException;
	
	void zipFolder(String directory) throws IOException;
	
	void convertTextFileToHtmlFile(E entity, String sourceName, String destName);
	
	void delete(File directory) throws IOException;

	File getFile(Tenant tenant, E entity, String filename);
	
}
