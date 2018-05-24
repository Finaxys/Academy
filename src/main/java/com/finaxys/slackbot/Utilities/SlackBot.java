package com.finaxys.slackbot.Utilities;

import allbegray.slack.SlackClientFactory;
import allbegray.slack.rtm.SlackRealTimeMessagingClient;
import allbegray.slack.webapi.SlackWebApiClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.finaxys.slackbot.DAL.DebugMode;
import com.finaxys.slackbot.interfaces.DebugModeService;

@Component
public class SlackBot {
	
	private static SlackWebApiClient slackWebApiClient;
	private static SlackRealTimeMessagingClient slackRealTimeMessagingClient;
	

	
	public static SlackWebApiClient getSlackWebApiClient() {
		return slackWebApiClient == null ? SlackClientFactory.createWebApiClient(Settings.botUserOauthAccessToken)
				: slackWebApiClient;
	}

	public static SlackRealTimeMessagingClient getSlackRealTimeMessagingClient() {
		return slackRealTimeMessagingClient == null
				? SlackClientFactory.createSlackRealTimeMessagingClient(Settings.botUserOauthAccessToken)
				: slackRealTimeMessagingClient;
	}
	
	public static void postMessageAsync(String channelId, String message) {
		new Thread(() -> {
			getSlackWebApiClient().postMessage(channelId, message);
		}).start();
	}

	public static void postMessageToDebugChannelAsync(String message) {
		System.out.println("*******" + Settings.debugChannelId);
		postMessageAsync(Settings.debugChannelId, message);
	}
	
	public static void postMessage(String channelId, String message, boolean flag) {
		System.out.println(flag);
		if(flag) {
			if (!channelId.equals(Settings.debugChannelId))				
				postMessageAsync(channelId, message);
			postMessageToDebugChannelAsync(message);
		}
		else
			postMessageAsync(channelId, message);
	
	}
}
