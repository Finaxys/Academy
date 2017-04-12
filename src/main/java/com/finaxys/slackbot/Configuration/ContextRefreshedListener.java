package com.finaxys.slackbot.Configuration;

import allbegray.slack.SlackClientFactory;
import allbegray.slack.rtm.SlackRealTimeMessagingClient;

import com.finaxys.slackbot.BUL.Classes.SlackApiAccessService;
import com.finaxys.slackbot.Utilities.Settings;
import com.finaxys.slackbot.Utilities.SlackBot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.context.event.ContextRefreshedEvent;

@Component
public class ContextRefreshedListener implements ApplicationListener<ContextRefreshedEvent> {

	private EventHolderBean eventHolderBean;
	
	// private static SlackWebApiClient slackWebApiClient;
	private static SlackRealTimeMessagingClient slackRealTimeMessagingClient;

	@Autowired
	public void setEventHolderBean(EventHolderBean eventHolderBean) {
		this.eventHolderBean = eventHolderBean;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

		if (!eventHolderBean.getEventFired()) {

			eventHolderBean.setEventFired(true);

			System.out.println("Start");
			ApplicationContext context = contextRefreshedEvent.getApplicationContext();

			SlackRealTimeMessagingClient slackRealTimeMessagingClient = SlackBot.getSlackRealTimeMessagingClient();

			
			/*
			slackRealTimeMessagingClient.addListener(Event.USER_CHANGE,
					(UserChangedListener) context.getBean("userChangedListener"));

			slackRealTimeMessagingClient.addListener(Event.CHANNEL_CREATED,
					(ChannelCreatedListener) context.getBean("channelCreatedListener"));

			slackRealTimeMessagingClient.addListener(Event.REACTION_ADDED,
					(ReactionAddedListener) context.getBean("reactionAddedListener"));

			slackRealTimeMessagingClient.addListener(Event.REACTION_REMOVED,
					(ReactionRemovedListener) context.getBean("reactionRemovedListener"));
			*/
			
			/*
			slackRealTimeMessagingClient.addListener(Event.MESSAGE,
					(MessageListener) context.getBean("messageListener"));
			
			*/

			slackRealTimeMessagingClient.connect();
			SlackApiAccessService.init();
			System.out.println("End");
			
		}

	}

	/*
	 * private SlackWebApiClient getSlackWebApiClient() { return
	 * slackWebApiClient == null ?
	 * SlackClientFactory.createWebApiClient(Settings.botUserOauthAccessToken) :
	 * slackWebApiClient; }
	 */

}