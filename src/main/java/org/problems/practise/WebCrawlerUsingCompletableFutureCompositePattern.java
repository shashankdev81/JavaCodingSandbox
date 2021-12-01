package org.problems.practise;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * // This is the HtmlParser's API interface.
 * // You should not implement it, or speculate about its implementation
 * interface HtmlParser {
 * public List<String> getUrls(String url) {}
 * }
 */

class WebCrawlerUsingCompletableFutureCompositePattern {


    private Map<String, Boolean> crawledUrls = new ConcurrentHashMap<String, Boolean>();

    private String START_URL_HOST;

    public List<String> crawl(String startUrl, HtmlParser htmlParser) {
        START_URL_HOST = startUrl.substring(7, startUrl.length()).split("/")[0];
        crawlWithCompletableFutures(startUrl, htmlParser).join();
        return crawledUrls.keySet().stream().collect(Collectors.toList());
    }

    private class UrlsCompositeFuture {

        private CompletableFuture<Object> future;

        private List<UrlsCompositeFuture> urlsFutures;

        public UrlsCompositeFuture() {
            this.urlsFutures = new ArrayList<UrlsCompositeFuture>();
        }

        public void add(UrlsCompositeFuture futures) {
            urlsFutures.add(futures);
        }

        public void addFuture(CompletableFuture<Object> future) {
            this.future = future;
        }

        public void join() {
            future.join();
            urlsFutures.forEach(f -> f.join());
        }
    }

    private UrlsCompositeFuture crawlWithCompletableFutures(String startUrl, HtmlParser htmlParser) {
        crawledUrls.put(startUrl, Boolean.TRUE);
        CompletableFuture<List<String>> toBeCrawledUrls = CompletableFuture.supplyAsync(new Supplier<List<String>>() {
            @Override
            public List<String> get() {
                List<String> crawledUrls = htmlParser.getUrls(startUrl);
                System.out.println("Fetched " + crawledUrls.size() + " urls in :" + Thread.currentThread().getName());
                return crawledUrls;
            }
        });
        UrlsCompositeFuture urlsCompositeFuture = new UrlsCompositeFuture();
        CompletableFuture<Object> future = toBeCrawledUrls.thenApplyAsync(urls -> {
            List<UrlsCompositeFuture> completableFutures = new ArrayList<UrlsCompositeFuture>();
            for (String url : urls) {
                if (isCrawlable(url)) {
                    urlsCompositeFuture.add(crawlWithCompletableFutures(url, htmlParser));
                }
            }
            return completableFutures;
        });
        urlsCompositeFuture.addFuture(future);
        return urlsCompositeFuture;
    }

    private boolean isCrawlable(String url) {
        String host = url.substring(7, url.length()).split("/")[0];
        return !crawledUrls.containsKey(url) && START_URL_HOST.equalsIgnoreCase(host);
    }


    private static class HtmlParser {

        private int ind = 1;
        private static List<String> sampleUrls = Arrays.asList(new String[]{"http://news.yahoo.com",
                "http://news.yahoo.com/news",
                "http://news.yahoo.com/a",
                "http://news.yahoo.com/b",
                "http://news.yahoo.com/c",
                "http://news.yahoo.com/d",
                "http://news.yahoo.com/e",
                "http://news.yahoo.com/f",
                "http://news.yahoo.com/g",
                "http://news.yahoo.com/h",
                "http://news.yahoo.com/k",
                "http://news.yahoo.com/news/topics/",
                "http://news.yahoo.com/us"});

        public List<String> getUrls(String startUrl) {
            try {
                Thread.sleep(10);
            } catch (Exception e) {
                e.printStackTrace();
            }

            List<String> res = ind > 10 ? new ArrayList<String>() : sampleUrls.subList(ind, ind + 3);
            ind += 3;
            return res;
        }

        public List<String> getAllUrls() {
            return sampleUrls;
        }

    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        WebCrawlerUsingCompletableFutureCompositePattern webCrawler = new WebCrawlerUsingCompletableFutureCompositePattern();
        HtmlParser htmlParser = new HtmlParser();
        List<String> crawledUrls = webCrawler.crawl("http://news.yahoo.com", htmlParser);
        System.out.println("crawledUrls size=" + crawledUrls.size() + ", sample urls=" + htmlParser.getAllUrls().size());
        boolean isCrawledAll = htmlParser.getAllUrls().stream().allMatch(url -> crawledUrls.contains(url));
        long end = System.currentTimeMillis();
        System.out.println("isCrawledAll=" + isCrawledAll + ", time taken=" + (end - start));
    }

}