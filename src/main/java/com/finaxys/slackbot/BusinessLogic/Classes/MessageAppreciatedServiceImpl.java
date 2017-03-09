package com.finaxys.slackbot.BusinessLogic.Classes;

import allbegray.slack.rtm.SlackRealTimeMessagingClient;
import allbegray.slack.type.Channel;
import allbegray.slack.webapi.SlackWebApiClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.BusinessLogic.Interfaces.MessageAppreciatedService;
import com.finaxys.slackbot.DAL.Classes.GenericRepositoryImpl;
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
	private GenericRepository<FinaxysProfile, String> myGenericRepo1;

	public GenericRepository<FinaxysProfile, String> getFinaxysProfileManager() {
		return myGenericRepo1;
	}

	public void setFinaxysProfileManager(GenericRepository<FinaxysProfile, String> finaxysProfileRepository) {
		this.myGenericRepo1 = finaxysProfileRepository;
	}

	public void addMessageAppreciatedScore(JsonNode jsonNode) {

		List<String> listEmojis = new ArrayList<String>();
		listEmojis.add("+1");
		listEmojis.add("clap");
		listEmojis.add("ok_hand");
		if (jsonNode != null && jsonNode.get("item_user").asText() != null) {
			String userId = jsonNode.get("item_user").asText();
			String myUserId = jsonNode.get("user").asText();
			String reaction = jsonNode.get("reaction").asText();
			if (listEmojis.contains(reaction)) {
				if (userId != null && userId!=myUserId) {
				{
					System.out.println(userId);
					FinaxysProfile userProfile = myGenericRepo1.findById(userId);
					if (userProfile != null) {
						userProfile.setScore(userProfile.getScore() + 10);
						myGenericRepo1.updateEntity(userProfile);
					}
				}
			}
		}
}
}
}
