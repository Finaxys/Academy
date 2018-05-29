package com.finaxys.slackbot.services;

import org.springframework.stereotype.Service;

import com.finaxys.slackbot.interfaces.HelpService;

@Service
public class HelpServiceImpl implements HelpService {
	public String fx_help(boolean isAdmin) {
    	
        String fxCommands =
                		 "*fx_events_list* \n List all the events. \n \n" +
                         "*fx_event_add* eventName description group|individual \n Adds a new event. \n \n" +
                         "*fx_event_remove* eventName \n Removes an event. \n \n" +
                         "*fx_event_details* eventName \n Gives an event details. \n \n" +
                         "*fx_events_by_date* yyyy-mm-dd \n List of events by specified date. \n \n" +
                         "*fx_events_by_type* group|individual \n List of events by type. \n \n" +
                         "*fx_action_add* eventName actionName description points \n Adds a new action. \n \n" +
                         "*fx_action_remove* eventName actionName \n Removes the specified action. \n \n" +
                         "*fx_action_performed* eventName actionName userName \n Adds an action to the user specified \n \n" +
                         "*fx_manager_add* userName eventName \n Adds a manager to an event. \n \n" +
                         "*fx_manager_remove* userName eventName \n Removes a manager from an event. \n \n" +
                         "*fx_leaderboard* [eventName|limitNumber] \n Gives the scores. \n \n" +
                         "*fx_score* userName \n Shows a user's scores \n";
        

        String fxAdminCommands =
        				 "*fxadmin_add* userName|password \n Adds an administrator if you have the permission or giving the right password promote you to an administrator. \n \n" +
        				 "*fxadmin_remove* userName password \n Removes an administrator if you have the permission to remove an administrator. \n \n" +
        				 "*fxadmin_enable_debug* \n Activates the debug mode for testing. \n \n" +
        				 "*fxadmin_disable_debug* \n Desactivates the debug mode. \n \n";
        
        
        String message = "List of the FX bot commands:\n" + fxCommands + (isAdmin ? " \n " + fxAdminCommands : "");;
        return message;
	}

}
