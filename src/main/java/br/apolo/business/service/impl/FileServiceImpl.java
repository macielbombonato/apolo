package br.apolo.business.service.impl;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;

import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.usermodel.SlideShow;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import br.apolo.business.model.FileContent;
import br.apolo.business.service.FileService;
import br.apolo.common.exception.GenericException;
import br.apolo.common.util.AppConfig;
import br.apolo.common.util.MessageBundle;
import br.apolo.data.model.BaseEntity;

@Service("fileService")
public class FileServiceImpl<E extends BaseEntity> implements FileService<E> {
	
	private static final Logger log = LoggerFactory.getLogger(FileServiceImpl.class);

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
	
	public void convertTextFileToHtmlFile(E entity, String sourceName, String destName) {
		
		String filePath = AppConfig.getObjectFilesPath() + 
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
		} catch (FileNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage(), e);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}

	}
	
	public int convertPPTFileToImages(E entity, String sourceName) {
		
		int pages = 0;
		
		String filePath = AppConfig.getObjectFilesPath() + 
				entity.getClass().getSimpleName() + 
				File.separator +
				entity.getId();
		
		String sourceFilePath = filePath +
				File.separator +
				sourceName;
		
		try {
	        FileInputStream inputStream = new FileInputStream(sourceFilePath);
	        SlideShow ppt = new SlideShow(inputStream);
	        inputStream.close();
	        Dimension pgsize = ppt.getPageSize();

	        Graphics2D graphics = null;
	        BufferedImage image = null;
	        
	        for(Slide slide : ppt.getSlides()){
	        	image = new BufferedImage(pgsize.width * 2, pgsize.height * 2, BufferedImage.TYPE_INT_RGB);
	        	graphics = (Graphics2D) image.createGraphics();
	        	graphics.scale(2.0, 2.0);

	        	slide.draw(graphics);
	        	
	    		try {
	    			File file = new File(filePath + File.separator + entity.getId() + "_" + (++pages) + ".png");
	    			ImageIO.write(image, "png", file);
	    		} catch (IOException e) {
	    			log.error(e.getMessage(), e);
	    		}
	    		
	    		graphics.dispose();
	        }
		} catch (FileNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage(), e);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}

		return pages;
	}
	
	public int convertPPTxFileToImages(E entity, String sourceName) {
		
		int pages = 0;
		
		String filePath = AppConfig.getObjectFilesPath() + 
				entity.getClass().getSimpleName() + 
				File.separator +
				entity.getId();
		
		String sourceFilePath = filePath +
				File.separator +
				sourceName;
		
		try {
	        FileInputStream inputStream = new FileInputStream(sourceFilePath);
	        
	        XMLSlideShow ppt = new XMLSlideShow(OPCPackage.open(sourceFilePath));
	        
	        inputStream.close();
	        Dimension pgsize = ppt.getPageSize();

	        Graphics2D graphics = null;
	        BufferedImage image = null;
	        
	        for(XSLFSlide slide : ppt.getSlides()){
	        	image = new BufferedImage(pgsize.width * 2, pgsize.height * 2, BufferedImage.TYPE_INT_RGB);
	        	graphics = (Graphics2D) image.createGraphics();
	        	graphics.scale(2.0, 2.0);

	        	slide.draw(graphics);
	        	
	    		try {
	    			File file = new File(filePath + File.separator + entity.getId() + "_" + (++pages) + ".png");
	    			ImageIO.write(image, "png", file);
	    		} catch (IOException e) {
	    			log.error(e.getMessage(), e);
	    		}
	    		
	    		graphics.dispose();
	        }
		} catch (FileNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage(), e);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		} catch (InvalidFormatException e) {
			log.error(e.getMessage(), e);
		}

		return pages;
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

		if (isDirectory && file.exists()) {
			try {
				delete(file);
				
				file.mkdirs();
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		} else if (isDirectory && !file.exists()) {
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
	        source = new FileInputStream(sourceFile).getChannel();
	        
	        destination = new FileOutputStream(destFile).getChannel();
	        destination.force(true);

	        long count = 0;
	        long size = source.size();              
	        while((count += destination.transferFrom(source, count, size-count))<size);
	    }
	    finally {
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
			File dir = new File(AppConfig.getObjectFilesPath() + "ZipPackages");
			if (!dir.exists()) {
				dir.mkdirs();
			}
			
			File zipFile = new File(
					AppConfig.getObjectFilesPath() + 
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
