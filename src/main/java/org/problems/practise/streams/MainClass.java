package org.problems.practise.streams;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class MainClass {

    private static Set<Article> allArticles = Article.readAll();
    private Function<Map<String, Long>, Map.Entry<String, Long>> function;

    public static void main(String[] args) {

        Map<Integer, Long> stringListByLength = Arrays.asList(new String[]{"one", "two", "three", "four", "five", "six", "seven"})
                .stream().collect(Collectors.groupingBy(String::length, Collectors.counting()));

        int minYear = allArticles.stream().map(a -> a.getInceptionYear()).min(Comparator.naturalOrder()).get();
        int minYear2 = allArticles.stream().map(a -> a.getInceptionYear()).collect(Collectors.minBy(Comparator.naturalOrder())).get();
        IntSummaryStatistics stats = allArticles.stream().mapToInt(a -> a.getInceptionYear()).summaryStatistics();
        System.out.println(minYear);
        System.out.println(stats);
        //allArticles.stream().flatMap(art -> art.getAuthors().s
        // tream().collect(Collectors.toMap(Author::getLastName, art.getTitle()));
        //.collect(Collectors.groupingBy(article -> article.))

        Map<Integer, Long> articlesPerYear = allArticles.stream().collect(Collectors.groupingBy(Article::getInceptionYear, Collectors.counting()));
        System.out.println(articlesPerYear);

        //2020-34, 2019-34
        Map.Entry<Integer, Long> max = articlesPerYear.entrySet().stream().max(Comparator.comparing(e -> e.getValue())).get();
        System.out.println(max.getKey() + ":" + max.getValue());

        //34 - [2019,2020]
        Map.Entry<Long, List<Map.Entry<Integer, Long>>> maxList = articlesPerYear.entrySet().stream().collect(Collectors.groupingBy(Map.Entry::getValue))
                .entrySet().stream().max(Comparator.comparing(e -> e.getKey())).get();
        System.out.println(maxList);

        //chen-20, ork-26
        Map<String, Long> authorsToNumberOfArticles = allArticles.stream().flatMap(article -> article.getAuthors().stream())
                .collect(Collectors.groupingBy(Author::getLastName, Collectors.counting()));

        //
        Map.Entry<String, Long> maxAuthor = authorsToNumberOfArticles.entrySet().stream().max(Comparator.comparing(e -> e.getValue())).get();
        System.out.println(maxAuthor);

        test();

        Collector<Article, ?, Map<Integer, Long>> groupBy = Collectors.groupingBy(Article::getInceptionYear, Collectors.counting());
        Function<Map<Integer, Long>, Map.Entry<Integer, Long>> finisher = articlesByYear1 -> articlesByYear1.entrySet().stream().max(Comparator.comparing(e -> e.getValue())).get();
        Map.Entry<Integer, Long> maxAuth2 = allArticles.stream().collect(Collectors.collectingAndThen(groupBy, finisher));
        System.out.println(maxAuth2);

    }

    private static void test() {
        long res = LongStream.of(2, 53, 17, 21, 11, 42, 30)
                .reduce(1, (acc, next) -> acc * next);
        long res2 = LongStream.of(1, 2, 3)
                .reduce(0, (acc, next) -> acc + next);
        long start = System.currentTimeMillis();
        String input = "Framework - Lead by example - Consensus: Broaden the discussion quorum & Consensus - Relationships: Connect and influence leadership. Build trust. Value my opinion. Rigour - Data driven decision making: system and business metrics Examples Successful Influence:- Production Stability, Reporting architecture 2.0, Team reorg around platform - Data, Reporting, etc, Audience Targeting, Various Initiatives, Areas of influence: Senior Engineering leaders: investment in new platforms, team/org structures, Product: Influence product to help prioritise new capabilities, phase out deliverables, Juniors: Quality of deliverables, health of platform, tech quotient of team, etc UnSuccessful Influence:- Shopping Entities Reporting architecture 2.0: 1. Identify whats wrong with current system. Product. NFR. Extensibility. etc 2. Work with product to build a case for re architecture (weak link) 3. Buy in from immediate boss 4. Share with team 5. Share with org level stake holders 6. Buy in from site architects Strength - Breadth and depth of technology - Excellent articulation skills - Framework to achieve business impact Weakness - Projects get shelved more often than not; so take it in stride and don't be married to a problem or a solution - Estimates";
        Stream<String> stream = Arrays.stream((input.split(" ")));
        //Stream<String> stream = Arrays.stream((input.split(" "))).parallel();
        Map<String, Long> wordCount = stream.map(String::trim)
                .map(w -> w.replaceAll("\\.", ""))
                .map(w -> w.replaceAll("\\(", ""))
                .map(w -> w.replaceAll(",", ""))
                .map(w -> w.replaceAll("-", ""))
                .map(w -> w.replaceAll(":", ""))
                .map(w -> w.replaceAll(";", ""))
                .filter(w -> w != null && !w.isEmpty())
                .map(String::toLowerCase).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        List<Map.Entry<String, Long>> orderedWordCount = wordCount.entrySet().stream().sorted(Comparator.comparing(Map.Entry::getKey)).collect(Collectors.toList());
        System.out.println(orderedWordCount);
        long end = System.currentTimeMillis();
        System.out.println("Time taken=" + (end - start));
        System.out.println(res2);
    }

    private static Map.Entry<String, Long> getMaxAuthor2(Map<String, Long> map) {
        return map.entrySet().stream().max(Comparator.comparing(e -> e.getValue())).get();
    }
}
