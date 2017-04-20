package com.finaxys.slackbot.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finaxys.slackbot.DAL.Event;
import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.interfaces.EventService;

@Service
public class EventServiceImpl implements EventService {

	@Autowired
	Repository<Event, String> roles;

	@Override
	public Event get(String id) {
		return roles.findById(id);
	}

	@Override
	public Event save(Event user) {
		roles.saveOrUpdate(user);
		return user;
	}

	@Override
	public void remove(Event user) {
		roles.delete(user);
	}

	@Override
	public List<Event> getAll() {
		return roles.getAll();
	}

}
