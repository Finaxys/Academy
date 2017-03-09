package com.finaxys.slackbot.Utilities;

import allbegray.slack.SlackClientFactory;
import allbegray.slack.rtm.SlackRealTimeMessagingClient;

/**
 * Created by Bannou on 07/03/2017.
 */
public class RealTimeApiFactory {
    private static SlackRealTimeMessagingClient slackRealTimeMessagingClient;

    private RealTimeApiFactory() {
    }

    public static SlackRealTimeMessagingClient getSlackRealTimeMessagingClient() {
        String slackBotToken = PropertyLoader.loadSlackBotProperties().getProperty("token");
        return slackRealTimeMessagingClient == null ? SlackClientFactory.createSlackRealTimeMessagingClient(slackBotToken) : slackRealTimeMessagingClient;
    }
}
