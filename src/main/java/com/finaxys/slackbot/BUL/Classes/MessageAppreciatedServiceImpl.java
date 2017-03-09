package com.finaxys.slackbot.BUL.Classes;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.BUL.Interfaces.MessageAppreciatedService;
import com.finaxys.slackbot.DAL.Classes.Repository;
import com.finaxys.slackbot.Domains.FinaxysProfile;

/**
 * Created by inesnefoussi on 3/7/17.
 */
@Service
public class MessageAppreciatedServiceImpl implements MessageAppreciatedService {

	@Autowired
	private Repository<FinaxysProfile, String> myGenericRepo1;

	public Repository<FinaxysProfile, String> getFinaxysProfileManager() {
		return myGenericRepo1;
	}

	public void setFinaxysProfileManager(Repository<FinaxysProfile, String> finaxysProfileRepository) {
		this.myGenericRepo1 = finaxysProfileRepository;
	}

	public void addMessageAppreciatedScore(JsonNode jsonNode) {

		List<String> listEmojis = new ArrayList<String>();
		listEmojis.add("+1");
		listEmojis.add("C");
		listEmojis.add("ok_hand");
		if (jsonNode != null && jsonNode.get("item_user").asText() != null) {
			String itemUserId = jsonNode.get("item_user").asText();
			String myUserId = jsonNode.get("user").asText();
			String reaction = jsonNode.get("reaction").asText();
			if (listEmojis.contains(reaction)) {
				if (itemUserId != null && itemUserId != myUserId) {
					{
						System.out.println(itemUserId);
						FinaxysProfile userProfile = myGenericRepo1.findById(itemUserId);
						if (userProfile != null) {
							userProfile.setScore(userProfile.getScore() + SCORE_GRID.APPRECIATED_MESSAGE.value());
							myGenericRepo1.updateEntity(userProfile);
						}
						else
						{
							FinaxysProfile user = myGenericRepo1.addEntity(new FinaxysProfile(itemUserId,false, SCORE_GRID.APPRECIATED_MESSAGE.value()));

						}
					}
				}
			}
		}
	}
}
