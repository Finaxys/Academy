package com.finaxys.slackbot.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finaxys.slackbot.DAL.Role;
import com.finaxys.slackbot.interfaces.RoleService;
import com.finaxys.slackbot.DAL.Repository;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	Repository<Role, String> roles;

	@Override
	public Role get(String id) {
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

}
