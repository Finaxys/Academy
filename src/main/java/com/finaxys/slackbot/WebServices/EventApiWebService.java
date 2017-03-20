package com.finaxys.slackbot.WebServices;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finaxys.slackbot.BUL.Interfaces.InnovateService;
import com.finaxys.slackbot.BUL.Interfaces.ReactionAddedService;
import com.finaxys.slackbot.BUL.Interfaces.ReactionRemovedService;
import com.finaxys.slackbot.BUL.Interfaces.RealMessageReward;
import com.finaxys.slackbot.Domains.Message;
import com.finaxys.slackbot.Utilities.PropertyLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Bannou on 14/03/2017.
 */
@RestController
public class EventApiWebService {

    @Autowired
    private RealMessageReward realMessageReward;

    @Autowired
    ReactionAddedService reactionAddedService;

    @Autowired
    ReactionRemovedService reactionRemovedService;

    @Autowired
    private InnovateService innovateService;

    @Autowired
    PropertyLoader propertyLoader;

    ObjectMapper objectMapper = new ObjectMapper();

    @RequestMapping(value = "/eventApi", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> initializeEventApi(@RequestBody JsonNode jsonNode) {
        if (jsonNode.has("challenge"))
            return new ResponseEntity(jsonNode.get("challenge").asText(), HttpStatus.OK);

        if (propertiesAreNotEqual("verification_token", jsonNode.get("token").asText())) {
            Message message = new Message("Wrong verification token !");
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }

        if (propertiesAreNotEqual("team_id", jsonNode.get("team_id").asText())) {
            Message message = new Message("Only for FinaxysTM members !");
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }

        String eventType = jsonNode.get("type").asText();
        JsonNode event = jsonNode.get("event");

        if (eventType.equals("message.channels")) {
            realMessageReward.rewardReadMessage(event);

        } else if (eventType.equals("reaction_added")) {
            reactionAddedService.addReactionAddedScore(event);

        } else if (eventType.equals("reaction_removed")) {
            reactionRemovedService.substituteReactionRemovedScore(event);

        } else if (eventType.equals("file_shared")) {
            innovateService.rewardFileSharing(event);

        } else if (eventType.equals("channel_created")) {
            innovateService.rewardTribeCreator(event);

        }

        return new ResponseEntity(HttpStatus.OK);
    }

    public boolean propertiesAreNotEqual(String propertyName, String propertyValue) {
        return !propertyValue.equals(propertyLoader.loadSlackBotProperties().getProperty(propertyName));
    }
}
