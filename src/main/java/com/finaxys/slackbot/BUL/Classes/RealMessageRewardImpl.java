package com.finaxys.slackbot.BUL.Classes;

import allbegray.slack.type.Channel;
import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.BUL.Interfaces.RealMessageReward;
import com.finaxys.slackbot.BUL.Matchers.RealMessageMatcher;
import com.finaxys.slackbot.BUL.Matchers.TribeChannelMatcher;
import com.finaxys.slackbot.DAL.SlackUser;
import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.DAL.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RealMessageRewardImpl implements RealMessageReward 
{
    @Autowired
    private Repository<SlackUser, String> slackUserRepository;

    @Autowired
	public SlackApiAccessService slackApiAccessService;
    
    @Autowired
    private Repository<Role, Integer> roleRepository;
    
    @Override
    @Transactional
    public void rewardReadMessage(JsonNode jsonNode) 
    {
    	if (noAdminsStored())
            setFinaxysProfileAsAdministrator(jsonNode);
        
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
        
        slackUserRepository.addEntity(slackUser);
    }

    public boolean noAdminsStored() 
    {
    	
        return roleRepository.getByCriterion("role", "admin").size()==0;

    }

    private void increaseSlackUserScore(String userId) {
    	
        new Thread(()->
        {
        	SlackUser profile = slackUserRepository.findById(userId);
        	
        	if (profile == null) 
        	{
        		String profileName = slackApiAccessService.getUser(userId).getName();
        		profile = new SlackUser(userId, profileName);
        	}
        	
        	profile.incrementScore(SCORE_GRID.SENT_A_REAL_MESSAGE.value());
        	slackUserRepository.saveOrUpdate(profile);
        				
        }).start();
        
    }

    private Channel getChannelById(String channelId) 
    {
        return slackApiAccessService.getChannel(channelId);
    }
}