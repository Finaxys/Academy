package com.finaxys.slackbot.WebServices;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finaxys.slackbot.Utilities.Settings;


@RestController
public class EventApiWebService extends BaseWebService{

    ObjectMapper objectMapper = new ObjectMapper();

    @RequestMapping(value = "/eventApi", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> initializeEventApi(@RequestBody JsonNode jsonNode) {
    	
        if (jsonNode.has("challenge"))
            return new ResponseEntity<String>(jsonNode.get("challenge").asText(), HttpStatus.OK);

        if (noAccess(jsonNode.get("token").asText(), jsonNode.get("team_id").asText()))
            return noAccessStringResponseEntity(jsonNode.get("token").toString(),jsonNode.get("team_id").asText());
        
        return new ResponseEntity<String>(HttpStatus.OK);
    }
    
    public boolean noAccess(String appVerificationToken, String slackTeam) 
    {
        if (appVerificationToken.equals(Settings.appVerificationToken) && slackTeam.equals(Settings.slackTeam))
            return false;
        
        return true;
    }
}
