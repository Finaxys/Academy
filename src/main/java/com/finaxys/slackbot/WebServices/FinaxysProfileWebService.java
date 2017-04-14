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

        if (noAccess(appVerificationToken, slackTeam))
            return noAccessResponseEntity(appVerificationToken, slackTeam);
        
        timer.capture();
        
        int size = text.isEmpty() ? -1 : Integer.parseInt(text);
        
        List<FinaxysProfile> users = finaxysProfileRepository.getAllOrderedByAsList("score", false, size);
        
        for (FinaxysProfile profile : users)
        {
        	messageText += profile.getName() + " " + profile.getScore() + "\n";
        }
        
        return newResponseEntity(messageText + timer, true);
    }
}