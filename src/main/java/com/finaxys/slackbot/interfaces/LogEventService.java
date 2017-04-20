package com.finaxys.slackbot.interfaces;

import java.util.List;

import com.finaxys.slackbot.DAL.LogEvent;

public interface LogEventService {

	LogEvent get(String id);
	
	LogEvent save(LogEvent log);
	
	void remove(LogEvent log);
	
	
	List<LogEvent> getAll();
	
	default void update(LogEvent log){
		
	}

}
