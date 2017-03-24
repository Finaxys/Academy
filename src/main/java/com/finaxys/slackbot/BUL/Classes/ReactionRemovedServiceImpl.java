package com.finaxys.slackbot.BUL.Classes;

import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.BUL.Interfaces.ReactionRemovedService;
import com.finaxys.slackbot.DAL.FinaxysProfile;
import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.Utilities.FinaxysSlackBotLogger;
import com.finaxys.slackbot.Utilities.SlackBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReactionRemovedServiceImpl implements ReactionRemovedService {

    @Autowired
    private Repository<FinaxysProfile, String> finaxysProfileRepository;

    @Override
    public void substituteReactionRemovedScore(JsonNode jsonNode) {

        List<String> listEmojis = new ArrayList<String>();
        listEmojis.add("+1");
        listEmojis.add("clap");
        listEmojis.add("ok_hand");
        if (jsonNode == null) return;
        if (jsonNode.get("item_user").asText() == null) return;
        String itemUserId = jsonNode.get("item_user").asText();
        String myUserId = jsonNode.get("user").asText();
        String reaction = jsonNode.get("reaction").asText();
        if (listEmojis.contains(reaction)) {
            if (itemUserId != null && itemUserId != myUserId) {
                {

                    FinaxysProfile userProfile = finaxysProfileRepository.findById(itemUserId);
                    if (userProfile != null) {
                        userProfile.decrementScore(SCORE_GRID.APPRECIATED_MESSAGE.value());
                        finaxysProfileRepository.updateEntity(userProfile);
                    }

                }
            }
            FinaxysSlackBotLogger.logReactionRemoved(SlackBot.getSlackWebApiClient().getUserInfo(myUserId).getName(), SlackBot.getSlackWebApiClient().getUserInfo(itemUserId).getName());
        }
    }

}


