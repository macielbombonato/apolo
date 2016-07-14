package apolo.api.controller;

import apolo.api.apimodel.FileDTO;
import apolo.api.controller.base.BaseAPIController;
import apolo.common.config.model.ApplicationProperties;
import apolo.common.exception.AccessDeniedException;
import apolo.common.exception.BusinessException;
import apolo.common.util.MessageBundle;
import apolo.data.model.base.BaseEntity;
import com.google.common.io.ByteStreams;
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
import java.io.IOException;

@Controller
@RequestMapping(value = "/file")
public class FileController extends BaseAPIController<BaseEntity> {

	@Autowired
	ApplicationProperties applicationProperties;

	@CrossOrigin(origins = "*")
	@PreAuthorize("permitAll")
	@RequestMapping(value = "/{tenant}/{entity}/{id}/{fileName}.{extension}" , method = RequestMethod.GET)
    public ResponseEntity<byte[]> getFile(
    		@PathVariable("tenant") String tenant,
    		@PathVariable("entity") String entity,
    		@PathVariable("id") String id,
    		@PathVariable("fileName") String fileName,
    		@PathVariable("extension") String extension,
			HttpServletRequest request,
			HttpServletResponse response
    		) {

		if (getUserFromRequestBySession(request) != null) {
			String filePath =
					applicationProperties.getUploadedFilesPath() +
							tenant +
							File.separator +
							entity +
							File.separator +
							id;

			FileSystemResource resource = new FileSystemResource(new File(filePath, fileName + "." + extension));

			byte[] fileBytes = null;
			try {
				fileBytes = ByteStreams.toByteArray(resource.getInputStream());
			} catch (IOException e) {
				throw new BusinessException(MessageBundle.getMessageBundle("error.500"));
			}

			ResponseEntity<byte[]> responseEntity = new ResponseEntity<byte[]>(fileBytes, HttpStatus.OK);

			return responseEntity;
		} else {
			response.setStatus(403);
			throw new AccessDeniedException(MessageBundle.getMessageBundle("error.403"));
		}
    }

	@CrossOrigin(origins = "*")
	@SuppressWarnings("rawtypes")
	@PreAuthorize("permitAll")
	@RequestMapping(value = "" , method = RequestMethod.POST)
	public ResponseEntity<FileSystemResource> putFile(
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
