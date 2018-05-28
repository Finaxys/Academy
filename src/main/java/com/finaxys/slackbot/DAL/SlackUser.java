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
    private int 	isAdmin;

    
    //private Set<SlackUserEvent> slackUserEvents;
    private Set<Action> actions ;
    private Set<Event> eventsManaging;
    public SlackUser() {
        this.score = 0;
        this.isAdmin = 0;
    }

    public SlackUser(String userId, String userName) {
        this.score 	= 0;
        this.slackUserId 	= userId;
        this.name 	= userName;
        actions = new HashSet<>();
        eventsManaging = new HashSet<>();
        this.isAdmin = 0;
        
    }

    public SlackUser(String userId, String userName,int score) {
        this.slackUserId 	= userId;
        this.name 	= userName;
        this.score 	= score;
        actions = new HashSet<>();
        this.isAdmin = 0;
    }

    public SlackUser(User user) {
    	slackUserId=user.getId();
    	name=user.getName();
    	actions = new HashSet<>();
    	this.isAdmin = 0;
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
    

    @Column(name = "IS_ADMIN")
    public int getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(int isAdmin) {
		this.isAdmin = isAdmin;
	}

	/*
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "slackUser", cascade = CascadeType.ALL)
    public Set<SlackUserEvent> getSlackUserEvents() {
        return slackUserEvents;
    }

    public void setSlackUserEvents(Set<SlackUserEvent> slackUserEvents) {
        this.slackUserEvents = slackUserEvents;
    }
    */
    
    @ManyToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL )
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
    
    public boolean equals (SlackUser slackUser) {
    	System.out.println(slackUser.getSlackUserId() + "  Compare to  " + this.getSlackUserId());
    	return this.slackUserId.equals(slackUser.getSlackUserId());
    }
    
    public int calculateScore(Event event) {
    	return actions.stream().filter(x->x.getEvent().equals(event)).mapToInt(x->x.getPoints()).sum();
    }
    
    public int calculateScore() {
    	return actions.stream().mapToInt(x->x.getPoints()).sum();   	
    }

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "eventManagers")
	public Set<Event> getEventsManaging() {
		return eventsManaging;
	}

	public void setEventsManaging(Set<Event> eventsManaging) {
		this.eventsManaging = eventsManaging;
	}
    
    
    
    
}
