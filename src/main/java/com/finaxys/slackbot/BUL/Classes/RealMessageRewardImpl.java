package com.finaxys.slackbot.BUL.Classes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.BUL.Interfaces.RealMessageReward;
import com.finaxys.slackbot.BUL.Matchers.RealMessageMatcher;
import com.finaxys.slackbot.BUL.Matchers.TribeChannelMatcher;
import com.finaxys.slackbot.DAL.SlackUser;
import com.finaxys.slackbot.interfaces.SlackUserService;

import allbegray.slack.type.Channel;

@Service
public class RealMessageRewardImpl implements RealMessageReward 
{
    @Autowired
    private SlackUserService slackUserService;

    @Autowired
	public SlackApiAccessService slackApiAccessService;
    
    @Override
    @Transactional
    public void rewardReadMessage(JsonNode jsonNode) 
    {
        
        String 	channelId 	= jsonNode.get("channel").asText();
        Channel channel 	= getChannelById(channelId);
        TribeChannelMatcher tribeChannelMatcher = new TribeChannelMatcher();
        
        if (tribeChannelMatcher.isNotTribe(channel.getName())) 
        	return;
        
        RealMessageMatcher 	realMessageMatcher = new RealMessageMatcher();
        
        String 	message = jsonNode.get("text").asText();
        
        
        
        if (realMessageMatcher.isRealMessage(message)) 
        {
            String userId = jsonNode.get("user").asText();
            increaseSlackUserScore(userId);
        }
    }

    public void setFinaxysProfileAsAdministrator(JsonNode jsonNode) 
    {
        String 		slackUserId 	= jsonNode.get("user").asText();

        String 		profileName 	= slackApiAccessService.getUser(slackUserId).getName();

        SlackUser 	slackUser 		= new SlackUser(slackUserId, profileName);
        
        slackUserService.save(slackUser);
    }

    private void increaseSlackUserScore(String userId) {
    	
        new Thread(()->
        {
        	SlackUser profile = slackUserService.get(userId);
        	
        	if (profile == null) 
        	{
        		String profileName = slackApiAccessService.getUser(userId).getName();
        		profile = new SlackUser(userId, profileName);
        	}
        	
        	//profile.incrementScore(SCORE_GRID.SENT_A_REAL_MESSAGE.value());
        	slackUserService.save(profile);
        				
        }).start();
        
    }

    private Channel getChannelById(String channelId) 
    {
        return slackApiAccessService.getChannel(channelId);
    }
}