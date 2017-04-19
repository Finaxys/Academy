package com.finaxys.slackbot.DAL;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@IdClass(SlackUser_Event_PK.class)
@Table(name = "SLACK_USER_EVENT")
public class SlackUser_Event implements Serializable {

	@Id private Integer eventIdpk;
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
