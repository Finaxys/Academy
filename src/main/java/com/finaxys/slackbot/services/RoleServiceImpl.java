package com.finaxys.slackbot.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finaxys.slackbot.DAL.Event;
import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.DAL.Role;
import com.finaxys.slackbot.interfaces.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	Repository<Role, Integer> roles;
	
	@Autowired
	Repository<Event, Integer> eventRepository;

	@Override
	public Role get(Integer id) {
		return roles.findById(id);
	}

	@Override
	public Role save(Role role) {
		roles.saveOrUpdate(role);
		return role;
	}

	@Override
	public void remove(Role role) {
		roles.delete(role);
	}

	@Override
	public List<Role> getAll() {
		return roles.getAll();
	}

	@Override
	public List<Role> getAllAdmins() {
		return roles.getByCriterion("role", "admin");
	}
	
	@Override
	public List<Role> getAllByEvent(Event event) {
		return roles.getByCriterion("eventId", event);
		 	
	}

	@Override
	public List<Role> getAllManagers() {
		return roles.getByCriterion("role","event_manager");
	}
}
