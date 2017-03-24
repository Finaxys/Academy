package com.finaxys.slackbot.WebServices;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finaxys.slackbot.DAL.Message;
import com.finaxys.slackbot.Utilities.FinaxysSlackBotLogger;
import com.finaxys.slackbot.Utilities.Settings;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;

@RestController
@RequestMapping("/help")
public class SupportWebService {
    private ObjectMapper objectMapper;


    public SupportWebService() {
        objectMapper = new ObjectMapper();
    }

    private boolean tokenIsValid(String token) {
        return token.equals(Settings.appVerificationToken);
    }

    private boolean teamIdIsValid(String teamDomain) {
        return teamDomain.equals(Settings.slackTeam);
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
    public ResponseEntity<JsonNode> getHelp(@RequestParam("appVerificationToken") String appVerificationToken,
                                            @RequestParam("slackTeam") String slackTeam) {
        FinaxysSlackBotLogger.logCommandRequest("/fx_help ");
        Message message = new Message("");
        String messageText = "";
        String fx_challenge_list             = "*/fx_challenge_list* \n List all the challenges.";
        String fx_challenge_add              = "*/fx_challenge_add* [challenge name],[group|individual],[description] \n Add a new challenge.";
        String fx_manager_add                = "*/fx_manager_add* [challenge name] @username  \n Add a challenge manager." ;
        String fx_manager_list               = "*/fx_manager_list* [challenge name] \n List challenge managers .";
        String fxmanager_challenge_score_add = "*/fxmanager_challenge_score_add* @username [points] points [name] challenge \n Add a score to a challenge participant ";
        String fxadmin_list                  = "*/fxadmin_list* \n List all administrators .";
        String fxadmin_add                   = "*/fxadmin_add* @username \n Add an administrator .";
        String fx_challenge_named            = "*/fx_challenge_named* [challenge name] \n Get a challenge details." ;
        String fx_challenges_by_date         = "*/fx_challenges_by_date* [yyyy-mm-dd] \n Get challenges by date";
        String fx_challenges_by_type         = "*/fx_challenges_by_type* [group|individual] Get challengss by type";
        String fx_challenge_score_list       = "*/fx_challenge_score_list*  [challengeName] challenge \n Gets the score list of a given challenge.";
        String fx_leaderboard                = "*/fx_leaderboard* [optional: count] \nGets the top scores .";
        String fxadmin_del                   = "*/fxadmin_del* @username delete an administrator";
        String fxmanager_del                 = "*/fxmanager_del @username* \n delete a challenge manager";
        String fx_event_add                  = "*/fx_event_add [event]* [points earned] \n add an event .";
        String fxadmin_manager_challenge_del = "*/fxadmin_manager_challenge_del* \n Delete a challenge !.";
        if (tokenIsValid(appVerificationToken) && teamIdIsValid(slackTeam)) {

            messageText += ( fx_challenge_list + " \n " + fx_challenge_add +" \n " +  fx_manager_add+ " \n " + fx_manager_list+" \n " + fxmanager_challenge_score_add+" \n " + fxadmin_list+" \n "+fxadmin_add+" \n "+fx_challenge_named+" \n "+fx_challenges_by_date+" \n "+fx_challenges_by_type+" \n "+ fx_challenge_score_list+" \n "+fx_leaderboard+" \n "+fxadmin_del+" \n "+fxmanager_del+" \n "+fx_event_add+" \n "+fxadmin_manager_challenge_del);

        }
        message.setText("/fx_help " + " \n " + messageText);

        FinaxysSlackBotLogger.logCommandResponse(message.getText());
        return showMessage(appVerificationToken, slackTeam, message);
    }
}
