package org.problems.practise;

import java.util.List;

public interface Mediator {

    public void register(ChatParticipant participant);

    public void deregister(ChatParticipant participant);
    
    public void send(String sender, String receiver, String message);

    public void send(String sender, List<String> receivers, String message);

}
