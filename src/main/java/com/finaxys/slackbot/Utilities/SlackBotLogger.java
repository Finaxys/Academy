package com.finaxys.slackbot.Utilities;

import org.apache.log4j.spi.LoggingEvent;
import org.springframework.stereotype.Component;

import com.finaxys.slackbot.DAL.LogEvent;

@Component
public class SlackBotLogger extends Log{

    
	@Override
	public void append(LoggingEvent event) {
		if(AppParameters.getValue("DB_LOG").equals("ON"))
			super.append(event);
		else System.out.println(new LogEvent(event));
	}
}


