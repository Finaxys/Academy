package com.finaxys.slackbot.Domains;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by Sahar on 22/03/2017.
 */
@Entity
@Table(name = "SCORE_GRID")
public class ActionScore implements Serializable{
    private String code ;
    private String action;
    private int points;

    public ActionScore() {}

    public ActionScore(String code, String action, int points) {
        this.code = code;
        this.action = action;
        this.points = points;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Id
    public String getCode() {
        return code;
    }

    public String getAction() {
        return action;
    }

    public int getPoints() {
        return points;
    }
}
