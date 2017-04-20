package com.finaxys.slackbot.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.DAL.Parameter;
import com.finaxys.slackbot.interfaces.ParameterService;

@Service
public class ParameterServiceImpl implements ParameterService {

	@Autowired
	Repository<Parameter, String> parameters;

	@Override
	public Parameter get(String id) {

		Parameter param = parameters.findById(id);
		if (param != null)
			return param;
		param = new Parameter();
		param.setName(id);
		parameters.saveOrUpdate(param);
		return param;
	}

	@Override
	public Parameter save(Parameter parameter) {
		parameters.saveOrUpdate(parameter);
		return parameter;
	}

	@Override
	public void remove(Parameter parameter) {
		parameters.delete(parameter);
	}

	@Override
	public List<Parameter> getAll() {
		return parameters.getAll();
	}

	@Override
	public String getAllAsLines() {
		List<Parameter> l = getAll();
		String result = "";
		
		for (Parameter parameter : l) {
			result += parameter.getName() + "\t:  " + parameter.getValue() + "\n";
		}
		
		return result;
		
	}

}
