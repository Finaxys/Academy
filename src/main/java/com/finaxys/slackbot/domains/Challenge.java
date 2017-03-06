package com.finaxys.slackbot.domains;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by inesnefoussi on 3/6/17.
 */
@Entity
public class Challenge implements Serializable{

    private Integer id;
    private String name;
    private String description;
    private FinaxysProfile creator;
    private List<FinaxysProfile_Challenge> participants;

    @Id
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

    public FinaxysProfile getCreator() {
        return creator;
    }

    public void setCreator(FinaxysProfile creator) {
        this.creator = creator;
    }

    @OneToMany(mappedBy = "challenge" , cascade = CascadeType.REMOVE)
    public List<FinaxysProfile_Challenge> getParticipants() {
        return participants;
    }

    public void setParticipants(List<FinaxysProfile_Challenge> participants) {
        this.participants = participants;
    }
}
