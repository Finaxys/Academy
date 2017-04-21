package com.finaxys.slackbot.Utilities;

import allbegray.slack.SlackClientFactory;
import allbegray.slack.rtm.SlackRealTimeMessagingClient;
import allbegray.slack.webapi.SlackWebApiClient;
import org.springframework.stereotype.Component;

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
		postMessageAsync(Settings.debugChannelId, message);
	}
}
