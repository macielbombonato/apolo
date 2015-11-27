package apolo.business.service.impl;

import apolo.business.model.FileContent;
import apolo.business.service.FileService;
import apolo.business.service.TenantService;
import apolo.common.config.model.ApplicationProperties;
import apolo.common.exception.AccessDeniedException;
import apolo.common.exception.BusinessException;
import apolo.common.util.MessageBundle;
import apolo.data.enums.Status;
import apolo.data.model.Tenant;
import apolo.data.repository.TenantRepository;
import apolo.security.UserPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service("tenantService")
public class TenantServiceImpl extends BaseServiceImpl<Tenant> implements TenantService {

	@Autowired
	private TenantRepository tenantRepository;

	@Autowired
	private FileService<Tenant> fileService;

	@Autowired
	private ApplicationProperties applicationProperties;

	public List<Tenant> list() {
		List<Tenant> result = tenantRepository.findByStatusNotOrderByNameAsc(Status.LOCKED);

		return result;
	}

	public Page<Tenant> list(Integer pageNumber) {
		if (pageNumber < 1) {
			pageNumber = 1;
		}

		PageRequest request = new PageRequest(pageNumber - 1, PAGE_SIZE, Sort.Direction.ASC, "name");

		return tenantRepository.findAll(request);
	}

	public Tenant find(Long id) {
		Tenant result = null;

		// Only users with permission can find anothers tenants, unless, they only can view your own tenant.
		if (getAuthenticatedUser() != null) {
			if (getAuthenticatedUser().getPermissions().contains(UserPermission.ADMIN)
					|| getAuthenticatedUser().getPermissions().contains(UserPermission.TENANT_MANAGER)) {
				result = tenantRepository.findOne(id);
			} else {
				result = getAuthenticatedUser().getTenant();
			}
		}

		return result;
	}

	public Tenant find(Tenant tenant, Long id) {
		return find(id);
	}

	@Transactional
	public Tenant save(Tenant entity) {
		return save(entity, null, null);
	}

	@Transactional
	public Tenant save(Tenant tenant, FileContent logo, FileContent icon) {

		if (tenant != null
				&& tenant.getId() == null) {
			tenant.setCreatedBy(getAuthenticatedUser());
			tenant.setCreatedAt(new Date());
		} else if (tenant != null
				&& tenant.getId() != null
				&& (tenant.getEmailPassword() == null
				|| "".equals(tenant.getEmailPassword()))) {
			Tenant dbTenant = this.find(tenant.getId());

			tenant.setEmailPassword(dbTenant.getEmailPassword());
		}

		if (logo != null
				&& logo.getFile() != null
				&& logo.getFile().getOriginalFilename() != null
				&& !logo.getFile().getOriginalFilename().isEmpty()) {

			if (tenant.getId() == null) {
				tenantRepository.saveAndFlush(tenant);
			}

			try {
				tenant.setLogo(
						fileService.uploadFile(
								tenant,
								tenant,
								logo,
								"logo",
								logo.getFile().getInputStream()
						)
				);
			} catch (IOException e) {
				String message = MessageBundle.getMessageBundle("error.500.5");
				throw new BusinessException(5, message);
			}
		}

		if (icon != null
				&& icon.getFile() != null
				&& icon.getFile().getOriginalFilename() != null
				&& !icon.getFile().getOriginalFilename().isEmpty()) {

			if (tenant.getId() == null) {
				tenantRepository.saveAndFlush(tenant);
			}

			try {
				tenant.setIcon(
						fileService.uploadFile(
								tenant,
								tenant,
								icon,
								"icon",
								icon.getFile().getInputStream()
						)
				);
			} catch (IOException e) {
				String message = MessageBundle.getMessageBundle("error.500.5");
				throw new BusinessException(5, message);
			}
		}

		Tenant result = null;

		try {
			tenant.setUpdatedBy(getAuthenticatedUser());
			tenant.setUpdatedAt(new Date());

			result = tenantRepository.saveAndFlush(tenant);
		} catch (Throwable e) {
			LOG.error(e.getMessage(), e);

			String message = MessageBundle.getMessageBundle("error.500.6");
			throw new BusinessException(5, message);
		}

		return result;
	}

	@Transactional
	public void remove(Tenant entity) {
		tenantRepository.delete(entity);
	}

	public Page<Tenant> search(Integer pageNumber, String param) {
		if (pageNumber < 1) {
			pageNumber = 1;
		}

		PageRequest request = new PageRequest(pageNumber - 1, PAGE_SIZE, Sort.Direction.ASC, "name");

		if (param != null) {
			param = "%" + param + "%";
		}

		return tenantRepository.findByUrlLikeOrNameLikeOrderByNameAsc(param, param, request);
	}

	public Tenant findByUrl(String url) {
		Tenant result = null;

		result = tenantRepository.findByUrl(url);

		return result;
	}

	public List<Tenant> list(Tenant tenant) {
		// not used in this service
		return list();
	}

	public Page<Tenant> list(Tenant tenant, Integer pageNumber) {
		// not used in this service
		return list(pageNumber);
	}

	public Tenant lock(Tenant entity) {
		if (getAuthenticatedUser().getTenant().equals(entity)) {
			String message = MessageBundle.getMessageBundle("error.403.6");
			throw new AccessDeniedException(6, message);
		} else if (entity.getUrl().equals(applicationProperties.getDefaultTenant())) {
			String message = MessageBundle.getMessageBundle("error.403.7");
			throw new AccessDeniedException(7, message);
		} else {
			entity.setStatus(Status.LOCKED);
			return tenantRepository.save(entity);
		}
	}

	public Tenant unlock(Tenant entity) {
		entity.setStatus(Status.ACTIVE);
		return tenantRepository.save(entity);
	}

	public Tenant getValidatedTenant(String tenantUrl) {
		Tenant tenant = null;

		tenant = findByUrl(tenantUrl);

		if (tenant == null) {
			tenant = findByUrl(applicationProperties.getDefaultTenant());
		}

		if (tenant != null) {
			if (tenant.getEmailFrom() == null || "".equals(tenant.getEmailFrom())) {
				tenant.setEmailFrom(applicationProperties.getEmailFrom());
				tenant.setEmailPassword(applicationProperties.getEmailPassword());
				tenant.setSmtpHost(applicationProperties.getSmtpHost());
				tenant.setSmtpPort(applicationProperties.getSmtpPort());
				tenant.setUseTLS(applicationProperties.getUseTLS());

				tenant.setSendAuthEmail(applicationProperties.getSendAuthEmail());
			}

		} else {
			String message = MessageBundle.getMessageBundle("tenant.not.found");
			throw new org.springframework.security.access.AccessDeniedException(message);
		}

		return tenant;
	}

	public long count() {
		long result = 0L;
		if (getAuthenticatedUser() != null
				&& getAuthenticatedUser().getPermissions().contains(UserPermission.ADMIN)) {
			result = tenantRepository.count();
		}

		return result;
	}

}
