package com.finaxys.slackbot.WebServices;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finaxys.slackbot.BUL.Matchers.ActionScoreAddMatcher;
import com.finaxys.slackbot.DAL.ActionScore;
import com.finaxys.slackbot.DAL.FinaxysProfile;
import com.finaxys.slackbot.DAL.Message;
import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.Utilities.FinaxysSlackBotLogger;
import com.finaxys.slackbot.Utilities.PropertyLoader;
import com.finaxys.slackbot.Utilities.Timer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by jiji on 22/03/2017.
 */
@RestController
@RequestMapping("/action")
public class ActionScoreWebService {
    //fx_usage_action_add "action name" [points earned]
    @Autowired
    PropertyLoader propertyLoader;

    ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private Repository<ActionScore, String> actionScoreRepository;
    int score;
    String action;

    @RequestMapping(value = "/scores", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<List<FinaxysProfile>> listScores(/*@RequestParam("token") String token,*/
                                                          /* @RequestParam("text") String text*/
                                                           /*,@RequestParam("team_domain") String teamDomain*/) {
       String messageText = "The command /Fx_usage_action_add  was invoked with args score = " + "text";


        FinaxysSlackBotLogger.logCommandRequest("/fx_display_scores ");
      /*  if (propertiesAreNotEqual("verification_token", token)) {
            Message message = new Message("Wrong verification token !");
            FinaxysSlackBotLogger.logCommandResponse(message.getText());
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
        ;

        if (propertiesAreNotEqual("finaxys_team_name", teamDomain)) {
            Message message = new Message(messageText+" \n"+"Only for FinaxysTM members !");
            FinaxysSlackBotLogger.logCommandResponse(message.getText());
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
        ;
*/
        ActionScoreAddMatcher actionScoreAddMatcher = new ActionScoreAddMatcher();
        String text="\"placer un collab\" -50 points";
        if (!actionScoreAddMatcher.isCorrect(text)) {
            Message message = new Message(messageText + " \n" + "command not valid");
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
        score = actionScoreAddMatcher.getActionScoreArgument(text);
        action = actionScoreAddMatcher.getActionNameArgument(text);


        actionScoreRepository.saveOrUpdate(new ActionScore(action,score));
        Message message = new Message(messageText + " \n" + "action added");
        return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
    }

    public boolean propertiesAreNotEqual(String propertyName, String propertyValue) {
        return !propertyValue.equals(propertyLoader.loadSlackBotProperties().getProperty(propertyName));
    }
}
