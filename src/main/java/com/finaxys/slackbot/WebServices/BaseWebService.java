package com.finaxys.slackbot.WebServices;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finaxys.slackbot.DAL.Event;
import com.finaxys.slackbot.DAL.Message;
import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.DAL.Role;
import com.finaxys.slackbot.Utilities.Log;
import com.finaxys.slackbot.Utilities.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class BaseWebService {
	
    @Autowired
    public Repository<Role, Integer> roleRepository;
    
    @Autowired
    Repository<Event, Integer> eventRepository;

    private ObjectMapper objectMapper = new ObjectMapper();
    
    public boolean isAdmin(String userId)
    {	
        List<Role> roles = roleRepository.getByCriterion("role", "admin");
        
        for (Role role : roles)
            if (role.getSlackUser().getSlackUserId().equals(userId))
                return true;
        
        return false;
    }
    
    
    public boolean isEventManager(String userId, String eventName) 
    {	
        List<Role> roles 	   = roleRepository.getByCriterion("role", "event_manager");
        //TODO
        System.out.println("eventRepository : " +  roles.size());
        int 	   eventId = eventRepository.getByCriterion("name", eventName).get(0).getEventId();
        
        for (Role role : roles)
            if (role.getSlackUser().getSlackUserId().equals(userId) && role.getEvent().getEventId() == eventId)
                return true;
        
        return false;
    }
    
    //TODO: uncomment the following method when the DB is ready
//    public boolean isAdminOrEventManager(String userId, String eventName)
//    {
//    	List<Role> eventRepository = roleRepository.getByCriterion("FINAXYSPROFILE_ID", userId);
//    	
//    	for (Role role : eventRepository)
//    	{
//    		if (role.getEventId() == 0 || role.getEvent().getName() == eventName)
//    			return true;
//    	}
//    			
//    	return false;
//    }

    
    public boolean noAccess(String appVerificationToken, String slackTeam) 
    {
        if (appVerificationToken.equals(Settings.appVerificationToken) && slackTeam.equals(Settings.slackTeam))
            return false;
        
        return true;
    }

    
    public ResponseEntity<JsonNode> noAccessResponseEntity(String appVerificationToken, String slackTeam) 
    {
        if (!appVerificationToken.equals(Settings.appVerificationToken))
            return newResponseEntity("Wrong app verification token !");
        
        if (!slackTeam.equals(Settings.slackTeam))
            return newResponseEntity("Only for Finaxys members !");
        
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
