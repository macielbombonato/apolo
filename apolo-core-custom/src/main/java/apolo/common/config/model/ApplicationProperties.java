package apolo.common.config.model;

public class ApplicationProperties {
	
	private String secretKey;

	private String ivKey;

	private String uploadedFilesPath;

	private String defaultTenant;

	private String emailFrom;

	private String emailUsername;

	private String emailPassword;

	private String smtpHost;

	private String smtpPort;

	private Boolean useTLS;

	private String googleAdClient;

	private String googleAdSlotOne;

	private String googleAdSlotTwo;

	private String googleAdSlotThree;

	private String googleAnalyticsUserAccount;

	private Boolean sendAuthEmail;

	private String facebookAppId;

	private String facebookAppSecret;

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

	public String getEmailFrom() {
		return emailFrom;
	}

	public void setEmailFrom(String emailFrom) {
		this.emailFrom = emailFrom;
	}

	public String getEmailPassword() {
		return emailPassword;
	}

	public void setEmailPassword(String emailPassword) {
		this.emailPassword = emailPassword;
	}

	public String getSmtpHost() {
		return smtpHost;
	}

	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}

	public String getSmtpPort() {
		return smtpPort;
	}

	public void setSmtpPort(String smtpPort) {
		this.smtpPort = smtpPort;
	}

	public Boolean getUseTLS() {
		return useTLS;
	}

	public void setUseTLS(Boolean useTLS) {
		this.useTLS = useTLS;
	}

	public String getGoogleAdClient() {
		return googleAdClient;
	}

	public void setGoogleAdClient(String googleAdClient) {
		this.googleAdClient = googleAdClient;
	}

	public String getGoogleAdSlotOne() {
		return googleAdSlotOne;
	}

	public void setGoogleAdSlotOne(String googleAdSlotOne) {
		this.googleAdSlotOne = googleAdSlotOne;
	}

	public String getGoogleAdSlotTwo() {
		return googleAdSlotTwo;
	}

	public void setGoogleAdSlotTwo(String googleAdSlotTwo) {
		this.googleAdSlotTwo = googleAdSlotTwo;
	}

	public String getGoogleAdSlotThree() {
		return googleAdSlotThree;
	}

	public void setGoogleAdSlotThree(String googleAdSlotThree) {
		this.googleAdSlotThree = googleAdSlotThree;
	}

	public String getGoogleAnalyticsUserAccount() {
		return googleAnalyticsUserAccount;
	}

	public void setGoogleAnalyticsUserAccount(String googleAnalyticsUserAccount) {
		this.googleAnalyticsUserAccount = googleAnalyticsUserAccount;
	}

	public Boolean getSendAuthEmail() {
		return sendAuthEmail;
	}

	public void setSendAuthEmail(Boolean sendAuthEmail) {
		this.sendAuthEmail = sendAuthEmail;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getIvKey() {
		return ivKey;
	}

	public void setIvKey(String ivKey) {
		this.ivKey = ivKey;
	}

	public String getFacebookAppId() {
		return facebookAppId;
	}

	public void setFacebookAppId(String facebookAppId) {
		this.facebookAppId = facebookAppId;
	}

	public String getFacebookAppSecret() {
		return facebookAppSecret;
	}

	public void setFacebookAppSecret(String facebookAppSecret) {
		this.facebookAppSecret = facebookAppSecret;
	}

	public String getEmailUsername() {
		return emailUsername;
	}

	public void setEmailUsername(String emailUsername) {
		this.emailUsername = emailUsername;
	}
}
