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
    public ResponseEntity<JsonNode> create(@RequestParam("token") 		String appVerificationToken,
                                           @RequestParam("team_domain") String slackTeam,
                                           @RequestParam("user_id") 	String profileId,
                                           @RequestParam("text") 		String arguments) {

        if (NoAccess(appVerificationToken, slackTeam))
            return NoAccessResponseEntity(appVerificationToken, slackTeam);
        
        if (!isAdmin(profileId) && roleRepository.getByCriterion("role", "admin").size() != 0)
            return NewResponseEntity("/fxadmin_del " + arguments + " \n " + "You are not an admin!",true);

        OneUsernameArgumentMatcher oneUsernameArgumentsMatcher = new OneUsernameArgumentMatcher();
        
        if (!oneUsernameArgumentsMatcher.isCorrect(arguments))
            return NewResponseEntity("/fxadmin_add  : " + arguments + " \n " + "Arguments should be :@Username", true);

        String userId 	= oneUsernameArgumentsMatcher.getUserIdArgument	 (arguments);
        String userName = oneUsernameArgumentsMatcher.getUserNameArgument(arguments);
        
        if (!isAdmin(userId)) 
        {
        	FinaxysProfile finaxysProfile = finaxysProfileRepository.findById(userId);
        	
            finaxysProfile = (finaxysProfile == null) ? new FinaxysProfile(userId, userName) : finaxysProfile;
            
            finaxysProfileRepository.saveOrUpdate(finaxysProfile);
            
            
            Role role = new Role("admin");
            
            role.setFinaxysProfile(finaxysProfile);
            
            roleRepository.saveOrUpdate(role);
            
            return NewResponseEntity("/fxadmin_add  : " + arguments + " \n " + "<@" + userId + "|" + SlackBot.getSlackWebApiClient().getUserInfo(userId).getName() + "> has just became an administrator!", true);
        } 
        else
            return NewResponseEntity("/fxadmin_add  : " + arguments + " \n " + "<@" + userId + "|" + SlackBot.getSlackWebApiClient().getUserInfo(userId).getName() + "> is already an administrator!", true);

    }

    

    @RequestMapping(value = "/admins/remove", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonNode> unsetFinaxysProfileAsAdministrator(@RequestParam("token") 		String appVerificationToken,
                                                                       @RequestParam("team_domain") String slackTeam,
                                                                       @RequestParam("user_id") 	String userId,
                                                                       @RequestParam("text") 		String arguments) {

        if (NoAccess(appVerificationToken, slackTeam))
            return NoAccessResponseEntity(appVerificationToken, slackTeam);

        if (!isAdmin(userId))
            return NewResponseEntity("/fxadmin_del " + arguments + " \n " + "You are not an admin!");

        OneUsernameArgumentMatcher oneUsernameArgumentsMatcher = new OneUsernameArgumentMatcher();

        if (!oneUsernameArgumentsMatcher.isCorrect(arguments))
            return NewResponseEntity("/fxadmin_del : " + arguments + " \n " + "Arguments should be:@Username !",true);

        String id = oneUsernameArgumentsMatcher.getUserIdArgument(arguments);
        
        if (!isAdmin(id))
            return NewResponseEntity("/fxadmin_del : " + arguments + " \n " + "<@" + id + "|" + SlackBot.getSlackWebApiClient().getUserInfo(id).getName() + "> is already not an administrator!",true);

        List<Role> roles = roleRepository.getByCriterion("role", "admin");
        
        for (Role role : roles) 
        {
            if (role.getFinaxysProfile().getId().equals(id)) 
            {
                roleRepository.delete(role);
                return  NewResponseEntity("/fxadmin_del : " + arguments + " \n " + "<@" + id + "|" + SlackBot.getSlackWebApiClient().getUserInfo(id).getName() + "> is no more an administrator!",true);
            }
        }
        
        return NewResponseEntity("/fxadmin_del : " + arguments + " \n " + "<@" + id + "|" + SlackBot.getSlackWebApiClient().getUserInfo(id).getName() + "> is not an administrator!",true);
    }
    
    

    @RequestMapping(value = "/admins/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonNode> getAdministrators(@RequestParam("token") 		String appVerificationToken,
                                                      @RequestParam("team_domain")  String slackTeam) {
    	
        Log.info("/fxadmin_list");
        
        if (NoAccess(appVerificationToken, slackTeam))
            return NoAccessResponseEntity(appVerificationToken, slackTeam);

        List<Role> roles 	   = roleRepository.getByCriterion("role", "admin");
        String 	   messageText = "List of Admins: \n";
        
        for (Role role : roles)
            messageText += "<@" + role.getFinaxysProfile().getName() + "|" + SlackBot.getSlackWebApiClient().getUserInfo(role.getFinaxysProfile().getId()).getName() + "> \n";
        
        messageText = (roles.size() > 0) ? messageText : "";
        
        return NewResponseEntity("/fxadmin_list :" + " \n" + messageText,true);
    }
}
