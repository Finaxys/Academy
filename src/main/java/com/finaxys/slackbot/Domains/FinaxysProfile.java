package com.finaxys.slackbot.Domains;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by inesnefoussi on 3/6/17.
 */
@Entity
@Table(name = "FINAXYS_PROFILE")
public class FinaxysProfile implements Serializable {
    private String id;
    private String name;
    private int score;
    private boolean challengeManager;
    private boolean administrator;
    @JsonIgnore
    private List<FinaxysProfile_Challenge> finaxysProfile_challenges;

    public FinaxysProfile() {
        this.score = 0;
    }

    public FinaxysProfile(String userId, String userName) {
        this.score = 0;
        this.id = userId;
        this.name = userName;
    }

    public FinaxysProfile(String userId, String userName, boolean isChallengeManager, int score) {
        this.id = userId;
        this.name = userName;
        this.score = score;
        this.challengeManager = isChallengeManager;
    }

    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public boolean isAdministrator() {
        return administrator;
    }

    public void setAdministrator(boolean administrator) {
        this.administrator = administrator;
    }

    @OneToMany(mappedBy = "finaxysProfile", cascade = CascadeType.REMOVE)
    public List<FinaxysProfile_Challenge> getFinaxysProfile_challenges() {
        return finaxysProfile_challenges;
    }

    public void setFinaxysProfile_challenges(List<FinaxysProfile_Challenge> finaxysProfile_challenges) {
        this.finaxysProfile_challenges = finaxysProfile_challenges;
    }

    public void incrementScore(int score) {
        this.score += score;
    }

    public void decrementScore(int score) {
        this.score -= score;
    }


}
