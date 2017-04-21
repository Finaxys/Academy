package com.finaxys.slackbot.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finaxys.slackbot.DAL.Event;
import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.DAL.SlackUser;
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

	@Override
	public SlackUserEvent getSlackUserEvent(Event event, SlackUser slackUser) {
		Iterator<SlackUserEvent> slackUserEventIterator = event.getAttendees().iterator();
		
		while (slackUserEventIterator.hasNext()) {
			SlackUserEvent slackUserEvent = slackUserEventIterator.next();
			if (slackUserEvent.getSlackUser().equals(slackUser)) {
				return slackUserEvent;
			}
		}
		return null;
	}
	@Override
	public List<SlackUserEvent> getAllByEvent(Event event) {
		Set<SlackUserEvent> set = event.getAttendees();
		return set.isEmpty() ? null : new ArrayList<SlackUserEvent>(event.getAttendees());
	}

	@Override
	public void update(SlackUserEvent slackUserEvent) {
		// TODO Auto-generated method stub
		
	}

}
