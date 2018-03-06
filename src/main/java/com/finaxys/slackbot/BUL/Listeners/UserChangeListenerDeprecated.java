/*package com.finaxys.slackbot.BUL.Listeners;

import allbegray.slack.webapi.SlackWebApiClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.BUL.Classes.SlackApiAccessService;
import com.finaxys.slackbot.DAL.SlackUser;
import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.Utilities.SlackBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserChangeListenerDeprecated 
{
    @Autowired
    private Repository<SlackUser, String> slackUserRepository;

    @Autowired
	public SlackApiAccessService slackApiAccessService;
	
    public UserChangeListenerDeprecated() {}

    public void handleMessage(JsonNode jsonNode) 
    {
        
    	new Thread(()->
    	{
    		String slackUserId 		= jsonNode.get("user").get("user_id").asText();
            SlackUser slackUser 	= slackUserRepository.findById(slackUserId);
            
            if (slackUser == null) 
            {
                SlackWebApiClient slackWebApiClient = SlackBot.getSlackWebApiClient();
                String 		slackUserIName 			= slackApiAccessService.getUser(slackUserId).getName();
                slackUser 							= new SlackUser(slackUserId, slackUserIName);
            }
            
            slackUserRepository.saveOrUpdate(slackUser);
    		
    	}).start();
    }
}
*/