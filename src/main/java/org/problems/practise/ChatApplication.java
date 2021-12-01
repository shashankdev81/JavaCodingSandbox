package org.problems.practise;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatApplication implements Mediator {

    private Map<String, ChatParticipant> colleagues = new HashMap<String, ChatParticipant>();

    @Override
    public void register(ChatParticipant participant) {
        colleagues.put(participant.getId(), participant);
    }

    @Override
    public void deregister(ChatParticipant participant) {
        colleagues.remove(participant.getId());
    }

    public void send(String sender, String receiver, String message) {
        colleagues.get(receiver).receive(sender, message);
    }

    @Override
    public void send(String sender, List<String> receivers, String message) {
        receivers.forEach(receiver -> colleagues.get(receiver).receive(sender, message));
    }

}
