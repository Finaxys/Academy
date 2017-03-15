package com.finaxys.slackbot.RestServices;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finaxys.slackbot.DAL.Repository;
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
 * Created by Bannou on 15/03/2017.
 */
@RestController
@RequestMapping("/admin")
public class AdministratorWebService {

    @Autowired
    Repository<FinaxysProfile, String> finaxysProfileRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    @RequestMapping(value = "/challengeManager", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonNode> setFinaxysProfileAsChallengeManager(JsonNode jsonNode) {
        String token = jsonNode.get("token").asText();
        if (propertiesAreNotEqual("verification_token", token)) {
            Message message = new Message("Wrong verification token !");
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        };

        String teamId = jsonNode.get("team_domain").asText();
        if (teamId.equals(PropertyLoader.loadSlackBotProperties().getProperty("finaxys_team_name"))) {
            Message message = new Message("Only for FinaxysTM members !");
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        };

        String text = jsonNode.get("text").asText();
        String finaxysProfileId = splitTextToArguments(text).get(0);
        FinaxysProfile finaxysProfile = finaxysProfileRepository.findById(finaxysProfileId);
        finaxysProfile = (finaxysProfile == null) ? new FinaxysProfile() : finaxysProfile;
        finaxysProfile.setChallengeManager(true);
        finaxysProfileRepository.saveOrUpdate(finaxysProfile);
        return new ResponseEntity(HttpStatus.OK);
    }

    public boolean propertiesAreNotEqual(String propertyName, String propertyValue){
        return !propertyValue.equals(PropertyLoader.loadSlackBotProperties().getProperty(propertyName));
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