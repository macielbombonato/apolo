package apolo.web.controller;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import apolo.common.config.model.ApplicationProperties;
import apolo.data.model.User;
import apolo.security.SecuredEnum;
import apolo.security.UserPermission;

@Controller
public class UploadedFilesController extends BaseController<User> {

	@Autowired
	ApplicationProperties applicationProperties;

    @SuppressWarnings("rawtypes")
    @SecuredEnum(UserPermission.AFTER_AUTH_USER)
    @RequestMapping(value = "/uploadedfiles/{entity}/{id}/{fileName}.{extension}" , method = RequestMethod.GET)
    public ResponseEntity<FileSystemResource> getFile(
            @PathVariable("entity") String entity,
            @PathVariable("id") String id,
            @PathVariable("fileName") String fileName,
            @PathVariable("extension") String extension
    ) {
        return getFile(
                applicationProperties.getDefaultTenant(),
                entity,
                id,
                fileName,
                extension
        );
    }

	@SuppressWarnings("rawtypes")
	@SecuredEnum(UserPermission.AFTER_AUTH_USER)
	@RequestMapping(value = "/{tenant}/uploadedfiles/{entity}/{id}/{fileName}.{extension}" , method = RequestMethod.GET)
    public ResponseEntity<FileSystemResource> getFile(
    		@PathVariable("tenant") String tenant,
    		@PathVariable("entity") String entity,
    		@PathVariable("id") String id,
    		@PathVariable("fileName") String fileName,
    		@PathVariable("extension") String extension
    		) {
		validatePermissions(
				UserPermission.AFTER_AUTH_USER
			);
		
		String filePath = 
				applicationProperties.getUploadedFilesPath() + 
				tenant +
				File.separator +
				entity +
				File.separator +
				id;
		
        FileSystemResource resource = new FileSystemResource(new File(filePath, fileName + "." + extension));
		@SuppressWarnings("unchecked")
		ResponseEntity<FileSystemResource> responseEntity = new ResponseEntity(resource, HttpStatus.OK);
        return responseEntity;
    }
}
