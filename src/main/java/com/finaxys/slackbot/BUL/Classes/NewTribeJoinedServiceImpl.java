package com.finaxys.slackbot.BUL.Classes;

import allbegray.slack.type.Channel;
import allbegray.slack.webapi.SlackWebApiClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.BUL.Interfaces.NewTribeJoinedService;
import com.finaxys.slackbot.BUL.Matchers.TribeChannelMatcher;
import com.finaxys.slackbot.DAL.FinaxysProfile;
import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.Utilities.Log;
import com.finaxys.slackbot.Utilities.SlackBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NewTribeJoinedServiceImpl implements NewTribeJoinedService {

	@Autowired
	private Repository<FinaxysProfile, String> finaxysProfileRepository;

	@Autowired
	public SlackApiAccessService slackApiAccessService;
	
	@Override
	@Transactional
	public void onNewTribeJoined(JsonNode jsonNode) {
		if (jsonIsValid(jsonNode)) {
			String userId = jsonNode.get("user").asText();
			String channelId = jsonNode.get("channel").asText();
			FinaxysProfile userProfile = finaxysProfileRepository.findById(userId);

			String name = slackApiAccessService.getUser(userId).getName();
			userProfile = (userProfile == null) ? new FinaxysProfile(userId, name) : userProfile;

			userProfile.incrementScore(SCORE_GRID.JOINED_TRIBUTE.value());

			finaxysProfileRepository.saveOrUpdate(userProfile);
			Log.logChannelTribeJoined(name, slackApiAccessService.getChannel(channelId).getName());
		}
	}

	private boolean jsonIsValid(JsonNode jsonNode) {
		if (!jsonNode.has("subtype"))
			return false;

		String messageSubtype = jsonNode.get("subtype").asText();

		if (!messageSubtype.equals("channel_join"))
			return false;

		String channelId = jsonNode.get("channel").asText();
		SlackWebApiClient webApiClient = SlackBot.getSlackWebApiClient();
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