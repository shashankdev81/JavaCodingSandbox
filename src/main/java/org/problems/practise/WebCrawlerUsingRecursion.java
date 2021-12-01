package org.problems.practise;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * // This is the HtmlParser's API interface.
 * // You should not implement it, or speculate about its implementation
 * interface HtmlParser {
 * public List<String> getUrls(String url) {}
 * }
 */

class WebCrawlerUsingRecursion {


    private String START_URL_HOST;

    private Set<String> crawledUrls = new HashSet<String>();


    public List<String> crawl(String startUrl, HtmlParser htmlParser) {
        START_URL_HOST = startUrl.substring(7, startUrl.length()).split("/")[0];
        recurse(startUrl, htmlParser);
        return crawledUrls.stream().collect(Collectors.toList());
    }

    private void recurse(String startUrl, HtmlParser htmlParser) {
        crawledUrls.add(startUrl);
        htmlParser.getUrls(startUrl).stream().filter(this::isCrawlable).forEach(url -> recurse(url, htmlParser));
    }

    private boolean isCrawlable(String url) {
        String host = url.substring(7, url.length()).split("/")[0];
        return !crawledUrls.contains(url) && START_URL_HOST.equalsIgnoreCase(host);
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
        WebCrawlerUsingRecursion webCrawler = new WebCrawlerUsingRecursion();
        HtmlParser htmlParser = new HtmlParser();
        List<String> crawledUrls = webCrawler.crawl("http://news.yahoo.com", htmlParser);
        System.out.println("crawledUrls size=" + crawledUrls.size() + ", sample urls=" + htmlParser.getAllUrls().size());
        boolean isCrawledAll = htmlParser.getAllUrls().stream().allMatch(url -> crawledUrls.contains(url));
        long end = System.currentTimeMillis();
        System.out.println("isCrawledAll=" + isCrawledAll + ", time taken=" + (end - start));
    }

}