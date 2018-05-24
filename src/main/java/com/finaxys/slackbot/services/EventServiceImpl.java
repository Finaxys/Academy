package com.finaxys.slackbot.services;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.BUL.Matchers.OneUsernameArgumentMatcher;
import com.finaxys.slackbot.BUL.Matchers.QuotesMatcher;
import com.finaxys.slackbot.DAL.Action;
import com.finaxys.slackbot.DAL.Event;
import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.DAL.SlackUser;
import com.finaxys.slackbot.Utilities.SlackBotTimer;
import com.finaxys.slackbot.interfaces.ActionService;
import com.finaxys.slackbot.interfaces.EventService;
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
	public void SetEventRepository(Repository<Event, Integer> eventRepository) {
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
		} else {
			Event event = new Event("FINAXYS", "Global event", "individual");
			this.save(event);
			return event;

		}
	}

	
	/*
	@Override
	public int getGlobalScore(SlackUser user) {
		if (getFinaxysEvent().getAttendees().isEmpty()) {
			return 0;
		}
		Iterator<SlackUserEvent> slackUserEventIterator = getFinaxysEvent().getAttendees().iterator();

		while (slackUserEventIterator.hasNext()) {
			SlackUserEvent slackUserEvent = slackUserEventIterator.next();
			if (slackUserEvent.getSlackUser().equals(user)) {
				return slackUserEvent.getScore();
			}
		}
		return 0;
	}
	
	*/

	@Override
	public List<Event> getEventByType(String type) {
		return eventRepository.getByCriterion("type", type);
	}

	@Override
	public String getStringFromList(List<Event> events) {
		String result = "";

		for (Event event : events) {
			result += event;
			result += "\n***********************************\n";
		}

		return result;
	}

	@Override
	public List<Event> getEventByDate(Date wantedDate) {
		return eventRepository.getByCriterion("creationDate", wantedDate);
	}
/*
	@Override
	public void addScore(Event event, String userId, Action action) {

		SlackUser user = slackUserService.get(userId);

		SlackUserEvent slackUserEvent = slackUserEventService.getSlackUserEvent(event, user);

		if (slackUserEvent != null) {
			slackUserEvent.addScore(action.getPoints());
		} else {
			slackUserEvent = new SlackUserEvent(action.getPoints(), event, user);
		}

		SlackUserEvent slackUserEvent2 = slackUserEvent;

		new Thread(() -> {
			slackUserEventService.save(slackUserEvent2);
		}).start();

	}
*/
	@Override
	public String addEventAction(String eventCode, String actionCode, String actionDesc, int actionPoints) {
			Event event = this.getEventByName(eventCode);

		if (event == null)
			return "Event does not exist.";

		try {
			
			QuotesMatcher qm = new QuotesMatcher();
			String description = "";
			if (qm.isCorrect(actionDesc))
				description = qm.getQuotesArgument(actionDesc);
			else
				return "Description must be between quotes.";

			Action action = new Action(actionCode, description.substring(0, description.length()-1), actionPoints);
			action.setEvent(event);
			if (event.getEventScores().stream().filter(a -> a.getCode().equals(actionCode)).count() != 0)// check actionCode isUnique in an event
				return "The action " + actionCode + " already exists in this event! ";
			else
				actionService.save(action);
			
		} catch (NumberFormatException e) {
			return "fx_event_action_add failed. Please, check the arguments types.";// + timer;
		}

		return "The action named " + actionCode + " has been successfully added to the event " + eventCode + ".";

	}

	/*
	@Override
	public String joinEvent(String userId, String eventName) {

		SlackUser user = slackUserService.get(userId);

		Event event = this.getEventByName(eventName);

		if (event == null)
			return "Nonexistent event";// + timer;

		// TODO
		// if (!isEventManager(eventManagerId, eventName) && !isAdmin(eventManagerId))
		// return "/fx_event_score_add " + eventName + "\n" + "You are neither admin nor
		// a event manager !" + timer;

		SlackUserEvent slackUserEvent = slackUserEventService.getSlackUserEvent(event, user);

		if (slackUserEvent != null) {
			return "You have already participated to this event " + eventName;
		} else {
			slackUserEvent = new SlackUserEvent(event, user);

		}

		try {
			slackUserEventService.save(slackUserEvent);
		} catch (Exception e) {
			return "/fx_event_join " + eventName + " \n" + "A problem has occured!";
		}

		return "You are now a member of this event " + eventName;
	}
	*/
	@Override
	public String addActionToSlackuser(String eventCode, String actionCode, String slackuserName) {
		Event event = this.getEventByName(eventCode);
		Action action = actionService.getActionByCode(actionCode);
		OneUsernameArgumentMatcher um = new OneUsernameArgumentMatcher();
		String userId = "";
		if (um.isCorrect(slackuserName))
			userId = um.getUserIdArgument(slackuserName);
		else
			return "Username incorrect.";

		SlackUser slackuser = slackUserService.get(userId);

		if (event == null)
			return "The event "+eventCode+ " does not exist.";
		if (action == null)
			return "The action "+actionCode+ " does not exist.";
		if (slackuser == null)
			return "The user "+slackuserName+ " does not exist.";
		if (event.getEventScores().stream().filter(a -> a.getCode().equals(action.getCode())).count() == 0)
			return "The action "+actionCode+ " does not exist in the event "+eventCode+".";

		if (slackuser.getActions() == null)
			slackuser.setActions(new HashSet<>());

		slackuser.getActions().add(action);
		slackUserService.save(slackuser);

		return "The action named " + actionCode + " has been successfully performed by " + slackuserName + " ! ";
	}

	//@Transactional
	public String removeEventAction(String eventCode, String actionCode) {
		Event event = this.getEventByName(eventCode);
		Action action = actionService.getActionByCode(actionCode);
		if (event == null)
			return "Event does not exist ";

		try {
			if (action == null)
				return "/fx_event_action_del " + actionCode + "\n " + " :  This action doesn't exist for this event ! ";
			else {

				event.getEventScores().remove(action);
				event.setEventScores(new HashSet<>());
				this.save(event);
				//actionService.remove(action);
			}
				
								
		} catch (NumberFormatException e) {
			return "fx_event_action_del failed. Please, check the arguments types. ";// + timer;
		}
		
		
		return "The action named " + actionCode + " has been successfully removed ! ";

	}
	
	public String addEvent(String[] command, JsonNode jsonNode) {
		SlackBotTimer timer = new SlackBotTimer();
		timer.capture();

		if (this.getEventByName(command[1]) != null)
			return "The event " + command[2] + " already exists ! ";
		else {
			new Thread(() -> {
				SlackUser user = slackUserService.get(jsonNode.get("user").asText().trim());
				Event event = new Event(command[1], command[2], command[3]);
				event.getEventManagers().add(user);
				this.save(event);
			}).start();
			timer.capture();
			return " The event named " + command[1] + " has been successfully added ! You can now manage it.";
		}
	}
	
	

}
