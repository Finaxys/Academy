package com.finaxys.slackbot.WebServices;

import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.BUL.Interfaces.SlackBotCommandService;
import com.finaxys.slackbot.Domains.FinaxysProfile;
import com.finaxys.slackbot.Domains.Message;
import com.finaxys.slackbot.Utilities.PropertyLoader;
import com.finaxys.slackbot.Utilities.SlackBot;
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
    PropertyLoader propertyLoader;
    ObjectMapper objectMapper = new ObjectMapper();

    @RequestMapping(value = "/listAll", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<List<FinaxysProfile>> listAllUsers() {
        String messageText = "";
        List<FinaxysProfile> users = slackBotCommandServiceImpl.listerUsers();
        for (int i = 0; i < users.size(); i++) {
            messageText += "- " + SlackBot.getSlackWebApiClient().getUserInfo(users.get(i).getId()).getName() + " " + users.get(i).getScore() + "\n";
        }
        Message message = new Message(messageText);
        return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);

    }

    @RequestMapping(value = "/scores", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<List<FinaxysProfile>> listScores(@RequestParam("token") String token,
                                                           @RequestParam("text") String text,
                                                           @RequestParam("team_domain") String teamDomain) {
        String messageText = "";


        if (propertiesAreNotEqual("verification_token", token)) {
            Message message = new Message("Wrong verification token !");
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
        ;


        if (propertiesAreNotEqual("finaxys_team_name", teamDomain)) {
            Message message = new Message("Only for FinaxysTM members !");
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
        ;

        if (text.isEmpty()) text = propertyLoader.loadSlackBotProperties().getProperty("defaultnumber");
        if (!text.trim().matches("^[1-9][0-9]*")) {
            Message message = new Message("command not valid");
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
        List<FinaxysProfile> users = slackBotCommandServiceImpl.listeScores(Integer.parseInt(text));
        for (int i = 0; i < users.size(); i++) {
            messageText += "- " + SlackBot.getSlackWebApiClient().getUserInfo(users.get(i).getId()).getName() + " " + users.get(i).getScore() + "\n";
        }
        Message message = new Message(messageText);
        return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
    }

    public boolean propertiesAreNotEqual(String propertyName, String propertyValue) {
        return !propertyValue.equals(propertyLoader.loadSlackBotProperties().getProperty(propertyName));
    }


}