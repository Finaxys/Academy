package com.finaxys.slackbot.Utilities.Classes;



import allbegray.slack.rtm.EventListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.BUL.Interfaces.MessageAppreciatedService;

@Component
public class MessageAppreciatedListner implements EventListener {
   
@Autowired
 MessageAppreciatedService appreciatedService ;
    public MessageAppreciatedListner()
    {
    	
    }

    public void handleMessage(JsonNode jsonNode) {	
    	System.out.println(jsonNode.toString());
    	appreciatedService.addMessageAppreciatedScore(jsonNode);
    }
       
}
