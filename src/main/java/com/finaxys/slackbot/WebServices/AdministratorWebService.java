package com.finaxys.slackbot.WebServices;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finaxys.slackbot.BUL.Interfaces.ReactionAddedService;
import com.finaxys.slackbot.BUL.Matchers.ChallengeManagerArgumentsMatcher;
import com.finaxys.slackbot.BUL.Matchers.OneUsernameArgumentMatcher;
import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.Domains.Challenge;
import com.finaxys.slackbot.Domains.FinaxysProfile;
import com.finaxys.slackbot.Domains.Message;
import com.finaxys.slackbot.Domains.Role;
import com.finaxys.slackbot.Utilities.FinaxysSlackBotLogger;
import com.finaxys.slackbot.Utilities.PropertyLoader;
import com.finaxys.slackbot.Utilities.SlackBot;
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
    Repository<Role, Integer> roleRepository;
    @Autowired
    Repository<Challenge, Integer> challengeRepository;

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
        FinaxysSlackBotLogger.logCommandRequest("/fx_add_adminitstrator " + arguments);
        if (propertiesAreNotEqual("verification_token", token)) {
            Message message = new Message("Wrong verification token !");
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
        ;

        if (propertiesAreNotEqual("finaxys_team_name", teamId)) {
            Message message = new Message("Only for FinaxysTM members !");
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
        ;
        OneUsernameArgumentMatcher oneUsernameArgumentsMatcher = new OneUsernameArgumentMatcher();
        if (!oneUsernameArgumentsMatcher.isCorrect(arguments)) {
            Message message = new Message("/fx_add_adminitstrator : " + arguments + " \n " + "Arguments should suit ' .... @Username ...' Pattern !");
            FinaxysSlackBotLogger.logCommandResponse("/fx_add_adminitstrator :" + arguments + " \n " + "Arguments should suit ' .... @Username ...' Pattern !");
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
        ;
        if (!userIsNotAdministrator(adminFinaxysProfileId) || roleRepository.getByCriterion("role", "admin").size() == 0) {
            String finaxysProfileId = oneUsernameArgumentsMatcher.getUserIdArgument(arguments);
            String finaxysProfileName = oneUsernameArgumentsMatcher.getUserNameArgument(arguments);
            if (userIsNotAdministrator(finaxysProfileId)) {
                FinaxysProfile finaxysProfile = finaxysProfileRepository.findById(finaxysProfileId);
                finaxysProfile = (finaxysProfile == null) ? new FinaxysProfile(finaxysProfileId, finaxysProfileName) : finaxysProfile;
                finaxysProfileRepository.saveOrUpdate(finaxysProfile);
                Role role = new Role("admin");
                role.setFinaxysProfile(finaxysProfile);
                roleRepository.saveOrUpdate(role);
                Message message = new Message("/fx_add_adminitstrator : " + arguments + " \n " + "<@" + finaxysProfileId + "|" + SlackBot.getSlackWebApiClient().getUserInfo(finaxysProfileId).getName() + "> has just became an administrator!");
                FinaxysSlackBotLogger.logCommandResponse("/fx_add_adminitstrator : " + arguments + " \n " + message.getText());
                return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
            } else {
                Message message = new Message("/fx_add_adminitstrator : " + arguments + " \n " + "<@" + finaxysProfileId + "|" + SlackBot.getSlackWebApiClient().getUserInfo(finaxysProfileId).getName() + "> is already an administrator!");
                FinaxysSlackBotLogger.logCommandResponse("/fx_add_adminitstrator : " + arguments + " \n " + message.getText());
                return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
            }
        } else {
            Message message = new Message("/fx_add_adminitstrator : " + arguments + " \n " + "You don't have administration authorization !");
            FinaxysSlackBotLogger.logCommandResponse("/fx_add_adminitstrator : " + "You dont have administration authorization !");
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/admins/remove", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonNode> unsetFinaxysProfileAsAdministrator(@RequestParam("token") String token,
                                                                       @RequestParam("team_domain") String teamId,
                                                                       @RequestParam("user_id") String adminFinaxysProfileId,
                                                                       @RequestParam("text") String arguments) {
        FinaxysSlackBotLogger.logCommandRequest("/fx_remove_adminitstrator " + arguments);
        if (propertiesAreNotEqual("verification_token", token)) {
            Message message = new Message("Wrong verification token !");
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
        ;

        if (propertiesAreNotEqual("finaxys_team_name", teamId)) {
            Message message = new Message("Only for FinaxysTM members !");
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
        ;

        if (userIsNotAdministrator(adminFinaxysProfileId)) {
            Message message = new Message("/fx_remove_adminitstrator " + arguments + " \n " + "You don't have administration authorization !");
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
        ;

        OneUsernameArgumentMatcher oneUsernameArgumentsMatcher = new OneUsernameArgumentMatcher();

        if (!oneUsernameArgumentsMatcher.isCorrect(arguments)) {
            Message message = new Message("/fx_remove_adminitstrator : " + arguments + " \n " + "Arguments should suit ' .... @Username ...' Pattern !");
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
        ;
        String finaxysProfileId = oneUsernameArgumentsMatcher.getUserIdArgument(arguments);
        if (userIsNotAdministrator(finaxysProfileId)) {
            Message message = new Message("/fx_remove_adminitstrator : " + arguments + " \n " + "<@" + finaxysProfileId + "|" + SlackBot.getSlackWebApiClient().getUserInfo(finaxysProfileId).getName() + "> is already not an administrator!");
            FinaxysSlackBotLogger.logCommandResponse(message.getText());
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }

        System.out.println("finaxys admin" + adminFinaxysProfileId);
        System.out.println("finaxysProfile id" + finaxysProfileId);
        FinaxysProfile finaxysProfile = finaxysProfileRepository.findById(finaxysProfileId);
        List<Role> roles = roleRepository.getByCriterion("role", "admin");
        for (Role role : roles) {
            System.out.println(role.getFinaxysProfile().getId());
            if (role.getFinaxysProfile().getId().equals(finaxysProfileId)) {
                roleRepository.delete(role);
                Message message = new Message("/fx_remove_adminitstrator : " + arguments + " \n " + "<@" + finaxysProfileId + "|" + SlackBot.getSlackWebApiClient().getUserInfo(finaxysProfileId).getName() + "> is no more an administrator!");
                FinaxysSlackBotLogger.logCommandResponse(message.getText());
                return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
            }
        }
        Message message = new Message("/fx_remove_adminitstrator : " + arguments + " \n " + "<@" + finaxysProfileId + "|" + SlackBot.getSlackWebApiClient().getUserInfo(finaxysProfileId).getName() + "> is not an administrator!");
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
        }
        ;

        if (propertiesAreNotEqual("finaxys_team_name", teamId)) {
            Message message = new Message("Only for FinaxysTM members !");
            FinaxysSlackBotLogger.logCommandResponse(message.getText());
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
        ;

        List<Role> roles = roleRepository.getByCriterion("role", "admin");
        String messageText = "List of Admins: \n";
        for (Role role : roles)
            messageText += "<@" + role.getFinaxysProfile().getName() + "|" + SlackBot.getSlackWebApiClient().getUserInfo(role.getFinaxysProfile().getId()).getName() + "> \n";
        messageText = (roles.size() > 0) ? messageText : "";
        Message message = new Message("/fx_list_administrators was invoked " + " \n" + messageText);
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
        }
        ;

        if (propertiesAreNotEqual("finaxys_team_name", teamId)) {
            Message message = new Message("Only for FinaxysTM members !");
            FinaxysSlackBotLogger.logCommandResponse(message.getText());
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
        ;

        ChallengeManagerArgumentsMatcher challengeManagerArgumentsMatcher = new ChallengeManagerArgumentsMatcher();

        if (!challengeManagerArgumentsMatcher.isCorrect(arguments)) {
            Message message = new Message("/fx_add_challenge_manager :" + arguments + "\n " + "Arguments should suit ' .... @Username ...' Pattern !");
            FinaxysSlackBotLogger.logCommandResponse(message.getText());
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
        ;

        String finaxysProfileId = challengeManagerArgumentsMatcher.getUserIdArgument(arguments);
        String finaxysProfileName = challengeManagerArgumentsMatcher.getUserNameArgument(arguments);
        String challengeId = challengeManagerArgumentsMatcher.getChallengeName(arguments);
        FinaxysProfile finaxysProfile = finaxysProfileRepository.findById(finaxysProfileId);
        Challenge challenge = challengeRepository.findById(Integer.parseInt(challengeId));
        if (challenge == null) {
            Message message = new Message("/fx_add_challenge_manager :" + arguments + "\n " + "challenge does not exist");
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        } else {
            finaxysProfile = (finaxysProfile == null) ? new FinaxysProfile(finaxysProfileId, finaxysProfileName) : finaxysProfile;
            Role role = new Role();
            role.setRole("challenge_manager");
            role.setChallengeId(Integer.parseInt(challengeId));
            roleRepository.addEntity(role);
            role.setFinaxysProfile(finaxysProfile);
            roleRepository.saveOrUpdate(role);
            Message message = new Message("/fx_add_challenge_manager : " + arguments + "\n " + "<@" + finaxysProfileId + "|" + SlackBot.getSlackWebApiClient().getUserInfo(finaxysProfile.getId()).getName() + "> has just became a challenge manager!");
            FinaxysSlackBotLogger.logCommandResponse(message.getText());
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/challenge_manager/remove", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonNode> unsetFinaxysProfileAsChallengeManager(@RequestParam("token") String token,
                                                                          @RequestParam("team_domain") String teamId,
                                                                          @RequestParam("user_id") String adminFinaxysProfileId,
                                                                          @RequestParam("text") String arguments) {
        FinaxysSlackBotLogger.logCommandRequest("/fx_remove_challenge_manager");
        if (propertiesAreNotEqual("verification_token", token)) {
            Message message = new Message("Wrong verification token !");
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
        ;

        if (propertiesAreNotEqual("finaxys_team_name", teamId)) {
            Message message = new Message("Only for FinaxysTM members !");
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
        ;

        if (userIsNotAdministrator(adminFinaxysProfileId)) {
            Message message = new Message("/fx_remove_challenge_manager  " + arguments + " \n " + "You don't have administration authorization !");
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
        ;

        OneUsernameArgumentMatcher oneUsernameArgumentsMatcher = new OneUsernameArgumentMatcher();

        if (!oneUsernameArgumentsMatcher.isCorrect(arguments)) {
            Message message = new Message("/fx_add_challenge_manager was invoked" + arguments + "\n " + "Arguments should suit ' .... @Username ...' Pattern !");
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
        ;

        String finaxysProfileId = oneUsernameArgumentsMatcher.getUserIdArgument(arguments);
        FinaxysProfile finaxysProfile = finaxysProfileRepository.findById(finaxysProfileId);
        finaxysProfile.setChallengeManager(false);
        finaxysProfileRepository.updateEntity(finaxysProfile);
        Message message = new Message("/fx_add_challenge_manager was invoked" + arguments + "\n " + "<@" + finaxysProfileId + "|" + SlackBot.getSlackWebApiClient().getUserInfo(finaxysProfileId).getName() + "> is no more a challenge manager!");
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
            Message message = new Message("/fx_list_challenge_managers was invoked" + " \n " + "You don't have administration authorization !");
            FinaxysSlackBotLogger.logCommandResponse(message.getText());
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
        ;

        List<FinaxysProfile> finaxysProfiles = finaxysProfileRepository.getByCriterion("challengeManager", true);
        String messageText = "Challenge managers' list:\n";
        for (FinaxysProfile finaxysProfile : finaxysProfiles)
            messageText += "<@" + finaxysProfile.getId() + "|" + SlackBot.getSlackWebApiClient().getUserInfo(finaxysProfile.getId()).getName() + "> \n";
        messageText = (finaxysProfiles.size() > 0) ? messageText : "";
        Message message = new Message("/fx_list_challenge_managers was invoked" + "\n " + messageText);
        FinaxysSlackBotLogger.logCommandResponse(message.getText());
        return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
    }

    public boolean propertiesAreNotEqual(String propertyName, String propertyValue) {
        return !propertyValue.equals(propertyLoader.loadSlackBotProperties().getProperty(propertyName));
    }

    public boolean userIsNotAdministrator(String adminFinaxysProfileId) {
        List<Role> roles = roleRepository.getByCriterion("role", "admin");
        for (Role role : roles) {
            if (role.getFinaxysProfile().getId().equals(adminFinaxysProfileId)) {
                return false;
            }
        }
        return true;
    }
}
