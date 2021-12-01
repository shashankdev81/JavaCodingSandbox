package org.problems.practise;

import java.lang.reflect.Array;
import java.util.Arrays;

public class MyApp {

    public static void main(String[] args) {
        ChatApplication app = new ChatApplication();
        ChatParticipant human = new Human("Shashank");
        ChatParticipant bot1 = new Human("bot1");
        ChatParticipant dominoes = new Human("dominoes");

        app.register(human);
        app.register(bot1);
        app.register(dominoes);

        app.send(human.getId(), bot1.getId(), "I have a problem");
        app.send(bot1.getId(), human.getId(), "How can I help you?");
        app.send(bot1.getId(), Arrays.asList(new String[]{human.getId(), dominoes.getId()}), "Alert!!");

    }
}
