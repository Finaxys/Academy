package com.finaxys.slackbot.DAL;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "SLACK_USER_EVENT")
public class SlackUserEvent implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int score;
    private Event event;
    private SlackUser slackUser;

    public SlackUserEvent() {
    }

    public SlackUserEvent(int score, Event event, SlackUser slackUser) {
        this.score = score;
        this.event = event;
        this.slackUser=slackUser;
    }
    

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @ManyToOne
    @Id
    @JoinColumn(name = "EVENT_ID", nullable=false)
    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @ManyToOne
    @Id
    @JoinColumn(name = "SLACK_USER_ID", nullable=false)
    public SlackUser getSlackUser() {
        return slackUser;
    }

    public void setSlackUser(SlackUser slackUser) {
        this.slackUser = slackUser;
    }
}
