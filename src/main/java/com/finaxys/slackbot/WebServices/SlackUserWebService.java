package com.finaxys.slackbot.WebServices;

import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.DAL.SlackUser;
import com.finaxys.slackbot.Utilities.SlackBotTimer;
import com.finaxys.slackbot.interfaces.SlackUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/slackUsers")
public class SlackUserWebService extends BaseWebService{

    @Autowired
    private SlackUserService slackUserService;

    @RequestMapping(value = "/fx_leaderboard", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonNode> listScores(@RequestParam("text") 	String text){
    	
    	SlackBotTimer timer = new SlackBotTimer();
    	
        String messageText = "/fx_leaderboard " + text + "\n";

        int size = text.isEmpty() ? -1 : Integer.parseInt(text);
        
        List<SlackUser> users = slackUserService.getAllOrderedByScore(size);
        
        for (SlackUser profile : users)
        {
        	messageText += profile.getName() + " " + profile.getScore() + "\n";
        }
        
        return newResponseEntity(messageText + timer, true);
    }
}