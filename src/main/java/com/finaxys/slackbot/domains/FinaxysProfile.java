package com.finaxys.slackbot.domains;

import allbegray.slack.type.User;

import javax.persistence.*;

/**
 * Created by inesnefoussi on 3/6/17.
 */
@Entity
@Table(name = "FINAXYS_PROFILE")
public class FinaxysProfile extends User{
    private String id;
    private int score;
    private boolean challengeManager;

    public FinaxysProfile() {
    }

    @Override
    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isChallengeManager() {
        return challengeManager;
    }

    public void setChallengeManager(boolean challengeManager) {
        this.challengeManager = challengeManager;
    }
}
