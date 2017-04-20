package com.finaxys.slackbot.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finaxys.slackbot.DAL.EventScore;
import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.interfaces.EventScoreService;

@Service
public class EventScoreServiceImpl implements EventScoreService {

	@Autowired
	Repository<EventScore, String> eventScores;

	@Override
	public EventScore get(String id) {
		return eventScores.findById(id);
	}

	@Override
	public EventScore save(EventScore eventScore) {
		eventScores.saveOrUpdate(eventScore);
		return eventScore;
	}

	@Override
	public void remove(EventScore eventScore) {
		eventScores.delete(eventScore);
	}

	@Override
	public List<EventScore> getAll() {
		return eventScores.getAll();
	}

}
