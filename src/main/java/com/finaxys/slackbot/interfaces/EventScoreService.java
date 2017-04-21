package com.finaxys.slackbot.interfaces;

import java.util.List;

import com.finaxys.slackbot.DAL.Action;

public interface EventScoreService {

	Action get(String id);
	
	Action save(Action eventScore);
	
	void remove(Action eventScore);
	
	
	List<Action> getAll();
	
	default void update(Action eventScore){
		
	}

}
