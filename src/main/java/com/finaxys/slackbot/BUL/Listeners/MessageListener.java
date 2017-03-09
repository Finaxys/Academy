package com.finaxys.slackbot.BUL.Listeners;

import allbegray.slack.rtm.EventListener;
import allbegray.slack.webapi.SlackWebApiClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.BUL.Interfaces.NewTributeJoinedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by inesnefoussi on 3/7/17.
 */
@Component
public class MessageListener implements EventListener {

    private SlackWebApiClient webApiClient;

    @Autowired
    private NewTributeJoinedService newTributeJoinedService;

    public MessageListener() {
    }

    public void handleMessage(JsonNode jsonNode) {
        newTributeJoinedService.onNewTributeJoined(jsonNode);
    }
}
