package com.finaxys.slackbot.interfaces;

import java.util.List;

import com.finaxys.slackbot.DAL.Parameter;

public interface ParameterService {

	Parameter get(String id);

	Parameter save(Parameter user);

	void remove(Parameter user);

	List<Parameter> getAll();

	default void update(Parameter user) {

	}

	String getAllAsLines();

}
