package com.finaxys.slackbot.BUL.Classes;

import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.BUL.Interfaces.ReactionAddedService;
import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.Domains.FinaxysProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by inesnefoussi on 3/7/17.
 */
@Service
public class ReactionAddedServiceImpl implements ReactionAddedService {

    @Autowired
    private Repository<FinaxysProfile, String> finaxysProfileRepository;


    @Override
    public void addReactionAddedScore(JsonNode jsonNode) {

        List<String> listEmojis = new ArrayList<String>();
        listEmojis.add("+1");
        listEmojis.add("clap");
        listEmojis.add("ok_hand");
        if (jsonNode != null && jsonNode.get("item_user").asText() != null) {
            String itemUserId = jsonNode.get("item_user").asText();
            String myUserId = jsonNode.get("user").asText();
            String reaction = jsonNode.get("reaction").asText();
            if (listEmojis.contains(reaction)) {
                if (itemUserId != null && itemUserId != myUserId) {
                    {
                        System.out.println(itemUserId);
                        FinaxysProfile userProfile = finaxysProfileRepository.findById(itemUserId);
                        if (userProfile != null) {
                            userProfile.incrementScore(SCORE_GRID.APPRECIATED_MESSAGE.value());
                            finaxysProfileRepository.updateEntity(userProfile);
                        } else {
                            finaxysProfileRepository.addEntity(new FinaxysProfile(itemUserId, false, SCORE_GRID.APPRECIATED_MESSAGE.value()));

                        }
                    }
                }
            }
        }
    }
}
