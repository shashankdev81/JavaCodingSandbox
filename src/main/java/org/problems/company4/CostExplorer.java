package org.problems.company4;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CostExplorer {


    public CostExplorer() {
        this.userToSubscriptionMap = new HashMap<>();
    }


    private Map<String, List<Subscription>> userToSubscriptionMap;

    public void subscribe(String userId, String productId, String subscriptionTypeId) {
        Product product = new Product(productId, "details", 100);
        userToSubscriptionMap.putIfAbsent(userId, new ArrayList<>());
        userToSubscriptionMap.get(userId).add(
            new Subscription(product, new SubscriptionType(subscriptionTypeId, "", 20),
                new Date()));

    }

    public int getYearlyCost(String userId) {
        int totalCost = 0;
        for (Subscription subscription : userToSubscriptionMap.get(userId)) {
            int from = subscription.getDate().getMonth();
            int now = new Date().getMonth();
            int months = 12 - (now - from);
            totalCost = subscription.getType().getCostPerMonth() * months;
        }
        return totalCost;
    }


    public AnnualReport getYearlyReportage(String userId) {
        AnnualReport report = new AnnualReport();
        for (int i = 1; i <= 12; i++) {
            for (Subscription subscription : userToSubscriptionMap.get(userId)) {
                int from = subscription.getDate().getMonth();
                if (i >= from) {
                    report.addBill(i, subscription.getProduct().getId(),
                        subscription.getType().getCostPerMonth());
                }
            }
        }

        return report;
    }

}
