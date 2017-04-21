package com.finaxys.slackbot.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finaxys.slackbot.DAL.Action;
import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.interfaces.EventScoreService;

@Service
public class EventScoreServiceImpl implements EventScoreService {

	@Autowired
	Repository<Action, String> eventScores;

	@Override
	public Action get(String id) {
		return eventScores.findById(id);
	}

	@Override
	public Action save(Action eventScore) {
		eventScores.saveOrUpdate(eventScore);
		return eventScore;
	}

	@Override
	public void remove(Action eventScore) {
		eventScores.delete(eventScore);
	}

	@Override
	public List<Action> getAll() {
		return eventScores.getAll();
	}

}
