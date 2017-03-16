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
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    PropertyLoader propertyLoader;

    ObjectMapper objectMapper = new ObjectMapper();

    @RequestMapping(value = "/challenge_manager/new", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonNode> setFinaxysProfileAsChallengeManager(@RequestParam("token") String token,
                                                                        @RequestParam("team_domain") String teamId,
                                                                        @RequestParam("user_id") String adminFinaxysProfileId,
                                                                        @RequestParam("text") String arguments) {
        if (propertiesAreNotEqual("verification_token", token)) {
            Message message = new Message("Wrong verification token !"+propertyLoader.loadSlackBotProperties().getProperty("verification_token"));
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        };

        if (propertiesAreNotEqual("finaxys_team_name", teamId)) {
            Message message = new Message("Only for FinaxysTM members !");
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        };

        if (userIsNotAdministrator(adminFinaxysProfileId)) {
            Message message = new Message("You don't have administration authorization !");
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        };

        String finaxysProfileId = splitTextToArguments(arguments).get(0);
        FinaxysProfile finaxysProfile = finaxysProfileRepository.findById(finaxysProfileId);
        finaxysProfile = (finaxysProfile == null) ? new FinaxysProfile() : finaxysProfile;
        finaxysProfile.setChallengeManager(true);
        finaxysProfileRepository.saveOrUpdate(finaxysProfile);
        Message message = new Message(finaxysProfile.getName() + "has just became a challenge manager!");
        return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
    }

    public boolean propertiesAreNotEqual(String propertyName, String propertyValue){
        return !propertyValue.equals(propertyLoader.loadSlackBotProperties().getProperty(propertyName));
    }

    public boolean userIsNotAdministrator(String adminFinaxysProfileId){
        FinaxysProfile adminFinaxysProfile = finaxysProfileRepository.findById(adminFinaxysProfileId);
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
