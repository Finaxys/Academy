package com.finaxys.slackbot.BUL.Listeners;

import allbegray.slack.rtm.EventListener;
import allbegray.slack.webapi.SlackWebApiClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.BUL.Interfaces.InnovateService;
import com.finaxys.slackbot.Utilities.FinaxysSlackBotLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostedFileListener implements EventListener {
    @Autowired
    InnovateService innovateService;

    private SlackWebApiClient slackWebApiClient;

    public PostedFileListener(SlackWebApiClient slackWebApiClient) {
        super();
    }

    public PostedFileListener() {
    }

    public void handleMessage(JsonNode jsonNode) {


        FinaxysSlackBotLogger.logPostedFile();
    }
}
