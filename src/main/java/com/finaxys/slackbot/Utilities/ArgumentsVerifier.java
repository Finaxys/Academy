package com.finaxys.slackbot.Utilities;

import java.util.HashMap;
import java.util.List;

public class ArgumentsVerifier 
{
	public ArgumentsReader reader;
	public HashMap<String, String> commandPatternMap;
	
	public ArgumentsVerifier()
	{
		commandPatternMap = new HashMap<String, String>();
		
		reader = new ArgumentsReader();
		
		commandPatternMap.put("/fx_event_list",       "");
		commandPatternMap.put("/fx_event_add",        "String Type String");
		commandPatternMap.put("/fx_manager_add",      "String UserId");
		commandPatternMap.put("/fx_manager_list",     "String");
		commandPatternMap.put("/fxadmin_list",        "");
		commandPatternMap.put("/fx_event_named",      "String");
		commandPatternMap.put("/fx_events_by_date",   "String");
		commandPatternMap.put("/fx_events_by_type",   "Type");
		commandPatternMap.put("/fx_event_score_list", "String");
		commandPatternMap.put("/fx_leaderboard",      "Integer");
		commandPatternMap.put("/fx_event_score_add",  "UserId Integer String");
		commandPatternMap.put("/fx_manager_del",      "String UserId");
		commandPatternMap.put("/fx_contest_add",      "String Integer");
		commandPatternMap.put("/fxadmin_add",         "UserId");
		commandPatternMap.put("/fxadmin_del",         "UserId");
		commandPatternMap.put("/fx_event_del",        "String");
		commandPatternMap.put("/fx_score",            "UserId");
		commandPatternMap.put("/fx_help",             "");
		commandPatternMap.put("/fx_action_add",       "Integer String Integer");
		commandPatternMap.put("/fx_event_action_add", "String Integer");
		commandPatternMap.put("/fx_action_score_add", "String UserId Integer");
	}
	
	public boolean Verify(String arguments, String command)
	{
		List<Token> argsParsed = reader.parse(arguments);
		
		if (argsParsed == null)
			return commandPatternMap.get(command).length() == 0;
		
		if (argsParsed.size() != commandPatternMap.get(command).split(" ").length)
			return false;
		
		int size = argsParsed.size();
		
		for (int i = 0; i < size; i++)
		{
			if (!argsParsed.get(i).type.equals(commandPatternMap.get(command).split(" ")[i]))
			{
				return false;
			}
		}
		return true;
	}
}
