package org.problems.streams;

public class Author {

    private final String lastName;

    public static Author of(String authorName) {
        return new Author(authorName);
    }

    private Author(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public String toString() {
        return "Author{" +
                "lastName='" + lastName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Author author = (Author) o;

        return lastName.equals(author.lastName);
    }

    @Override
    public int hashCode() {
        return lastName.hashCode();
    }
}
