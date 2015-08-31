package apolo.web.controller;

import apolo.common.config.model.ApplicationProperties;
import apolo.data.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.File;

@Controller
public class UploadedFilesController extends BaseController<User> {

	@Autowired
	ApplicationProperties applicationProperties;

    @SuppressWarnings("rawtypes")
	@PreAuthorize("permitAll")
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
	@PreAuthorize("permitAll")
	@RequestMapping(value = "/{tenant}/uploadedfiles/{entity}/{id}/{fileName}.{extension}" , method = RequestMethod.GET)
    public ResponseEntity<FileSystemResource> getFile(
    		@PathVariable("tenant") String tenant,
    		@PathVariable("entity") String entity,
    		@PathVariable("id") String id,
    		@PathVariable("fileName") String fileName,
    		@PathVariable("extension") String extension
    		) {
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
