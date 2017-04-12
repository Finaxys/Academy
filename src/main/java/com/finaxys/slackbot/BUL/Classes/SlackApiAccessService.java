package com.finaxys.slackbot.BUL.Classes;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import com.finaxys.slackbot.Utilities.Settings;
import com.finaxys.slackbot.Utilities.SlackBot;
import allbegray.slack.type.Channel;
import allbegray.slack.type.User;

@Service
public class SlackApiAccessService {

	private static Map<String, User> allUsers;

	private static Map<String, Channel> allChannels;

	public static void init() {
		System.out.println("Init SlackApiAccessService" + Settings.appVerificationToken);

		SlackBot.getSlackRealTimeMessagingClient().connect();

		List<Channel> channels = SlackBot.getSlackWebApiClient().getChannelList();
		List<User> users = SlackBot.getSlackWebApiClient().getUserList();

		allUsers = new HashMap<String, User>();
		allChannels = new HashMap<String, Channel>();

		for (int i = 0; i < users.size(); i++) {
			allUsers.put(users.get(i).getId(), users.get(i));

			System.out.println(users.get(i).getName());
		}
		for (int i = 0; i < channels.size(); i++) {
			allChannels.put(channels.get(i).getId(), channels.get(i));
			System.out.println(channels.get(i).getName());
		}

	}

	public User getUser(String userId) {
		if (allUsers == null) {
			allUsers = new HashMap<String, User>();
		}
		if (!allUsers.containsKey(userId)) {
			allUsers.put(userId, SlackBot.getSlackWebApiClient().getUserInfo(userId));
		}
		return allUsers.get(userId);
	}

	public Channel getChannel(String channelId) {
		if (allChannels == null) {
			allChannels = new HashMap<String, Channel>();
		}
		if (!allChannels.containsKey(channelId)) {
			allChannels.put(channelId, SlackBot.getSlackWebApiClient().getChannelInfo(channelId));
		}
		return allChannels.get(channelId);
	}
}
