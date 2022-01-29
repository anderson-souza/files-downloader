package com.aps.services;

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
import java.util.logging.Level;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

@Log
public class DownloadService {

    private final String[] urls;
    private final String saveFolderPath;

    public DownloadService(final String[] urls, final String saveFolderPath) {
        this.urls = urls;
        this.saveFolderPath = saveFolderPath;
    }

    private static final String REGEX_DOWNLOADABLE_LINK = "(\\.)(.+(?<!/)$)";
    private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd HH:mm";
    private final Pattern pattern = Pattern.compile(REGEX_DOWNLOADABLE_LINK);

    public void downloadFiles() {
        try {
            runDownloadThreads(getDownloadFiles());
        } catch (final IOException e) {
            log.log(Level.SEVERE, "Ocorreu um erro na aplicação", e);
        }
    }

    private void runDownloadThreads(final Set<DownloadFile> downloadFiles) {
        if (!downloadFiles.isEmpty()) {
            final ExecutorService executor = Executors
                .newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            downloadFiles.stream().map(DownloadFileRunnable::new).forEach(executor::execute);
            executor.shutdown();
        } else {
            log.info("Não foram encontrados registros para download");
        }
    }

    private Set<DownloadFile> getDownloadFiles() throws IOException {

        final Set<DownloadFile> allFiles = new HashSet<>();
        for (final String url : urls) {
            allFiles.addAll(getFiles(url));
        }

        return allFiles;
    }

    private Set<DownloadFile> getFiles(final String url) throws IOException {
        final Set<Element> downloadableLinks = getAllDownloadableLinksFromURL(url);
        log.log(Level.INFO, String.format("Foram encontrados %s links", downloadableLinks.size()));

        final Set<DownloadFile> files = new HashSet<>();

        log.info("Iniciando montagem de objetos para download");
        downloadableLinks.forEach(link -> {
            log.info("Iniciando coleta de data e tamanho do arquivo");
            Date date = null;
            String size = "";

            final Node dateSizeNode = link.nextSibling();
            if (dateSizeNode instanceof TextNode && isNotBlank(((TextNode) dateSizeNode).text())) {
                try {
                    final String text = StringUtils.trim(((TextNode) dateSizeNode).text());
                    date = new SimpleDateFormat(DATE_FORMAT_PATTERN)
                        .parse(StringUtils.substring(text, 0, 16));
                    size = StringUtils.substring(text, 17);
                } catch (final Exception e) {
                    log.log(Level.SEVERE, "Ocorreu um erro ao fazer o parse da data", e);
                    e.printStackTrace();
                }
            }

            final DownloadFile downloadFile = new DownloadFileBuilder()
                .setDownloadURL(url + getHrefFromElement(link))
                .setFileName(getHrefFromElement(link))
                .setLastModified(date)
                .setSize(size)
                .createDownloadFile();

            log.info(String.format("Arquivo montado para download %s", downloadFile));

            files.add(downloadFile);
        });

        log.info("Fim da montagem de objetos para download");
        return files;
    }

    private String getHrefFromElement(final Element link) {
        return link.attr("href");
    }

    private Set<Element> getAllDownloadableLinksFromURL(final String url) throws IOException {
        log.info(String.format("Iniciando busca por links na URL: %s", url));
        final Elements hrefs = Jsoup.connect(url).get().select("a[href]");
        return hrefs.stream().filter(href -> isValidLink(getHrefFromElement(href)))
            .collect(Collectors.toSet());
    }

    private boolean isValidLink(final String link) {
        return pattern.matcher(link).find();
    }

}
