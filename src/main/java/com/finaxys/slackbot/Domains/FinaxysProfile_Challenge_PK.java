package com.finaxys.slackbot.Domains;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by inesnefoussi on 3/6/17.
 */
@Embeddable
public class FinaxysProfile_Challenge_PK implements Serializable{

    private Integer challenge;
    private String finaxysProfile;

    public FinaxysProfile_Challenge_PK() {
    }

    @Column(name = "CHALLENGE_ID")
    public Integer getChallenge() {
        return challenge;
    }

    public void setChallenge(Integer challenge) {
        this.challenge = challenge;
    }

    @Column(name = "FINAXYSPROFILE_ID")
    public String getFinaxysProfile() {
        return finaxysProfile;
    }

    public void setFinaxysProfile(String finaxysProfile) {
        this.finaxysProfile = finaxysProfile;
    }
}
