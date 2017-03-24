package com.finaxys.slackbot.WebServices;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finaxys.slackbot.DAL.Message;
import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.DAL.Role;
import com.finaxys.slackbot.Utilities.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class BaseWebService {
    @Autowired
    public Repository<Role, Integer> roleRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    public boolean isAdmin(String userId) {
        List<Role> roles = roleRepository.getByCriterion("role", "admin");
        for (Role role : roles)
            if (role.getFinaxysProfile().getId().equals(userId))
                return true;
        return false;
    }

    public boolean NoAccess(String appVerificationToken, String slackTeam) {
        if (appVerificationToken.equals(Settings.appVerificationToken) && slackTeam.equals(Settings.slackTeam))
            return false;
        return true;
    }

    public ResponseEntity<JsonNode> NoAccessResponseEntity(String appVerificationToken, String slackTeam) {
        if (!appVerificationToken.equals(Settings.appVerificationToken))
            return NewResponseEntity("Wrong app verification token !");
        if (!slackTeam.equals(Settings.slackTeam))
            return NewResponseEntity("Only for Finaxys members !");
        return null;
    }

    public ResponseEntity<JsonNode> NewResponseEntity(Message message) {
        return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
    }

    public ResponseEntity<JsonNode> NewResponseEntity(String message) {
        return NewResponseEntity(new Message(message));
    }
}
