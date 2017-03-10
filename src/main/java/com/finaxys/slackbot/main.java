package com.finaxys.slackbot;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import com.finaxys.slackbot.BUL.Listeners.ChannelCreatedListener;
import com.finaxys.slackbot.BUL.Listeners.MessageListener;
import com.finaxys.slackbot.Configuration.Classes.SpringContext;
import com.finaxys.slackbot.Utilities.RealTimeApiFactory;

import allbegray.slack.rtm.Event;
import allbegray.slack.rtm.SlackRealTimeMessagingClient;

/**
 * Created by inesnefoussi on 3/6/17.
 */
public class main {
	public static void main(String[] args) {
		AbstractApplicationContext context = new AnnotationConfigApplicationContext(SpringContext.class);
		SlackRealTimeMessagingClient slackRealTimeMessagingClient = RealTimeApiFactory
				.getSlackRealTimeMessagingClient();
		slackRealTimeMessagingClient.addListener(Event.MESSAGE, (MessageListener) context.getBean("messageListener"));
		slackRealTimeMessagingClient.addListener(Event.CHANNEL_CREATED,
				(ChannelCreatedListener) context.getBean("channelCreatedListener"));
		slackRealTimeMessagingClient.connect();

	}
}
