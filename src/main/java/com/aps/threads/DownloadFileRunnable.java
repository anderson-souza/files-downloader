package com.aps.threads;

import com.aps.model.DownloadFile;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import lombok.extern.java.Log;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;

/**
 * Classe responsável por realizar o download do arquivo
 */
@Log
public class DownloadFileRunnable implements Runnable {

    private final DownloadFile downloadFile;

    public DownloadFileRunnable(final DownloadFile downloadFile) {
        this.downloadFile = downloadFile;
    }

    @Override
    public void run() {
        log.info("INICIANDO THREAD PARA DOWNLOAD");
        if (downloadFile != null && downloadFile.isValid()) {
            log.info(String.format("BAIXANDO O ARQUIVO %s DO LINK %s", downloadFile.getFileName(),
                downloadFile.getDownloadURL()));
            newDownload();
        }
        log.info("FINALIZACAO DA THREAD DE DOWNLOAD");
    }

    private void newDownload() {
        final CloseableHttpClient client = HttpClients.createDefault();
        final File myFile = new File(
            FilenameUtils.concat(downloadFile.getSaveFolderPath(), downloadFile.getFileName()));
        log.info("INICIANDO DOWNLOAD: " + downloadFile.getFileName());
        try (CloseableHttpResponse response = client
            .execute(new HttpGet(downloadFile.getDownloadURL()))) {
            final HttpEntity entity = response.getEntity();
            if (entity != null) {
                final InputStream inputStream = entity.getContent();
                try (OutputStream outputStream = new FileOutputStream(myFile)) {
                    IOUtils.copy(inputStream, outputStream);
                }
                log.info("DOWNLOAD REALIZADO COM SUCESSO: " + downloadFile.getFileName());
            }
        } catch (final IOException e) {
            log.log(Level.SEVERE, "Erro ao baixar o arquivo", e);
            e.printStackTrace();
        }
    }
}
