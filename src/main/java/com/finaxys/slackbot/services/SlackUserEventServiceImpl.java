package com.finaxys.slackbot.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.DAL.Role;
import com.finaxys.slackbot.DAL.SlackUserEvent;
import com.finaxys.slackbot.interfaces.SlackUserEventService;

@Service
public class SlackUserEventServiceImpl implements SlackUserEventService {

	@Autowired
	Repository<SlackUserEvent, String> slackUserEvents;

	@Override
	public SlackUserEvent get(String id) {
		return slackUserEvents.findById(id);
	}

	@Override
	public SlackUserEvent save(SlackUserEvent slackUserEvent) {
		slackUserEvents.saveOrUpdate(slackUserEvent);
		return slackUserEvent;
	}

	@Override
	public void remove(SlackUserEvent slackUserEvent) {
		slackUserEvents.delete(slackUserEvent);
	}

	@Override
	public List<SlackUserEvent> getAll() {
		return slackUserEvents.getAll();
	}

}
