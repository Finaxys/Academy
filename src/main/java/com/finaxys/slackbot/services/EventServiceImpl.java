package com.finaxys.slackbot.services;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finaxys.slackbot.DAL.Event;
import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.DAL.SlackUser;
import com.finaxys.slackbot.DAL.SlackUserEvent;
import com.finaxys.slackbot.interfaces.EventService;

@Service
public class EventServiceImpl implements EventService {

	@Autowired
	Repository<Event, Integer> eventRepository;

	@Override
	public Event get(int id) {
		return eventRepository.findById(id);
	}

	@Override
	public Event save(Event user) {
		eventRepository.saveOrUpdate(user);
		return user;
	}

	@Override
	public void remove(Event user) {
		eventRepository.delete(user);
	}

	@Override
	public List<Event> getAll() {
		return eventRepository.getAll();
	}

	@Override
	public Event getEventByName(String eventName) {
		List<Event> events = eventRepository.getByCriterion("name", eventName); 
		if (!events.isEmpty()) {
			return events.get(0);
		}
		return null;
	}

	@Override
	public Event getFinaxysEvent() {
		List<Event> events = eventRepository.getByCriterion("name", "FINAXYS");
		if (!events.isEmpty()) {
			return events.get(0);
		}
		else {
			Event event = new Event("FINAXYS","Global event", "individual");
			this.save(event);
			return event;
			
		}
	}

	@Override
	public int getGlobalScore(SlackUser user) {
		if(getFinaxysEvent().getAttendees().isEmpty()) {
			return 0;
		}
		Iterator<SlackUserEvent> slackUserEventIterator = getFinaxysEvent().getAttendees().iterator();
		
		while (slackUserEventIterator.hasNext()) {
			SlackUserEvent slackUserEvent = slackUserEventIterator.next();
			if (slackUserEvent .getSlackUser().equals(user)) {
				return slackUserEvent.getScore();
			}
		}
		return 0;
	}

	@Override
	public List<Event> getEventByType(String type) {
		return eventRepository.getByCriterion("type", type);
	}
}
