package com.finaxys.slackbot.Domains;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by inesnefoussi on 3/6/17.
 */
@Entity
@Table(name = "FINAXYS_PROFILE_CHALLENGE")
public class FinaxysProfile_Challenge implements Serializable {

    private FinaxysProfile_Challenge_PK key;
    private int score;
    private Challenge challenge;
    private FinaxysProfile finaxysProfile;

    public FinaxysProfile_Challenge() {
    }

    public FinaxysProfile_Challenge(int score, Challenge challenge, FinaxysProfile finaxysProfile) {
        this.score = score;
        this.key = new FinaxysProfile_Challenge_PK();
        key.setChallenge(challenge.getId());
        key.setFinaxysProfile(finaxysProfile.getId());
    }

    @EmbeddedId
    public FinaxysProfile_Challenge_PK getKey() {
        return key;
    }

    public void setKey(FinaxysProfile_Challenge_PK key) {
        this.key = key;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @JoinColumn(name = "CHALLENGE_FK" , insertable = false , updatable = false)
    @ManyToOne
    public Challenge getChallenge() {
        return challenge;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

    @JoinColumn(name = "FINAXYSPROFILE_FK" , insertable = false , updatable = false)
    @ManyToOne
    public FinaxysProfile getFinaxysProfile() {
        return finaxysProfile;
    }

    public void setFinaxysProfile(FinaxysProfile finaxysProfile) {
        this.finaxysProfile = finaxysProfile;
    }
}
