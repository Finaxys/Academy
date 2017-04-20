package com.finaxys.slackbot.interfaces;

import java.util.List;

import com.finaxys.slackbot.DAL.SlackUser;

public interface SlackUserService {

	SlackUser get(String id);
	
	SlackUser save(SlackUser user);
	
	void remove(SlackUser user);
	
	
	List<SlackUser> getAll();
	
	default void update(SlackUser user){
		
	}

}
