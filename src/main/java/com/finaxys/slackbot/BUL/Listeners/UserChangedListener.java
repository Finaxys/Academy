package com.finaxys.slackbot.BUL.Listeners;

import allbegray.slack.rtm.EventListener;
import allbegray.slack.webapi.SlackWebApiClient;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.finaxys.slackbot.BUL.Classes.SlackApiAccessService;
import com.finaxys.slackbot.DAL.SlackUser;
import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.Utilities.SlackBot;

@Component
public class UserChangedListener implements EventListener {
    
	@Autowired
    private Repository<SlackUser, String> finaxysProfileRepository;
	
	@Autowired
	public SlackApiAccessService slackApiAccessService;
	
    public UserChangedListener() {
    }

    @Override
    public void handleMessage(JsonNode jsonNode) {
    	
        System.out.println("i'aaaaaam here" + jsonNode.toString());
        
        if (jsonNode.has("type")) {
        	
            if (!jsonNode.get("type").asText().equals("user_change")) return;
            if ( jsonNode.get("type").asText().equals("user_change")) {
            	
                String finaxysProfileId 	= jsonNode.get("user").get("id").asText();
                String finaxysProfileIName 	= slackApiAccessService.getUser(finaxysProfileId)
                                                      .getName();
                SlackUser finaxysProfile = finaxysProfileRepository.findById(finaxysProfileId);
                
                if (finaxysProfile == null) {
                    finaxysProfile = new SlackUser(finaxysProfileId, finaxysProfileIName);
                }
                
                finaxysProfile.setName(finaxysProfileIName);
                finaxysProfileRepository.saveOrUpdate(finaxysProfile);
            }
        }
    }
}

