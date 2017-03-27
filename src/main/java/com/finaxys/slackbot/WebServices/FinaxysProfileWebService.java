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
public class FinaxysProfileWebService extends BaseWebService{

    @Autowired
    SlackBotCommandService slackBotCommandServiceImpl;
    @Autowired
    private Repository<FinaxysProfile, String> finaxysProfileRepository;

    @RequestMapping(value = "/scores", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonNode> listScores(@RequestParam("token") String appVerificationToken,
                                               @RequestParam("team_domain")  String slackTeam,
                                               @RequestParam("text") String text){
        String messageText = "/fx_LeaderBoard " + text+"\n";

        if (NoAccess(appVerificationToken, slackTeam))
            return NoAccessResponseEntity(appVerificationToken, slackTeam);
          if(text.equals(" "))
          {
              List<FinaxysProfile> users = finaxysProfileRepository.getAllOrderedByAsList("score", false,finaxysProfileRepository.getAll().size());
              for (int i = 0; i < users.size(); i++) {
                  messageText += users.get(i).getName() + " " + users.get(i).getScore() + "\n";
              }
              return NewResponseEntity(messageText, true);
          }
        if (!text.trim().matches("^[1-9][0-9]*")) {
            return NewResponseEntity(messageText+" \n"+"Arguments should be [number]", true);

        }

        List<FinaxysProfile> users = finaxysProfileRepository.getAllOrderedByAsList("score", false, Integer.parseInt(text));
        for (int i = 0; i < users.size(); i++) {
            messageText += users.get(i).getName() + " " + users.get(i).getScore() ;
        }

        return NewResponseEntity(messageText, true);
    }
}