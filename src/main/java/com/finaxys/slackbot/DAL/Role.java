package com.finaxys.slackbot.DAL;

import java.io.Serializable;

import javax.persistence.*;

@Entity
public class Role implements Serializable{

	private static final long serialVersionUID = 1L;
	
	
	private Integer id ;
    private String role ;
    private Challenge challenge;
    private FinaxysProfile slackUser ;
    
    
    public Role() {
    }

    
    public Role(String role){
    	this.role = role;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROLE_ID")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public Challenge getChallenge() {
        return challenge;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }
    
    @ManyToOne(fetch = FetchType.LAZY)
    public FinaxysProfile getSlackUser() {
        return slackUser;
    }

    public void setSlackUser(FinaxysProfile finaxysProfile) {
        this.slackUser = finaxysProfile;
    }
}
