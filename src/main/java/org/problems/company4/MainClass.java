package org.problems.company4;

public class MainClass {

    public static void main(String[] args) {

        CostExplorer explorer = new CostExplorer();
        explorer.subscribe("Shashank", "JIRA", "Basic");
        explorer.subscribe("Shashank", "Confluence", "Basic");
        System.out.println(explorer.getYearlyCost("Shashank"));

    }

}
