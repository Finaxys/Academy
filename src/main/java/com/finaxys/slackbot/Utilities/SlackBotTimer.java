package com.finaxys.slackbot.Utilities;

import com.finaxys.slackbot.services.AppParameters;

public class SlackBotTimer extends Timer{
	
	
	@Override
	public String toString() {
		if(AppParameters.getValue("TIMER").equals("ON"))
			return super.toString();
		return "";
	}
}
