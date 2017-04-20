package com.finaxys.slackbot.interfaces;

import java.util.List;

import com.finaxys.slackbot.DAL.EventScore;

public interface EventScoreService {

	EventScore get(String id);
	
	EventScore save(EventScore eventScore);
	
	void remove(EventScore eventScore);
	
	
	List<EventScore> getAll();
	
	default void update(EventScore eventScore){
		
	}

}
