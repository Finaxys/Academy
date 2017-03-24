package com.finaxys.slackbot.WebServices;

import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.DAL.Message;
import com.finaxys.slackbot.Utilities.Log;
import com.finaxys.slackbot.Utilities.Settings;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fx_help")
public class SupportWebService extends BaseWebService {
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<JsonNode> fx_help(@RequestParam("appVerificationToken") String appVerificationToken,
                                            @RequestParam("slackTeam") String slackTeam,
                                            @RequestParam("userId") String userId) {
        if (NoAccess(appVerificationToken, slackTeam))
            return NoAccessResponseEntity(appVerificationToken, slackTeam);

        String fxCommands =
                "*/fx_challenge_list* \n List all the challenges. \n" +
                        "*/fx_challenge_add* [challenge name],[group|individual],[description] \n Add a new challenge. \n" +
                        "*/fx_manager_add* [challenge name] @username  \n Add a challenge manager. \n" +
                        "*/fx_manager_list* [challenge name] \n List challenge managers . \n" +
                        "*/fxadmin_list* \n List all administrators. \n" +
                        "*/fx_challenge_named* [challenge name] \n Get a challenge details. \n" +
                        "*/fx_challenges_by_date* [yyyy-mm-dd] \n Get challenges by date \n" +
                        "*/fx_challenges_by_type* [group|individual] \n Get challenges by type \n" +
                        "*/fx_challenge_score_list*  [challengeName] challenge \n Gets the score list of a given challenge. \n" +
                        "*/fx_leaderboard* [optional: count] \nGets the top scores. \n" +
                        "*/fxmanager_challenge_score_add* @username [points] points [name] challenge \n Add a score to a challenge participant. \n" +
                        "*/fxmanager_del @username* \n delete a challenge manager. \n" +
                        "*/fx_event_add [event]* [points earned] \n add an event.";

        String fxAdminCommands =
                "*/fxadmin_add* @username \n Add an administrator. \n" +
                        "*/fxadmin_del* @username \n Delete an administrator. \n" +
                        "*/fxadmin_manager_challenge_del* \n Delete a challenge!";

        String message = "/fx_help\nList of the FX bot commands:\n " + fxCommands + (isAdmin(userId) ? " \n " + fxAdminCommands : "");
        Log.info(message);
        return NewResponseEntity(message);
    }
}
