package apolo.business.service.impl;

import apolo.business.model.FileContent;
import apolo.business.service.FileService;
import apolo.common.config.model.ApplicationProperties;
import apolo.common.exception.GenericException;
import apolo.common.util.MessageBundle;
import apolo.data.model.base.BaseEntity;
import apolo.data.model.Tenant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@SuppressWarnings("rawtypes")
@Service("fileService")
public class FileServiceImpl<E extends BaseEntity> implements FileService<E> {
	
	private static final Logger log = LoggerFactory.getLogger(FileServiceImpl.class);
	
	@Autowired
	private ApplicationProperties applicationProperties;
	
	public String uploadFile(Tenant tenant, E entity, FileContent file, String filename, InputStream inputStream) {
        filename = filename + extractFileExtension(file.getFile().getOriginalFilename());
		
		byte[] buffer = new byte[8 * 1024];
		
		try {
			File f = null;

			/*
			 * @see validateFileName javadoc
			 */
            filename = validateFileName(filename);

			String filePath = 
					applicationProperties.getUploadedFilesPath() + 
					tenant.getUrl() + 
					File.separator +
					entity.getClass().getSimpleName() + 
					File.separator +
					entity.getId();

			f = createAndValidateFile(filePath, true);
			f = createAndValidateFile(filePath + File.separator + filename, false);

			OutputStream output = new FileOutputStream(f);
			int bytesRead;
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				output.write(buffer, 0, bytesRead);
			}
			output.close();

			return filename;

		} catch (IOException e) {
			String message = MessageBundle.getMessageBundle("commons.errorUploadingFile");
			throw new GenericException(message);
		}
	}
	
	public void convertTextFileToHtmlFile(E entity, String sourceName, String destName) {
		
		String filePath = applicationProperties.getUploadedFilesPath() + 
				entity.getClass().getSimpleName() + 
				File.separator +
				entity.getId();
		
		String sourceFilePath = filePath +
				File.separator +
				sourceName;
		
		String destFilePath = filePath +
				File.separator +
				destName;
		
		PrintWriter writer;
		try {
			writer = new PrintWriter(destFilePath, "UTF-8");
			writer.println("<html>");
			writer.println("  <body>");
			writer.println("    <p>");

			BufferedReader reader = new BufferedReader(new FileReader(sourceFilePath));
			String line = null;
			while ((line = reader.readLine()) != null) {
				writer.println("      " + line + " <br/>");
			}
			
			writer.println("    </p>");
			writer.println("  </body>");
			writer.println("</html>");
			writer.close();
			
			reader.close();
		} catch (FileNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage(), e);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
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

		if (isDirectory) {
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
	
	public void copyFile(File sourceFile, File destFile) throws IOException {
	    if(!destFile.exists()) {
	        destFile.createNewFile();
	    }

	    FileChannel source = null;
	    FileChannel destination = null;
	    try {
	        FileInputStream fileInputStream = new FileInputStream(sourceFile);
			source = fileInputStream.getChannel();
	        
	        FileOutputStream fileOutputStream = new FileOutputStream(destFile);
			destination = fileOutputStream.getChannel();
	        destination.force(true);

	        long count = 0;
	        long size = source.size();              
	        while((count += destination.transferFrom(source, count, size-count))<size);
	        
	        fileInputStream.close();
	        fileOutputStream.close();
	    } finally {
	        if(source != null) {
	            source.close();
	        }
	        if(destination != null) {
	            destination.close();
	        }
	    }
	} 
	
	public void delete(File directory) throws IOException {
		if(directory.isDirectory()){
	    	//directory is empty, then delete it
			if(directory.list().length == 0){
				directory.delete();
			} else {
				//list all the directory contents
				String files[] = directory.list();

				for (String temp : files) {
					//construct the file structure
					File fileDelete = new File(directory, temp);
					//recursive delete
					delete(fileDelete);
				}
	        	   
				//check the directory again, if empty then delete it
				if(directory.list().length == 0){
					directory.delete();
				}
			}
		} else {
			//if file, then delete it
			directory.delete();
		}
	}

	@Override
	public File getFile(Tenant tenant, E entity, String filename) {
		File result = null;

		filename = validateFileName(filename);

		String filePath =
				applicationProperties.getUploadedFilesPath() +
						tenant.getUrl() +
						File.separator +
						entity.getClass().getSimpleName() +
						File.separator +
						entity.getId();

		result = createAndValidateFile(filePath, true);
		result = createAndValidateFile(filePath + File.separator + filename, false);

		return result;
	}

	public void copyDirectory(File sourceLocation , File targetLocation) throws IOException {
	    if (sourceLocation.isDirectory()) {
	        if (!targetLocation.exists()) {
	            targetLocation.mkdirs();
	        }

	        String[] children = sourceLocation.list();
	        for (int i=0; i<children.length; i++) {
	            copyDirectory(new File(sourceLocation, children[i]),
	                    new File(targetLocation, children[i]));
	        }
	    } else {

	        InputStream in = new FileInputStream(sourceLocation);
	        OutputStream out = new FileOutputStream(targetLocation);

	        byte[] buf = new byte[1024];
	        int len;
	        while ((len = in.read(buf)) > 0) {
	            out.write(buf, 0, len);
	        }
	        in.close();
	        out.close();
	    }
	}

	public void zipFolder(String directory) throws IOException {
		File directoryToZip = new File(directory);
		List<File> fileList = new ArrayList<File>();
		getAllFiles(directoryToZip, fileList);
		writeZipFile(directoryToZip, fileList);
	}

	private void getAllFiles(File dir, List<File> fileList) {
		File[] files = dir.listFiles();
		for (File file : files) {
			fileList.add(file);
			if (file.isDirectory()) {
				getAllFiles(file, fileList);
			} 
		}
	}

	private void writeZipFile(File directoryToZip, List<File> fileList) {
		try {
			File dir = new File(applicationProperties.getUploadedFilesPath() + "ZipPackages");
			if (!dir.exists()) {
				dir.mkdirs();
			}
			
			File zipFile = new File(
					applicationProperties.getUploadedFilesPath() + 
					"ZipPackages" + 
					File.separator +  
					directoryToZip.getName() + 
					".zip"
				);
			
			if (zipFile.exists()) {
				delete(zipFile);
				zipFile.createNewFile();
			}
			
			FileOutputStream fos = new FileOutputStream(zipFile);
			
			ZipOutputStream zos = new ZipOutputStream(fos);

			for (File file : fileList) {
				if (!file.isDirectory()) { 
					addToZip(directoryToZip, file, zos);
				}
			}

			zos.close();
			fos.close();
		} catch (FileNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}

	private void addToZip(File directoryToZip, File file, ZipOutputStream zos) throws FileNotFoundException, IOException {

		FileInputStream fis = new FileInputStream(file);

		String zipFilePath = file.getCanonicalPath().substring(directoryToZip.getCanonicalPath().length() + 1,
				file.getCanonicalPath().length());
		
		ZipEntry zipEntry = new ZipEntry(zipFilePath);
		zos.putNextEntry(zipEntry);

		byte[] bytes = new byte[1024];
		int length;
		while ((length = fis.read(bytes)) >= 0) {
			zos.write(bytes, 0, length);
		}

		zos.closeEntry();
		fis.close();
	}
	
}
