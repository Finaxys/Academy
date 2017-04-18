package com.finaxys.slackbot.DAL;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "SLACK_USER_EVENT")
public class SlackUser_Event implements Serializable {

    private SlackUser_Event_PK key;
    private int score;
    private Event event;
    private SlackUser slackUser;

    public SlackUser_Event() {
    }

    public SlackUser_Event(int score, Integer eventId, String slackUserId,Event event, SlackUser slackUser) {
        this.score = score;
        this.key = new SlackUser_Event_PK();
        key.setEvent(eventId);
        key.setSlackUser(slackUserId);
        this.event = event;
        this.slackUser=slackUser;

    }
    public SlackUser_Event(int score, Integer eventId, String slackUserId) {
        this.score = score;
        this.key = new SlackUser_Event_PK();
        key.setEvent(eventId);
        key.setSlackUser(slackUserId);
    }

    @EmbeddedId
    public SlackUser_Event_PK getKey() {
        return key;
    }

    public void setKey(SlackUser_Event_PK key) {
        this.key = key;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @JoinColumn(name = "EVENT_ID" , insertable = false, updatable = false)
    @ManyToOne
    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @JoinColumn(name = "SLACK_USER_ID", insertable = false, updatable = false)
    @ManyToOne
    public SlackUser getProfile() {
        return slackUser;
    }

    public void setProfile(SlackUser slackUser) {
        this.slackUser = slackUser;
    }
}
