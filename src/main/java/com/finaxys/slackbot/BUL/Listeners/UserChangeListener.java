package com.finaxys.slackbot.BUL.Listeners;

import allbegray.slack.webapi.SlackWebApiClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.BUL.Classes.SlackApiAccessService;
import com.finaxys.slackbot.DAL.SlackUser;
import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.Utilities.SlackBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserChangeListener {
	
    @Autowired
    private Repository<SlackUser, String> finaxysProfileRepository;

    @Autowired
	public SlackApiAccessService slackApiAccessService;
	
    public UserChangeListener() {
    }

    public void handleMessage(JsonNode jsonNode) {
    	
        String finaxysProfileId 		= jsonNode.get("user").get("user_id").asText();
        SlackUser finaxysProfile 	= finaxysProfileRepository.findById(finaxysProfileId);
        
        if (finaxysProfile == null) {
        	
            SlackWebApiClient slackWebApiClient = SlackBot.getSlackWebApiClient();
            String finaxysProfileIName 			= slackApiAccessService.getUser(finaxysProfileId).getName();
            finaxysProfile 						= new SlackUser(finaxysProfileId, finaxysProfileIName);
        }
        
        //TODO finaxysProfile.setAdministrator(true);
        finaxysProfileRepository.saveOrUpdate(finaxysProfile);
    }
}
