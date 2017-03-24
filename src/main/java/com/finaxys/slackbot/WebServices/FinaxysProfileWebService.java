package com.finaxys.slackbot.WebServices;

import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.BUL.Interfaces.SlackBotCommandService;
import com.finaxys.slackbot.DAL.FinaxysProfile;
import com.finaxys.slackbot.DAL.Message;
import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.Utilities.Log;
import com.finaxys.slackbot.Utilities.Settings;
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


    ObjectMapper objectMapper = new ObjectMapper();


    @RequestMapping(value = "/scores", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<List<FinaxysProfile>> listScores(@RequestParam("appVerificationToken") String appVerificationToken,
                                                           @RequestParam("text") String text,
                                                           @RequestParam("slackTeam") String slackTeam) {
        String messageText = "/fx_LeaderBoard " + text;
        Log.info("/fx_LeaderBoard ");
        if (!Settings.appVerificationToken.equals(appVerificationToken)) {

            Message message = new Message("Wrong verification token !" + Settings.appVerificationToken);
            Log.info(message.getText());
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
        if (!Settings.slackTeam.equals(slackTeam)) {
            Message message = new Message("Only for FinaxysTM members !");
            Log.info(message.getText());
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        } ;
          if(text.equals(" "))
          {
              List<FinaxysProfile> users = finaxysProfileRepository.getAllOrderedByAsList("score", false,finaxysProfileRepository.getAll().size());
              for (int i = 0; i < users.size(); i++) {
                  messageText += users.get(i).getName() + " " + users.get(i).getScore() + "\n";
              }
              Message message = new Message(messageText);
              Log.info(message.getText());
              return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);

          }
        if (!text.trim().matches("^[1-9][0-9]*")) {
            Message message = new Message(messageText+" \n"+"Arguments should be [number]");
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }

        List<FinaxysProfile> users = finaxysProfileRepository.getAllOrderedByAsList("score", false, Integer.parseInt(text));
        for (int i = 0; i < users.size(); i++) {
            messageText += users.get(i).getName() + " " + users.get(i).getScore() + "\n";
        }
        Message message = new Message(messageText);
        Log.info(message.getText());
        return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
    }
}