package com.finaxys.slackbot.BUL.Classes;

import allbegray.slack.type.Channel;
import allbegray.slack.webapi.SlackWebApiClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.BUL.Interfaces.NewTributeJoinedService;
import com.finaxys.slackbot.BUL.Matchers.TribeChannelMatcher;
import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.Domains.FinaxysProfile;
import com.finaxys.slackbot.Utilities.FinaxysSlackBotLogger;
import com.finaxys.slackbot.Utilities.SlackBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by inesnefoussi on 3/7/17.
 */
@Service
public class NewTributeJoinedServiceImpl implements NewTributeJoinedService {

    @Autowired
    private Repository<FinaxysProfile, String> finaxysProfileRepository;

    @Override
    @Transactional
    public void onNewTributeJoined(JsonNode jsonNode) {

        if (jsonIsValid(jsonNode)) {
            String userId = jsonNode.get("user").asText();
            String channelId = jsonNode.get("channel").asText();
            FinaxysProfile userProfile = finaxysProfileRepository.findById(userId);

            if (userProfile != null)
                userProfile.incrementScore(SCORE_GRID.JOINED_TRIBUTE.value());
            else {
                userProfile = new FinaxysProfile();
                userProfile.setId(userId);
                userProfile.setScore(SCORE_GRID.JOINED_TRIBUTE.value());
            }

            finaxysProfileRepository.saveOrUpdate(userProfile);
            FinaxysSlackBotLogger.logChannelTributeJoined(SlackBot.getSlackWebApiClient().getUserInfo(userId).getName(),SlackBot.getSlackWebApiClient().getChannelInfo(channelId).getName());

        }
    }

    private boolean jsonIsValid(JsonNode jsonNode) {
        System.out.println(jsonNode.toString());
        if (jsonNode == null) return false;
        if (!jsonNode.has("subtype")) return false;

        String messageSubtype = jsonNode.get("subtype").asText();

        if (!messageSubtype.equals("channel_join")) return false;

        String channelId = jsonNode.get("channel").asText();
        SlackWebApiClient webApiClient = SlackBot.getSlackWebApiClient();
        Channel channel = webApiClient.getChannelInfo(channelId);
        String channelName = channel.getName();

        if (channelName == null) return false;

        TribeChannelMatcher tribeChannelMatcher = new TribeChannelMatcher();

        if (tribeChannelMatcher.isNotTribe(channelName)) return false;
        if (jsonNode.get("user").asText() == null)
            return false;
        return true;
    }

}