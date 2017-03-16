package com.finaxys.slackbot.RestServices;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.Domains.Challenge;
import com.finaxys.slackbot.Domains.FinaxysProfile;
import com.finaxys.slackbot.Domains.Message;
import com.finaxys.slackbot.Utilities.PropertyLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bannou on 16/03/2017.
 */
@RestController
@RequestMapping("/score")
public class ScoreWebService {
    @Autowired
    Repository<FinaxysProfile, String> finaxysProfileRepository;

    @Autowired
    Repository<Challenge, String> challengeRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonNode> addScoreForChallenge(JsonNode jsonNode) {
       return new ResponseEntity(HttpStatus.OK);
    }

    public boolean propertiesAreNotEqual(String propertyName, String propertyValue){
        return !propertyValue.equals(PropertyLoader.loadSlackBotProperties().getProperty(propertyName));
    }

    public boolean userIsNotChallengeManager(String finaxysProfileId){
        FinaxysProfile adminFinaxysProfile = finaxysProfileRepository.findById(finaxysProfileId);
        return !adminFinaxysProfile.isAdministrator();
    }

    public List<String> splitTextToArguments(String text){
        List<String> arguments = new ArrayList<>();
        while ((text.indexOf("<") != -1) && (text.indexOf("<") < text.indexOf(">"))){
            arguments.add(text.substring(text.indexOf("@")+1, text.indexOf("|")));
            text = text.substring(text.indexOf(">")+1);
        }
        return arguments;
    }
}
