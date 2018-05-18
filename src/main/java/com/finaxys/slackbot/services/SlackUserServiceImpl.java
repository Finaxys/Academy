package com.finaxys.slackbot.services;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finaxys.slackbot.BUL.Classes.SlackApiAccessService;
import com.finaxys.slackbot.BUL.Matchers.OneUsernameArgumentMatcher;
import com.finaxys.slackbot.DAL.Event;
import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.DAL.Role;
import com.finaxys.slackbot.DAL.SlackUser;
import com.finaxys.slackbot.Utilities.ArgumentsSplitter;
import com.finaxys.slackbot.Utilities.Settings;
import com.finaxys.slackbot.Utilities.SlackBot;
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

		return user.getIsAdmin() == 1;
		/*
		Iterator<Role> it = user.getRoles().iterator();
		while (it.hasNext()) {
			Role r = it.next();
			if (r.getRole().equals("admin"))
				return true;
		}
		
		return false;
		*/
	}
	
	

	@Override
	public String setUserAsAdmin(String id) {
		SlackUser user = users.findById(id);

		if (user == null) {
			user = new SlackUser(slackApiAccessService.getUser(id));
			this.save(user);
		}
		
		user.setIsAdmin(1);
		this.save(user);
		return "User is admin now !";
	}
	
	

	@Override
	public String setCurrentUserAsAdmin(String id, String password) {
		if (password.equals(Settings.adminPass)) {
			if(!this.isAdmin(id)) {
				setUserAsAdmin(id);
				return "You are adminstrator now !";
			}
			else {
				return "You are already an administrator!";
			}
		}
		else 
			return "Wrong password";
	}

	public String addUserAsAdmin(String currentUserId, String userId) {
		if (!this.isAdmin(currentUserId) )
			return "You are not an admin! You need to be an administrator to use fxadmin_add";
		
		OneUsernameArgumentMatcher um = new OneUsernameArgumentMatcher();
		String slackUserId = "";
		if (um.isCorrect(userId))
			slackUserId = um.getUserIdArgument(userId);
		else
			return "Username incorrect";
		
		if (!this.isAdmin(slackUserId) ) {
			return this.setUserAsAdmin(slackUserId);			
		}
		else
			return "The user you want to add is already an administrator!";
			
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

	@Override
	public void updateScore(String id, int score) {
		SlackUser user = users.findById(id);
		user.incrementScore(score);
		users.saveOrUpdate(user);
	}
		
	
}
