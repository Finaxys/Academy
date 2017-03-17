package com.finaxys.slackbot.BUL.Listeners;

import allbegray.slack.rtm.EventListener;
import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.BUL.Interfaces.InnovateService;
import com.finaxys.slackbot.Utilities.FinaxysSlackBotLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by inesnefoussi on 3/9/17.
 */
@Component
public class ChannelCreatedListener implements EventListener {

    @Autowired
    private InnovateService innovateService;

    @Override
    public void handleMessage(JsonNode jsonNode) {
        innovateService.addInnovateScore(jsonNode, this);
    }
}
