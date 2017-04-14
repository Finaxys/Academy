package com.finaxys.slackbot.DAL;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "FINAXYS_PROFILE_CHALLENGE")
public class FinaxysProfile_Challenge implements Serializable {

    private FinaxysProfile_Challenge_PK key;
    private int score;
    private Challenge challenge;
    private FinaxysProfile finaxysProfile;

    public FinaxysProfile_Challenge() {
    }

    public FinaxysProfile_Challenge(int score, Integer challengeId, String finaxysProfileId,Challenge challenge,FinaxysProfile finaxysProfile) {
        this.score = score;
        this.key = new FinaxysProfile_Challenge_PK();
        key.setChallenge(challengeId);
        key.setFinaxysProfile(finaxysProfileId);
        this.challenge = challenge ;
        this.finaxysProfile=finaxysProfile;

    }
    public FinaxysProfile_Challenge(int score, Integer challengeId, String finaxysProfileId) {
        this.score = score;
        this.key = new FinaxysProfile_Challenge_PK();
        key.setChallenge(challengeId);
        key.setFinaxysProfile(finaxysProfileId);
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

    @JoinColumn(name = "CHALLENGE_FK", insertable = true, updatable = false)
    @ManyToOne
    public Challenge getChallenge() {
        return challenge;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

    @JoinColumn(name = "FINAXYSPROFILE_FK", insertable = true, updatable = false)
    @ManyToOne
    public FinaxysProfile getFinaxysProfile() {
        return finaxysProfile;
    }

    public void setFinaxysProfile(FinaxysProfile finaxysProfile) {
        this.finaxysProfile = finaxysProfile;
    }
}
