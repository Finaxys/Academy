package test;



import allbegray.slack.rtm.EventListener;
import allbegray.slack.type.Channel;
import allbegray.slack.webapi.SlackWebApiClient;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.BusinessLogic.Classes.MessageAppreciatedServiceImpl;
import com.finaxys.slackbot.BusinessLogic.Interfaces.InnovateService;
import com.finaxys.slackbot.BusinessLogic.Interfaces.MessageAppreciatedService;

@Component
public class MessageAppreciatedListner implements EventListener {
   
@Autowired
 InnovateService innovateService ;
    public MessageAppreciatedListner()
    {
    	
    }

    public void handleMessage(JsonNode jsonNode) {	
    	System.out.println(jsonNode.toString());
    	//appreciatedService.addMessageAppreciatedScore(jsonNode);
    }
       
}
