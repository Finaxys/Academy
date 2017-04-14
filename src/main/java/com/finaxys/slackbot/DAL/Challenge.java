package com.finaxys.slackbot.DAL;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "CHALLENGE")
public class Challenge implements Serializable {

    private Integer id;
    private String 	name;
    private String 	description;
    private Date 	creationDate;
    private String 	type;
    private List<FinaxysProfile_Challenge> participants;

    public Challenge() {
        creationDate = new Date();
        participants = new ArrayList<>();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Temporal(TemporalType.DATE)
    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) { this.creationDate = creationDate; }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "challenge", cascade = CascadeType.REMOVE , fetch = FetchType.EAGER)
    public List<FinaxysProfile_Challenge> getParticipants() {
        return participants;
    }

    public void setParticipants(List<FinaxysProfile_Challenge> participants) {
        this.participants = participants;
    }
}
