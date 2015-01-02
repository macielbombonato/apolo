package apolo.business.model;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

public class FileContent implements Serializable {

	private static final long serialVersionUID = 8665901982422935475L;

	private String name;

	private MultipartFile file;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

}
