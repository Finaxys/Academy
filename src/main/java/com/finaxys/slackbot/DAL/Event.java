package com.finaxys.slackbot.DAL;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "EVENT")
public class Event implements Serializable {

    private Integer eventId;
    private String 	name;
    private String 	description;
    private Date 	creationDate;
    private String 	type;
    private Set<SlackUserEvent> attendees;
    public Set<Action> eventScores;

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
        return eventId;
    }

    public void setEventId(Integer id) {
        this.eventId = id;
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
    
    @ManyToMany
    @JoinTable(
    	      name="EVENT_ACTION",
    	      joinColumns=@JoinColumn(name="EVENT_ID", referencedColumnName="EVENT_ID"),
    	      inverseJoinColumns=@JoinColumn(name="ACTION_ID", referencedColumnName="ACTION_ID"))
    
    public Set<Action> getEventScores() {
		return eventScores;
	}

	public void setEventScores(Set<Action> eventScores) {
		this.eventScores = eventScores;
	}

	public String toString() {
    	return "Event name: "
				+ this.getName() 
				+ ", number of participants: " 
				+ this.getAttendees().size() 
				+ " \n ";
    }
    
    public boolean equals (Event event) {
    	return event.getEventId()==this.getEventId();
    }
}
