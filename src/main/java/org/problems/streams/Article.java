package org.problems.streams;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

/**
 * Created by José
 */
public class Article {

    private int inceptionYear;
    private final String title;
    private final String articleType;

    private List<Author> authors;

    public static Set<Article> readAll() {
        Path papersPath = Paths.get("files/papers.lst.txt");


        try (Stream<String> lines = Files.lines(papersPath);) {
            return lines.map(Article::of).collect(toSet());
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return new HashSet<Article>();
    }

    public static Article of(String line) {
        int inceptionYear = extractInceptionYear(line);
        String articleType = line.substring(12, 23).trim();
        String authorsAndTitle = line.substring(24).trim();

        int firstComa = authorsAndTitle.indexOf(',');
        if (firstComa == -1) {
            String title = authorsAndTitle.trim();
            Article article = new Article(inceptionYear, title, articleType);
            article.authors = new ArrayList<>();
            return article;
        } else {
            String authorsLine = authorsAndTitle.substring(0, firstComa);
            String[] authors = authorsLine.split("&");
            String title = authorsAndTitle.substring(firstComa + 1).trim();

            Article article = new Article(inceptionYear, title, articleType);
            article.authors = Arrays.stream(authors).map(String::trim).map(Author::of).collect(Collectors.toList());
            return article;
        }
    }

    private static int extractInceptionYear(String line) {
        line = line.substring(0, 4);
        if (line.endsWith("??")) {
            return 1900;
        } else if (line.endsWith("?")) {
            return 1980;
        } else {
            return Integer.parseInt(line);
        }
    }

    private Article(int inceptionYear, String title, String articleType) {
        this.inceptionYear = inceptionYear;
        this.title = title;
        this.articleType = articleType;
    }

    public int getInceptionYear() {
        return inceptionYear;
    }

    public String getTitle() {
        return title;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    @Override
    public String toString() {
        return "Article{" +
                "inceptionYear='" + inceptionYear + '\'' +
                ", title='" + title + '\'' +
                ", authors=" + authors +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Article article = (Article) o;

        if (inceptionYear != article.inceptionYear) return false;
        if (!title.equals(article.title)) return false;
        return articleType.equals(article.articleType);
    }

    @Override
    public int hashCode() {
        int result = inceptionYear;
        result = 31 * result + title.hashCode();
        result = 31 * result + articleType.hashCode();
        return result;
    }
}