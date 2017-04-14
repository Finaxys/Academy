package com.finaxys.slackbot.DAL;

import javax.persistence.*;

@Entity
public class Role {
    private Integer id ;
    private String 	role ;
    private int 	challengeId ;
    private FinaxysProfile finaxysProfile ;

    public Role() {
    }

    public Role(String role) {
        this.role = role;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROLE_ID", unique = true, nullable = false)
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

    public int getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(int challengeId) {
        this.challengeId = challengeId;
    }
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FINAXYSPROFILE_ID", nullable = false)
    public FinaxysProfile getFinaxysProfile() {
        return finaxysProfile;
    }

    public void setFinaxysProfile(FinaxysProfile finaxysProfile) {
        this.finaxysProfile = finaxysProfile;
    }
}
