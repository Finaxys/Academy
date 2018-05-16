package com.finaxys.slackbot.DAL;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ACTION")
public class Action implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private String code;
	private String description;
	private int points;

	public Action() {
	}

	public Action(String event, int score) {
		description = event;
		points = score;
	}

	public Action(String actionCode, String actionDesc, int points) {
		super();
		this.description = actionDesc;
		this.points = points;
		this.code = actionCode;
	}
		
	@Id
	@Column(name="ACTION_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}	
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}
}
