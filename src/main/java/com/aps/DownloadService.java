package com.aps;

import com.aps.model.DownloadFile;
import com.aps.model.DownloadFile.DownloadFileBuilder;
import com.aps.threads.DownloadFileRunnable;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

public class DownloadService {

    public static final String BASE_URL = "https://archive.apache.org/dist/maven/plugins/";

    public void baixarArquivos() {

        Set<DownloadFile> downloadFiles = getDownloadFiles();

        runDownloadThreads(downloadFiles);
    }

    private void runDownloadThreads(Set<DownloadFile> downloadFiles) {
        ExecutorService executor = Executors
            .newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        downloadFiles.stream().map(DownloadFileRunnable::new).forEach(executor::execute);
        executor.shutdown();
    }

    private Set<DownloadFile> getDownloadFiles() {
        DownloadFile downloadFile = new DownloadFileBuilder()
            .setDownloadURL("")
            .setFileName("")
            .setLastModified(new Date())
            .setSize("")
            .createDownloadFile();
        return Collections.emptySet();
    }

    private Set<String> buscarLinksPaginaWeb() throws IOException {
        Elements links = Jsoup.connect(BASE_URL).get().select("a[href]");

        return links.stream().filter(p -> p.attr("href").contains("3.0.1"))
            .map(r -> r.attr("href"))
            .collect(Collectors.toSet());
    }

}
