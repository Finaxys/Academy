package com.finaxys.slackbot.WebServices;

import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.BUL.Classes.SlackApiAccessService;
import com.finaxys.slackbot.BUL.Matchers.OneUsernameArgumentMatcher;
import com.finaxys.slackbot.DAL.*;
import com.finaxys.slackbot.Utilities.Log;
import com.finaxys.slackbot.Utilities.Settings;
import com.finaxys.slackbot.Utilities.SlackBot;
import com.finaxys.slackbot.Utilities.Timer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdministratorWebService extends BaseWebService {

    @Autowired
    Repository<FinaxysProfile, String> finaxysProfileRepository;
    
    @Autowired
    Repository<Challenge, Integer> challengeRepository;
    
    @Autowired
	public SlackApiAccessService slackApiAccessService;

    @RequestMapping(value = "/admins/new", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonNode> create(@RequestParam("token") 		String appVerificationToken,
                                           @RequestParam("team_domain") String slackTeam,
                                           @RequestParam("user_id") 	String profileId,
                                           @RequestParam("text") 		String arguments) {
    	Timer timer = new Timer();
		
        if (NoAccess(appVerificationToken, slackTeam))
            return NoAccessResponseEntity(appVerificationToken, slackTeam);
        timer.capture();
        if (!isAdmin(profileId) && roleRepository.getByCriterion("role", "admin").size() != 0)
            return NewResponseEntity("/fxadmin_del " + arguments + " \n " + "You are not an admin!" + timer,true);
        timer.capture();
        
        OneUsernameArgumentMatcher oneUsernameArgumentsMatcher = new OneUsernameArgumentMatcher();
        
        if (!oneUsernameArgumentsMatcher.isCorrect(arguments))
            return NewResponseEntity("/fxadmin_add  : " + arguments + " \n " + "Arguments should be :@Username " + timer, true);
        timer.capture();
        
        String userId 	= oneUsernameArgumentsMatcher.getUserIdArgument	 (arguments);
        String userName = oneUsernameArgumentsMatcher.getUserNameArgument(arguments);
        
        if (!isAdmin(userId)) 
        {
        	FinaxysProfile finaxysProfile = finaxysProfileRepository.findById(userId);
        	
            finaxysProfile = (finaxysProfile == null) ? new FinaxysProfile(userId, userName) : finaxysProfile;
            
            finaxysProfileRepository.saveOrUpdate(finaxysProfile);
            
            
            Role role = new Role("admin");
            
            role.setSlackUser(finaxysProfile);
            
            roleRepository.saveOrUpdate(role);
            timer.capture();
            
            return NewResponseEntity("/fxadmin_add  : " + arguments + " \n " + "<@" + userId + "|" + SlackBot.getSlackWebApiClient().getUserInfo(userId).getName() + "> has just became an administrator! " + timer, true);
        } 
        else
            return NewResponseEntity("/fxadmin_add  : " + arguments + " \n " + "<@" + userId + "|" + SlackBot.getSlackWebApiClient().getUserInfo(userId).getName() + "> is already an administrator!"  + timer, true);

    }

    

    @RequestMapping(value = "/admins/remove", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonNode> unsetFinaxysProfileAsAdministrator(@RequestParam("token") 		String appVerificationToken,
                                                                       @RequestParam("team_domain") String slackTeam,
                                                                       @RequestParam("user_id") 	String userId,
                                                                       @RequestParam("text") 		String arguments) {
    	Timer timer = new Timer();
		
        if (NoAccess(appVerificationToken, slackTeam))
            return NoAccessResponseEntity(appVerificationToken, slackTeam);
        timer.capture();
        if (!isAdmin(userId))
            return NewResponseEntity("/fxadmin_del " + arguments + " \n " + "You are not an admin!" + timer);
        timer.capture();
        OneUsernameArgumentMatcher oneUsernameArgumentsMatcher = new OneUsernameArgumentMatcher();

        if (!oneUsernameArgumentsMatcher.isCorrect(arguments))
            return NewResponseEntity("/fxadmin_del : " + arguments + " \n " + "Arguments should be:@Username !" + timer,true);
        timer.capture();
        String id = oneUsernameArgumentsMatcher.getUserIdArgument(arguments);
        
        if (!isAdmin(id))
            return NewResponseEntity("/fxadmin_del : " + arguments + " \n " + "<@" + id + "|" + SlackBot.getSlackWebApiClient().getUserInfo(id).getName() + "> is already not an administrator!" + timer,true);
        timer.capture();
        List<Role> roles = roleRepository.getByCriterion("role", "admin");
        
        for (Role role : roles) 
        {
            if (role.getSlackUser().getId().equals(id)) 
            {
                roleRepository.delete(role);
                return  NewResponseEntity("/fxadmin_del : " + arguments + " \n " + "<@" + id + "|" + SlackBot.getSlackWebApiClient().getUserInfo(id).getName() + "> is no more an administrator!" + timer,true);
            }
        }
        timer.capture();
        return NewResponseEntity("/fxadmin_del : " + arguments + " \n " + "<@" + id + "|" + SlackBot.getSlackWebApiClient().getUserInfo(id).getName() + "> is not an administrator!" + timer,true);
    }
    
    

    @RequestMapping(value = "/admins/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonNode> getAdministrators(@RequestParam("token") 		String appVerificationToken,
                                                      @RequestParam("team_domain")  String slackTeam) {
    	Timer timer = new Timer();
		
        Log.info("/fxadmin_list");
        timer.capture();
        if (NoAccess(appVerificationToken, slackTeam))
            return NoAccessResponseEntity(appVerificationToken, slackTeam);
        timer.capture();
        List<Role> roles 	   = roleRepository.getByCriterion("role", "admin");
        String 	   messageText = "List of Admins: \n";
        
        timer.capture();
        
        for (Role role : roles)
            messageText += "<@" + slackApiAccessService.getUser(role.getSlackUser().getId()).getName() + "|" + slackApiAccessService.getUser(role.getSlackUser().getId()).getName() + "> \n";
        
        messageText = (roles.size() > 0) ? messageText : "";
        
        timer.capture();
        
        return NewResponseEntity("/fxadmin_list :" + " \n" + messageText + timer,true);
    }
}
