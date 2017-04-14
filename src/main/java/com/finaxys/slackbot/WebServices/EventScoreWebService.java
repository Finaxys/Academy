package com.finaxys.slackbot.WebServices;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finaxys.slackbot.BUL.Matchers.EventScoreAddMatcher;
import com.finaxys.slackbot.DAL.EventScore;
import com.finaxys.slackbot.DAL.FinaxysProfile;
import com.finaxys.slackbot.DAL.Message;
import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.Utilities.Log;
import com.finaxys.slackbot.Utilities.Settings;
import com.finaxys.slackbot.Utilities.Timer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/event")
public class EventScoreWebService extends BaseWebService {

    @Autowired
    private Repository<EventScore, String> eventScoreRepository;
    int score;
    String event;

    
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonNode> eventAdd(@RequestParam("token") 		String appVerificationToken,
                                             @RequestParam("team_domain") String slackTeam,
                                             @RequestParam("text") 		String text) {
    	
    	Timer timer = new Timer();

        if (noAccess(appVerificationToken, slackTeam))
            return noAccessResponseEntity(appVerificationToken, slackTeam);
        
        timer.capture();
        
        EventScoreAddMatcher eventScoreAddMatcher = new EventScoreAddMatcher();
        
        if (!eventScoreAddMatcher.isCorrect(text))
            return newResponseEntity("/fx_event_add " + text + " \n" + "Arguments should be : [\"event\"] [points earned]" + timer , true);

        score = eventScoreAddMatcher.getActionScoreArgument(text);
        event = eventScoreAddMatcher.getActionNameArgument (text);
        
        timer.capture();
        
        new Thread(new Runnable()
        {
        	public void run()
        	{
        		eventScoreRepository.saveOrUpdate(new EventScore(event, score));
        	}
        }).start();
        
        timer.capture();
        
        return newResponseEntity("/fx_event_add " + text + " \n" + "event added successfully " + timer, true);
    }
}