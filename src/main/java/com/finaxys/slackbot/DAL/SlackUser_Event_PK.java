package com.finaxys.slackbot.DAL;

import java.io.Serializable;

import javax.persistence.Id;

public class SlackUser_Event_PK implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer eventId;
	private String slackUserId;

	public SlackUser_Event_PK() {

	}

	public Integer getEvent() {
		return eventId;
	}

	public void setEvent(Integer event) {
		this.eventId = event;
	}

	public String getSlackUser() {
		return slackUserId;
	}

	public void setSlackUser(String slackUser) {
		this.slackUserId = slackUser;
	}
}
