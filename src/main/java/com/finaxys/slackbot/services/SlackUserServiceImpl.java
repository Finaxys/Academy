package com.finaxys.slackbot.services;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finaxys.slackbot.BUL.Classes.SlackApiAccessService;
import com.finaxys.slackbot.BUL.Matchers.OneUsernameArgumentMatcher;
import com.finaxys.slackbot.DAL.Action;
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

		return "<@" + this.get(id).getSlackUserId() + "> is an administrator now !";
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
	public String removeAdmin(String userId) {
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
		// user.incrementScore(score);
		users.saveOrUpdate(user);
	}

	@Override
	public String displayScoreUser(String userId) {
		OneUsernameArgumentMatcher um = new OneUsernameArgumentMatcher();
		 
		if (um.isCorrect(userId)) {
			String slackUserId = um.getUserIdArgument(userId);
			SlackUser user = this.get(slackUserId);
			if (user == null)
				return "The user " + userId + " does not exist.";
			else {
				StringBuilder sb = new StringBuilder(userId + "*'s score in details* \n\n");
				Map<String, Integer> usersScores = new HashMap<String, Integer>();
				for (SlackUser slackUser : this.getAll()) {
					if (slackUser.getActions() == null) {
						slackUser.setActions(new HashSet<>());
					}
					if (slackUser.getActions().size() != 0)
						usersScores.put(slackUser.getSlackUserId(), slackUser.calculateScore());
				}
				Map<String, Integer> streamMap =
						usersScores.entrySet().stream()
					       .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
					       .collect(Collectors.toMap(
					          Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
				
				sb.append("*Position is:* " + (new ArrayList<String>(streamMap.keySet()).indexOf(slackUserId) + 1) + "\n");				
				sb.append("*Score is:* " + user.calculateScore() + "\n\n");
				sb.append("*Events :*\n");
				
				events.getAll().stream()
				.forEach(event -> {
					int score = user.calculateScore(event);
					if (score != 0) {
						sb.append("*" + event.getName() + ":*\n");
						sb.append("Score : " + score);
						sb.append("\nActions List: \n");
						user.getActions().stream().filter(x->x.getEvent().equals(event))
						.forEach(action -> {
							sb.append("\t *" + action.getCode() + " : " + action.getDescription() + "\n");
						});
						sb.append("****************\n");
					}							
				});
				
				return sb.toString();
			}
		} else
			return "Error : the username " + userId + " is incorrect !";

	}

	@Override
	public String listAllAdmins() {
			StringBuilder sb = new StringBuilder("Admins List:\n");
			this.getAll().stream().filter(u -> u.getIsAdmin() == 1)
					.forEach(u -> sb.append("<@" + u.getSlackUserId() + ">\n"));
			return sb.toString();		
	}

}
