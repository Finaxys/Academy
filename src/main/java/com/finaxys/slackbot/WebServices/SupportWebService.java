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
                		 "*/fx_event_list*                                                    || List all the events. \n" +
                         "*/fx_event_add* [event name] [group|individual] [description]       || Adds a new event. \n" +
                         "*/fx_manager_add* [event name] @username                            || Adds an event manager. \n" +
                         "*/fx_manager_list* [event name]                                     || List of event managers . \n" +
                         "*/fxadmin_list*                                                     || List of all administrators. \n" +
                         "*/fx_event_named* [event name]                                      || Gives an event details. \n" +
                         "*/fx_events_by_date* [yyyy-mm-dd]                                   || List of events by date \n" +
                         "*/fx_events_by_type* [group|individual]                             || List of events by type \n" +
                         "*/fx_event_score_list*  [eventName] event                           || Gives the score list of a given event. \n" +
                         "*/fx_leaderboard* [optional: count]                                 || Gives the top scores. \n" +
                         "*/fxmanager_event_score_add* @username [points] points [name] event || Adds a score to an event attendee. \n" +
                         "*/fxmanager_del* @username                                          || Removes an event manager. \n" +
                         "*/fx_contest_add* [contest] [points earned]                         || Adds a contest.";

        String fxAdminCommands =
        				 "*/fxadmin_add* @username                                            || Adds an administrator. \n" +
                         "*/fxadmin_del* @username                                            || Removes an administrator. \n" +
                         "*/fxadmin_manager_event_del*                                        || Removes an event!" +
                         "*/fxadmin_param* [parameter_name] [parameter_value]				  || Set the value of a parameter \n"+
                         "*/fxadmin_list_params												  || List all parameters \n";

        String message = "/fx_help\nList of the FX bot commands:\n " + fxCommands + (isAdmin(userId) ? " \n " + fxAdminCommands : "");

        return newResponseEntity(message + timer,true);
    }
}
