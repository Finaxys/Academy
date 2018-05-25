package com.finaxys.slackbot.WebServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finaxys.slackbot.DAL.Message;
import com.finaxys.slackbot.Utilities.Log;
import com.finaxys.slackbot.Utilities.Settings;
import com.finaxys.slackbot.interfaces.EventService;

@Component
public class BaseWebService {
	
    
    @Autowired
    EventService eventService;

    private ObjectMapper objectMapper = new ObjectMapper();
    
    public boolean isAdmin(String userId)
    {	
        
        return false;
    }
    
    public boolean isEventManager(String userId, String eventName) 
    {	
        
        return false;
    }
    
    
    public ResponseEntity<JsonNode> noAccessResponseEntity(String appVerificationToken, String slackTeam) 
    {
        if (!appVerificationToken.equals(Settings.appVerificationToken))
            return newResponseEntity("Wrong app verification token !");
        
        if (!slackTeam.equals(Settings.slackTeam)) {
        	Log.info("Warning the slackTeamsId are not the same!!");
            return newResponseEntity("Only for Finaxys members !");
        }
        
        return null;
    }

    public ResponseEntity<String> noAccessStringResponseEntity(String appVerificationToken, String slackTeam) 
    {
        if (!Settings.appVerificationToken.equals(appVerificationToken)) 
        {
            Message message = new Message("Wrong verification token !");
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }

        if (!Settings.slackTeamId.equals(slackTeam)) 
        {
            Message message = new Message("Only for FinaxysTM members !");
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
        
        return null;
    }
    
    
    public ResponseEntity<JsonNode> newResponseEntity(Message message) 
    {
        return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
    }

    
    public ResponseEntity<JsonNode> newResponseEntity(String message) 
    {
        return newResponseEntity(new Message(message));
    }
    
    
    public ResponseEntity<JsonNode> newResponseEntity(String message,boolean logAsInfo) 
    {
        if(logAsInfo)
            Log.info(message);
        
       return newResponseEntity(new Message(message));
    }
}
