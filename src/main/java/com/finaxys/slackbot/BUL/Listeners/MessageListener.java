package com.finaxys.slackbot.BUL.Listeners;

import allbegray.slack.rtm.EventListener;
import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.BUL.Interfaces.NewTributeJoinedService;
import com.finaxys.slackbot.Utilities.FinaxysSlackBotLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by inesnefoussi on 3/7/17.
 */
@Component
public class MessageListener implements EventListener {

    @Autowired
    private NewTributeJoinedService newTributeJoinedService;

    public MessageListener() {
    }

    public NewTributeJoinedService getNewTributeJoinedService() {
        return newTributeJoinedService;
    }

    public void setNewTributeJoinedService(NewTributeJoinedService newTributeJoinedService) {
        this.newTributeJoinedService = newTributeJoinedService;
    }

    public void handleMessage(JsonNode jsonNode) {
        FinaxysSlackBotLogger.logger.info("Message being handled...");
        newTributeJoinedService.onNewTributeJoined(jsonNode);
    }
}
