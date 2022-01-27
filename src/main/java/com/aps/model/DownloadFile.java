package com.aps.model;

import java.util.Date;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class DownloadFile {

    private final String downloadURL;
    private final String fileName;
    private final Date lastModified;
    private final String size;

    private DownloadFile(final String downloadURL, final String fileName, final Date lastModified, final String size) {
        this.downloadURL = downloadURL;
        this.fileName = fileName;
        this.lastModified = lastModified;
        this.size = size;
    }

    public boolean isValid() {
        return (downloadURL != null && !downloadURL.isEmpty())
            && (fileName != null && !fileName.isEmpty());
    }

    public static class DownloadFileBuilder {

        private String downloadURL;
        private String fileName;
        private Date lastModified;
        private String size;

        public DownloadFileBuilder setDownloadURL(final String downloadURL) {
            this.downloadURL = downloadURL;
            return this;
        }

        public DownloadFileBuilder setFileName(final String fileName) {
            this.fileName = fileName;
            return this;
        }

        public DownloadFileBuilder setLastModified(final Date lastModified) {
            this.lastModified = lastModified;
            return this;
        }

        public DownloadFileBuilder setSize(final String size) {
            this.size = size;
            return this;
        }

        public DownloadFile createDownloadFile() {
            return new DownloadFile(downloadURL, fileName, lastModified, size);
        }
    }
}
