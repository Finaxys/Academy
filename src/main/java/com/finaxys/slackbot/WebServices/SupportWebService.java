package com.finaxys.slackbot.WebServices;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.Utilities.SlackBotTimer;

@RestController
@RequestMapping("/fx_help")
public class SupportWebService extends BaseWebService {
	
	
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<JsonNode> fx_help(@RequestParam("user_id") String userId) {
    	
    	SlackBotTimer timer = new SlackBotTimer();
    	
    	
    	
        String fxCommands =
                		 "*/fx_event_list* \n List all the events. \n \n" +
                         "*/fx_event_add* [event name] [group|individual] [description] \n Adds a new event. \n \n" +
                         "*/fx_event_named* [event name] \n Gives an event details. \n \n" +
                         "*/fx_events_by_date* [yyyy-mm-dd] \n List of events by date \n \n" +
                         "*/fx_events_by_type* [group|individual] \n List of events by type \n \n" +
                         "*/fx_event_score_list*  [eventName] event \n Gives the score list of a given event. \n \n" +
                         "*/fx_event_del* \n Removes an event! \n \n" +
                         "*/fx_event_score_add* @username [points] [name] \n Adds a score to an event attendee. \n \n" +
                         "*/fx_action_add* \n [Code] [ActionName] [Points] \n \n" +
                         "*/fx_action_score_add* \n [EventName] [UserName] [ActionCode] \n \n" +
                         "*/fx_manager_add* [event name] @username \n Adds an event manager. \n \n" +
                         "*/fx_manager_list* [event name] \n List of event managers . \n \n" +
                         "*/fx_manager_del* eventName @username \n Removes an event manager. \n \n" +
                         "*/fx_leaderboard* [optional: count] \n Gives the top scores. \n \n" +
                         "*/fx_contest_add* [contest] [points earned] \n Adds a w<contest. \n \n" +
                         "*/fx_score* [userName] \n Show a user's scores \n"+
        				 "*/fxadmin_list* \n List1 of all administrators. \n \n";
        

        String fxAdminCommands =
        				 "*/fxadmin_add* @username \n Adds an administrator. \n \n" +
                         "*/fxadmin_del* @username \n Removes an administrator. \n \n" +
                         "*/fxadmin_param* [parameter_name] [parameter_value] \n Set the value of a parameter \n \n"+
                         "*/fxadmin_list_params* \n List all parameters \n \n";
        
        
        String message = "/fx_help\nList of the FX bot commands:\n " + fxCommands + (isAdmin(userId) ? " \n " + fxAdminCommands : "");

        timer.capture();
        return newResponseEntity(message +timer,true);
    }
}
