package com.finaxys.slackbot.BUL.Classes;

import allbegray.slack.type.Channel;

import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.BUL.Interfaces.InnovateService;
import com.finaxys.slackbot.BUL.Listeners.ChannelCreatedListener;
import com.finaxys.slackbot.BUL.Listeners.MessageListener;
import com.finaxys.slackbot.BUL.Matchers.TribeChannelMatcher;
import com.finaxys.slackbot.DAL.FinaxysProfile;
import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.Utilities.FinaxysSlackBotLogger;
import com.finaxys.slackbot.Utilities.Log;
import com.finaxys.slackbot.Utilities.SlackBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InnovateServiceImpl implements InnovateService {

    @Autowired
    private Repository<FinaxysProfile, String> finaxysProfileRepository;

    public void rewardTribeCreator(JsonNode json) {
        String userId = json.get("channel").get("creator").asText();
        String channelId = json.get("channel").get("id").asText();

        Channel channel = SlackBot.getSlackWebApiClient().getChannelInfo(channelId);
        TribeChannelMatcher tribeChannelMatcher = new TribeChannelMatcher();
        if (tribeChannelMatcher.isNotTribe(channel.getName()))
            return;

        FinaxysProfile userProfile = finaxysProfileRepository.findById(userId);
        if (userProfile == null) {
            String name = SlackBot.getSlackWebApiClient().getUserInfo(userId).getName();
            userProfile = new FinaxysProfile(userId, name);
        }
        userProfile.incrementScore(SCORE_GRID.WAS_INNOVATIVE.value());
        finaxysProfileRepository.saveOrUpdate(userProfile);
        Log.logChannelTribeCreated(SlackBot.getSlackWebApiClient().getUserInfo(userId).getName(), SlackBot.getSlackWebApiClient().getChannelInfo(channelId).getName());


    }

    public void rewardFileSharing(JsonNode json) {
    	System.out.println("rewardFileSharing!");
        
        String userId = json.get("user_id").asText();
        String channelId = json.get("channel").get("id").asText();
        FinaxysProfile userProfile = finaxysProfileRepository.findById(userId);
        if (userProfile == null) {
            String name = SlackBot.getSlackWebApiClient().getUserInfo(userId).getName();
            userProfile = new FinaxysProfile(userId, name);
        }
        userProfile.incrementScore(SCORE_GRID.WAS_INNOVATIVE.value());
        finaxysProfileRepository.saveOrUpdate(userProfile);
        Log.logPostedFile(SlackBot.getSlackWebApiClient().getUserInfo(userId).getName(), SlackBot.getSlackWebApiClient().getChannelInfo(channelId).getName());
    }


    public void addInnovateScore(JsonNode json, ChannelCreatedListener channelCreatedListener) {
    	System.out.println("addInnovateScore!");
        String userId = json.get("channel").get("creator").asText();
        String channelId = json.get("channel").get("id").asText();
        Channel channel = SlackBot.getSlackWebApiClient().getChannelInfo(channelId);
        TribeChannelMatcher tribeChannelMatcher = new TribeChannelMatcher();
        if (tribeChannelMatcher.isNotTribe(channel.getName())) return;
        FinaxysProfile userProfile = finaxysProfileRepository.findById(userId);
        String name = SlackBot.getSlackWebApiClient().getUserInfo(userId).getName();
        userProfile = (userProfile == null) ? new FinaxysProfile(userId,name) : userProfile;
        userProfile.incrementScore(SCORE_GRID.WAS_INNOVATIVE.value());
        finaxysProfileRepository.saveOrUpdate(userProfile);
        FinaxysSlackBotLogger.logChannelTribuCreated(name,SlackBot.getSlackWebApiClient().getChannelInfo(channelId).getName());

    }

    public void addInnovateScore(JsonNode json, MessageListener messageListener) {
    	System.out.println("addInnovateScore!");
        
        if (!json.has("subtype"))
            return;

        if (!json.get("subtype").asText().equals("file_share"))
            return;
        String userId = json.get("user").asText();
        FinaxysProfile userProfile = finaxysProfileRepository.findById(userId);
        String name = SlackBot.getSlackWebApiClient().getUserInfo(userId).getName();
        userProfile = (userProfile == null) ? new FinaxysProfile(userId,name) : userProfile;
        userProfile.incrementScore(SCORE_GRID.WAS_INNOVATIVE.value());
        finaxysProfileRepository.saveOrUpdate(userProfile);
        FinaxysSlackBotLogger.logPostedFile(SlackBot.getSlackWebApiClient().getUserInfo(userId).getName());
    }
}

