package com.finaxys.slackbot.RestServices;

import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.BUL.Interfaces.SlackBotCommandService;
import com.finaxys.slackbot.Domains.FinaxysProfile;
import com.finaxys.slackbot.Domains.Message;
import com.finaxys.slackbot.Utilities.PropertyLoader;
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
    ObjectMapper objectMapper = new ObjectMapper();

    @RequestMapping(value = "/listAll", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<List<FinaxysProfile>> listAllUsers() {
        System.out.println();
        List<FinaxysProfile> users = slackBotCommandServiceImpl.listerUsers();
        return new ResponseEntity<List<FinaxysProfile>>(users, HttpStatus.OK);
    }

    @RequestMapping(value = "/scores", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<List<FinaxysProfile>> listScores(@RequestParam("token") String token,
                                                           @RequestParam("text") String text,
                                                           @RequestParam("team_domain") String teamDomain) {




       /* if (propertiesAreNotEqual("verification_token", token)) {
            Message message = new Message("Wrong verification token !");
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        };


        if (propertiesAreNotEqual("finaxys_team_name", teamDomain)) {
            Message message = new Message("Only for FinaxysTM members !");
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        };*/

        if(text.isEmpty()) text = PropertyLoader.loadSlackBotProperties().getProperty("defaultnumber");
        System.out.print("3");
        List<FinaxysProfile> users = slackBotCommandServiceImpl.listeScores(Integer.parseInt(text));

        return new ResponseEntity<List<FinaxysProfile>>(users, HttpStatus.OK);



    }


    public boolean propertiesAreNotEqual(String propertyName, String propertyValue){
        return !propertyValue.equals(PropertyLoader.loadSlackBotProperties().getProperty(propertyName));
    }



}
