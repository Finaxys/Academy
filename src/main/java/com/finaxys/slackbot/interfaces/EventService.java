package com.finaxys.slackbot.interfaces;

import java.util.Date;
import java.util.List;

import com.finaxys.slackbot.DAL.Action;
import com.finaxys.slackbot.DAL.Event;
import com.finaxys.slackbot.DAL.SlackUser;

public interface EventService {

	Event get(int id);
	
	Event save(Event event);
	
	void remove(Event event);
	
	List<Event> getAll();

	Event getEventByName(String eventName);
	
	List<Event> getEventByType(String type);

	Event getFinaxysEvent();

	int getGlobalScore(SlackUser user);

	String getStringFromList(List<Event> events);

	List<Event> getEventByDate(Date wantedDate);
	
	void addAction(Event event, int code);

	void addScore(Event event, String userId, Action action);



}
