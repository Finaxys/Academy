package com.finaxys.slackbot.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.DAL.SlackUser;
import com.finaxys.slackbot.services.SlackUserService;

@Service
public class SlackUserServiceImpl implements SlackUserService {

	@Autowired
	Repository<SlackUser, String> users;

	@Override
	public SlackUser get(String id) {
		return users.findById(id);
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

}
