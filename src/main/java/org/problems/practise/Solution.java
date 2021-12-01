package org.problems.practise;


import java.util.*;
import java.util.concurrent.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * // This is the HtmlParser's API interface.
 * // You should not implement it, or speculate about its implementation
 * interface HtmlParser {
 * public List<String> getUrls(String url) {}
 * }
 */

class Solution {

    private int PARALLELISM = 3;

    private Map<String, Boolean> crawledUrls = new ConcurrentHashMap<String, Boolean>();

    private BlockingQueue<String> uncrawledUrls = new LinkedBlockingDeque<String>();

    private ExecutorService service = Executors.newFixedThreadPool(PARALLELISM);

    private String START_URL_HOST;

    public List<String> crawl1(String startUrl, HtmlParser htmlParser) {
        START_URL_HOST = startUrl.substring(7, startUrl.length()).split("/")[0];
        crawlWithTreeOfThreads(startUrl, htmlParser);
        List<String> result = new ArrayList<String>();
        result.addAll(crawledUrls.keySet());
        return result;
    }

    public List<String> crawl2(String startUrl, HtmlParser htmlParser) {
        START_URL_HOST = startUrl.substring(7, startUrl.length()).split("/")[0];
        return crawlWithParallelStream(startUrl, htmlParser).collect(Collectors.toList());
    }

    public List<String> crawl(String startUrl, HtmlParser htmlParser) {
        START_URL_HOST = startUrl.substring(7, startUrl.length()).split("/")[0];
        List<CompletableFuture<List<String>>> futures = crawlWithCompletableFutures(startUrl, htmlParser);
        List<String> crawledUrlsCol = new ArrayList<String>();
        for (CompletableFuture<List<String>> f : futures) {
            try {
                f.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        crawledUrlsCol.addAll(crawledUrls.keySet());
        return crawledUrlsCol;
    }

    private void crawlWithTreeOfThreads(String startUrl, HtmlParser htmlParser) {
        crawledUrls.put(startUrl, Boolean.TRUE);
        List<Thread> threads = new ArrayList<>();
        for (String url : htmlParser.getUrls(startUrl)) {
            if (!isCrawlable(url)) {
                continue;
            }
            threads.add(new Thread(new Runnable() {
                @Override
                public void run() {
                    crawlWithTreeOfThreads(url, htmlParser);
                }
            }));
        }
        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private void crawlWithParallelStream2(String startUrl, HtmlParser htmlParser) {
        crawledUrls.put(startUrl, Boolean.TRUE);
        htmlParser.getUrls(startUrl).parallelStream().filter(url -> isCrawlable(url)).forEach(url -> crawlWithParallelStream2(startUrl, htmlParser));
    }

    private Stream<String> crawlWithParallelStream(String startUrl, HtmlParser htmlParser) {
        try (Stream<String> stream = htmlParser.getUrls(startUrl)
                .parallelStream()
                .filter(url -> isCrawlable(url))
                .flatMap(url -> crawlWithParallelStream(url, htmlParser))) {
            return Stream.concat(Stream.of(startUrl), stream);
        }
    }

    private void crawlWithCallable(String startUrl, HtmlParser htmlParser) {
        crawledUrls.put(startUrl, Boolean.TRUE);
        Future<UrlColl> urlToBeCrawled = service.submit(new Crawler(startUrl, htmlParser));
        try {
            List<String> crawledUrls = urlToBeCrawled.get().getUrls();
            for (String url : crawledUrls) {
                if (isCrawlable(url)) {
                    crawlWithCallable(url, htmlParser);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private List<CompletableFuture<List<String>>> crawlWithCompletableFutures(String startUrl, HtmlParser htmlParser) {
        crawledUrls.put(startUrl, Boolean.TRUE);
        CompletableFuture<List<String>> toBeCrawledUrls = CompletableFuture.supplyAsync(new Supplier<List<String>>() {
            @Override
            public List<String> get() {
                return htmlParser.getUrls(startUrl);
            }
        });
        List<CompletableFuture<List<String>>> futures = new ArrayList<CompletableFuture<List<String>>>();
        toBeCrawledUrls.thenApply(urls -> {
            for (String url : urls) {
                if (isCrawlable(url)) {
                    futures.addAll(crawlWithCompletableFutures(url, htmlParser));
                }
            }

            return futures.stream().collect(Collectors.toList());
        });
        return futures;
    }

    private boolean isCrawlable(String url) {
        String host = url.substring(7, url.length()).split("/")[0];
        return !crawledUrls.containsKey(url) && START_URL_HOST.equalsIgnoreCase(host);
    }

    private class Crawler implements Callable<Solution.UrlColl> {

        private String url;

        private HtmlParser parser;

        public Crawler(String url, HtmlParser parser) {
            this.url = url;
            this.parser = parser;
        }

        @Override
        public Solution.UrlColl call() throws Exception {
            return new Solution.UrlColl(parser.getUrls(url));
        }
    }

    private class UrlColl {
        private List<String> urls;

        public List<String> getUrls() {
            return urls;
        }

        public UrlColl(List<String> urls) {
            this.urls = urls;
        }
    }

    private class HtmlParser {
        public List<String> getUrls(String startUrl) {
            return Arrays.asList(new String[]{"http://news.yahoo.com", "http://news.yahoo.com/news", "http://news.yahoo.com/news/topics/", "http://news.yahoo.com/us"});
        }
    }

    public static void main(String[] args) {
        String url = "http://news.yahoo.com/news/topics/";
        String host = url.substring(7, url.length()).split("/")[0];
        System.out.println(host);
    }

}