package org.problems.practise;

public class Human implements ChatParticipant {

    public Human(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    private String id;

    public void receive(String originId, String message) {
        System.out.println("recevived message from " + originId + ", message=" + message);
    }
}
