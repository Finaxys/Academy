package com.finaxys.slackbot.services;

import org.springframework.stereotype.Service;

import com.finaxys.slackbot.Utilities.SlackBotTimer;
import com.finaxys.slackbot.interfaces.HelpService;

@Service
public class HelpServiceImpl implements HelpService {
	
	public String fx_help() {
		

    	SlackBotTimer timer = new SlackBotTimer();
    	
    	
    	
        String fxCommands =
                		 "*fx_events_list* \n List all the events. \n \n" +
                         "*fx_event_add* [event name] [description] [group or individual]  \n Adds a new event. \n \n" +
                         "*fx_event_details* [event name] \n Gives an event details. \n \n" +
                         "*fx_events_by_date* [yyyy-mm-dd] \n List of events by date \n \n" +
                         "*fx_events_by_type* [group or individual] \n List of events by type \n \n" +
                         "*fx_action_add* [event name] [action name] [description] [points] \n Adds a new action \n \n" +
                         "*fx_action_performed* [event name] [action name] [user name] \n Adds an action to the user specified \n \n" +
                         "*fx_leaderboard* [optional: event name] \n Gives the top scores (in general or of the event specified). \n \n" +
                         "*fx_score* [user name] \n Show a user's scores \n";
        

        String fxAdminCommands =
        				 "*fxadmin_add* @username \n Adds an administrator. \n \n" +
                         "*fxadmin_del* @username \n Removes an administrator. \n \n" +
                         "*fxadmin_param* [parameter_name] [parameter_value] \n Set the value of a parameter \n \n"+
                         "*fxadmin_list_params* \n List all parameters \n \n" +
                         "*fxadmin_list* \n List1 of all administrators. \n \n";
        
        
        String message = "/fx_help\nList of the FX bot commands:\n" + fxCommands ;

        timer.capture();
        return message + timer;
	}

}
