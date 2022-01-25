package com.aps.threads;

import java.io.File;
import java.io.IOException;
import lombok.extern.java.Log;
import org.apache.hc.client5.http.fluent.Request;

/**
 * Classe responsável por realizar o download do arquivo
 */
@Log
public class DownloadFileRunnable implements Runnable {

    private final DownloadFile downloadFile;

    public DownloadFileRunnable(DownloadFile downloadFile) {
        this.downloadFile = downloadFile;
    }

    @Override
    public void run() {
        log.info("INICIANDO THREAD PARA DOWNLOAD");
        if (downloadFile != null && downloadFile.isValid()) {
            log.info(String.format("BAIXANDO O ARQUIVO %s DO LINK %s", downloadFile.getFileName(),
                downloadFile.getDownloadURL()));
            downloadApacheHttp();
        }
        log.info("FINALIZACAO DA THREAD DE DOWNLOAD");
    }

    private void downloadApacheHttp() {
        log.info("INICIANDO DOWNLOAD: " + downloadFile.getFileName());
        File myFile = new File(downloadFile.getFileName());
        try {
            Request.get(downloadFile.getDownloadURL()).execute().saveContent(myFile);
            log.info("DOWNLOAD REALIZADO COM SUCESSO: " + downloadFile.getFileName());
        } catch (IOException e) {
            log.severe("Erro ao baixar o arquivo: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
}
