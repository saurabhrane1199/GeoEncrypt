package com.encrypto.android.geoencrypt.models;

public class Files {
    private String fileName;
    private String fileId;
    private String storageUrl;

    Files(String fileName){
        this.fileName = fileName;
    }

    public String getFileId() {
        return fileId;
    }

    public String getFilename() {
        return fileName;
    }

    public String getStorageUrl() {
        return storageUrl;
    }
}
