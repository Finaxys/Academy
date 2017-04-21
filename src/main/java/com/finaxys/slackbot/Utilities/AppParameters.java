package com.finaxys.slackbot.Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finaxys.slackbot.DAL.Parameter;
import com.finaxys.slackbot.DAL.Repository;

@Service
public class AppParameters {

	private static Map<String, Parameter> parameters;

	private Repository<Parameter, String> parametersRepository;

	@Autowired
	public void setParametersRepository(Repository<Parameter, String> parametersRepository) {
		this.parametersRepository = parametersRepository;
		AppParameters.parameters = new HashMap<>();
		for (Parameter param : parametersRepository.getAll()) {
			parameters.put(param.getName(), param);
		}
	}

	public Parameter get(String id) {
		Parameter param = parameters.get(id.toUpperCase());
		
		if(param != null)
			return param;
		
		
		param = new Parameter();
		param.setName(id);
		
		parametersRepository.saveOrUpdate(param);
		parameters.put(param.getName(), param);
		
		return param;
	}

	public Parameter save(Parameter parameter) {
		parameters.put(parameter.getName(), parameter);
		parametersRepository.saveOrUpdate(parameter);
		return parameter;
	}

	public void remove(Parameter parameter) {
		parametersRepository.delete(parameter);
		parameters.remove(parameter.getName());
	}

	public List<Parameter> getAll() {
		List<Parameter> result = new ArrayList<>();
		for (Parameter parameter : parameters.values()) {
			result.add(parameter);
		}
		return result;
	}

	public String getAllAsLines() {
		List<Parameter> l = getAll();
		String result = "";
		for (Parameter parameter : l) {
			result += parameter.getName() + "\t:  " + parameter.getValue() + "\n";
		}
		return result;
	}

	public static String getValue(String paramName) {
		Parameter param = parameters.get(paramName.toUpperCase());
		if(param == null)
			return "";
		return parameters.get(paramName.toUpperCase()).getValue();
	}

}
