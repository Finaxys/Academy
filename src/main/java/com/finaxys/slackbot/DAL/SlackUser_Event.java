package com.finaxys.slackbot.DAL;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "SLACK_USER_EVENT")
public class SlackUser_Event implements Serializable {

    private int score;
    private Event event;
    private SlackUser slackUser;

    public SlackUser_Event() {
    }

    public SlackUser_Event(int score, Integer eventId, String slackUserId,Event event, SlackUser slackUser) {
        this.score = score;
        this.event = event;
        this.slackUser=slackUser;
    }
    
    public SlackUser_Event(int score, Integer eventId, String slackUserId) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @ManyToOne
    @Id
    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @ManyToOne
    @Id
    public SlackUser getSlackUser() {
        return slackUser;
    }

    public void setSlackUser(SlackUser slackUser) {
        this.slackUser = slackUser;
    }
}
