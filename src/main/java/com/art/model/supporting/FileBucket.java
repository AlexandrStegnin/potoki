package com.art.model.supporting;

import org.springframework.web.multipart.MultipartFile;

public class FileBucket {
    private MultipartFile file;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
