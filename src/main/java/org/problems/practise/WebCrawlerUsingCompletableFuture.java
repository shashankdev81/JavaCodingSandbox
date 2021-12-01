package org.problems.practise;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * // This is the HtmlParser's API interface.
 * // You should not implement it, or speculate about its implementation
 * interface HtmlParser {
 * public List<String> getUrls(String url) {}
 * }
 */

class WebCrawlerUsingCompletableFuture {


    private Map<String, Boolean> crawledUrls = new ConcurrentHashMap<String, Boolean>();

    private String START_URL_HOST;

    private AtomicInteger depth = new AtomicInteger(0);

    public List<String> crawl(String startUrl, HtmlParser htmlParser) {
        START_URL_HOST = startUrl.substring(7, startUrl.length()).split("/")[0];
        List<String> crawledUrlsCol = new ArrayList<String>();
        crawlWithCompletableFutures(startUrl, htmlParser);
        crawledUrlsCol.addAll(crawledUrls.keySet());
        return crawledUrlsCol;
    }


    private List<CompletableFuture<String>> crawlWithCompletableFutures(String startUrl, HtmlParser htmlParser) {
        crawledUrls.put(startUrl, Boolean.TRUE);
        // transform String -> List<String> ->  CompletableFuture<List<String>>
        CompletableFuture<List<String>> toBeCrawledUrls = CompletableFuture.supplyAsync(new Supplier<List<String>>() {
            @Override
            public List<String> get() {
                try {
                    Thread.sleep(10);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                List<String> crawledUrls = htmlParser.getUrls(startUrl);
                System.out.println("Fetched " + crawledUrls.size() + " urls in :" + Thread.currentThread().getName());
                return crawledUrls;
            }
        });
        // transform CompletableFuture<List<String>> List<String> -> List<CompletableFuture<String>>
        CompletableFuture<List<CompletableFuture<String>>> result = toBeCrawledUrls.thenApplyAsync(urls -> {
            List<CompletableFuture<String>> completableFutures = new ArrayList<>();
            for (String url : urls) {
                if (isCrawlable(url)) {
                    completableFutures.addAll(crawlWithCompletableFutures(url, htmlParser));
                }
            }
            return completableFutures;
        });
        return result.join();
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
        WebCrawlerUsingCompletableFuture webCrawler = new WebCrawlerUsingCompletableFuture();
        HtmlParser htmlParser = new HtmlParser();
        List<String> crawledUrls = webCrawler.crawl("http://news.yahoo.com", htmlParser);
        System.out.println("crawledUrls size=" + crawledUrls.size() + ", sample urls=" + htmlParser.getAllUrls().size());
        boolean isCrawledAll = htmlParser.getAllUrls().stream().allMatch(url -> crawledUrls.contains(url));
        long end = System.currentTimeMillis();
        System.out.println("isCrawledAll=" + isCrawledAll + ", time taken=" + (end - start));
    }

}