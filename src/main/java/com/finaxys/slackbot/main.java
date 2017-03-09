package com.finaxys.slackbot;


import allbegray.slack.rtm.Event;
import allbegray.slack.rtm.SlackRealTimeMessagingClient;
import com.finaxys.slackbot.BUL.Listeners.MessageListener;
import com.finaxys.slackbot.Utilities.RealTimeApiFactory;

/**
 * Created by inesnefoussi on 3/6/17.
 */
public class main {

    public static void main(String... args) {
        SlackRealTimeMessagingClient slackRealTimeMessagingClient = RealTimeApiFactory.getSlackRealTimeMessagingClient();
        slackRealTimeMessagingClient.addListener(Event.MESSAGE, new MessageListener());
        slackRealTimeMessagingClient.connect();
    }
}
