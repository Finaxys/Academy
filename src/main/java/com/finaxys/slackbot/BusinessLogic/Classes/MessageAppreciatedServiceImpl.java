package com.finaxys.slackbot.BusinessLogic.Classes;

import allbegray.slack.rtm.SlackRealTimeMessagingClient;
import allbegray.slack.type.Channel;
import allbegray.slack.webapi.SlackWebApiClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.BusinessLogic.Interfaces.MessageAppreciatedService;
import com.finaxys.slackbot.DAL.Interfaces.GenericRepository;
import com.finaxys.slackbot.Domains.FinaxysProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by inesnefoussi on 3/7/17.
 */
@Service
public class MessageAppreciatedServiceImpl implements MessageAppreciatedService {

	@Autowired
	private GenericRepository<FinaxysProfile, String> finaxysProfileRepository;

	public GenericRepository<FinaxysProfile, String> getFinaxysProfileManager() {
		return finaxysProfileRepository;
	}

	public void setFinaxysProfileManager(GenericRepository<FinaxysProfile, String> finaxysProfileRepository) {
		this.finaxysProfileRepository = finaxysProfileRepository;
	}

	
	public void addMessageAppreciatedScore(JsonNode jsonNode) {
		String userId = jsonNode.get("item_user").asText();
		List<String> listEmojis = new ArrayList<String>();
		listEmojis.add("+1");
		listEmojis.add("clap");
		listEmojis.add("ok_hand");
		if (jsonNode != null) {
			String reaction = jsonNode.get("reaction").asText();
			if (listEmojis.contains(reaction)) {
				if (userId != null) {
					FinaxysProfile userProfile = finaxysProfileRepository.findById(userId);
					if (userProfile != null) {
						userProfile.setScore(userProfile.getScore() + 10);
						finaxysProfileRepository.updateEntity(userProfile);
					}
				}
			}
			
			
			}
		else
		{System.out.println("cc");}
	}
}
		
	
