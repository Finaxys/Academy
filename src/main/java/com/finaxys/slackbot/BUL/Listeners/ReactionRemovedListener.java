
package com.finaxys.slackbot.BUL.Listeners;

import allbegray.slack.rtm.EventListener;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.finaxys.slackbot.BUL.Interfaces.ReactionRemovedService;

@Component
public class ReactionRemovedListener implements EventListener {

    @Autowired
    ReactionRemovedService reactionRemovedService;

    public ReactionRemovedListener() {
    }

    public void handleMessage(JsonNode jsonNode) {
        System.out.println(jsonNode.toString());
        reactionRemovedService.substituteReactionRemovedScore(jsonNode);

    }

}
