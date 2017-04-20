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
@RequestMapping("/admins")
public class AdministratorWebService extends BaseWebService {

    @Autowired
    Repository<SlackUser, String> slackUserRepository;
    
    @Autowired
    Repository<Event, Integer> eventRepository;
    
    @Autowired
	public SlackApiAccessService slackApiAccessService;
    
    //test

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonNode> create(@RequestParam("token") 		String appVerificationToken,
                                           @RequestParam("team_domain") String slackTeam,
                                           @RequestParam("user_id") 	String profileId,
                                           @RequestParam("text") 		String arguments) {
    	Timer timer = new Timer();
		
        if (noAccess(appVerificationToken, slackTeam))
            return noAccessResponseEntity(appVerificationToken, slackTeam);
        timer.capture();
        if (!isAdmin(profileId) && roleRepository.getByCriterion("role", "admin").size() != 0)
            return newResponseEntity("/fxadmin_del " + arguments + " \n " + "You are not an admin!" + timer,true);
        timer.capture();
        
        OneUsernameArgumentMatcher oneUsernameArgumentsMatcher = new OneUsernameArgumentMatcher();
        
        if (!oneUsernameArgumentsMatcher.isCorrect(arguments))
            return newResponseEntity("/fxadmin_add  : " + arguments + " \n " + "Arguments should be :@Username " + timer, true);
        
        timer.capture();
        
        String userId 	= oneUsernameArgumentsMatcher.getUserIdArgument	 (arguments);
        String userName = slackApiAccessService.getUser(userId).getName();
        		
        System.out.println("Name : "  + userName);
        if (!isAdmin(userId)) 
        {
        	SlackUser slackUser = slackUserRepository.findById(userId);
            slackUser = (slackUser == null) ? new SlackUser(userId, userName) : slackUser;
            
            slackUserRepository.saveOrUpdate(slackUser);
            
            Role role = new Role("admin");
            
            role.setSlackUser(slackUser);
            
            new Thread(() -> { roleRepository.saveOrUpdate(role);; }).start();
                     
            timer.capture();
            
            return newResponseEntity("/fxadmin_add  : " + arguments + " \n " + "<@" + userId + "|" + SlackBot.getSlackWebApiClient().getUserInfo(userId).getName() + "> has just became an administrator! " + timer, true);
        } 
        else
            return newResponseEntity("/fxadmin_add  : " + arguments + " \n " + "<@" + userId + "|" + SlackBot.getSlackWebApiClient().getUserInfo(userId).getName() + "> is already an administrator!"  + timer, true);

    }

    

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonNode> remove(@RequestParam("token") 		String appVerificationToken,
                                           @RequestParam("team_domain") String slackTeam,
                                           @RequestParam("user_id") 	String userId,
                                           @RequestParam("text") 		String arguments) {
    	Timer timer = new Timer();
		
        if (noAccess(appVerificationToken, slackTeam))
            return noAccessResponseEntity(appVerificationToken, slackTeam);
        timer.capture();
        if (!isAdmin(userId))
            return newResponseEntity("/fxadmin_del " + arguments + " \n " + "You are not an admin!" + timer);
        timer.capture();
        OneUsernameArgumentMatcher oneUsernameArgumentsMatcher = new OneUsernameArgumentMatcher();

        if (!oneUsernameArgumentsMatcher.isCorrect(arguments))
            return newResponseEntity("/fxadmin_del : " + arguments + " \n " + "Arguments should be:@Username !" + timer,true);
        timer.capture();
        String id = oneUsernameArgumentsMatcher.getUserIdArgument(arguments);
        
        if (!isAdmin(id))
            return newResponseEntity("/fxadmin_del : " + arguments + " \n " + "<@" + id + "|" + SlackBot.getSlackWebApiClient().getUserInfo(id).getName() + "> is already not an administrator!" + timer,true);
        timer.capture();
        List<Role> roles = roleRepository.getByCriterion("role", "admin");
        
        for (Role role : roles) 
        {
            if (role.getSlackUser().getSlackUserId().equals(id)) 
            {
                roleRepository.delete(role);
                return  newResponseEntity("/fxadmin_del : " + arguments + " \n " + "<@" + id + "|" + SlackBot.getSlackWebApiClient().getUserInfo(id).getName() + "> is no more an administrator!" + timer,true);
            }
        }
        timer.capture();
        return newResponseEntity("/fxadmin_del : " + arguments + " \n " + "<@" + id + "|" + SlackBot.getSlackWebApiClient().getUserInfo(id).getName() + "> is not an administrator!" + timer,true);
    }
    
    

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonNode> getAdministrators(@RequestParam("token") 		String appVerificationToken,
                                                      @RequestParam("team_domain")  String slackTeam) {
    	Timer timer = new Timer();
		
        Log.info("/fxadmin_list");
        timer.capture();
        if (noAccess(appVerificationToken, slackTeam))
            return noAccessResponseEntity(appVerificationToken, slackTeam);
        timer.capture();
        List<Role> roles 	   = roleRepository.getByCriterion("role", "admin");
        String 	   messageText = "List of Admins: \n";
        
        timer.capture();
        
        for (Role role : roles)
            messageText += "<@" + slackApiAccessService.getUser(role.getSlackUser().getSlackUserId()).getName() + "|" + slackApiAccessService.getUser(role.getSlackUser().getSlackUserId()).getName() + "> \n";
        
        messageText = (roles.size() > 0) ? messageText : "";
        
        timer.capture();
        
        return newResponseEntity("/fxadmin_list :" + " \n" + messageText + timer,true);
    }
}
