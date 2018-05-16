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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "EVENT")
public class Event implements Serializable {

    private Integer eventId;
    private String 	name;
    private String 	description;
    @Temporal(TemporalType.DATE)
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
    
    @OneToMany(mappedBy = "event", fetch = FetchType.EAGER,cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    public Set<Action> getEventScores() {
		return eventScores;
	}

	public void setEventScores(Set<Action> eventScores) {
		this.eventScores = eventScores;
	}
	
	public String displayActions() {
		StringBuilder sb = new StringBuilder();
		this.eventScores.forEach(x->sb.append("\t* "+ x.getCode() + " : " + x.getDescription() +"\n"));
		return sb.toString();
	}

	public String toString() {
    	return "Event name: " + this.getName() + "\n"
				+ "Description: " + this.getDescription() + "\n"
				+ "Type: " + this.getType() + "\n"
			    + "Creation date : " + this.getCreationDate() + "\n"
				+ "Number of participants: " + this.getAttendees().size() + " \n "
    			+ "Actions of the event : \n" + this.displayActions();
    } 
    
    public boolean equals (Event event) {
    	return event.getEventId()==this.getEventId();
    }
}
