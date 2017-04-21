package com.finaxys.slackbot.BUL.Classes;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;
import org.springframework.stereotype.Service;
import com.finaxys.slackbot.Utilities.Settings;
import com.finaxys.slackbot.Utilities.SlackBot;
import allbegray.slack.type.Channel;
import allbegray.slack.type.User;

@Service
public class SlackApiAccessService
{

	private static Map<String, User> allUsers;

	private static Map<String, Channel> allChannels;
	
	public static void refreshCache() 
	{
		List<Channel> 	channels 	= SlackBot.getSlackWebApiClient().getChannelList();
		List<User> 		users 		= SlackBot.getSlackWebApiClient().getUserList();

		HashMap<String, User> 		tmpAllUsers 	= new HashMap<String, User>();
		HashMap<String, Channel> 	tmpAllChannels 	= new HashMap<String, Channel>();

		for (int i = 0; i < users.size(); i++) 
		{
			tmpAllUsers.put(users.get(i).getId(), users.get(i));
			
			System.out.println(users.get(i).getId() + " : " + users.get(i).getName());
		}
		
		for (int i = 0; i < channels.size(); i++) 
		{
			tmpAllChannels.put(channels.get(i).getId(), channels.get(i));
		}
		
		allUsers = tmpAllUsers;
		allChannels = tmpAllChannels;

	}

	public User getUser(String userId) 
	{
		if (!allUsers.containsKey(userId)) 
		{
			allUsers.put(userId, SlackBot.getSlackWebApiClient().getUserInfo(userId));
		}
		return allUsers.get(userId);
	}

	public Channel getChannel(String channelId) 
	{
		System.out.println(allChannels);
		
		if (!allChannels.containsKey(channelId)) 
		{
			allChannels.put(channelId, SlackBot.getSlackWebApiClient().getChannelInfo(channelId));
		}
		
		return allChannels.get(channelId);
	}
	
	public void updateUser(String userId)
	{
		allUsers.put(userId, SlackBot.getSlackWebApiClient().getUserInfo(userId));
	}
	
	public void updateChannel(String channelId)
	{
		allChannels.put(channelId, SlackBot.getSlackWebApiClient().getChannelInfo(channelId));
	}
}
