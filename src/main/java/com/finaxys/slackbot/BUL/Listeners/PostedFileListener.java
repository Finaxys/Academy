package com.finaxys.slackbot.BUL.Listeners;


import allbegray.slack.rtm.EventListener;
import allbegray.slack.type.Channel;
import allbegray.slack.type.User;
import allbegray.slack.webapi.SlackWebApiClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.BUL.Interfaces.InnovateService;

@Component
public class PostedFileListener implements EventListener {
	@Autowired
	InnovateService innovateService ;
	
	
    private SlackWebApiClient slackWebApiClient;
    
    public PostedFileListener(SlackWebApiClient slackWebApiClient) {
        super();
       
    }
    
    
    
    
    public SlackWebApiClient getSlackWebApiClient() {
		return slackWebApiClient;
	}




	public void setSlackWebApiClient(SlackWebApiClient slackWebApiClient) {
		this.slackWebApiClient = slackWebApiClient;
	}




	public PostedFileListener()
    {}

    public void handleMessage(JsonNode jsonNode) {
        
    
        if(jsonNode.has("username"))
             {
            
            if(jsonNode.get("username").equals("bot") )
                
            {
            System.out.println("bot");
            
        
            }
              }
         if(jsonNode.has("subtype") )
                    { 
                if(jsonNode.get("subtype").asText().equals("file_share") ){
                Channel c = slackWebApiClient.getChannelInfo(jsonNode.get("channel").asText());
                
                System.out.println("file_shared");
                System.out.println(jsonNode.toString());
                
                User u= slackWebApiClient.getUserInfo(jsonNode.get("user").asText());
                System.out.println(c.getName()+" "+u.getName());
                // ajouter score
                innovateService.addInnovateScore(u);
               
                
                }

}
    }
}