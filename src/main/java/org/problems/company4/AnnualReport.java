package org.problems.company4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnnualReport {

    private Map<Integer, List<Bill>> yearlyBillage;

    public AnnualReport() {
        yearlyBillage = new HashMap<>();
        for (int i = 0; i < 12; i++) {
            yearlyBillage.put(i + 1, new ArrayList<>());
        }
    }

    @Override
    public String toString() {
        return "AnnualReport{" +
            "yearlyBillage=" + yearlyBillage +
            '}';
    }


    private class Bill {

        public String getProductId() {
            return productId;
        }

        public int getCost() {
            return cost;
        }

        private String productId;
        private int cost;

        public Bill(String productId, int cost) {
            this.productId = productId;
            this.cost = cost;
        }

        @Override
        public String toString() {
            return "Bill{" +
                "productId='" + productId + '\'' +
                ", cost=" + cost +
                '}';
        }
    }

    public void addBill(int month, String productId, int cost) {
        yearlyBillage.get(month).add(new Bill(productId, cost));
    }

}
