package com.finaxys.slackbot.interfaces;

import java.util.List;

import com.finaxys.slackbot.DAL.Event;

public interface EventService {

	Event get(String id);
	
	Event save(Event event);
	
	void remove(Event event);
	
	
	List<Event> getAll();
	
	default void update(Event event){
		
	}

}
