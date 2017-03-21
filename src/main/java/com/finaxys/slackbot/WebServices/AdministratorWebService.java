package com.finaxys.slackbot.WebServices;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finaxys.slackbot.BUL.Interfaces.ReactionAddedService;
import com.finaxys.slackbot.BUL.Matchers.OneUsernameArgumentMatcher;
import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.Domains.FinaxysProfile;
import com.finaxys.slackbot.Domains.Message;
import com.finaxys.slackbot.Utilities.FinaxysSlackBotLogger;
import com.finaxys.slackbot.Utilities.PropertyLoader;
import com.finaxys.slackbot.Utilities.SlackBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    ReactionAddedService reactionAddedService;

    @Autowired
    PropertyLoader propertyLoader;


    ObjectMapper objectMapper = new ObjectMapper();

    @RequestMapping(value = "/admins/new", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonNode> setFinaxysProfileAsAdministrator(@RequestParam("token") String token,
                                                                     @RequestParam("team_domain") String teamId,
                                                                     @RequestParam("user_id") String adminFinaxysProfileId,
                                                                     @RequestParam("text") String arguments) {
        FinaxysSlackBotLogger.logCommandRequest("/fx_add_adminitstrator");
        if (propertiesAreNotEqual("verification_token", token)) {
            Message message = new Message("Wrong verification token !");
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

        OneUsernameArgumentMatcher oneUsernameArgumentsMatcher = new OneUsernameArgumentMatcher();

        if (!oneUsernameArgumentsMatcher.isCorrect(arguments)) {
            Message message = new Message("Arguments should suit ' .... @Username ...' Pattern !");

            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        };

        String finaxysProfileId = oneUsernameArgumentsMatcher.getUserIdArgument(arguments);
        FinaxysProfile finaxysProfile = finaxysProfileRepository.findById(finaxysProfileId);
        finaxysProfile = (finaxysProfile == null) ? new FinaxysProfile(finaxysProfileId) : finaxysProfile;
        finaxysProfile.setAdministrator(true);
        finaxysProfileRepository.saveOrUpdate(finaxysProfile);
        Message message = new Message("<@" + finaxysProfileId + "|" + SlackBot.getSlackWebApiClient().getUserInfo(finaxysProfileId).getName() + "> has just became an administrator!");
        FinaxysSlackBotLogger.logCommandResponse(message.getText());
        return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
    }

    @RequestMapping(value = "/admins/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonNode> getAdministrators(@RequestParam("token") String token,
                                                      @RequestParam("team_domain") String teamId,
                                                      @RequestParam("user_id") String adminFinaxysProfileId) {
        FinaxysSlackBotLogger.logCommandRequest("/fx_list_administrators");
        if (propertiesAreNotEqual("verification_token", token)) {
            Message message = new Message("Wrong verification token !");
            FinaxysSlackBotLogger.logCommandResponse(message.getText());
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        };

        if (propertiesAreNotEqual("finaxys_team_name", teamId)) {
            Message message = new Message("Only for FinaxysTM members !");
            FinaxysSlackBotLogger.logCommandResponse(message.getText());
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        };

        if (userIsNotAdministrator(adminFinaxysProfileId)) {
            Message message = new Message("You don't have administration authorization !");
            FinaxysSlackBotLogger.logCommandResponse(message.getText());
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        };

        List<FinaxysProfile> finaxysProfiles = finaxysProfileRepository.getByCriterion("administrator", true);
        String messageText = "Administrators' list: \n";
        for (FinaxysProfile finaxysProfile : finaxysProfiles)
            messageText += "<@" + finaxysProfile.getId() + "|" + SlackBot.getSlackWebApiClient().getUserInfo(finaxysProfile.getId()).getName() + "> \n";
        messageText = (finaxysProfiles.size()>0)?messageText :"";
        Message message = new Message(messageText);
        FinaxysSlackBotLogger.logCommandResponse(message.getText());
        return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
    }

    @RequestMapping(value = "/challenge_manager/new", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonNode> setFinaxysProfileAsChallengeManager(@RequestParam("token") String token,
                                                                        @RequestParam("team_domain") String teamId,
                                                                        @RequestParam("user_id") String adminFinaxysProfileId,
                                                                        @RequestParam("text") String arguments) {
        FinaxysSlackBotLogger.logCommandRequest("/fx_add_challenge_manager ");
        if (propertiesAreNotEqual("verification_token", token)) {

            Message message = new Message("Wrong verification token !" + propertyLoader.loadSlackBotProperties().getProperty("verification_token"));
            FinaxysSlackBotLogger.logCommandResponse(message.getText());
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        };

        if (propertiesAreNotEqual("finaxys_team_name", teamId)) {
            Message message = new Message("Only for FinaxysTM members !");
            FinaxysSlackBotLogger.logCommandResponse(message.getText());
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        };

        if (userIsNotAdministrator(adminFinaxysProfileId)) {
            Message message = new Message("You don't have administration authorization !");
            FinaxysSlackBotLogger.logCommandResponse(message.getText());
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        };

        OneUsernameArgumentMatcher oneUsernameArgumentsMatcher = new OneUsernameArgumentMatcher();

        if (!oneUsernameArgumentsMatcher.isCorrect(arguments)) {
            Message message = new Message("Arguments should suit ' .... @Username ...' Pattern !");
            FinaxysSlackBotLogger.logCommandResponse(message.getText());
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        };

        String finaxysProfileId = oneUsernameArgumentsMatcher.getUserIdArgument(arguments);
        FinaxysProfile finaxysProfile = finaxysProfileRepository.findById(finaxysProfileId);
        finaxysProfile = (finaxysProfile == null) ? new FinaxysProfile(finaxysProfileId) : finaxysProfile;
        finaxysProfile.setChallengeManager(true);
        finaxysProfileRepository.saveOrUpdate(finaxysProfile);
        Message message = new Message("<@" + finaxysProfileId + "|" + SlackBot.getSlackWebApiClient().getUserInfo(finaxysProfile.getId()).getName() + "> has just became a challenge manager!");
        FinaxysSlackBotLogger.logCommandResponse(message.getText());
        return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
    }

    @RequestMapping(value = "/challenge_manager/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonNode> getChallengeManagers(@RequestParam("token") String token,
                                                         @RequestParam("team_domain") String teamId,
                                                         @RequestParam("user_id") String adminFinaxysProfileId) {
        FinaxysSlackBotLogger.logCommandRequest("/fx_list_challenge_managers");
        if (propertiesAreNotEqual("verification_token", token)) {
            Message message = new Message("Wrong verification token !" + propertyLoader.loadSlackBotProperties().getProperty("verification_token"));
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
        ;

        if (propertiesAreNotEqual("finaxys_team_name", teamId)) {
            Message message = new Message("Only for FinaxysTM members !");
            FinaxysSlackBotLogger.logCommandResponse(message.getText());
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
        ;

        if (userIsNotAdministrator(adminFinaxysProfileId)) {
            Message message = new Message("You don't have administration authorization !");
            FinaxysSlackBotLogger.logCommandResponse(message.getText());
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
        ;

        List<FinaxysProfile> finaxysProfiles = finaxysProfileRepository.getByCriterion("challengeManager", true);
        String messageText = "Challenge managers' list:\n";
        for (FinaxysProfile finaxysProfile : finaxysProfiles)
            messageText += "<@" + finaxysProfile.getId() + "|" + SlackBot.getSlackWebApiClient().getUserInfo(finaxysProfile.getId()).getName() + "> \n";
        messageText = (finaxysProfiles.size()>0)?messageText :"";
        Message message = new Message(messageText);
        FinaxysSlackBotLogger.logCommandResponse(message.getText());
        return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
    }

    public boolean propertiesAreNotEqual(String propertyName, String propertyValue) {
        return !propertyValue.equals(propertyLoader.loadSlackBotProperties().getProperty(propertyName));
    }

    public boolean userIsNotAdministrator(String adminFinaxysProfileId) {
        FinaxysProfile adminFinaxysProfile = finaxysProfileRepository.findById(adminFinaxysProfileId);
        return !adminFinaxysProfile.isAdministrator();
    }
}
