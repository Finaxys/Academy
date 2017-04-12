package com.finaxys.slackbot.BUL.Classes;

import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.BUL.Interfaces.ReactionRemovedService;
import com.finaxys.slackbot.DAL.FinaxysProfile;
import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.Utilities.Log;
import com.finaxys.slackbot.Utilities.SlackBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReactionRemovedServiceImpl implements ReactionRemovedService {

	@Autowired
	private Repository<FinaxysProfile, String> finaxysProfileRepository;

	@Autowired
	public SlackApiAccessService slackApiAccessService;
	
	@Override
	public void substituteReactionRemovedScore(JsonNode jsonNode) {
		List<String> listEmojis = new ArrayList<String>();
		listEmojis.add("+1");
		listEmojis.add("clap");
		listEmojis.add("ok_hand");

		if (jsonNode == null)
			return;

		if (jsonNode.get("item_user").asText() == null)
			return;

		String itemUserId			= jsonNode.get("item_user").asText();
		String myUserId 			= jsonNode.get("user").asText();
		String reaction 			= jsonNode.get("reaction").asText();
		FinaxysProfile userProfile 	= finaxysProfileRepository.findById(itemUserId);

		if (listEmojis.contains(reaction) && itemUserId != null && itemUserId != myUserId && userProfile != null) {
			userProfile.decrementScore(SCORE_GRID.APPRECIATED_MESSAGE.value());
			finaxysProfileRepository.updateEntity(userProfile);
			Log.logReactionRemoved(slackApiAccessService.getUser(myUserId).getName(),
					slackApiAccessService.getUser(itemUserId).getName());

		}
	}

}
