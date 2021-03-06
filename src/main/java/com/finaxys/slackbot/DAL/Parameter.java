package com.finaxys.slackbot.DAL;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Parameter implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String name;
	private String value;

	public Parameter() {
		// TODO Auto-generated constructor stub
	}
	
	
	
	public Parameter(String name, String value) {
		super();
		setName(name);
		setValue(value);
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name.toUpperCase();
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value.toUpperCase();
	}

}
