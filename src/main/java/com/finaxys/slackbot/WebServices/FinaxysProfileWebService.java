package com.finaxys.slackbot.WebServices;

import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.BUL.Interfaces.SlackBotCommandService;
import com.finaxys.slackbot.DAL.FinaxysProfile;
import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.Utilities.Timer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/finaxysProfiles")
public class FinaxysProfileWebService extends BaseWebService{

    @Autowired
    SlackBotCommandService slackBotCommandServiceImpl;
    
    @Autowired
    private Repository<FinaxysProfile, String> finaxysProfileRepository;

    @RequestMapping(value = "/scores", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonNode> listScores(@RequestParam("token") 		String appVerificationToken,
                                               @RequestParam("team_domain") String slackTeam,
                                               @RequestParam("text") 		String text){
    	
    	Timer timer = new Timer();
    	
        String messageText = "/fx_LeaderBoard " + text + "\n";

        if (NoAccess(appVerificationToken, slackTeam))
            return NoAccessResponseEntity(appVerificationToken, slackTeam);
        
        timer.capture();
        
        if(text.equals(" "))
        {
        	List<FinaxysProfile> users = finaxysProfileRepository.getAllOrderedByAsList("score", false,finaxysProfileRepository.getAll().size());
        	
        	timer.capture();
        	
            for (int i = 0; i < users.size(); i++)
            {
            	messageText += users.get(i).getName() + " " + users.get(i).getScore() + "\n";
            }
            
            timer.capture();
            
              return NewResponseEntity(messageText + timer, true);
        }
        
        timer.capture();
        
        if (!text.trim().matches("^[1-9][0-9]*"))
            return NewResponseEntity(messageText+" \n"+"Arguments should be [number]" + timer , true);

        List<FinaxysProfile> users = finaxysProfileRepository.getAllOrderedByAsList("score", false, Integer.parseInt(text));
        
        timer.capture();
        
        for (int i = 0; i < users.size(); i++)
        {
            messageText += users.get(i).getName() + " " + users.get(i).getScore();
        }
        
        timer.capture();

        return NewResponseEntity(messageText + timer, true);
    }
}