package com.finaxys.slackbot.BUL.Classes;

import allbegray.slack.type.Channel;
import allbegray.slack.webapi.SlackWebApiClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.BUL.Interfaces.NewTributeJoinedService;
import com.finaxys.slackbot.BUL.Matchers.TribeChannelMatcher;
import com.finaxys.slackbot.DAL.Classes.Repository;
import com.finaxys.slackbot.Domains.FinaxysProfile;
import com.finaxys.slackbot.Utilities.FinaxysSlackBotLogger;
import com.finaxys.slackbot.Utilities.WebApiFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by inesnefoussi on 3/7/17.
 */
@Service
public class NewTributeJoinedServiceImpl implements NewTributeJoinedService {

    @Autowired
    private Repository<FinaxysProfile, String> finaxysProfileManager;

    public Repository<FinaxysProfile, String> getFinaxysProfileManager() {
        return finaxysProfileManager;
    }

    public void setFinaxysProfileManager(Repository<FinaxysProfile, String> finaxysProfileManager) {
        this.finaxysProfileManager = finaxysProfileManager;
    }

    @Transactional
    public void onNewTributeJoined(JsonNode jsonNode) {
        if (jsonIsValid(jsonNode)) {
            String userId = jsonNode.get("user").asText();

            FinaxysProfile userProfile = finaxysProfileManager.findById(userId);

            if (userProfile != null) {
                userProfile.setScore(userProfile.getScore() + SCORE_GRID.JOINED_TRIBUTE.value());
                finaxysProfileManager.updateEntity(userProfile);
            } else {
                FinaxysProfile finaxysProfile = new FinaxysProfile();
                finaxysProfile.setId(userId);
                finaxysProfile.setScore(SCORE_GRID.JOINED_TRIBUTE.value());

                finaxysProfileManager.addEntity(finaxysProfile);

                FinaxysSlackBotLogger.logger.info("************* New user added to database; Responsible class: " + this.getClass().getSimpleName() + " ************** ");
            }
        }
    }

    private boolean jsonIsValid(JsonNode jsonNode) {
        if (jsonNode == null) {
            FinaxysSlackBotLogger.logger.error("************* json is null ************** ");
            return false;
        }
        if (!jsonNode.has("subtype")) {
            FinaxysSlackBotLogger.logger.error("************* This type of messages doesn't have a subtype ************** ");
            return false;
        }
        String messageSubtype = jsonNode.get("subtype").asText();

        if (!messageSubtype.equals("channel_join")) {
            FinaxysSlackBotLogger.logger.error("************* This is not a channel join ************** ");
            return false;
        }
        SlackWebApiClient webApiClient = WebApiFactory.getSlackWebApiClient();
        String channelId = jsonNode.get("channel").asText();
        Channel channel = webApiClient.getChannelInfo(channelId);
        String channelName = channel.getName();

        if (channelName == null) {
            FinaxysSlackBotLogger.logger.error("************* Channel name is null ************** ");
            return false;
        }

        TribeChannelMatcher tribeChannelMatcher = new TribeChannelMatcher(channelName);
        if (!tribeChannelMatcher.isTribe()) {
            FinaxysSlackBotLogger.logger.error("************* This is not a tribute ************** ");
            return false;
        }
        if (jsonNode.get("user").asText() == null) {
            return false;
        }
        return true;
    }

}
