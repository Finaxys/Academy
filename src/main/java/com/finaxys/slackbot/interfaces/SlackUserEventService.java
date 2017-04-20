package com.finaxys.slackbot.interfaces;

import java.util.List;

import com.finaxys.slackbot.DAL.SlackUserEvent;

public interface SlackUserEventService {

	SlackUserEvent get(String id);
	
	SlackUserEvent save(SlackUserEvent slackUserEvent);
	
	void remove(SlackUserEvent slackUserEvent);
	
	
	List<SlackUserEvent> getAll();
	
	default void update(SlackUserEvent slackUserEvent){
		
	}

}
