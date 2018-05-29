package com.finaxys.slackbot.services;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.BUL.Matchers.DateMatcher;
import com.finaxys.slackbot.BUL.Matchers.OneUsernameArgumentMatcher;
import com.finaxys.slackbot.DAL.Action;
import com.finaxys.slackbot.DAL.Event;
import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.DAL.SlackUser;
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
	public String getEventsByDate(String text) {

		DateMatcher dateMatcher = new DateMatcher();

		if (!dateMatcher.match(text.trim()))
			return "Date format : yyyy-MM-dd";
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date wantedDate = new Date();
		try {
			wantedDate = dateFormat.parse(text);
		} catch (Exception e) {
		}

		List<Event> events = eventRepository.getByCriterion("creationDate", wantedDate);
		if (events.isEmpty())
			return "There are no events on this date: " + text; // + timer;
		else
			return "List of events on date : " + wantedDate + "\n" + this.getStringFromList(events);// + timer;
	}

	@Override
	public String getEventsByType(String eventType) {
		List<Event> events = eventRepository.getByCriterion("type", eventType);
		if (events.isEmpty())
			return "No events with type: " + eventType;

		return "List of events with type : " + eventType + " \n" + this.getStringFromList(events);
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
	 * @Override public int getGlobalScore(SlackUser user) { if
	 * (getFinaxysEvent().getAttendees().isEmpty()) { return 0; }
	 * Iterator<SlackUserEvent> slackUserEventIterator =
	 * getFinaxysEvent().getAttendees().iterator();
	 * 
	 * while (slackUserEventIterator.hasNext()) { SlackUserEvent slackUserEvent =
	 * slackUserEventIterator.next(); if
	 * (slackUserEvent.getSlackUser().equals(user)) { return
	 * slackUserEvent.getScore(); } } return 0; }
	 * 
	 */

	@Override
	public String getStringFromList(List<Event> events) {
		String result = "";

		for (Event event : events) {
			result += event;
			result += "\n***********************************\n";
		}

		return result;
	}

	/*
	 * @Override public void addScore(Event event, String userId, Action action) {
	 * 
	 * SlackUser user = slackUserService.get(userId);
	 * 
	 * SlackUserEvent slackUserEvent =
	 * slackUserEventService.getSlackUserEvent(event, user);
	 * 
	 * if (slackUserEvent != null) { slackUserEvent.addScore(action.getPoints()); }
	 * else { slackUserEvent = new SlackUserEvent(action.getPoints(), event, user);
	 * }
	 * 
	 * SlackUserEvent slackUserEvent2 = slackUserEvent;
	 * 
	 * new Thread(() -> { slackUserEventService.save(slackUserEvent2); }).start();
	 * 
	 * }
	 */
	@Override
	public String addEventAction(String eventCode, String actionCode, String actionDesc, int actionPoints) {
		Event event = this.getEventByName(eventCode);

		if (event == null)
			return "Event does not exist.";

		try {
/*
			QuotesMatcher qm = new QuotesMatcher();
			String description = "";
			if (qm.isCorrect(actionDesc))
				description = qm.getQuotesArgument(actionDesc);
			else
				return "Description must be between quotes.";
				*/

			Action action = new Action(actionCode, actionDesc, actionPoints);
			action.setEvent(event);
			if (event.getEventScores().stream().filter(a -> a.getCode().equals(actionCode)).count() != 0)// check
																											// actionCode
																											// isUnique
																											// in an
																											// event
				return "The action " + actionCode + " already exists in this event! ";
			else
				actionService.save(action);

		} catch (NumberFormatException e) {
			return "fx_event_action_add failed. Please, check the arguments types.";// + timer;
		}

		return "The action named " + actionCode + " has been successfully added to the event " + eventCode + ".";

	}

	/*
	 * @Override public String joinEvent(String userId, String eventName) {
	 * 
	 * SlackUser user = slackUserService.get(userId);
	 * 
	 * Event event = this.getEventByName(eventName);
	 * 
	 * if (event == null) return "Nonexistent event";// + timer;
	 * 
	 * // TODO // if (!isEventManager(eventManagerId, eventName) &&
	 * !isAdmin(eventManagerId)) // return "/fx_event_score_add " + eventName + "\n"
	 * + "You are neither admin nor // a event manager !" + timer;
	 * 
	 * SlackUserEvent slackUserEvent =
	 * slackUserEventService.getSlackUserEvent(event, user);
	 * 
	 * if (slackUserEvent != null) { return
	 * "You have already participated to this event " + eventName; } else {
	 * slackUserEvent = new SlackUserEvent(event, user);
	 * 
	 * }
	 * 
	 * try { slackUserEventService.save(slackUserEvent); } catch (Exception e) {
	 * return "/fx_event_join " + eventName + " \n" + "A problem has occured!"; }
	 * 
	 * return "You are now a member of this event " + eventName; }
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
			return "The event " + eventCode + " does not exist.";
		if (action == null)
			return "The action " + actionCode + " does not exist.";
		if (slackuser == null)
			return "The user " + slackuserName + " does not exist.";
		if (event.getEventScores().stream().filter(a -> a.getCode().equals(action.getCode())).count() == 0)
			return "The action " + actionCode + " does not exist in the event " + eventCode + ".";

		if (slackuser.getActions() == null)
			slackuser.setActions(new HashSet<>());

		slackuser.getActions().add(action);
		slackUserService.save(slackuser);

		return "The action named " + actionCode + " has been successfully performed by " + slackuserName + " ! ";
	}

	public String removeEventAction(String currentUserId, String eventCode, String actionCode) {
		Event event = this.getEventByName(eventCode);

		if (event == null)
			return "Event does not exist ";

		try {
			if (event.getEventManagers().stream().filter(m -> m.getSlackUserId().equals(currentUserId)).count() != 0) {
				Action action = event.getEventScores().stream().filter(a -> a.getCode().equals(actionCode)).findFirst().orElse(null);// actionService.getActionByCode(actionCode);
				if (action == null)
					return "The action " + actionCode + " doesn't exist for the event " + eventCode + " ! ";
				else {
					for (SlackUser user : action.getSlackUsers()) {
						user.setActions(user.getActions().stream().filter(a -> a.getId() != action.getId())
								.collect(Collectors.toSet()));
						slackUserService.save(user);
					}
					actionService.remove(action);
				}
			}
			else
				return "You are not a manager of the event " + eventCode;

		} catch (NumberFormatException e) {
			return "fx_action_remove failed. Please, check the arguments types. ";
		}

		return "The action named " + actionCode + " has been successfully removed ! ";

	}
	
	public String removeEvent(String currentUserId, String eventCode) {
		SlackUser slackuser = slackUserService.get(currentUserId);
		Event event = this.getEventByName(eventCode);
		
		if(event == null)
			return "Event does not exist.";
		
		try {
			if(event.getEventManagers().stream().
				filter(m->m.getSlackUserId().equals(currentUserId)).count()==0) 
				return "You are not a manager of the event "+event.getName()+ ". You can't remove it !";
			else {
				for(Action action : event.getEventScores()) {
					for(SlackUser user: action.getSlackUsers()) {
						user.setActions(user.getActions().stream().
								filter(a->a.getId() != action.getId() ).collect(Collectors.toSet()));
						slackUserService.save(user);
					}
					actionService.remove(action);
				}
				
				for(SlackUser user : event.getEventManagers()) {
					event.getEventManagers().remove(user);
					this.save(event);
				}
				this.remove(event);
			}
		} catch (NumberFormatException e) {
			return "fx_event_remove failed. Please, check the arguments types. ";// + timer;
		}
		return "The event " + eventCode + " has been succesfully removed.";
		
	}
	

	public String addEvent(String[] command, JsonNode jsonNode) {
		if (this.getEventByName(command[1]) != null)
			return "The event " + command[2] + " already exists ! ";
		else {
			new Thread(() -> {
				SlackUser user = slackUserService.get(jsonNode.get("user").asText().trim());
				Event event = new Event(command[1], command[2], command[3]);
				event.getEventManagers().add(user);
				this.save(event);
			}).start();
			return " The event named " + command[1] + " has been successfully added ! You can now manage it.";
		}
	}

	@Override
	public String addManager(String currentUserId, String userId, String eventName) {
		Event event = this.getEventByName(eventName);
		if (event == null)
			return "The event " + eventName + " does not exists ! ";
		else {
			if (event.getEventManagers().stream().filter(m -> m.getSlackUserId().equals(currentUserId)).count() != 0) {
				OneUsernameArgumentMatcher um = new OneUsernameArgumentMatcher();
				if (um.isCorrect(userId)) {
					String slackUserId = um.getUserIdArgument(userId);
					if (event.getEventManagers().stream().filter(m -> m.getSlackUserId().equals(slackUserId))
							.count() == 0) {
						event.getEventManagers().add(slackUserService.get(slackUserId));
						this.save(event);
						return "The user " + userId + " is now a manager of the event " + eventName;
					} else
						return "The user " + userId + " is already a manager of the event " + eventName;

				}
				return "Username " + userId + " incorrect !";
			}
			return "You are not a manager of the event " + eventName;
		}
	}

	@Override
	public String removeManager(String currentUserId, String userId, String eventName) {
		Event event = this.getEventByName(eventName);
		if (event == null)
			return "The event " + eventName + " does not exists ! ";
		else {
			if (event.getEventManagers().stream().filter(m -> m.getSlackUserId().equals(currentUserId)).count() != 0) {
				OneUsernameArgumentMatcher um = new OneUsernameArgumentMatcher();
				if (um.isCorrect(userId)) {
					String slackUserId = um.getUserIdArgument(userId);
					if (event.getEventManagers().stream().filter(m -> m.getSlackUserId().equals(slackUserId))
							.count() != 0) {
						event.setEventManagers(event.getEventManagers().stream()
								.filter(m -> !m.getSlackUserId().equals(slackUserId)).collect(Collectors.toSet()));
						this.save(event);
						return "The user " + userId + " is no longer a manager of the event " + eventName;
					} else
						return "The user " + userId + " is not a manager of the event " + eventName;

				}
				return "Username " + userId + " incorrect !";
			}
			return "You are not a manager of the event " + eventName;
		}
	}

}
