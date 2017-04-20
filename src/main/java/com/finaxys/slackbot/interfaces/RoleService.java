package com.finaxys.slackbot.interfaces;

import java.util.List;

import com.finaxys.slackbot.DAL.Role;

public interface RoleService {

	Role get(String id);
	
	Role save(Role role);
	
	void remove(Role role);
	
	
	List<Role> getAll();
	
	default void update(Role role){
		
	}

}
