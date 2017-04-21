package com.finaxys.slackbot.interfaces;

import java.util.List;

import com.finaxys.slackbot.DAL.Action;

public interface ActionService {

	Action get(String id);
	
	Action save(Action action);
	
	void remove(Action action);
	
	List<Action> getAll();
}
