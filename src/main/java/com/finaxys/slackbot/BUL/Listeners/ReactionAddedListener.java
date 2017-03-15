package com.finaxys.slackbot.BUL.Listeners;


import allbegray.slack.rtm.EventListener;
import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.BUL.Interfaces.ReactionAddedService;
import com.finaxys.slackbot.Utilities.FinaxysSlackBotLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReactionAddedListener implements EventListener {

    @Autowired
    ReactionAddedService reactionAddedService;

    public ReactionAddedListener() {
    }

    public void handleMessage(JsonNode jsonNode) {
        System.out.println(jsonNode.toString());
        reactionAddedService.addReactionAddedScore(jsonNode);
        FinaxysSlackBotLogger.logReactionAdded();
    }

}
