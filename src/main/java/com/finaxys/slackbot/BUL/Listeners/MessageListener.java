package com.finaxys.slackbot.BUL.Listeners;

import allbegray.slack.rtm.EventListener;
import allbegray.slack.webapi.SlackWebApiClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.BUL.Interfaces.NewTributeJoinedService;
import com.finaxys.slackbot.BUL.Interfaces.RealMessageReward;
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

    @Autowired
    private RealMessageReward realMessageReward;

    public MessageListener() {
    }

    public void handleMessage(JsonNode jsonNode) {
        newTributeJoinedService.onNewTributeJoined(jsonNode);
        realMessageReward.rewardReadlMessage(jsonNode);
    }
}
