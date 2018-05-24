package com.finaxys.slackbot.BUL.Classes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.BUL.Interfaces.ChannelLeftService;
import com.finaxys.slackbot.BUL.Matchers.TribeChannelMatcher;
import com.finaxys.slackbot.DAL.SlackUser;
import com.finaxys.slackbot.Utilities.Log;
import com.finaxys.slackbot.interfaces.SlackUserService;

import allbegray.slack.type.Channel;

@Service
public class ChannelLeftServiceImpl implements ChannelLeftService {

	@Autowired
	public SlackApiAccessService slackApiAccessService;

	@Autowired
	private SlackUserService slackUserService;
	
	@Transactional
	@Override
	public void onChannelLeaveMessage(JsonNode jsonNode) 
	{
		if (jsonIsValid(jsonNode)) 
		{
			String 	channelId 	= jsonNode.get("channel").asText();
			Channel channel 	= slackApiAccessService.getChannel(channelId);
			TribeChannelMatcher matcher = new TribeChannelMatcher();

			if (matcher.isNotTribe(channel.getName()))
				return;

			String userId = jsonNode.get("user").asText();
			
			SlackUser profile = slackUserService.get(userId);

			if (profile.getScore() == 0)
				return;
		
			profile.decrementScore(SCORE_GRID.JOINED_TRIBUTE.value());
			
			new Thread(()->{	slackUserService.save(profile);	}).start();
			
			Log.logMemberLeftChannel(profile.getName(), channel.getName());
		}
	}

	private boolean jsonIsValid(JsonNode jsonNode) 
	{
		if (!jsonNode.has("user"))
			return false;
		return true;
	}
}
