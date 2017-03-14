package com.finaxys.slackbot.BUL.Classes;

import com.finaxys.slackbot.Utilities.SlackBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.BUL.Interfaces.InnovateService;
import com.finaxys.slackbot.BUL.Listeners.ChannelCreatedListener;
import com.finaxys.slackbot.BUL.Listeners.MessageListener;
import com.finaxys.slackbot.BUL.Matchers.TribeChannelMatcher;
import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.Domains.FinaxysProfile;

import allbegray.slack.type.Channel;

@Service
public class InnovateServiceImpl implements InnovateService {

	@Autowired
	private Repository<FinaxysProfile, String> finaxysProfileRepository;
	/*
	 * public Repository<FinaxysProfile, String> getFinaxysProfileManager() {
	 * return finaxysProfileRepository; }
	 *
	 * public void setFinaxysProfileManager(Repository<FinaxysProfile, String>
	 * finaxysProfileRepository) { this.finaxysProfileRepository =
	 * finaxysProfileRepository; }
	 */

	public void addInnovateScore(JsonNode json, ChannelCreatedListener channelCreatedListener) {
		String userId = json.get("channel").get("creator").asText();

		String channelId = json.get("channel").get("id").asText();
		Channel channel = SlackBot.getSlackWebApiClient().getChannelInfo(channelId);
		TribeChannelMatcher tribeChannelMatcher = new TribeChannelMatcher();
		if (tribeChannelMatcher.isNotTribe(channel.getName()))
			return;
		FinaxysProfile userProfile = finaxysProfileRepository.findById(userId);
		userProfile = (userProfile == null) ? new FinaxysProfile(userId) : userProfile;
		userProfile.incrementScore(SCORE_GRID.WAS_INNOVATIVE.value());
		finaxysProfileRepository.saveOrUpdate(userProfile);

	}

	public void addInnovateScore(JsonNode json, MessageListener messageListener) {

		if (!json.has("subtype"))
			return;
		if (!json.get("subtype").asText().equals("file_share"))
			return;
		String userId = json.get("user").asText();
		FinaxysProfile userProfile = finaxysProfileRepository.findById(userId);
		userProfile = (userProfile == null) ? new FinaxysProfile(userId) : userProfile;
		userProfile.incrementScore(SCORE_GRID.WAS_INNOVATIVE.value());
		finaxysProfileRepository.saveOrUpdate(userProfile);

	}

}
