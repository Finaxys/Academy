package com.finaxys.slackbot.Domains;

import allbegray.slack.type.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by inesnefoussi on 3/6/17.
 */
@Entity
@Table(name = "FINAXYS_PROFILE")
public class FinaxysProfile extends User implements Serializable{
    private String id;
    private int score;
    private boolean challengeManager;
    private List<FinaxysProfile_Challenge> finaxysProfile_challenges;

    public FinaxysProfile() {
    }
    public FinaxysProfile(String userId ,boolean isChallengeManager ,int score)
    {
    	this.id = userId ;
    	this.score =score;
    	this.challengeManager =isChallengeManager;
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

    @OneToMany(mappedBy = "finaxysProfile" , cascade = CascadeType.REMOVE)
    public List<FinaxysProfile_Challenge> getFinaxysProfile_challenges() {
        return finaxysProfile_challenges;
    }

    public void setFinaxysProfile_challenges(List<FinaxysProfile_Challenge> finaxysProfile_challenges) {
        this.finaxysProfile_challenges = finaxysProfile_challenges;
    }
    
    
}
