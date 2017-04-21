package com.finaxys.slackbot.Utilities;

public class SlackBotTimer extends Timer{
	
	
	@Override
	public String toString() {
		if(AppParameters.getValue("TIMER").equals("ON"))
			return super.toString();
		return "";
	}
}
