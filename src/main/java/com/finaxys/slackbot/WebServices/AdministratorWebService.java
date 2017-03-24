package com.finaxys.slackbot.WebServices;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finaxys.slackbot.BUL.Interfaces.ReactionAddedService;
import com.finaxys.slackbot.BUL.Matchers.ChallengeManagerArgumentsMatcher;
import com.finaxys.slackbot.BUL.Matchers.OneUsernameArgumentMatcher;
import com.finaxys.slackbot.DAL.*;
import com.finaxys.slackbot.Utilities.FinaxysSlackBotLogger;
import com.finaxys.slackbot.Utilities.Settings;
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

    ObjectMapper objectMapper = new ObjectMapper();

    @RequestMapping(value = "/admins/new", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonNode> setFinaxysProfileAsAdministrator(@RequestParam("appVerificationToken") String appVerificationToken,
                                                                     @RequestParam("slackTeam") String slackTeam,
                                                                     @RequestParam("user_id") String adminFinaxysProfileId,
                                                                     @RequestParam("text") String arguments) {
        FinaxysSlackBotLogger.logCommandRequest("/fxadmin_add " + arguments);
        if (!Settings.appVerificationToken.equals(appVerificationToken)) {
            Message message = new Message("Wrong verification token !");
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }


        if (!Settings.slackTeam.equals(slackTeam)) {
            Message message = new Message("Only for FinaxysTM members !");
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }

        OneUsernameArgumentMatcher oneUsernameArgumentsMatcher = new OneUsernameArgumentMatcher();
        if (!oneUsernameArgumentsMatcher.isCorrect(arguments)) {
            Message message = new Message("/fxadmin_add  : " + arguments + " \n " + "Arguments should be :@Username");
            FinaxysSlackBotLogger.logCommandResponse("/fx_add_adminitstrator :" + arguments + " \n " + "Arguments should be : @Username");
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }

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
                Message message = new Message("/fxadmin_add  : " + arguments + " \n " + "<@" + finaxysProfileId + "|" + SlackBot.getSlackWebApiClient().getUserInfo(finaxysProfileId).getName() + "> has just became an administrator!");
                FinaxysSlackBotLogger.logCommandResponse("/fx_add_adminitstrator : " + arguments + " \n " + message.getText());
                return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
            } else {
                Message message = new Message("/fxadmin_add  : " + arguments + " \n " + "<@" + finaxysProfileId + "|" + SlackBot.getSlackWebApiClient().getUserInfo(finaxysProfileId).getName() + "> is already an administrator!");
                FinaxysSlackBotLogger.logCommandResponse("/fx_add_adminitstrator : " + arguments + " \n " + message.getText());
                return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
            }
        } else {
            Message message = new Message("/fxadmin_add  : " + arguments + " \n " + "You have to be an admin!");
            FinaxysSlackBotLogger.logCommandResponse("/fx_add_adminitstrator : " + "You have to be an admin!");
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/admins/remove", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonNode> unsetFinaxysProfileAsAdministrator(@RequestParam("appVerificationToken") String appVerificationToken,
                                                                       @RequestParam("slackTeam") String slackTeam,
                                                                       @RequestParam("user_id") String adminFinaxysProfileId,
                                                                       @RequestParam("text") String arguments) {
        FinaxysSlackBotLogger.logCommandRequest("/fxadmin_del " + arguments);
        if (Settings.appVerificationToken.equals(appVerificationToken)) {
            Message message = new Message("Wrong verification token !");
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
        ;

        if (Settings.slackTeam.equals(slackTeam)) {
            Message message = new Message("Only for FinaxysTM members !");
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
        ;

        if (userIsNotAdministrator(adminFinaxysProfileId)) {
            Message message = new Message("/fxadmin_del " + arguments + " \n " + "You are not an admin!");
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
        ;

        OneUsernameArgumentMatcher oneUsernameArgumentsMatcher = new OneUsernameArgumentMatcher();

        if (!oneUsernameArgumentsMatcher.isCorrect(arguments)) {
            Message message = new Message("/fxadmin_del : " + arguments + " \n " + "Arguments should be:@Username !");
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
        ;
        String finaxysProfileId = oneUsernameArgumentsMatcher.getUserIdArgument(arguments);
        if (userIsNotAdministrator(finaxysProfileId)) {
            Message message = new Message("/fxadmin_del : " + arguments + " \n " + "<@" + finaxysProfileId + "|" + SlackBot.getSlackWebApiClient().getUserInfo(finaxysProfileId).getName() + "> is already not an administrator!");
            FinaxysSlackBotLogger.logCommandResponse(message.getText());
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }


        List<Role> roles = roleRepository.getByCriterion("role", "admin");
        for (Role role : roles) {
            System.out.println(role.getFinaxysProfile().getId());
            if (role.getFinaxysProfile().getId().equals(finaxysProfileId)) {
                roleRepository.delete(role);
                Message message = new Message("/fxadmin_del : " + arguments + " \n " + "<@" + finaxysProfileId + "|" + SlackBot.getSlackWebApiClient().getUserInfo(finaxysProfileId).getName() + "> is no more an administrator!");
                FinaxysSlackBotLogger.logCommandResponse(message.getText());
                return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
            }
        }
        Message message = new Message("/fxadmin_del : " + arguments + " \n " + "<@" + finaxysProfileId + "|" + SlackBot.getSlackWebApiClient().getUserInfo(finaxysProfileId).getName() + "> is not an administrator!");
        FinaxysSlackBotLogger.logCommandResponse(message.getText());
        return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
    }

    @RequestMapping(value = "/admins/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonNode> getAdministrators(@RequestParam("appVerificationToken") String appVerificationToken,
                                                      @RequestParam("slackTeam") String slackTeam) {
        FinaxysSlackBotLogger.logCommandRequest("/fxadmin_list");
        if (!Settings.appVerificationToken.equals(appVerificationToken)) {
            Message message = new Message("Wrong verification token !");
            FinaxysSlackBotLogger.logCommandResponse(message.getText());
            return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
        }
        ;

        if (!Settings.slackTeam.equals(slackTeam)) {
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
        Message message = new Message("/fxadmin_list :" + " \n" + messageText);
        FinaxysSlackBotLogger.logCommandResponse(message.getText());
        return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
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
