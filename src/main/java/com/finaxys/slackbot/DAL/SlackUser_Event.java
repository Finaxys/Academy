package com.finaxys.slackbot.DAL;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@IdClass(SlackUser_Event_PK.class)
@Table(name = "SLACK_USER_EVENT")
public class SlackUser_Event implements Serializable {

	@Id private Integer eventId;
	@Id private String slackUserId;
    private int score;
    private Event event;
    private SlackUser slackUser;

    public SlackUser_Event() {
    }

    public SlackUser_Event(int score, Integer eventId, String slackUserId,Event event, SlackUser slackUser) {
        this.score = score;
        this.eventId=eventId;
        this.slackUserId=slackUserId;
        this.event = event;
        this.slackUser=slackUser;

    }
    public SlackUser_Event(int score, Integer eventId, String slackUserId) {
        this.score = score;
        this.eventId=eventId;
        this.slackUserId=slackUserId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @JoinColumn(name = "EVENT_ID" , insertable = true, updatable = false)
    @ManyToOne
    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @JoinColumn(name = "SLACK_USER_ID", insertable = true, updatable = false)
    @ManyToOne
    public SlackUser getSlackUser() {
        return slackUser;
    }

    public void setSlackUser(SlackUser slackUser) {
        this.slackUser = slackUser;
    }
}
