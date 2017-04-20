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
@RequestMapping("/slackUsers")
public class SlackUserWebService extends BaseWebService{

    @Autowired
    private Repository<SlackUser, String> slackUserRepository;

    @RequestMapping(value = "/scores", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonNode> listScores(@RequestParam("text") 	String text){
    	
    	Timer timer = new Timer();
    	
        String messageText = "/fx_LeaderBoard " + text + "\n";

        int size = text.isEmpty() ? -1 : Integer.parseInt(text);
        
        List<SlackUser> users = slackUserRepository.getAllOrderedByAsList("score", false, size);
        
        for (SlackUser profile : users)
        {
        	messageText += profile.getName() + " " + profile.getScore() + "\n";
        }
        
        return newResponseEntity(messageText + timer, true);
    }
}