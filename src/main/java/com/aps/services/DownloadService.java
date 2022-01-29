package com.aps.services;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import com.aps.model.DownloadFile;
import com.aps.model.DownloadFile.DownloadFileBuilder;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

public class DownloadService {

    public static final String BASE_URL = "https://archive.apache.org/dist/maven/plugins/";

    private static final String REGEX_DOWNLOADABLE_LINK = "(\\.)(.+(?<!/)$)";
    public static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd HH:mm";
    final Pattern pattern = Pattern.compile(REGEX_DOWNLOADABLE_LINK);

    public void downloadFiles() {
        try {
            runDownloadThreads(getDownloadFiles());
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private void runDownloadThreads(final Set<DownloadFile> downloadFiles) {
//        final ExecutorService executor = Executors
//            .newFixedThreadPool(Runtime.getRuntime().availableProcessors());
//        downloadFiles.stream().map(DownloadFileRunnable::new).forEach(executor::execute);
//        executor.shutdown();
        System.out.println(downloadFiles);
    }

    private Set<DownloadFile> getDownloadFiles() throws IOException {
        final Set<Element> downloadableLinks = getAllDownloadableLinksFromURL(BASE_URL);

        final Set<DownloadFile> files = new HashSet<>();

        downloadableLinks.forEach(link -> {
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

    private Set<Element> getAllDownloadableLinksFromURL(final String url) throws IOException {
        final Elements hrefs = Jsoup.connect(url).get().select("a[href]");
        return hrefs.stream().filter(href -> isValidLink(getHrefFromElement(href)))
            .collect(Collectors.toSet());
    }

    private boolean isValidLink(final String link) {
        return pattern.matcher(link).find();
    }

}
