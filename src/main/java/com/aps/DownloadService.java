package com.aps;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import com.aps.model.DownloadFile;
import com.aps.model.DownloadFile.DownloadFileBuilder;
import com.aps.threads.DownloadFileRunnable;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

public class DownloadService {

    public static final String BASE_URL = "https://archive.apache.org/dist/maven/plugins/";

    public void downloadFiles() {
        try {
            runDownloadThreads(getDownloadFiles());
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private void runDownloadThreads(final Set<DownloadFile> downloadFiles) {
        final ExecutorService executor = Executors
            .newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        downloadFiles.stream().map(DownloadFileRunnable::new).forEach(executor::execute);
        executor.shutdown();
    }

    private Set<DownloadFile> getDownloadFiles() throws IOException {
        final Elements downloadableLinks = getAllDownloadableLinksFromURL();

        final Set<DownloadFile> files = new HashSet<>();

        downloadableLinks.stream().filter(p -> p.attr("href").contains("2.10")).forEach(link -> {
            Date date = null;
            String size = "";

            final Node dateSizeNode = link.nextSibling();
            if (dateSizeNode instanceof TextNode && isNotBlank(((TextNode) dateSizeNode).text())) {
                try {
                    final String text = StringUtils.trim(((TextNode) dateSizeNode).text());
                    date = new SimpleDateFormat("yyyy-MM-dd")
                        .parse(StringUtils.substring(text, 0, 10));
                    size = StringUtils.substring(text, 17);
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }

            files.add(new DownloadFileBuilder()
                .setDownloadURL(BASE_URL + getHrefFromElement(link))
                .setFileName(getHrefFromElement(link))
                .setLastModified(date)
                .setSize(size)
                .createDownloadFile());
        });

        return files;
    }

    private String getHrefFromElement(final Element link) {
        return link.attr("href");
    }

    private Elements getAllDownloadableLinksFromURL() throws IOException {
        return Jsoup.connect(BASE_URL).get().select("a[href]");
    }

}
