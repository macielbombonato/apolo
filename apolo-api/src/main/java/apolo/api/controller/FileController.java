package apolo.api.controller;

import apolo.api.controller.base.BaseAPIController;
import apolo.business.service.UserService;
import apolo.common.config.model.ApplicationProperties;
import apolo.common.exception.AccessDeniedException;
import apolo.common.exception.BusinessException;
import apolo.common.util.MessageBundle;
import apolo.data.model.Tenant;
import apolo.data.model.User;
import apolo.data.model.base.BaseEntity;
import apolo.security.Permission;
import com.google.common.io.ByteStreams;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping(value = "/file")
public class FileController extends BaseAPIController<BaseEntity> {

	@Inject
	private ApplicationProperties applicationProperties;

	@Inject
	private UserService userService;

	@CrossOrigin(origins = "*")
	@PreAuthorize("permitAll")
	@RequestMapping(value = "/{tenant}/{entity}/{id}/{fileName}.{extension}" , method = RequestMethod.GET)
    public ResponseEntity<byte[]> getFile(
    		@PathVariable("tenant") String tenantUrl,
    		@PathVariable("entity") String entity,
    		@PathVariable("id") Long id,
    		@PathVariable("fileName") String fileName,
    		@PathVariable("extension") String extension,
			HttpServletRequest request,
			HttpServletResponse response
    		) {

		boolean authorized = true;

		if (request.getUserPrincipal() == null) {
			authorized = false;

			response.setStatus(401);
			throw new AccessDeniedException(MessageBundle.getMessageBundle("error.401"));
		} else {
			List<User> users = userService.findByLogin(request.getUserPrincipal().getName());

			if (users != null
					&& !users.isEmpty()
					&& users.size() > 0) {
				Tenant tenant = users.get(0).getTenant();

				if (!tenantUrl.equals(tenant.getUrl())
						&& !checkAccess(users.get(0), tenantUrl, request, Permission.TENANT_MANAGER)) {
					authorized = false;
				}
			}
		}

		if (authorized) {
			String filePath =
					applicationProperties.getUploadedFilesPath() +
							tenantUrl +
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
}
