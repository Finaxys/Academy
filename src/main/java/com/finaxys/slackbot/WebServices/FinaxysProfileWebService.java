package com.finaxys.slackbot.WebServices;

import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.BUL.Interfaces.SlackBotCommandService;
import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.Domains.FinaxysProfile;
import com.finaxys.slackbot.Domains.Message;
import com.finaxys.slackbot.Utilities.FinaxysSlackBotLogger;
import com.finaxys.slackbot.Utilities.PropertyLoader;
import com.finaxys.slackbot.Utilities.SlackBot;
import com.finaxys.slackbot.Utilities.Timer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

@RestController
@RequestMapping("/finaxysProfiles")
public class FinaxysProfileWebService {

    @Autowired
    SlackBotCommandService slackBotCommandServiceImpl;
    @Autowired
    private Repository<FinaxysProfile, String> finaxysProfileRepository;

    @Autowired
    PropertyLoader propertyLoader;

    ObjectMapper objectMapper = new ObjectMapper();



    @RequestMapping(value = "/scores", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<List<FinaxysProfile>> listScores(@RequestParam("token") String token,
                                                           @RequestParam("text") String text,
                                                           @RequestParam("team_domain") String teamDomain) {
        String messageText = "";

        FinaxysSlackBotLogger.logCommandRequest("/fx_display_scores ");
        if (propertiesAreNotEqual("verification_token", token)) {
            Message message = new Message("Wrong verification token !");
            FinaxysSlackBotLogger.logCommandResponse(message.getText());
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
        ;


        if (propertiesAreNotEqual("finaxys_team_name", teamDomain)) {
            Message message = new Message("Only for FinaxysTM members !");
            FinaxysSlackBotLogger.logCommandResponse(message.getText());
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
        ;

       if (text.isEmpty()) text = propertyLoader.loadSlackBotProperties().getProperty("defaultnumber");
        if (!text.trim().matches("^[1-9][0-9]*")) {
            Message message = new Message("command not valid");
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
        Timer.start();

        Timer.elapsed("Service1 ");
        List<FinaxysProfile> users = finaxysProfileRepository.getAllOrderedByAsList("score", false, 6);
        Timer.elapsed("Service2 ");
        for (int i = 0; i < users.size(); i++) {
            messageText += "- " + users.get(i).getName() + " " + users.get(i).getScore() + "\n";
        }
        Timer.elapsed("Service3 ");
        Message message = new Message(messageText);
        Timer.elapsed("Service4 ");
        FinaxysSlackBotLogger.logCommandResponse(message.getText());
        Timer.elapsed("Service5 ");
        return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
    }

    public boolean propertiesAreNotEqual(String propertyName, String propertyValue) {
        return !propertyValue.equals(propertyLoader.loadSlackBotProperties().getProperty(propertyName));
    }


}