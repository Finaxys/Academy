package com.finaxys.slackbot.BUL.Classes;

import allbegray.slack.type.Channel;
import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.BUL.Interfaces.NewTribeJoinedService;
import com.finaxys.slackbot.BUL.Matchers.TribeChannelMatcher;
import com.finaxys.slackbot.DAL.SlackUser;
import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.Utilities.Log;
import com.finaxys.slackbot.interfaces.SlackUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NewTribeJoinedServiceImpl implements NewTribeJoinedService {

	@Autowired
	private SlackUserService slackUserService;

	@Autowired
	public  SlackApiAccessService slackApiAccessService;
	
	@Override
	@Transactional
	public void onNewTribeJoined(JsonNode jsonNode) {
		if (jsonIsValid(jsonNode)) {
			String userId = jsonNode.get("user").asText();
			String channelId = jsonNode.get("channel").asText();
			

			new Thread(()->{ 	SlackUser userProfile = slackUserService.get(userId);
								String name = slackApiAccessService.getUser(userId).getName();
								userProfile = (userProfile == null) ? new SlackUser(userId, name) : userProfile;

								//userProfile.incrementScore(SCORE_GRID.JOINED_TRIBUTE.value());
								slackUserService.save(userProfile);
								Log.logChannelTribeJoined(name, slackApiAccessService.getChannel(channelId).getName());
								
							}).start();
		}
	}

	private boolean jsonIsValid(JsonNode jsonNode) {
		String channelId = jsonNode.get("channel").asText();
		Channel channel = slackApiAccessService.getChannel(channelId);
		String channelName = channel.getName();

		if (channelName == null)
			return false;

		TribeChannelMatcher tribeChannelMatcher = new TribeChannelMatcher();

		if (tribeChannelMatcher.isNotTribe(channelName))
			return false;
		if (jsonNode.get("user").asText() == null)
			return false;
		return true;
	}
}