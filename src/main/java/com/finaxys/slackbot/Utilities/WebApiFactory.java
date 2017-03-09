package com.finaxys.slackbot.Utilities;

import allbegray.slack.SlackClientFactory;
import allbegray.slack.webapi.SlackWebApiClient;

/**
 * Created by Bannou on 07/03/2017.
 */
public class WebApiFactory {
    private static SlackWebApiClient slackWebApiClient;

    private WebApiFactory() {
    }

    public static SlackWebApiClient getSlackWebApiClient() {
        String slackBotToken = PropertyLoader.loadSlackBotProperties().getProperty("token");
        return slackWebApiClient == null ? SlackClientFactory.createWebApiClient(slackBotToken) : slackWebApiClient;
    }
}
