package com.finaxys.slackbot.BUL.Listeners;

import allbegray.slack.rtm.EventListener;
import allbegray.slack.webapi.SlackWebApiClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.BUL.Interfaces.*;
import com.finaxys.slackbot.DAL.FinaxysProfile;
import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.Utilities.SlackBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Created by Bannou on 21/03/2017.
 */
@Component
public class UserChangeListener {
    @Autowired
    private Repository<FinaxysProfile, String> finaxysProfileRepository;

    public UserChangeListener() {
    }

    public void handleMessage(JsonNode jsonNode) {
        String finaxysProfileId = jsonNode.get("user").get("user_id").asText();
        FinaxysProfile finaxysProfile = finaxysProfileRepository.findById(finaxysProfileId);
        if (finaxysProfile == null){
            SlackWebApiClient slackWebApiClient = SlackBot.getSlackWebApiClient();
            String finaxysProfileIName = slackWebApiClient.getUserInfo(finaxysProfileId).getName();
            finaxysProfile = new FinaxysProfile(finaxysProfileId, finaxysProfileIName);
        }
        finaxysProfile.setAdministrator(true);
        finaxysProfileRepository.saveOrUpdate(finaxysProfile);
    }
}
