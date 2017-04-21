package com.finaxys.slackbot.services;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.DAL.Role;
import com.finaxys.slackbot.DAL.SlackUser;
import com.finaxys.slackbot.interfaces.RoleService;
import com.finaxys.slackbot.interfaces.SlackUserService;

@Service
public class SlackUserServiceImpl implements SlackUserService {

	@Autowired
	Repository<SlackUser, String> users;

	@Autowired
	RoleService roles;

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

	@Override
	public boolean isAdmin(String id) {
		Iterator<Role> it = users.findById(id).getRoles().iterator();
		while(it.hasNext()){
			Role r = it.next();
			if (r.getRole().equals("admin"))
				return true;
		}
		return false;
	}

	@Override
	public List<SlackUser> getAllOrderedByScore(int size) {
		return users.getAllOrderedByAsList("score", false, size);
	}	
}
