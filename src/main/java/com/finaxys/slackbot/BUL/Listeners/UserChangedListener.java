package com.finaxys.slackbot.BUL.Listeners;

import allbegray.slack.rtm.EventListener;
import allbegray.slack.webapi.SlackWebApiClient;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.finaxys.slackbot.DAL.FinaxysProfile;
import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.Utilities.SlackBot;

@Component
public class UserChangedListener implements EventListener {
    
	@Autowired
    private Repository<FinaxysProfile, String> finaxysProfileRepository;

    public UserChangedListener() {
    }

    @Override
    public void handleMessage(JsonNode jsonNode) {
    	
        System.out.println("i'aaaaaam here" + jsonNode.toString());
        
        if (jsonNode.has("type")) {
        	
            if (!jsonNode.get("type").asText().equals("user_change")) return;
            if ( jsonNode.get("type").asText().equals("user_change")) {
            	
                String finaxysProfileId 	= jsonNode.get("user").get("id").asText();
                String finaxysProfileIName 	= SlackBot.getSlackWebApiClient()
                                                      .getUserInfo(finaxysProfileId)
                                                      .getName();
                FinaxysProfile finaxysProfile = finaxysProfileRepository.findById(finaxysProfileId);
                
                if (finaxysProfile == null) {
                    finaxysProfile = new FinaxysProfile(finaxysProfileId, finaxysProfileIName);
                }
                
                finaxysProfile.setName(finaxysProfileIName);
                finaxysProfileRepository.saveOrUpdate(finaxysProfile);
            }
        }
    }
}

