package org.problems.company4;

public class SubscriptionType {

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCostPerMonth() {
        return costPerMonth;
    }

    private String id;

    private String name;

    private int costPerMonth;

    public SubscriptionType(String id, String name, int costPerMonth) {
        this.id = id;
        this.name = name;
        this.costPerMonth = costPerMonth;
    }


}
