package com.finaxys.slackbot.RestServices;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Bannou on 14/03/2017.
 */
@RestController
public class EventApiInitializer {
    @RequestMapping(value = "/initializeEventApi", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> initializeEventApi(@RequestBody JsonNode jsonNode) {
        String challenge = jsonNode.get("challenge").asText();
        return new ResponseEntity(challenge, HttpStatus.OK);
    }
}
