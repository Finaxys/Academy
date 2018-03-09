package com.finaxys.slackbot.services;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finaxys.slackbot.BUL.Classes.SlackApiAccessService;
import com.finaxys.slackbot.DAL.Event;
import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.DAL.Role;
import com.finaxys.slackbot.DAL.SlackUser;
import com.finaxys.slackbot.interfaces.RoleService;
import com.finaxys.slackbot.interfaces.SlackUserService;

@Service
public class SlackUserServiceImpl implements SlackUserService {

	Repository<SlackUser, String> users;

	@Autowired
	public void setUsers(Repository<SlackUser, String> users) {
		users.getAll();
		this.users = users;
	}
	
	Repository<Event, Integer> events;

	@Autowired
	public void setEvents(Repository<Event, Integer> events) {
		this.events = events;
	}


	@Autowired
	RoleService roles;

	@Autowired
	SlackApiAccessService slackApiAccessService;

	@Override
	public SlackUser get(String id) {
		SlackUser user = users.findById(id);
		
		if (user == null) {

			user = new SlackUser(slackApiAccessService.getUser(id));
			this.save(user);
		}

		return user;
	}

	@Override
	public SlackUser save(SlackUser user) {
		users.saveOrUpdate(user);
		return user;
	}

	@Override
	public void remove(SlackUser user) {
		users.delete(user);
	}

	@Override
	public List<SlackUser> getAll() {
		return users.getAll();
	}

	@Override
	public boolean isAdmin(String id) {
		SlackUser user = users.findById(id);

		if (user == null)
			return false;

		Iterator<Role> it = user.getRoles().iterator();
		while (it.hasNext()) {
			Role r = it.next();
			if (r.getRole().equals("admin"))
				return true;
		}
		return false;
	}

	@Override
	public boolean isEventManager(String id, String eventName) {
		SlackUser user = users.findById(id);

		if (user == null)
			return false;
		List<Event> list = events.getByCriterion("name", eventName);
		
		if(list.size() <= 0)
			return false;
		
		Integer eventId = list.get(0).getEventId();
		
		Iterator<Role> it = user.getRoles().iterator();
		while (it.hasNext()) {
			Role r = it.next();
			if (r.getRole().equals("event_manager") && r.getEvent().getEventId().equals(eventId))
				return true;
		}
		return false;
	}

	@Override
	public List<SlackUser> getAllOrderedByScore(int size) {
		return users.getAllOrderedByAsList("score", false, size);
	}
}
