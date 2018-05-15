package com.finaxys.slackbot.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finaxys.slackbot.DAL.Action;
import com.finaxys.slackbot.DAL.Event;
import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.interfaces.ActionService;

@Service
public class ActionServiceImpl implements ActionService {

	@Autowired
	Repository<Action, String> actionRepository;

	@Autowired
	Repository<Event, Integer> eventRepository;

	@Override
	public Action get(String id) {
		return actionRepository.findById(id);
	}

	@Override
	public Action save(Action action) {
		actionRepository.saveOrUpdate(action);
		return action;
	}

	@Override
	public void remove(Action action) {
		actionRepository.delete(action);
	}

	@Override
	public List<Action> getAll() {
		return actionRepository.getAll();
	}
}
