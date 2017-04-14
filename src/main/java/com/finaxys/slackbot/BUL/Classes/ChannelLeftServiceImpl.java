package com.finaxys.slackbot.BUL.Classes;

import allbegray.slack.type.Channel;
import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.BUL.Interfaces.ChannelLeftService;
import com.finaxys.slackbot.BUL.Matchers.TribeChannelMatcher;
import com.finaxys.slackbot.DAL.FinaxysProfile;
import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.Utilities.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChannelLeftServiceImpl implements ChannelLeftService {

	@Autowired
	public SlackApiAccessService slackApiAccessService;

	@Autowired
	private Repository<FinaxysProfile, String> finaxysProfileRepository;

	@Transactional
	@Override
	public void onChannelLeaveMessage(JsonNode jsonNode) {
		if (jsonIsValid(jsonNode)) {
			String channelId = jsonNode.get("channel").asText();
			Channel channel = slackApiAccessService.getChannel(channelId);
			TribeChannelMatcher matcher = new TribeChannelMatcher();

			if (matcher.isNotTribe(channel.getName()))
				return;

			String userId = jsonNode.get("user").asText();
			FinaxysProfile profile = finaxysProfileRepository.findById(userId);

			if (profile.getScore() == 0)
				return;

			profile.decrementScore(SCORE_GRID.JOINED_TRIBUTE.value());
			finaxysProfileRepository.updateEntity(profile);

			Log.logMemberLeftChannel(profile.getName(), channel.getName());
		}
	}

	private boolean jsonIsValid(JsonNode jsonNode) {
		if (!jsonNode.has("user"))
			return false;
		return true;
	}
}
