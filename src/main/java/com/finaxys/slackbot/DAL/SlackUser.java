package com.finaxys.slackbot.DAL;

import javax.persistence.*;

import allbegray.slack.type.User;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "SLACK_USER")
public class SlackUser implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String 	slackUserId;
    private String 	name;
    private int 	score;

    private Set<Role> roles = new HashSet<>();
    
    private Set<SlackUserEvent> slackUserEvents;
    public Set<Action> actions;

    public SlackUser() {
        this.score = 0;
    }

    public SlackUser(String userId, String userName) {
        this.score 	= 0;
        this.slackUserId 	= userId;
        this.name 	= userName;
    }

    public SlackUser(String userId, String userName,int score) {
        this.slackUserId 	= userId;
        this.name 	= userName;
        this.score 	= score;
    }

    public SlackUser(User user) {
    	slackUserId=user.getId();
    	name=user.getName();
	}

	@Id
    @Column(name = "SLACK_USER_ID")
    public String getSlackUserId() {
        return slackUserId;
    }

    public void setSlackUserId(String id) {
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
    
    @OneToMany(mappedBy = "slackUser", fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    public Set<Role> getRoles() {
        return roles;
    }
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
    	      name="SLACK_USER_ACTION",
    	      joinColumns=@JoinColumn(name="SLACK_USER_ID", referencedColumnName="SLACK_USER_ID"),
    	      inverseJoinColumns=@JoinColumn(name="ACTION_ID", referencedColumnName="ACTION_ID"))
    public Set<Action> getActions() {
    	return actions;
    }
    
    public void setActions(Set<Action> actions) {
    	this.actions = actions;
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
    
    public boolean equals (SlackUser slackUser) {
    	System.out.println(slackUser.getSlackUserId() + "  Compare to  " + this.getSlackUserId());
    	return this.slackUserId.equals(slackUser.getSlackUserId());
    }
}
