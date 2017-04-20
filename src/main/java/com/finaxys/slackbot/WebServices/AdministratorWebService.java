package com.finaxys.slackbot.WebServices;

import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.BUL.Classes.SlackApiAccessService;
import com.finaxys.slackbot.BUL.Matchers.OneUsernameArgumentMatcher;
import com.finaxys.slackbot.DAL.*;
import com.finaxys.slackbot.Utilities.Log;
import com.finaxys.slackbot.Utilities.Settings;
import com.finaxys.slackbot.Utilities.SlackBot;
import com.finaxys.slackbot.Utilities.Timer;
import com.finaxys.slackbot.interfaces.ParameterService;
import com.finaxys.slackbot.interfaces.SlackUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/admins")

public class AdministratorWebService extends BaseWebService {

    
    @Autowired
    Repository<Event, Integer> eventRepository;
    
    @Autowired
	public SlackApiAccessService slackApiAccessService;
    
    @Autowired
    private ParameterService parameterService;
    
    @Autowired
    private SlackUserService slackUserService;
   
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonNode> create(	@RequestParam("user_id") 	String profileId,
            								@RequestParam("text") 		String arguments)
                                         {
    	Timer timer = new Timer();

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
        	SlackUser slackUser = slackUserService.get(userId);
            slackUser = (slackUser == null) ? new SlackUser(userId, userName) : slackUser;
            slackUserService.save(slackUser);
            
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
    public ResponseEntity<JsonNode> remove(@RequestParam("user_id") 	String userId,
                                           @RequestParam("text") 		String arguments) {
    	Timer timer = new Timer();
		
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
            	new Thread(()->{roleRepository.delete(role);}).start();
                
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
        List<Role> roles 	   = roleRepository.getByCriterion("role", "admin");
        String 	   messageText = "List of Admins: \n";
        
        timer.capture();
        
        for (Role role : roles)
            messageText += "<@" + slackApiAccessService.getUser(role.getSlackUser().getSlackUserId()).getName() + "|" + slackApiAccessService.getUser(role.getSlackUser().getSlackUserId()).getName() + "> \n";
        
        messageText = (roles.size() > 0) ? messageText : "";
        
        timer.capture();
        
        return newResponseEntity("/fxadmin_list :" + " \n" + messageText + timer,true);
    }
    
    @RequestMapping(value = "/param", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonNode> param(	@RequestParam("user_id") 	String userId,
            								@RequestParam("text") 		String arguments)
                                         {
    	Timer timer = new Timer();

    	if (!isAdmin(userId))
            return newResponseEntity("/fxadmin_del " + arguments + " \n " + "You are not an admin!" + timer);
        timer.capture();
    	Parameter param = parameterService.get(arguments.split(" ")[0]);
    	param.setValue(arguments.split(" ")[1]);
    	parameterService.save(param);
    	
    	timer.capture();
      
    	
    	return newResponseEntity("/fxadmin_param :" + " \n" + "OK!" + timer,true);	//TODO change OK!!
    }
    
    @RequestMapping(value = "/listParams", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonNode> listParams(	@RequestParam("user_id") 	String userId)
                                         {
    	Timer timer = new Timer();
    	
    	if (!isAdmin(userId))
            return newResponseEntity("/fxadmin_del : You are not an admin!" + timer);
    	
    	return newResponseEntity("/fxadmin_param :" + " \n" + parameterService.getAllAsLines() + timer,true);	//TODO change OK!!
    }
}
