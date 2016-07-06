package apolo.api.apimodel;

import apolo.api.apimodel.base.BaseAPIModel;
import org.springframework.web.multipart.MultipartFile;

public class FileDTO extends BaseAPIModel {

    private MultipartFile file;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
