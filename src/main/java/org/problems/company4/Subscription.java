package org.problems.company4;

import java.util.Date;

public class Subscription {

    public Product getProduct() {
        return product;
    }

    public SubscriptionType getType() {
        return type;
    }

    public Date getDate() {
        return date;
    }

    private Product product;

    private SubscriptionType type;

    private Date date;

    public Subscription(Product product, SubscriptionType type, Date date) {
        this.product = product;
        this.type = type;
        this.date = date;
    }
}
