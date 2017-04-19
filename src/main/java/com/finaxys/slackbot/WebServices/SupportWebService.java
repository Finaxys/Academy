package com.finaxys.slackbot.WebServices;

import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.DAL.Message;
import com.finaxys.slackbot.Utilities.Log;
import com.finaxys.slackbot.Utilities.Settings;
import com.finaxys.slackbot.Utilities.Timer;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fx_help")
public class SupportWebService extends BaseWebService {
	
	
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<JsonNode> fx_help(@RequestParam("token") 		 String appVerificationToken,
                                            @RequestParam("team_domain") String slackTeam,
                                            @RequestParam("user_id") 	 String userId) {
    	
    	Timer timer = new Timer();
    	
        if (noAccess(appVerificationToken, slackTeam))
            return noAccessResponseEntity(appVerificationToken, slackTeam);

        String fxCommands =
                		 "*/fx_event_list* \n List all the events. \n" +
                         "*/fx_event_add* [event name],[group|individual],[description] \n Add a new event. \n" +
                         "*/fx_manager_add* [event name] @username  \n Add a event manager. \n" +
                         "*/fx_manager_list* [event name] \n List event managers . \n" +
                         "*/fxadmin_list* \n List all administrators. \n" +
                         "*/fx_event_named* [event name] \n Get a event details. \n" +
                         "*/fx_events_by_date* [yyyy-mm-dd] \n Get events by date \n" +
                         "*/fx_events_by_type* [group|individual] \n Get events by type \n" +
                         "*/fx_event_score_list*  [eventName] event \n Gets the score list of a given event. \n" +
                         "*/fx_leaderboard* [optional: count] \nGets the top scores. \n" +
                         "*/fxmanager_event_score_add* @username [points] points [name] event \n Add a score to a event participant. \n" +
                         "*/fxmanager_del @username* \n delete a event manager. \n" +
                         "*/fx_event_add [event]* [points earned] \n add an event.";

        String fxAdminCommands =
        				 "*/fxadmin_add* @username \n Add an administrator. \n" +
                         "*/fxadmin_del* @username \n Delete an administrator. \n" +
                         "*/fxadmin_manager_event_del* \n Delete a event!";

        String message = "/fx_help\nList of the FX bot commands:\n " + fxCommands + (isAdmin(userId) ? " \n " + fxAdminCommands : "");

        return newResponseEntity(message + timer,true);
    }
}
