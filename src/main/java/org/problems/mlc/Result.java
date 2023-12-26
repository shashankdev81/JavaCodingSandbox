package org.problems.mlc;

public class Result {
    private String value;
    private Long time;


    public Result(String value, Long time) {
        this.value = value;
        this.time = time;
    }

    public String getValue() {
        return value;
    }

    public Long getTime() {
        return time;
    }
}
