package com.finaxys.slackbot.WebServices;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finaxys.slackbot.BUL.Interfaces.InnovateService;
import com.finaxys.slackbot.BUL.Interfaces.ReactionAddedService;
import com.finaxys.slackbot.BUL.Interfaces.ReactionRemovedService;
import com.finaxys.slackbot.BUL.Interfaces.RealMessageReward;
import com.finaxys.slackbot.DAL.Message;
import com.finaxys.slackbot.Utilities.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class EventApiWebService {

    ObjectMapper objectMapper = new ObjectMapper();

    @RequestMapping(value = "/eventApi", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> initializeEventApi(@RequestBody JsonNode jsonNode) {
        if (jsonNode.has("challenge"))
            return new ResponseEntity(jsonNode.get("challenge").asText(), HttpStatus.OK);

        if (!Settings.appVerificationToken.equals(jsonNode.get("token").asText())) {
            Message message = new Message("Wrong verification token !");
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }

        if (!Settings.slackTeamId.equals(jsonNode.get("team_id").asText())) {
            Message message = new Message("Only for FinaxysTM members !");
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.OK);
    }


}
