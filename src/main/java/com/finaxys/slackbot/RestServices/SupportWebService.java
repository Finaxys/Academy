package com.finaxys.slackbot.RestServices;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finaxys.slackbot.Domains.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by inesnefoussi on 3/17/17.
 */
@RestController
@RequestMapping("/help")
public class SupportWebService {
    private List<String> commandsNames;
    private List<String> commandsArgumets;
    private List<String> commandsDescription;
    private ObjectMapper objectMapper;

    public SupportWebService() {
        commandsNames = new ArrayList<>();
        commandsArgumets = new ArrayList<>();
        commandsDescription = new ArrayList<>();
        objectMapper = new ObjectMapper();
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<JsonNode> getHelp(@RequestParam("token") String token,
                                            @RequestParam("team_domain") String teamDomain) {
        Message message = new Message("");
        try {
            FileReader namesFileReader = new FileReader("src/main/resources/listOfCommandsNames.txt");
            FileReader argumentsFileReader = new FileReader("src/main/resources/listOfCommandsArguments.txt");
            FileReader descriptionsFileReader = new FileReader("src/main/resources/listOfCommandsDescriptions.txt");

            BufferedReader namesBufferedReader = new BufferedReader(namesFileReader);
            BufferedReader argumentsBufferedReader = new BufferedReader(argumentsFileReader);
            BufferedReader descriptionsBufferedReader = new BufferedReader(descriptionsFileReader);

            String currentCommand , currentCommandArguments, currentCommandDescription;
            String messageText = "";
            while ((currentCommand = namesBufferedReader.readLine()) != null &&
                    (currentCommandArguments = argumentsBufferedReader.readLine()) != null &&
                    (currentCommandDescription = descriptionsBufferedReader.readLine()) != null) {
                messageText += currentCommand+" \n"+currentCommandArguments+" \n"+currentCommandDescription+" \n";
                message = new Message(messageText);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity(objectMapper.convertValue(message, JsonNode.class), HttpStatus.OK);
    }
}
