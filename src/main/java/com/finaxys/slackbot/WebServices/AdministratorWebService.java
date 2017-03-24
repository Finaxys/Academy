package com.finaxys.slackbot.WebServices;

import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.BUL.Matchers.OneUsernameArgumentMatcher;
import com.finaxys.slackbot.DAL.*;
import com.finaxys.slackbot.Utilities.Log;
import com.finaxys.slackbot.Utilities.Settings;
import com.finaxys.slackbot.Utilities.SlackBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdministratorWebService extends BaseWebService {

    @Autowired
    Repository<FinaxysProfile, String> finaxysProfileRepository;

    @Autowired
    Repository<Challenge, Integer> challengeRepository;

    @RequestMapping(value = "/admins/new", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonNode> setFinaxysProfileAsAdministrator(@RequestParam("appVerificationToken") String appVerificationToken,
                                                                     @RequestParam("slackTeam") String slackTeam,
                                                                     @RequestParam("user_id") String adminFinaxysProfileId,
                                                                     @RequestParam("text") String arguments) {
        Log.info("/fxadmin_add " + arguments);
        if (NoAccess(appVerificationToken, slackTeam))
            return NoAccessResponseEntity(appVerificationToken, slackTeam);

        OneUsernameArgumentMatcher oneUsernameArgumentsMatcher = new OneUsernameArgumentMatcher();
        if (!oneUsernameArgumentsMatcher.isCorrect(arguments)) {
            Message message = new Message("/fxadmin_add  : " + arguments + " \n " + "Arguments should be :@Username");
            Log.info("/fx_add_adminitstrator :" + arguments + " \n " + "Arguments should be : @Username");
            return NewResponseEntity(message);
        }

        if (!isAdmin(adminFinaxysProfileId) || roleRepository.getByCriterion("role", "admin").size() == 0) {
            String finaxysProfileId = oneUsernameArgumentsMatcher.getUserIdArgument(arguments);
            String finaxysProfileName = oneUsernameArgumentsMatcher.getUserNameArgument(arguments);
            if (isAdmin(finaxysProfileId)) {
                FinaxysProfile finaxysProfile = finaxysProfileRepository.findById(finaxysProfileId);
                finaxysProfile = (finaxysProfile == null) ? new FinaxysProfile(finaxysProfileId, finaxysProfileName) : finaxysProfile;
                finaxysProfileRepository.saveOrUpdate(finaxysProfile);
                Role role = new Role("admin");
                role.setFinaxysProfile(finaxysProfile);
                roleRepository.saveOrUpdate(role);
                Message message = new Message("/fxadmin_add  : " + arguments + " \n " + "<@" + finaxysProfileId + "|" + SlackBot.getSlackWebApiClient().getUserInfo(finaxysProfileId).getName() + "> has just became an administrator!");
                Log.info("/fx_add_adminitstrator : " + arguments + " \n " + message.getText());
                return NewResponseEntity(message);
            } else {
                Message message = new Message("/fxadmin_add  : " + arguments + " \n " + "<@" + finaxysProfileId + "|" + SlackBot.getSlackWebApiClient().getUserInfo(finaxysProfileId).getName() + "> is already an administrator!");
                Log.info("/fx_add_adminitstrator : " + arguments + " \n " + message.getText());
                return NewResponseEntity(message);
            }
        } else {
            Message message = new Message("/fxadmin_add  : " + arguments + " \n " + "You have to be an admin!");
            Log.info("/fx_add_adminitstrator : " + "You have to be an admin!");
            return NewResponseEntity(message);
        }
    }

    @RequestMapping(value = "/admins/remove", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonNode> unsetFinaxysProfileAsAdministrator(@RequestParam("appVerificationToken") String appVerificationToken,
                                                                       @RequestParam("slackTeam") String slackTeam,
                                                                       @RequestParam("user_id") String userId,
                                                                       @RequestParam("text") String arguments) {
        Log.info("/fxadmin_del " + arguments);
        if (NoAccess(appVerificationToken, slackTeam))
            return NoAccessResponseEntity(appVerificationToken, slackTeam);

        if (!isAdmin(userId))
            return NewResponseEntity("/fxadmin_del " + arguments + " \n " + "You are not an admin!");

        OneUsernameArgumentMatcher oneUsernameArgumentsMatcher = new OneUsernameArgumentMatcher();

        if (!oneUsernameArgumentsMatcher.isCorrect(arguments))
            return NewResponseEntity("/fxadmin_del : " + arguments + " \n " + "Arguments should be:@Username !");
        ;
        String finaxysProfileId = oneUsernameArgumentsMatcher.getUserIdArgument(arguments);
        if (isAdmin(finaxysProfileId)) {
            Message message = new Message("/fxadmin_del : " + arguments + " \n " + "<@" + finaxysProfileId + "|" + SlackBot.getSlackWebApiClient().getUserInfo(finaxysProfileId).getName() + "> is already not an administrator!");
            Log.info(message.getText());
            return NewResponseEntity(message);
        }


        List<Role> roles = roleRepository.getByCriterion("role", "admin");
        for (Role role : roles) {
            System.out.println(role.getFinaxysProfile().getId());
            if (role.getFinaxysProfile().getId().equals(finaxysProfileId)) {
                roleRepository.delete(role);
                Message message = new Message("/fxadmin_del : " + arguments + " \n " + "<@" + finaxysProfileId + "|" + SlackBot.getSlackWebApiClient().getUserInfo(finaxysProfileId).getName() + "> is no more an administrator!");
                Log.info(message.getText());
                return NewResponseEntity(message);
            }
        }
        Message message = new Message("/fxadmin_del : " + arguments + " \n " + "<@" + finaxysProfileId + "|" + SlackBot.getSlackWebApiClient().getUserInfo(finaxysProfileId).getName() + "> is not an administrator!");
        Log.info(message.getText());
        return NewResponseEntity(message);
    }

    @RequestMapping(value = "/admins/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonNode> getAdministrators(@RequestParam("appVerificationToken") String appVerificationToken,
                                                      @RequestParam("slackTeam") String slackTeam) {
        Log.info("/fxadmin_list");
        if (!Settings.appVerificationToken.equals(appVerificationToken)) {
            Message message = new Message("Wrong verification token !");
            Log.info(message.getText());
            return NewResponseEntity(message);
        }
        ;

        if (!Settings.slackTeam.equals(slackTeam)) {
            Message message = new Message("Only for FinaxysTM members !");
            Log.info(message.getText());
            return NewResponseEntity(message);
        }
        ;

        List<Role> roles = roleRepository.getByCriterion("role", "admin");
        String messageText = "List of Admins: \n";
        for (Role role : roles)
            messageText += "<@" + role.getFinaxysProfile().getName() + "|" + SlackBot.getSlackWebApiClient().getUserInfo(role.getFinaxysProfile().getId()).getName() + "> \n";
        messageText = (roles.size() > 0) ? messageText : "";
        Message message = new Message("/fxadmin_list :" + " \n" + messageText);
        Log.info(message.getText());
        return NewResponseEntity(message);
    }






}
