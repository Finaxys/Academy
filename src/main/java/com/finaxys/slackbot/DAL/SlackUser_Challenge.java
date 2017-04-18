package com.finaxys.slackbot.DAL;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "FINAXYS_PROFILE_CHALLENGE")
public class SlackUser_Challenge implements Serializable {

    private SlackUser_Challenge_PK key;
    private int score;
    private Challenge challenge;
    private SlackUser profile;

    public SlackUser_Challenge() {
    }

    public SlackUser_Challenge(int score, Integer challengeId, String finaxysProfileId,Challenge challenge,SlackUser finaxysProfile) {
        this.score = score;
        this.key = new SlackUser_Challenge_PK();
        key.setChallenge(challengeId);
        key.setFinaxysProfile(finaxysProfileId);
        this.challenge = challenge ;
        this.profile=finaxysProfile;

    }
    public SlackUser_Challenge(int score, Integer challengeId, String finaxysProfileId) {
        this.score = score;
        this.key = new SlackUser_Challenge_PK();
        key.setChallenge(challengeId);
        key.setFinaxysProfile(finaxysProfileId);
    }

    @EmbeddedId
    public SlackUser_Challenge_PK getKey() {
        return key;
    }

    public void setKey(SlackUser_Challenge_PK key) {
        this.key = key;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @JoinColumn(name = "CHALLENGE_ID" , insertable = false, updatable = false)
    @ManyToOne
    public Challenge getChallenge() {
        return challenge;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

    @JoinColumn(name = "FINAXYSPROFILE_ID", insertable = false, updatable = false)
    @ManyToOne
    public SlackUser getProfile() {
        return profile;
    }

    public void setProfile(SlackUser finaxysProfile) {
        this.profile = finaxysProfile;
    }
}
