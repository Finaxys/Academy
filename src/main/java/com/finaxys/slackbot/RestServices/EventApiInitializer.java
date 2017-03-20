package com.finaxys.slackbot.RestServices;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class EventApiInitializer {

    @Autowired
    private RealMessageReward realMessageReward;

    @Autowired
    PropertyLoader propertyLoader;

    ObjectMapper objectMapper = new ObjectMapper();

    @RequestMapping(value = "/eventApi", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> initializeEventApi(@RequestParam("challenge") String challenge,
                                                     @RequestParam("token") String token,
                                                     @RequestParam("team_id") String teamId,
                                                     @RequestParam("team_id") JsonNode event) {
        if (!challenge.isEmpty())
            return new ResponseEntity(challenge, HttpStatus.OK);

        if (propertiesAreNotEqual("verification_token", token)) {
            Message message = new Message("Wrong verification token !");
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }

        if (propertiesAreNotEqual("finaxys_team_id", teamId)) {
            Message message = new Message("Only for FinaxysTM members !");
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }

        String eventType = event.get("type").asText();

        if (eventType.equals("message.channels"))
            realMessageReward.rewardReadlMessage(event.get("event"));

        return new ResponseEntity(HttpStatus.OK);
    }

    public boolean propertiesAreNotEqual(String propertyName, String propertyValue) {
        return !propertyValue.equals(propertyLoader.loadSlackBotProperties().getProperty(propertyName));
    }
}
