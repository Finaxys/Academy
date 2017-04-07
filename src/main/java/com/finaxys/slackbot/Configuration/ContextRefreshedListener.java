package com.finaxys.slackbot.Configuration;

import allbegray.slack.SlackClientFactory;
import allbegray.slack.rtm.Event;
import allbegray.slack.rtm.SlackRealTimeMessagingClient;
import allbegray.slack.webapi.SlackWebApiClient;
import com.finaxys.slackbot.BUL.Listeners.ChannelCreatedListener;
import com.finaxys.slackbot.BUL.Listeners.MessageListener;
import com.finaxys.slackbot.BUL.Listeners.ReactionAddedListener;
import com.finaxys.slackbot.BUL.Listeners.ReactionRemovedListener;
import com.finaxys.slackbot.BUL.Listeners.UserChangedListener;

import com.finaxys.slackbot.Utilities.PropertyLoader;
import com.finaxys.slackbot.Utilities.Settings;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.finaxys.slackbot.Configuration.SpringContext;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Component
public class ContextRefreshedListener implements ApplicationListener<ContextRefreshedEvent> {

	private EventHolderBean eventHolderBean;

	
	private static SlackWebApiClient slackWebApiClient;
	private static SlackRealTimeMessagingClient slackRealTimeMessagingClient;

	
	
	@Autowired
	public void setEventHolderBean(EventHolderBean eventHolderBean) {
		this.eventHolderBean = eventHolderBean;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

		if (!eventHolderBean.getEventFired()) {
			
			File f = new File(".");
			System.out.println("eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
			System.out.println(f.getAbsolutePath());
			System.out.println(Settings.botUserOauthAccessToken);
			/*
			eventHolderBean.setEventFired(true);
			System.out.println("Context Event Received");
			System.out.println(Settings.botUserOauthAccessToken);
			ApplicationContext context = contextRefreshedEvent.getApplicationContext();
			System.out.println(context.getClass().toString());
			System.out.println("Start : Jar Project Config");
			System.out.println("userChangedListener : " + context.getBean("userChangedListener"));
			SlackRealTimeMessagingClient slackRealTimeMessagingClient = getSlackRealTimeMessagingClient();
			slackRealTimeMessagingClient.addListener(Event.USER_CHANGE,
					(UserChangedListener) context.getBean("userChangedListener"));
			slackRealTimeMessagingClient.addListener(Event.CHANNEL_CREATED,
					(ChannelCreatedListener) context.getBean("channelCreatedListener"));
			slackRealTimeMessagingClient.addListener(Event.REACTION_ADDED,
					(ReactionAddedListener) context.getBean("reactionAddedListener"));
			slackRealTimeMessagingClient.addListener(Event.REACTION_REMOVED,
					(ReactionRemovedListener) context.getBean("reactionRemovedListener"));
			
			//slackRealTimeMessagingClient.addListener(Event.MESSAGE,
			//(MessageListener) context.getBean("messageListener"));
			slackRealTimeMessagingClient.connect();
			
			
			System.out.println((MessageListener) context.getBean("messageListener"));
			System.out.println("End : Jar Project Config");
			*/
		}

	}
	
	private SlackWebApiClient getSlackWebApiClient() {
		return slackWebApiClient == null ? SlackClientFactory.createWebApiClient(Settings.botUserOauthAccessToken) : slackWebApiClient;
	}

	private SlackRealTimeMessagingClient getSlackRealTimeMessagingClient() {
		return slackRealTimeMessagingClient == null
				? SlackClientFactory.createSlackRealTimeMessagingClient(Settings.botUserOauthAccessToken) : slackRealTimeMessagingClient;
	}
}