package com.finaxys.slackbot.Utilities;

import allbegray.slack.SlackClientFactory;
import allbegray.slack.rtm.Event;
import allbegray.slack.rtm.SlackRealTimeMessagingClient;
import allbegray.slack.webapi.SlackWebApiClient;
import com.finaxys.slackbot.BUL.Listeners.ChannelCreatedListener;
import com.finaxys.slackbot.BUL.Listeners.MessageListener;
import com.finaxys.slackbot.BUL.Listeners.ReactionAddedListener;
import com.finaxys.slackbot.BUL.Listeners.ReactionRemovedListener;
import com.finaxys.slackbot.Configuration.SpringContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

/**
 * Created by Bannou on 09/03/2017.
 */
@Component
public class SlackBot {

    private static PropertyLoader propertyLoader;
    private static SlackWebApiClient slackWebApiClient;
    private static SlackRealTimeMessagingClient slackRealTimeMessagingClient;

    @Autowired
    public void setPropertyLoader(PropertyLoader propertyLoader) {
        SlackBot.propertyLoader =propertyLoader;
    }

    public static SlackWebApiClient getSlackWebApiClient() {
        return slackWebApiClient == null ? SlackClientFactory.createWebApiClient(propertyLoader.loadSlackBotProperties().getProperty("token")) : slackWebApiClient;
    }

    public static SlackRealTimeMessagingClient getSlackRealTimeMessagingClient() {
        return slackRealTimeMessagingClient == null
                ? SlackClientFactory.createSlackRealTimeMessagingClient(propertyLoader.loadSlackBotProperties().getProperty("token")) : slackRealTimeMessagingClient;
    }


}


