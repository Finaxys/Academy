package com.finaxys.slackbot.DAL;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "FINAXYS_PROFILE")
public class SlackUser implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String 	id;
    private String 	name;
    private int 	score;

    private Set<Role> roles;
    
    private Set<SlackUser_Challenge> finaxysProfile_challenges;

    public SlackUser() {
        this.score = 0;
    }

    public SlackUser(String userId, String userName) {
        this.score 	= 0;
        this.id 	= userId;
        this.name 	= userName;
    }

    public SlackUser(String userId, String userName, boolean isChallengeManager, int score) {
        this.id 	= userId;
        this.name 	= userName;
        this.score 	= score;
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

    @Transient
    public boolean isAdministrator() {
        return roles.stream().filter(r->r.getRole().equals("admin")).count() > 0;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "profile", cascade = CascadeType.ALL)
    public Set<SlackUser_Challenge> getFinaxysProfile_challenges() {
        return finaxysProfile_challenges;
    }

    public void setFinaxysProfile_challenges(Set<SlackUser_Challenge> finaxysProfile_challenges) {
        this.finaxysProfile_challenges = finaxysProfile_challenges;
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
