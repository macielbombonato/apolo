package apolo.common.config.model;

public class ApplicationProperties {
	
	private String defaultTenant;
	
	private String uploadedFilesPath;
	
	public String getUploadedFilesPath() {
		return uploadedFilesPath;
	}

	public void setUploadedFilesPath(String uploadedFilesPath) {
		this.uploadedFilesPath = uploadedFilesPath;
	}

	public String getDefaultTenant() {
		return this.defaultTenant;
	}

	public void setDefaultTenant(String defaultTenant) {
		this.defaultTenant = defaultTenant;
	}

}
