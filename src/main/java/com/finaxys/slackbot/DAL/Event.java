package com.finaxys.slackbot.DAL;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "EVENT")
public class Event implements Serializable {

    private Integer id;
    private String 	name;
    private String 	description;
    private Date 	creationDate;
    private String 	type;
    private Set<SlackUserEvent> attendees;

    public Event() {
        creationDate = new Date();
        attendees = new HashSet<>();
    }    

    public Event(String name, String description, String type) {
		super();
		attendees = new HashSet<>();
		this.name = name;
		this.description = description;
		this.creationDate = new Date();
		this.type = type;
	}

	@Id
    @Column(name = "EVENT_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getEventId() {
        return id;
    }

    public void setEventId(Integer id) {
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
    @OneToMany(mappedBy = "event", cascade = CascadeType.REMOVE , fetch = FetchType.EAGER)
    public Set<SlackUserEvent> getAttendees() {
        return attendees;
    }

    public void setAttendees(Set<SlackUserEvent> attendees) {
        this.attendees = attendees;
    }
}
