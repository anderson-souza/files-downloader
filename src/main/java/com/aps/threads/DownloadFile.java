package com.aps.threads;

public class DownloadFile {

    private final String downloadURL;
    private final String fileName;

    public DownloadFile(String downloadURL, String fileName) {
        this.downloadURL = downloadURL;
        this.fileName = fileName;
    }

    public String getDownloadURL() {
        return downloadURL;
    }

    public String getFileName() {
        return fileName;
    }

    public boolean isValid() {
        return (downloadURL != null && !downloadURL.isEmpty())
            && (fileName != null && !fileName.isEmpty());
    }
}
