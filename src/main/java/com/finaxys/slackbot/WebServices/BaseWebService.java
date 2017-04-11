package com.finaxys.slackbot.WebServices;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finaxys.slackbot.DAL.Challenge;
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
    Repository<Challenge, Integer> challengeRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    
    public boolean isAdmin(String userId)
    {
    	//faire une requête unique du type "SELECT ROLE_ID FROM Role WHERE FINAXYSPROFILE_ID = userId AND role = "admin"
    	
        List<Role> roles = roleRepository.getByCriterion("role", "admin");
        
        for (Role role : roles)
            if (role.getFinaxysProfile().getId().equals(userId))
                return true;
        
        return false;
    }
    
    
    public boolean isChallengeManager(String userId, String challengeName) 
    {
    	//faire une requête du type "SELECT ROLE_ID FROM Role WHERE FINAXYSPROFILE_ID = userId AND role = challenge_manager AND #p
    	
        List<Role> roles 	   = roleRepository.getByCriterion("role", "challenge_manager");
        int 	   challengeId = challengeRepository.getByCriterion("name", challengeName).get(0).getId();
        
        for (Role role : roles)
            if (role.getFinaxysProfile().getId().equals(userId) && role.getChallengeId() == challengeId)
                return true;
        
        return false;
    }

    
    public boolean NoAccess(String appVerificationToken, String slackTeam) 
    {
        if (appVerificationToken.equals(Settings.appVerificationToken) && slackTeam.equals(Settings.slackTeam))
            return false;
        
        return true;
    }

    
    public ResponseEntity<JsonNode> NoAccessResponseEntity(String appVerificationToken, String slackTeam) 
    {
        if (!appVerificationToken.equals(Settings.appVerificationToken))
            return NewResponseEntity("Wrong app verification token !");
        
        if (!slackTeam.equals(Settings.slackTeam))
            return NewResponseEntity("Only for Finaxys members !");
        
        return null;
    }

    public ResponseEntity<String> NoAccessStringResponseEntity(String appVerificationToken, String slackTeam) 
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
    
    
    public ResponseEntity<JsonNode> NewResponseEntity(Message message) 
    {
        return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
    }

    
    public ResponseEntity<JsonNode> NewResponseEntity(String message) 
    {
        return NewResponseEntity(new Message(message));
    }
    
    
    public ResponseEntity<JsonNode> NewResponseEntity(String message,boolean logAsInfo) 
    {
        if(logAsInfo)
            Log.info(message);
        
       return NewResponseEntity(new Message(message));
    }
}
