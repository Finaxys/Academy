package com.finaxys.slackbot.BUL.Classes;

import allbegray.slack.type.Channel;
import allbegray.slack.webapi.SlackWebApiClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.BUL.Interfaces.NewTributeJoinedService;
import com.finaxys.slackbot.BUL.Matchers.TribeChannelMatcher;
import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.Domains.FinaxysProfile;
import com.finaxys.slackbot.Utilities.SlackBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by inesnefoussi on 3/7/17.
 */
@Service
public class NewTributeJoinedServiceImpl implements NewTributeJoinedService {

    @Autowired
    private Repository<FinaxysProfile, String> finaxysProfileRepository;

    @Transactional
    public void onNewTributeJoined(JsonNode jsonNode) {
        if (jsonIsValid(jsonNode)) {
            String userId = jsonNode.get("user").asText();

            FinaxysProfile userProfile = finaxysProfileRepository.findById(userId);

            if (userProfile != null) {
                System.out.println("************* current score: " + userProfile.getScore() + " ************** ");
                userProfile.setScore(userProfile.getScore() + SCORE_GRID.JOINED_TRIBUTE.value());
                System.out.println("************* new score: " + userProfile.getScore() + " ************** ");
                finaxysProfileRepository.updateEntity(userProfile);
            } else {
                FinaxysProfile finaxysProfile = new FinaxysProfile();
                finaxysProfile.setId(userId);
                finaxysProfile.setScore(SCORE_GRID.JOINED_TRIBUTE.value());

                finaxysProfileRepository.addEntity(finaxysProfile);

                System.out.println("************* New user added ************** ");
            }
        }
    }

    private boolean jsonIsValid(JsonNode jsonNode) {
        if (jsonNode == null) {
            System.out.println("************* json is null ************** ");
            return false;
        }
        if (!jsonNode.has("subtype")) {
            System.out.println("************* This type of messages doesn't have a subtype ************** ");
            return false;
        }
        String messageSubtype = jsonNode.get("subtype").asText();

        if (!messageSubtype.equals("channel_join")) {
            System.out.println("************* This is not a channel join ************** ");
            return false;
        }
        String channelId = jsonNode.get("channel").asText();
        SlackWebApiClient webApiClient = SlackBot.getSlackWebApiClient();
        Channel channel = webApiClient.getChannelInfo(channelId);
        String channelName = channel.getName();

        if (channelName == null) {
            System.out.println("************* Channel name is null ************** ");
            return false;
        }

        TribeChannelMatcher tribeChannelMatcher = new TribeChannelMatcher();
        if (tribeChannelMatcher.isNotTribe(channelName)) {
            System.out.println("************* This is not a tribute ************** ");
            return false;
        }
        if (jsonNode.get("user").asText() == null) {
            return false;
        }
        return true;
    }

}
