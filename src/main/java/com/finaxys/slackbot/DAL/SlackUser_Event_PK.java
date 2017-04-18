package com.finaxys.slackbot.DAL;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class SlackUser_Event_PK implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer event;
	private String slackUser;

	public SlackUser_Event_PK() {

	}

	@Column(name = "EVENT_ID")
	public Integer getEvent() {
		return event;
	}

	public void setEvent(Integer event) {
		this.event = event;
	}

	@Column(name = "SLACK_USER_ID")
	public String getSlackUser() {
		return slackUser;
	}

	public void setSlackUser(String slackUser) {
		this.slackUser = slackUser;
	}
}
