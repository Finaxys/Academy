package com.finaxys.slackbot.interfaces;

import java.util.List;

import com.finaxys.slackbot.DAL.Event;
import com.finaxys.slackbot.DAL.SlackUser;

public interface EventService {

	Event get(int id);
	
	Event save(Event event);
	
	void remove(Event event);
	
	List<Event> getAll();

	Event getEventByName(String eventName);

	Event getFinaxysEvent();

	int getGlobalScore(SlackUser user);



}
