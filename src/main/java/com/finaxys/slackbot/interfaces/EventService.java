package com.finaxys.slackbot.interfaces;

import java.util.Date;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.DAL.Action;
import com.finaxys.slackbot.DAL.Event;
import com.finaxys.slackbot.DAL.SlackUser;

public interface EventService {

	Event get(int id);
	
	Event save(Event event);
	
	void remove(Event event);
	
	List<Event> getAll();

	Event getEventByName(String eventName);
	
	

	Event getFinaxysEvent();

	//int getGlobalScore(SlackUser user);

	String getStringFromList(List<Event> events);

	String getEventsByDate(String wantedDate);
	String getEventsByType(String type);

	//void addScore(Event event, String userId, Action action);

	String addEventAction(String eventCode, String actionCode, String actionDesc, int actionPoints);
	
	String addActionToSlackuser(String eventCode, String actionCode, String slackuserName);

	//String joinEvent(String userId, String eventName);

	String removeEventAction(String eventCode, String actionCode);
	
	String addEvent(String[] command, JsonNode jsonNode);
	
	String removeEvent(String currentUserId, String eventCode);
	
	
}
