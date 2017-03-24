package com.finaxys.slackbot.WebServices;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finaxys.slackbot.BUL.Matchers.EventScoreAddMatcher;
import com.finaxys.slackbot.DAL.EventScore;
import com.finaxys.slackbot.DAL.FinaxysProfile;
import com.finaxys.slackbot.DAL.Message;
import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.Utilities.FinaxysSlackBotLogger;
import com.finaxys.slackbot.Utilities.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by jiji on 22/03/2017.
 */
@RestController
@RequestMapping("/event")
public class EventScoreWebService {

    ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private Repository<EventScore, String> eventScoreRepository;
    int score;
    String event;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<List<FinaxysProfile>> listScores(@RequestParam("appVerificationToken") String appVerificationToken,
                                                           @RequestParam("text") String text
            , @RequestParam("slackTeam") String slackTeam) {
        String messageText = "/fx_event_add " + text;


        FinaxysSlackBotLogger.logCommandRequest("/fx_event_add "+text);
        if (!Settings.appVerificationToken.equals(appVerificationToken)) {

            Message message = new Message("Wrong verification token !" + Settings.appVerificationToken);
            FinaxysSlackBotLogger.logCommandResponse(message.getText());
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
        if (!Settings.slackTeam.equals(slackTeam)) {
            Message message = new Message("Only for FinaxysTM members !");
            FinaxysSlackBotLogger.logCommandResponse(message.getText());
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
        ;

        EventScoreAddMatcher eventScoreAddMatcher = new EventScoreAddMatcher();
        if (!eventScoreAddMatcher.isCorrect(text)) {
            Message message = new Message(messageText + " \n" + "Arguments should be : [\"event\"] [points earned]");
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
        score = eventScoreAddMatcher.getActionScoreArgument(text);
        event = eventScoreAddMatcher.getActionNameArgument(text);


        eventScoreRepository.saveOrUpdate(new EventScore(event, score));
        Message message = new Message(messageText + " \n" + "event added successfully ");
        return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
    }
}
