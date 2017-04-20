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
			//TODO delete
			System.out.println(users.get(i).getId() + " : " + users.get(i).getName());
		}
		for (int i = 0; i < channels.size(); i++) {
			allChannels.put(channels.get(i).getId(), channels.get(i));
		}

	}

	public User getUser(String userId) {
		if (!allUsers.containsKey(userId)) {
			allUsers.put(userId, SlackBot.getSlackWebApiClient().getUserInfo(userId));
		}
		return allUsers.get(userId);
	}

	public Channel getChannel(String channelId) {
		System.out.println(allChannels);
		if (!allChannels.containsKey(channelId)) {
			allChannels.put(channelId, SlackBot.getSlackWebApiClient().getChannelInfo(channelId));
		}
		return allChannels.get(channelId);
	}
	
	public void updateUser(String userId){
		allUsers.put(userId, SlackBot.getSlackWebApiClient().getUserInfo(userId));
	}
	
	public void updateChannel(String channelId){
		allChannels.put(channelId, SlackBot.getSlackWebApiClient().getChannelInfo(channelId));
	}
}
