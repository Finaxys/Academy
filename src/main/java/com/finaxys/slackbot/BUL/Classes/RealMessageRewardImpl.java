package com.finaxys.slackbot.BUL.Classes;

import allbegray.slack.type.Channel;
import allbegray.slack.webapi.SlackWebApiClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.BUL.Interfaces.RealMessageReward;
import com.finaxys.slackbot.BUL.Matchers.RealMessageMatcher;
import com.finaxys.slackbot.BUL.Matchers.TribeChannelMatcher;
import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.Domains.FinaxysProfile;
import com.finaxys.slackbot.Utilities.SlackBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RealMessageRewardImpl implements RealMessageReward {

    @Autowired
    private Repository<FinaxysProfile, String> finaxysProfileRepository;

    @Override
    @Transactional
    public void rewardReadMessage(JsonNode jsonNode) {
        if (noAdminsStored())
            setFinaxysProfileAsAdministrator(jsonNode);
        String channelId = jsonNode.get("channel").asText();
        Channel channel = getChannelById(channelId);
        TribeChannelMatcher tribeChannelMatcher = new TribeChannelMatcher();
        if (tribeChannelMatcher.isNotTribe(channel.getName())) return;
        RealMessageMatcher realMessageMatcher = new RealMessageMatcher();
        String message = jsonNode.get("text").asText();
        if (realMessageMatcher.isRealMessage(message)) {
            String userId = jsonNode.get("user").asText();
            increaseSlackUserScore(userId, SCORE_GRID.SENT_A_REAL_MESSAGE);
        }
    }

    public void setFinaxysProfileAsAdministrator(JsonNode jsonNode) {
        String finaxysProfileId = jsonNode.get("user").asText();

        SlackWebApiClient slackWebApiClient = SlackBot.getSlackWebApiClient();
        String profileName = slackWebApiClient.getUserInfo(finaxysProfileId).getName();

        FinaxysProfile finaxysProfile = new FinaxysProfile(finaxysProfileId, profileName);
        finaxysProfile.setAdministrator(true);
        finaxysProfileRepository.addEntity(finaxysProfile);
    }

    public boolean noAdminsStored() {
        return finaxysProfileRepository.getByCriterion("administrator", true).isEmpty();
    }

    private void increaseSlackUserScore(String userId, SCORE_GRID sentARealMessage) {
        FinaxysProfile profile = finaxysProfileRepository.findById(userId);
        if (profile == null) {
            SlackWebApiClient slackWebApiClient = SlackBot.getSlackWebApiClient();
            String profileName = slackWebApiClient.getUserInfo(userId).getName();
            profile = new FinaxysProfile(userId, profileName);
        }
        profile.incrementScore(sentARealMessage.value());
        finaxysProfileRepository.saveOrUpdate(profile);
    }

    private Channel getChannelById(String channelId) {
        return SlackBot.getSlackWebApiClient().getChannelInfo(channelId);
    }
}