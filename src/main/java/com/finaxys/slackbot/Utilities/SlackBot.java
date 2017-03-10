package com.finaxys.slackbot.Utilities;

import allbegray.slack.SlackClientFactory;
import allbegray.slack.rtm.Event;
import allbegray.slack.rtm.SlackRealTimeMessagingClient;
import allbegray.slack.webapi.SlackWebApiClient;
import com.finaxys.slackbot.BUL.Listeners.ChannelCreatedListener;
import com.finaxys.slackbot.BUL.Listeners.MessageListener;
import com.finaxys.slackbot.Configuration.Classes.SpringContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

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

    public static void main(String[] args) {
        AbstractApplicationContext context = new AnnotationConfigApplicationContext(SpringContext.class);
        SlackRealTimeMessagingClient slackRealTimeMessagingClient = getSlackRealTimeMessagingClient();
        slackRealTimeMessagingClient.addListener(Event.MESSAGE, (MessageListener) context.getBean("messageListener"));
        slackRealTimeMessagingClient.addListener(Event.CHANNEL_CREATED, (ChannelCreatedListener) context.getBean("channelCreatedListener"));
        slackRealTimeMessagingClient.connect();
    }
}
