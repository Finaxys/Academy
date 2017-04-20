package com.finaxys.slackbot.DAL;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "SLACK_USER")
public class SlackUser implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String 	slackUserId;
    private String 	name;
    private int 	score;

    private Set<Role> roles;
    
    private Set<SlackUserEvent> slackUserEvents;

    public SlackUser() {
        this.score = 0;
    }

    public SlackUser(String userId, String userName) {
        this.score 	= 0;
        this.slackUserId 	= userId;
        this.name 	= userName;
    }

    public SlackUser(String userId, String userName, boolean isChallengeManager, int score) {
        this.slackUserId 	= userId;
        this.name 	= userName;
        this.score 	= score;
    }

    @Id
    @Column(name = "SLACK_USER_ID")
    public String getSlackUserId() {
        return slackUserId;
    }

    public void setslackUserId(String id) {
        this.slackUserId = id;
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

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "slackUser", cascade = CascadeType.ALL)
    public Set<SlackUserEvent> getSlackUserEvents() {
        return slackUserEvents;
    }

    public void setSlackUserEvents(Set<SlackUserEvent> slackUserEvents) {
        this.slackUserEvents = slackUserEvents;
    }
    
    @OneToMany(mappedBy = "slackUser", fetch = FetchType.EAGER)
    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void incrementScore(int score) {
        this.score += score;
    }

    public void decrementScore(int score) {
        this.score -= score;
    }
}
