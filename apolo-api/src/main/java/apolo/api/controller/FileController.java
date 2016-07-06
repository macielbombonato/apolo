package apolo.api.controller;

import apolo.api.apimodel.FileDTO;
import apolo.api.controller.base.BaseAPIController;
import apolo.common.config.model.ApplicationProperties;
import apolo.common.exception.AccessDeniedException;
import apolo.common.util.MessageBundle;
import apolo.data.model.base.BaseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

@Controller
@RequestMapping(value = "/file")
public class FileController extends BaseAPIController<BaseEntity> {

	@Autowired
	ApplicationProperties applicationProperties;

	@CrossOrigin(origins = "*")
	@SuppressWarnings("rawtypes")
	@PreAuthorize("permitAll")
	@RequestMapping(value = "/{tenant}/{entity}/{id}/{fileName}.{extension}" , method = RequestMethod.GET)
    public ResponseEntity<FileSystemResource> getFile(
    		@PathVariable("tenant") String tenant,
    		@PathVariable("entity") String entity,
    		@PathVariable("id") String id,
    		@PathVariable("fileName") String fileName,
    		@PathVariable("extension") String extension,
			HttpServletRequest request,
			HttpServletResponse response
    		) {

//		if (isAutheticated(request)) {
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
//		} else {
//			response.setStatus(403);
//			throw new AccessDeniedException(MessageBundle.getMessageBundle("error.403"));
//		}

    }

	@CrossOrigin(origins = "*")
	@SuppressWarnings("rawtypes")
	@PreAuthorize("permitAll")
	@RequestMapping(value = "/{tenant}/{entity}/{id}" , method = RequestMethod.POST)
	public ResponseEntity<FileSystemResource> putFile(
			@PathVariable("tenant") String tenant,
			@PathVariable("entity") String entity,
			@PathVariable("id") String id,
			FileDTO file,
			HttpServletRequest request,
			HttpServletResponse response
	) {

		if (isAutheticated(request)) {

			return null;
		} else {
			response.setStatus(403);
			throw new AccessDeniedException(MessageBundle.getMessageBundle("error.403"));
		}

	}
}
