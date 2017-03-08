package com.finaxys.slackbot.BUL.Classes;

import allbegray.slack.type.Channel;
import allbegray.slack.webapi.SlackWebApiClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.BUL.Interfaces.NewTributeJoinedService;
import com.finaxys.slackbot.DAL.Classes.Repository;
import com.finaxys.slackbot.Domains.FinaxysProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by inesnefoussi on 3/7/17.
 */
@Service
public class NewTributeJoinedServiceImpl implements NewTributeJoinedService {

    @Autowired
    private Repository<FinaxysProfile,String> finaxysProfileManager;

    public Repository<FinaxysProfile, String> getFinaxysProfileManager() {
        return finaxysProfileManager;
    }

    public void setFinaxysProfileManager(Repository<FinaxysProfile, String> finaxysProfileManager) {
        this.finaxysProfileManager = finaxysProfileManager;
    }

    @Transactional
    public void onNewTributeJoined(JsonNode jsonNode , SlackWebApiClient webApiClient) {
        if (jsonIsValid(jsonNode, webApiClient)) {
            String userId = jsonNode.get("user").asText();

            FinaxysProfile userProfile = finaxysProfileManager.findById(userId);

            if (userProfile != null) {
                System.out.println("************* current score: " + userProfile.getScore() + " ************** ");
                userProfile.setScore(userProfile.getScore() + SCORE_GRID.JOINED_TRIBUTE.value());
                System.out.println("************* new score: " + userProfile.getScore() + " ************** ");
                finaxysProfileManager.updateEntity(userProfile);
            } else {
                FinaxysProfile finaxysProfile = new FinaxysProfile();
                finaxysProfile.setId(userId);
                finaxysProfile.setScore(SCORE_GRID.JOINED_TRIBUTE.value());

                finaxysProfileManager.addEntity(finaxysProfile);

                System.out.println("************* New user added ************** ");
            }
        }
    }

    private boolean jsonIsValid(JsonNode jsonNode ,SlackWebApiClient webApiClient) {
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
        Channel channel = webApiClient.getChannelInfo(channelId);
        String channelName = channel.getName();

        if (channelName == null) {
            System.out.println("************* Channel name is null ************** ");
            return false;
        }
        if (channelName.length() <= 5) {
            System.out.println("************* This is not a tribute ************** ");
            return false;
        }
        String channelNameStarting = channelName.substring(0, 5);
        if (!channelNameStarting.toUpperCase().equals("TRIBU")) {
            System.out.println("************* This is not a tribute ************** ");
            return false;
        }
        if (jsonNode.get("user").asText() == null) {
            return false;
        }
        return true;
    }

}
