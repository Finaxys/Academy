package com.finaxys.slackbot.BUL.Listeners;

import allbegray.slack.rtm.EventListener;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.finaxys.slackbot.BUL.Interfaces.InnovateService;

@Component
public class ChannelCreatedListener implements EventListener {

    @Autowired
    private InnovateService innovateService;

    @Override
    public void handleMessage(JsonNode jsonNode) {

        innovateService.rewardChannelCreated(jsonNode);
    }
}
