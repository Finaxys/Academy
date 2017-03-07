package com.finaxys.slackbot;

import allbegray.slack.rtm.Event;
import allbegray.slack.rtm.EventListener;
import allbegray.slack.rtm.SlackRealTimeMessagingClient;
import allbegray.slack.webapi.SlackWebApiClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.DAL.Interfaces.GenericRepository;
import com.finaxys.slackbot.Configuration.Classes.SpringContext;
import com.finaxys.slackbot.Domains.Challenge;
import com.finaxys.slackbot.Domains.FinaxysProfile;
import com.finaxys.slackbot.Domains.FinaxysProfile_Challenge;
import com.finaxys.slackbot.Domains.FinaxysProfile_Challenge_PK;
import com.finaxys.slackbot.Utilities.Classes.PropertyLoader;
import com.finaxys.slackbot.Utilities.Classes.RealTimeApiFactory;
import com.finaxys.slackbot.Utilities.Classes.WebApiFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

/**
 * Created by inesnefoussi on 3/6/17.
 */
public class main {

    public static void main(String... args) {

    }
}
