package com.finaxys.slackbot.DAL;

import javax.persistence.*;

@Entity
@Table(name = "EVENT_SCORE")
public class EventScore {
    private Integer code;
    private String 	action;
    private int 	points;

    public EventScore(Integer code, String action, int points) {
        this.code = code;
        this.action = action;
        this.points = points;
    }

    public EventScore(String action, int points) {
        this.action = action;
        this.points = points;
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
