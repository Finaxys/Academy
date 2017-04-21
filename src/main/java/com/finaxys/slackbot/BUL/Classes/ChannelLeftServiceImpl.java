package com.finaxys.slackbot.BUL.Classes;

import allbegray.slack.type.Channel;
import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.BUL.Interfaces.ChannelLeftService;
import com.finaxys.slackbot.BUL.Matchers.TribeChannelMatcher;
import com.finaxys.slackbot.DAL.SlackUser;
import com.finaxys.slackbot.DAL.SlackUserEvent;
import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.Utilities.Log;
import com.finaxys.slackbot.interfaces.SlackUserEventService;
import com.finaxys.slackbot.interfaces.SlackUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChannelLeftServiceImpl implements ChannelLeftService {

	@Autowired
	public SlackApiAccessService slackApiAccessService;

	@Autowired
	private SlackUserService slackUserService;
	
	@Autowired
	private SlackUserEventService slackUserEventService;

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
