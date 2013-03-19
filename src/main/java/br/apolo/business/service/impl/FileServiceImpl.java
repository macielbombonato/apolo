package br.apolo.business.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.springframework.stereotype.Service;

import br.apolo.business.model.FileContent;
import br.apolo.business.service.FileService;
import br.apolo.common.exception.GenericException;
import br.apolo.common.util.AppConfig;
import br.apolo.common.util.MessageBundle;
import br.apolo.data.model.BaseEntity;

@Service("fileService")
public class FileServiceImpl<E extends BaseEntity> implements FileService<E> {

	@Override
	public String uploadFile(E entity, FileContent file, InputStream inputStream) {
		String fileName = entity.getId() + extractFileExtension(file.getFile().getOriginalFilename());
		
		byte[] buffer = new byte[8 * 1024];
		
		try {
			File f = null;

			/*
			 * @see validateFileName javadoc
			 */
			fileName = validateFileName(fileName);

			String filePath = 
					AppConfig.getObjectFilesPath() + 
					entity.getClass().getSimpleName() + 
					File.separator +
					entity.getId();

			f = createAndValidateFile(filePath, true);
			f = createAndValidateFile(filePath + File.separator + fileName, false);

			OutputStream output = new FileOutputStream(f);
			int bytesRead;
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				output.write(buffer, 0, bytesRead);
			}
			output.close();

			return fileName;

		} catch (IOException e) {
			String message = MessageBundle.getMessageBundle("commons.errorUploadingFile");
			throw new GenericException(message);
		}
	}
	
	/**
	 * Internet Explorer Browser send the full path in field file name. We do this validation to prevent incorrect data stored in
	 * the database or send incorrect path to method createAndValidateFile
	 * 
	 * @param fileName
	 * @return new fileName String without full client path
	 */
	private String validateFileName(String fileName) {
		if (fileName != null && fileName.contains(File.separator)) {
			/*
			 * If the second syntax is used in Windows OS we'll take an exception
			 */
			if ("\\".equals(File.separator)) {
				int index = fileName.split("\\\\").length;
				fileName = fileName.split("\\\\")[index - 1];
			} else {
				int index = fileName.split(File.separator).length;
				fileName = fileName.split(File.separator)[index - 1];
			}
		}
		return fileName;
	}
	
	private File createAndValidateFile(String name, boolean isDirectory) {
		File file = new File(name);

		if (!file.exists() && isDirectory) {
			file.mkdirs();
		}

		return file;
	}
	
	public String extractFileExtension(String fileName) {
		if (fileName != null && !fileName.isEmpty()) {
			/*
			 * Split and replaceAll have problems when we use dot ('.'). To resolve this problems, first we change dot to another
			 * character and split using this character.
			 */
			String[] splitedFileName = fileName.replace(".", "@@").split("@@");
			return "." + splitedFileName[splitedFileName.length - 1].toLowerCase();
		}

		String message = MessageBundle.getMessageBundle("commons.illegalFilenameException");
		throw new GenericException(message);
	}

}
