package com.aps;

import com.aps.threads.DownloadFile;
import com.aps.threads.DownloadFileRunnable;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import lombok.extern.java.Log;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

@Log
public class App {

    public static final String BASE_URL = "https://archive.apache.org/dist/maven/plugins/";

    public static void main(String[] args) {
        baixarArquivos();
    }

    private static void baixarArquivos() {
        try {
            Set<String> linksZip = buscarLinksPaginaWeb();

            ExecutorService executor = Executors
                .newFixedThreadPool(Runtime.getRuntime().availableProcessors());

            linksZip.stream()
                .map(link -> new DownloadFileRunnable(new DownloadFile(BASE_URL + link, link)))
                .forEach(executor::execute);
            executor.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Set<String> buscarLinksPaginaWeb() throws IOException {
        Elements links = Jsoup.connect(BASE_URL).get().select("a[href]");

        return links.stream().filter(p -> p.attr("href").contains("3.0.1"))
            .map(r -> r.attr("href"))
            .collect(Collectors.toSet());
    }
}
