package com.finaxys.slackbot.BUL.Listeners;

import allbegray.slack.rtm.EventListener;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.finaxys.slackbot.BUL.Classes.SlackApiAccessService;

@Component
public class ChannelRenameListener implements EventListener 
{
    @Autowired
    private SlackApiAccessService slackApiAccessService;

    @Override
    public void handleMessage(JsonNode jsonNode) 
    {
    	if (jsonNode.has("type")) 
    	{
         	if (!jsonNode.get("type").asText().equals("channel_rename")) 
         		return;
         	
            if ( jsonNode.get("type").asText().equals("channel_rename")) 
            {
            	String channelId = jsonNode.get("channel").get("id").asText();
            	
            	slackApiAccessService.updateChannel(channelId);
            }
        }
    }
}
