package com.finaxys.slackbot.DAL;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name = "ROLE")
public class Role implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int id ;
    private String role ;
    
    private Event event;
    private SlackUser slackUser ;
    
    
    public Role() {
    }

    public Role(String role){
    	this.role = role;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROLE_ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
    
    @ManyToOne(fetch = FetchType.LAZY)
    public SlackUser getSlackUser() {
        return slackUser;
    }

    public void setSlackUser(SlackUser slackUser) {
        this.slackUser = slackUser;
    }
}
