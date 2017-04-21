package com.finaxys.slackbot.interfaces;

import java.util.List;

import com.finaxys.slackbot.DAL.Event;
import com.finaxys.slackbot.DAL.SlackUser;
import com.finaxys.slackbot.DAL.SlackUserEvent;

public interface SlackUserEventService {

	SlackUserEvent get(String id);
	
	SlackUserEvent save(SlackUserEvent slackUserEvent);
	
	void remove(SlackUserEvent slackUserEvent);
	
	List<SlackUserEvent> getAll();
	
	void update(SlackUserEvent slackUserEvent);

	SlackUserEvent getSlackUserEvent(Event event, SlackUser slackUser);
	
	List<SlackUserEvent> getAllByEvent(Event event);

}
