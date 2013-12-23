package br.apolo.web.controller;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.apolo.common.config.model.ApplicationProperties;

@Controller
@RequestMapping(value = "/uploadedfiles")
public class UploadedFilesController {

	@Autowired
	ApplicationProperties applicationProperties;
	
	@SuppressWarnings("rawtypes")
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "{entity}/{id}/{fileName}.{extension}" , method = RequestMethod.GET) 
    public ResponseEntity<FileSystemResource> getFile(
    		@PathVariable("entity") String entity,
    		@PathVariable("id") String id,
    		@PathVariable("fileName") String fileName,
    		@PathVariable("extension") String extension
    		) {
		String filePath = 
				applicationProperties.getUploadedFilesPath() + 
				entity +
				File.separator +
				id;
		
        FileSystemResource resource = new FileSystemResource(new File(filePath, fileName + "." + extension));
		@SuppressWarnings("unchecked")
		ResponseEntity<FileSystemResource> responseEntity = new ResponseEntity(resource, HttpStatus.OK);
        return responseEntity;
    }
	
}
