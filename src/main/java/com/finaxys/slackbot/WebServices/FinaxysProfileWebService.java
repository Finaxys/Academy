package com.finaxys.slackbot.WebServices;

import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.DAL.SlackUser;
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
    private Repository<SlackUser, String> finaxysProfileRepository;

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
        
        List<SlackUser> users = finaxysProfileRepository.getAllOrderedByAsList("score", false, size);
        
        for (SlackUser profile : users)
        {
        	messageText += profile.getName() + " " + profile.getScore() + "\n";
        }
        
        return newResponseEntity(messageText + timer, true);
    }
}