package org.problems.company4;

public class Product {

    private String id;

    public String getId() {
        return id;
    }

    public String getDetails() {
        return details;
    }

    public int getCost() {
        return cost;
    }

    private String details;

    private int cost;


    public Product(String id, String details, int cost) {
        this.id = id;
        this.details = details;
        this.cost = cost;
    }
}
