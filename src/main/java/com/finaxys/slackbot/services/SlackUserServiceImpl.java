package com.finaxys.slackbot.services;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finaxys.slackbot.BUL.Classes.SlackApiAccessService;
import com.finaxys.slackbot.BUL.Matchers.OneUsernameArgumentMatcher;
import com.finaxys.slackbot.DAL.Event;
import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.DAL.SlackUser;
import com.finaxys.slackbot.Utilities.Settings;
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
		 * Iterator<Role> it = user.getRoles().iterator(); while (it.hasNext()) { Role r
		 * = it.next(); if (r.getRole().equals("admin")) return true; }
		 * 
		 * return false;
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

		return this.get(id).getName() + " is an administrator now !";
	}

	public String addUserAsAdmin(String currentUserId, String parameter) {

		OneUsernameArgumentMatcher um = new OneUsernameArgumentMatcher();
		String slackUserId = "";

		if (um.isCorrect(parameter)) {
			if (!this.isAdmin(currentUserId))
				return "You are not an admin! You need to be an administrator to add an administrator !";
			slackUserId = um.getUserIdArgument(parameter);
			if (!this.isAdmin(slackUserId)) {
				return this.setUserAsAdmin(slackUserId);
			} else
				return "The user " + parameter + " is already an administrator !";
		}

		else if (parameter.equals(Settings.adminPass)) {
			if (!this.isAdmin(currentUserId)) {
				setUserAsAdmin(currentUserId);
				return "You are adminstrator now !";
			} else {
				return "You are already an administrator !";
			}
		} else
			return "You entered a wrong password !";
	}

	@Override
	public String removeAdmin(String userId, String pass) {
		if (pass.equals(Settings.adminPass)) {
			OneUsernameArgumentMatcher um = new OneUsernameArgumentMatcher();
			String slackUserId = "";

			if (um.isCorrect(userId)) {
				slackUserId = um.getUserIdArgument(userId);
				if (!this.isAdmin(slackUserId)) {
					return "The user " + userId + " is not an administrator !";
				} else {
					SlackUser user = users.findById(slackUserId);
					if (user == null) {
						user = new SlackUser(slackApiAccessService.getUser(slackUserId));
						this.save(user);
					}
					user.setIsAdmin(0);
					this.save(user);

					return userId + " is no longer an administrator now !";
				}
			} else
				return "Error : the username " + userId + " is incorrect !";
		} else
			return "You entered a wrong password !";

	}

	@Override
	public boolean isEventManager(String id, String eventName) {
		SlackUser user = users.findById(id);

		if (user == null)
			return false;
		List<Event> list = events.getByCriterion("name", eventName);

		if (list.size() <= 0)
			return false;

		return false;
	}

	@Override
	public List<SlackUser> getAllOrderedByScore(int size) {
		return users.getAllOrderedByAsList("score", false, size);
	}

	@Override
	public void updateScore(String id, int score) {
		SlackUser user = users.findById(id);
		//user.incrementScore(score);
		users.saveOrUpdate(user);
	}

}
