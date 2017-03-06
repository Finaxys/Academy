package com.finaxys.slackbot.utilities;

import allbegray.slack.SlackClientFactory;
import allbegray.slack.rtm.SlackRealTimeMessagingClient;

/**
 * Created by inesnefoussi on 3/2/17.
 */
public class ScoreSlackBot {
    private static SlackRealTimeMessagingClient slackRealTimeMessagingClient;

    private ScoreSlackBot() {
    }

    public static SlackRealTimeMessagingClient getSlackRealTimeMessagingClient() {

        return slackRealTimeMessagingClient == null ?  SlackClientFactory.createSlackRealTimeMessagingClient("") :  slackRealTimeMessagingClient;
    }
}
