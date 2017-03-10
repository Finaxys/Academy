package com.finaxys.slackbot;

import allbegray.slack.rtm.Event;
import allbegray.slack.rtm.SlackRealTimeMessagingClient;
import com.finaxys.slackbot.BUL.Listeners.MessageListener;
import com.finaxys.slackbot.Configuration.Classes.SpringContext;
import com.finaxys.slackbot.Utilities.RealTimeApiFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

/**
 * Created by inesnefoussi on 3/6/17.
 */
public class main {

    public static void main(String... args) {
        AbstractApplicationContext context = new AnnotationConfigApplicationContext(SpringContext.class);
        SlackRealTimeMessagingClient slackRealTimeMessagingClient = RealTimeApiFactory.getSlackRealTimeMessagingClient();
        //SlackWebApiClient slackWebApiClient = WebApiFactory.getSlackWebApiClient();
        slackRealTimeMessagingClient.addListener(Event.MESSAGE, (MessageListener)context.getBean("messageListener"));
        slackRealTimeMessagingClient.connect();
       /* TribeChannelMatcher matcher = new TribeChannelMatcher("tribu");
        System.out.println(matcher.isTribe());*/
    }
}
