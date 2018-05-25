package com.finaxys.slackbot.services;

import org.springframework.stereotype.Service;

import com.finaxys.slackbot.interfaces.HelpService;

@Service
public class HelpServiceImpl implements HelpService {
	public String fx_help(boolean isAdmin) {
    	
        String fxCommands =
                		 "*fx_events_list* \n List all the events. \n \n" +
                         "*fx_event_add* eventName description group|individual  \n Adds a new event. \n \n" +
                         "*fx_event_details* eventName \n Gives an event details. \n \n" +
                         "*fx_events_by_date* [yyyy-mm-dd] \n List of events by specified date \n \n" +
                         "*fx_events_by_type* [group or individual] \n List of events by type \n \n" +
                         "*fx_action_add* [event name] [action name] [description] [points] \n Adds a new action \n \n" +
                         "*fx_action_performed* [event name] [action name] [user name] \n Adds an action to the user specified \n \n" +
                         "*fx_manager_add* username eventName \n add a manager to an event. \n \n" +
                         "*fx_manager_remove* username eventName \n remove a manager from an event. \n \n" +
                         "*fx_leaderboard* [eventName|limitNumber] \n Gives the scores. \n \n" +
                         "*fx_score* username \n Show a user's scores \n";
        

        String fxAdminCommands =
        				 "*fxadmin_add* username | password \n Adds an administrator if you have the permission or giving the right password promote you to an administrator. \n \n" +
        				 "*fxadmin_remove* username password \n remove an administrator if you have the permission to remove an administrator. \n \n" +
        				 "*fxadmin_enable_debug* \n Activate the debug mode for testing. \n \n" +
        				 "*fxadmin_disable_debug* \n Desactivate the debug mode. \n \n";
        
        
        String message = "List of the FX bot commands:\n" + fxCommands + (isAdmin ? " \n " + fxAdminCommands : "");;
        return message;
	}

}
