package com.finaxys.slackbot.RestServices;

import com.finaxys.slackbot.BUL.Interfaces.SlackBotCommandService;
import com.finaxys.slackbot.Domains.FinaxysProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/finaxysProfiles")
public class FinaxysProfileWebService {

    @Autowired
    SlackBotCommandService slackBotCommandServiceImpl;

    @RequestMapping(value = "/listAll", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<List<FinaxysProfile>> listAllUsers() {
        System.out.println();
        List<FinaxysProfile> users = slackBotCommandServiceImpl.listerUsers();
        return new ResponseEntity<List<FinaxysProfile>>(users, HttpStatus.OK);
    }

    @RequestMapping(value = "/scores", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<List<FinaxysProfile>> listScores(@RequestParam("number") int number) {
        System.out.println("test");
        List<FinaxysProfile> users = slackBotCommandServiceImpl.listeScores(number);
        return new ResponseEntity<List<FinaxysProfile>>(users, HttpStatus.OK);
    }



}
