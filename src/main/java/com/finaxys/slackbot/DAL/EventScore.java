package com.finaxys.slackbot.DAL;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "EVENT_SCORE")
public class EventScore implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer code;
	private String 	action;
	private int 	points;

	public EventScore() {
	}

	public EventScore(String event, int score) {
		action=event;
		points=score;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}
}
