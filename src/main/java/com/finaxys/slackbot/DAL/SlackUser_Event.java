package com.finaxys.slackbot.DAL;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@IdClass(SlackUser_Event_PK.class)
@Table(name = "SLACK_USER_EVENT")
public class SlackUser_Event implements Serializable {

	@Id private Integer eventIdpk;
	public String getSlackUserIdpk() {
		return slackUserIdpk;
	}

	public void setSlackUserIdpk(String slackUserIdpk) {
		this.slackUserIdpk = slackUserIdpk;
	}

	public Integer getEventIdpk() {
		return eventIdpk;
	}

	public void setEventIdpk(Integer eventIdpk) {
		this.eventIdpk = eventIdpk;
	}

	@Id private String slackUserIdpk;
    private int score;
    private Event event;
    private SlackUser slackUser;

    public SlackUser_Event() {
    }

    public SlackUser_Event(int score, Integer eventId, String slackUserId,Event event, SlackUser slackUser) {
        this.score = score;
        this.eventIdpk=eventId;
        this.slackUserIdpk=slackUserId;
        this.event = event;
        this.slackUser=slackUser;
    }
    
    public SlackUser_Event(int score, Integer eventId, String slackUserId) {
        this.score = score;
        this.eventIdpk=eventId;
        this.slackUserIdpk=slackUserId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @ManyToOne
    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @ManyToOne
    public SlackUser getSlackUser() {
        return slackUser;
    }

    public void setSlackUser(SlackUser slackUser) {
        this.slackUser = slackUser;
    }
}
