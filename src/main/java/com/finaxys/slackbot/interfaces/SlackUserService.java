package com.finaxys.slackbot.interfaces;

import java.util.List;

import com.finaxys.slackbot.DAL.SlackUser;

public interface SlackUserService {

	SlackUser get(String id);
	
	SlackUser save(SlackUser user);
	
	void remove(SlackUser user);
	
	List<SlackUser> getAll();

	boolean isAdmin(String id);
	String setUserAsAdmin(String id);
	String addUserAsAdmin(String currentUserId, String userId);
	String setCurrentUserAsAdmin(String id, String password);
	List<SlackUser> getAllOrderedByScore(int size);

	boolean isEventManager(String id, String eventName);
	
	void updateScore(String id, int score);

}
