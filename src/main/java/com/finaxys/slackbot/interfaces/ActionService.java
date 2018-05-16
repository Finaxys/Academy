package com.finaxys.slackbot.interfaces;

import java.util.List;

import com.finaxys.slackbot.DAL.Action;

public interface ActionService {
	
	Action save(Action action);
	
	void remove(Action action);
	
	List<Action> getAll();

	Action get(long id);
	Action getActionByCode(String code);
}
