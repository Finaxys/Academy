package com.finaxys.slackbot.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.finaxys.slackbot.BUL.Classes.SlackApiAccessService;
import com.finaxys.slackbot.BUL.Listeners.ChannelChangedListener;
import com.finaxys.slackbot.BUL.Listeners.ChannelCreatedListener;
import com.finaxys.slackbot.BUL.Listeners.MessageListener;
import com.finaxys.slackbot.BUL.Listeners.ReactionAddedListener;
import com.finaxys.slackbot.BUL.Listeners.ReactionRemovedListener;
import com.finaxys.slackbot.BUL.Listeners.UserChangedListener;
import com.finaxys.slackbot.Utilities.SlackBot;

import allbegray.slack.rtm.Event;
import allbegray.slack.rtm.SlackRealTimeMessagingClient;

@Component
public class ContextRefreshedListener implements ApplicationListener<ContextRefreshedEvent> {

	private EventHolderBean eventHolderBean;

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

			slackRealTimeMessagingClient.addListener(Event.USER_CHANGE,
					(UserChangedListener) context.getBean("userChangedListener"));

			slackRealTimeMessagingClient.addListener(Event.CHANNEL_CREATED,
					(ChannelCreatedListener) context.getBean("channelCreatedListener"));


			slackRealTimeMessagingClient.addListener(Event.REACTION_ADDED,
					(ReactionAddedListener) context.getBean("reactionAddedListener"));

			slackRealTimeMessagingClient.addListener(Event.REACTION_REMOVED,
					(ReactionRemovedListener) context.getBean("reactionRemovedListener"));

			slackRealTimeMessagingClient.addListener(Event.MESSAGE,
					(MessageListener) context.getBean("messageListener"));

			slackRealTimeMessagingClient.addListener(Event.CHANNEL_RENAME,
					(ChannelChangedListener) context.getBean("channelChangedListener"));
			slackRealTimeMessagingClient.connect();
			SlackApiAccessService.init();

		}

	}
}