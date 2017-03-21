package com.finaxys.slackbot.WebServices;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finaxys.slackbot.Domains.Message;
import com.finaxys.slackbot.Utilities.FinaxysSlackBotLogger;
import com.finaxys.slackbot.Utilities.PropertyLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;

/**
 * Created by inesnefoussi on 3/17/17.
 */
@RestController
@RequestMapping("/help")
public class SupportWebService {
    private ObjectMapper objectMapper;

    @Autowired
    private PropertyLoader propertyLoader;

    public SupportWebService() {
        objectMapper = new ObjectMapper();
    }

    private boolean tokenIsValid(String token) {
        return token.equals(propertyLoader.loadSlackBotProperties().getProperty("verification_token"));
    }

    private boolean teamIdIsValid(String teamDomain) {
        return teamDomain.equals(propertyLoader.loadSlackBotProperties().getProperty("finaxys_team_name"));
    }

    private ResponseEntity<JsonNode> showMessage(String token, String teamDomain, Message message) {
        if (!tokenIsValid(token)) {
            message = new Message("Wrong verification token !");
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
        if (!teamIdIsValid(teamDomain)) {
            message = new Message("Only for FinaxysTM members !");
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
        return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<JsonNode> getHelp(@RequestParam("token") String token,
                                            @RequestParam("team_domain") String teamDomain) {
        FinaxysSlackBotLogger.logCommandRequest("/fx_help");
        Message message = new Message("");

        if (tokenIsValid(token) && teamIdIsValid(teamDomain)) {
            BufferedReader namesBufferedReader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/listOfCommandsNames.txt")));
            BufferedReader argumentsBufferedReader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/listOfCommandsArguments.txt")));
            BufferedReader descriptionsBufferedReader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/listOfCommandsDescriptions.txt")));
            String messageText = "";
            int i = 1;

            try {
                while ((namesBufferedReader.read()) != -1) {
                    String commandName = namesBufferedReader.readLine();
                    String commandArguments = argumentsBufferedReader.readLine();
                    String commandDescription = descriptionsBufferedReader.readLine();
                    messageText += (i + ". Command name: " + commandName + " \n " + "Command arguments: " + commandArguments + " \n " + commandDescription + " \n ");
                    i++;
                }
                message.setText(messageText);
            } catch (IOException e) {
                e.printStackTrace();
                message = new Message("There was an error accessing some help files. Please try again later \n " + e.toString());
            }
            FinaxysSlackBotLogger.logCommandResponse(message.getText());
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        } else
            return showMessage(token, teamDomain, message);
    }
}
