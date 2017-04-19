package com.finaxys.slackbot.BUL.Classes;

import allbegray.slack.type.Channel;
import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.BUL.Interfaces.RealMessageReward;
import com.finaxys.slackbot.BUL.Matchers.RealMessageMatcher;
import com.finaxys.slackbot.BUL.Matchers.TribeChannelMatcher;
import com.finaxys.slackbot.DAL.SlackUser;
import com.finaxys.slackbot.DAL.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RealMessageRewardImpl implements RealMessageReward {

    @Autowired
    private Repository<SlackUser, String> finaxysProfileRepository;

    @Autowired
	public SlackApiAccessService slackApiAccessService;
	
    @Override
    @Transactional
    public void rewardReadMessage(JsonNode jsonNode) {
        if (noAdminsStored())
            setFinaxysProfileAsAdministrator(jsonNode);
        
        String 	channelId 	= jsonNode.get("channel").asText();
        Channel channel 	= getChannelById(channelId);
        TribeChannelMatcher tribeChannelMatcher = new TribeChannelMatcher();
        
        if (tribeChannelMatcher.isNotTribe(channel.getName())) 
        	return;
        
        RealMessageMatcher realMessageMatcher = new RealMessageMatcher();
        String message = jsonNode.get("text").asText();
        
        if (realMessageMatcher.isRealMessage(message)) {
            String userId = jsonNode.get("user").asText();
            increaseSlackUserScore(userId);
        }
    }

    public void setFinaxysProfileAsAdministrator(JsonNode jsonNode) {
        String finaxysProfileId = jsonNode.get("user").asText();

        String profileName = slackApiAccessService.getUser(finaxysProfileId).getName();

        SlackUser finaxysProfile = new SlackUser(finaxysProfileId, profileName);
        //TODO finaxysProfile.setAdministrator(true);
        finaxysProfileRepository.addEntity(finaxysProfile);
    }

    public boolean noAdminsStored() {
    	//TODO corriger avec le role admin
    	return true;
        //return finaxysProfileRepository.getByCriterion("administrator", true).isEmpty();
    }

    private void increaseSlackUserScore(String userId) {
        SlackUser profile = finaxysProfileRepository.findById(userId);
        if (profile == null) {
            String profileName = slackApiAccessService.getUser(userId).getName();
            profile = new SlackUser(userId, profileName);
        }
        profile.incrementScore(SCORE_GRID.SENT_A_REAL_MESSAGE.value());
        finaxysProfileRepository.saveOrUpdate(profile);
    }

    private Channel getChannelById(String channelId) {
        return slackApiAccessService.getChannel(channelId);
    }
}