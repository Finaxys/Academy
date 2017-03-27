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
public class EventApiWebService extends BaseWebService{

    ObjectMapper objectMapper = new ObjectMapper();

    @RequestMapping(value = "/eventApi", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> initializeEventApi(@RequestBody JsonNode jsonNode) {
        if (jsonNode.has("challenge"))
            return new ResponseEntity(jsonNode.get("challenge").asText(), HttpStatus.OK);

        if (NoAccess(jsonNode.get("token").asText(), jsonNode.get("team_id").asText()))
            return NoAccessStringResponseEntity(jsonNode.get("token").toString(),jsonNode.get("team_id").asText());
        return new ResponseEntity(HttpStatus.OK);

    }


}
