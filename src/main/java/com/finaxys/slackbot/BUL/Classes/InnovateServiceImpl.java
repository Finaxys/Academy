package com.finaxys.slackbot.BUL.Classes;

import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.BUL.Interfaces.InnovateService;
import com.finaxys.slackbot.BUL.Matchers.TribeChannelMatcher;
import com.finaxys.slackbot.DAL.FinaxysProfile;
import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.Utilities.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InnovateServiceImpl implements InnovateService {

	@Autowired
	private Repository<FinaxysProfile, String> finaxysProfileRepository;

	@Autowired
	public SlackApiAccessService slackApiAccessService;

	public void rewardChannelCreated(JsonNode json) {
		String channelId = json.get("channel").get("id").asText();
		String channelName = slackApiAccessService.getChannel(channelId).getName();
		TribeChannelMatcher tribeChannelMatcher = new TribeChannelMatcher();

		if (tribeChannelMatcher.isNotTribe(channelName))
			return;

		String userId = json.get("channel").get("creator").asText();
		FinaxysProfile userProfile = finaxysProfileRepository.findById(userId);
		String name = slackApiAccessService.getUser(userId).getName();
		userProfile = (userProfile == null) ? new FinaxysProfile(userId, name) : userProfile;

		addInnovateScore(userProfile);

		Log.logChannelTribeCreated(name, channelName);
	}

	public void rewardFileShared(JsonNode json) {

		String userId = json.get("user").asText();
		FinaxysProfile userProfile = finaxysProfileRepository.findById(userId);
		String name = slackApiAccessService.getUser(userId).getName();
		userProfile = (userProfile == null) ? new FinaxysProfile(userId, name) : userProfile;

		addInnovateScore(userProfile);

		Log.logPostedFile(name, "");
	}

	private void addInnovateScore(FinaxysProfile userProfile) {
		userProfile.incrementScore(SCORE_GRID.WAS_INNOVATIVE.value());
		finaxysProfileRepository.saveOrUpdate(userProfile);
	}
}
