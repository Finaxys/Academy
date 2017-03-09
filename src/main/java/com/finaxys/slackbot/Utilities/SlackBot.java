package com.finaxys.slackbot.Utilities;

import allbegray.slack.SlackClientFactory;
import allbegray.slack.rtm.SlackRealTimeMessagingClient;
import allbegray.slack.webapi.SlackWebApiClient;

/**
 * Created by Bannou on 09/03/2017.
 */
public class SlackBot {
    private static SlackWebApiClient slackWebApiClient;
    private static SlackRealTimeMessagingClient slackRealTimeMessagingClient;
    private static String slackBotToken = PropertyLoader.loadSlackBotProperties().getProperty("token");

    private SlackBot() {
    }

    public static SlackWebApiClient getSlackWebApiClient() {

        return slackWebApiClient == null ? SlackClientFactory.createWebApiClient(slackBotToken) : slackWebApiClient;
    }

    public static SlackRealTimeMessagingClient getSlackRealTimeMessagingClient() {
        return slackRealTimeMessagingClient == null ? SlackClientFactory.createSlackRealTimeMessagingClient(slackBotToken) : slackRealTimeMessagingClient;
    }
}
