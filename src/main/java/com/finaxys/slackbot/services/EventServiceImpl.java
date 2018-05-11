package com.finaxys.slackbot.services;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finaxys.slackbot.DAL.Action;
import com.finaxys.slackbot.DAL.Event;
import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.DAL.SlackUser;
import com.finaxys.slackbot.DAL.SlackUserEvent;
import com.finaxys.slackbot.interfaces.ActionService;
import com.finaxys.slackbot.interfaces.EventService;
import com.finaxys.slackbot.interfaces.SlackUserEventService;
import com.finaxys.slackbot.interfaces.SlackUserService;

@Service
public class EventServiceImpl implements EventService {

	@Autowired
	Repository<Event, Integer> eventRepository;
	
	@Autowired
	ActionService actionService;
	
	@Autowired
	SlackUserService slackUserService;
	
	@Autowired
	SlackUserEventService slackUserEventService;

	
	@Autowired
	public void SetEventRepository(Repository<Event, Integer> eventRepository){
		this.eventRepository = eventRepository;
		eventRepository.getAll();
	}
	
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
	public void remove(Event event) {
		System.out.println("delete : " + event);
		eventRepository.delete(event);
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
	
	@Override
	public String getStringFromList(List<Event> events) 
	{
		String result = "";

		for (Event event : events) 
		{
			result += event;
			result += "\n***********************************\n";
		}

		return result;
	}

	@Override
	public List<Event> getEventByDate(Date wantedDate) {
		return eventRepository.getByCriterion("creationDate", wantedDate);
	}

	@Override
	public void addAction(Event event, int code) {
		Action action = actionService.get(code);
		
		System.out.println("trsr");
		
		if (action == null)
		{
			System.out.println("upgodhl");
			return;
		}
		
		event.getEventScores().add(action);
		
		System.out.println(event.getEventScores().iterator().next().getCode());
		
		new Thread(()->{eventRepository.saveOrUpdate(event);}).start();
		
	}

	@Override
	public void addScore(Event event, String userId, Action action) {
		
		
		SlackUser user = slackUserService.get(userId);
		
		SlackUserEvent slackUserEvent = slackUserEventService.getSlackUserEvent(event, user);
		
		if (slackUserEvent!=null) {
			slackUserEvent.addScore(action.getPoints());
		}
		else {
			slackUserEvent = new SlackUserEvent(action.getPoints(), event, user);
		}
		
		SlackUserEvent slackUserEvent2 = slackUserEvent;
		
		new Thread(()->{slackUserEventService.save(slackUserEvent2);}).start();
		
	}
}
