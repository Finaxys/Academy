package com.finaxys.slackbot.BUL.Listeners;

import allbegray.slack.rtm.EventListener;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.finaxys.slackbot.BUL.Classes.SlackApiAccessService;
import com.finaxys.slackbot.DAL.SlackUser;
import com.finaxys.slackbot.DAL.Repository;

@Component
public class UserChangeListener implements EventListener 
{
	@Autowired
    private Repository<SlackUser, String> slackUserRepository;
	
	@Autowired
	public SlackApiAccessService slackApiAccessService;
	
    public UserChangeListener() {}

    @Override
    public void handleMessage(JsonNode jsonNode) 
    {
    	
        System.out.println("i'aaaaaam here" + jsonNode.toString());
        
        if (jsonNode.has("type")) 
        {
            if (!jsonNode.get("type").asText().equals("user_change")) 
            	return;
            
            if ( jsonNode.get("type").asText().equals("user_change")) 
            {
                new Thread(()->
                {	
                	String slackUserId 	= jsonNode.get("user").get("id").asText();
                    
                    slackApiAccessService.updateUser(slackUserId);
                    
                    String slackUserIName 	= slackApiAccessService.getUser(slackUserId).getName();
                    
                    SlackUser slackUser 	= slackUserRepository.findById(slackUserId);
                    
                    if (slackUser == null) 
                    {
                        slackUser = new SlackUser(slackUserId, slackUserIName);
                    }
                    
                    slackUser.setName(slackUserIName);
                    
                    slackUserRepository.saveOrUpdate(slackUser);
                	
                }).start();
            }
        }
    }
}

