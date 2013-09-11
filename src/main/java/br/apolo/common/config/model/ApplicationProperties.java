package br.apolo.common.config.model;

public class ApplicationProperties {
	
	private String uploadedFilesPath;
	
	private String filesHandler;
	
	private String pdfImageExtractorExecutablePath;
	
	private String videoConverterExecutablePath;

	public String getUploadedFilesPath() {
		return uploadedFilesPath;
	}

	public void setUploadedFilesPath(String uploadedFilesPath) {
		this.uploadedFilesPath = uploadedFilesPath;
	}

	public String getPdfImageExtractorExecutablePath() {
		return pdfImageExtractorExecutablePath;
	}

	public void setPdfImageExtractorExecutablePath(
			String pdfImageExtractorExecutablePath) {
		this.pdfImageExtractorExecutablePath = pdfImageExtractorExecutablePath;
	}

	public String getVideoConverterExecutablePath() {
		return videoConverterExecutablePath;
	}

	public void setVideoConverterExecutablePath(String videoConverterExecutablePath) {
		this.videoConverterExecutablePath = videoConverterExecutablePath;
	}

	public String getFilesHandler() {
		return filesHandler;
	}

	public void setFilesHandler(String filesHandler) {
		this.filesHandler = filesHandler;
	}

}
