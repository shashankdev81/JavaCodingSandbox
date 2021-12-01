package org.problems.practise;


import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;

/**
 * // This is the HtmlParser's API interface.
 * // You should not implement it, or speculate about its implementation
 * interface HtmlParser {
 * public List<String> getUrls(String url) {}
 * }
 */

class Solution2 {

    private int PARALLELISM = 3;

    private Map<String, Boolean> crawledUrls = new ConcurrentHashMap<String, Boolean>();

    private BlockingQueue<String> uncrawledUrls = new LinkedBlockingDeque<String>();

    private ExecutorService service = Executors.newFixedThreadPool(PARALLELISM);

    private String START_URL_HOST;

    public List<String> crawl(String startUrl, HtmlParser htmlParser) {
        START_URL_HOST = startUrl.substring(7, startUrl.length()).split("/")[0];
        for (int i = 0; i < PARALLELISM; i++) {
            service.submit(new Crawler(i + 1, htmlParser));
        }
        try {
            uncrawledUrls.put(startUrl);
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<String> result = new ArrayList<String>();
        result.addAll(crawledUrls.keySet());
        return result;
    }

    private boolean isCrawlable(String url) {
        String host = url.substring(7, url.length()).split("/")[0];
        return !crawledUrls.containsKey(url) && START_URL_HOST.equalsIgnoreCase(host);
    }


    private class Crawler implements Runnable {

        private int num;

        private HtmlParser parser;

        public Crawler(int num, HtmlParser parser) {

            this.num = num;
            this.parser = parser;
        }

        @Override
        public void run() {
            while (true) {
                if (!uncrawledUrls.isEmpty()) {
                    String toBeCrawledUrl = uncrawledUrls.poll();
                    crawledUrls.put(toBeCrawledUrl, Boolean.TRUE);
                    List<String> urls = parser.getUrls(toBeCrawledUrl);
                    for(String url: urls){
                        if(isCrawlable(url)){
                            try {
                                uncrawledUrls.put(url);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }
            }
        }
    }


    private class HtmlParser {
        public List<String> getUrls(String startUrl) {
            return Arrays.asList(new String[]{"http://news.yahoo.com", "http://news.yahoo.com/news", "http://news.yahoo.com/news/topics/", "http://news.yahoo.com/us"});
        }
    }
}