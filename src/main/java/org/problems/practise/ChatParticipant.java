package org.problems.practise;

public interface ChatParticipant {

    public String getId();

    public void receive(String originId, String message);

}
