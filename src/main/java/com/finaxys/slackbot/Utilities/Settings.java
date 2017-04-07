package com.finaxys.slackbot.Utilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@PropertySource(value = "file:/site/wwwroot/bin/apache-tomcat-8.0.41/credentials.properties")
@Component
public class Settings {
    private static Environment environment;
    @Autowired
    public void setEnvironment(Environment environment){this.environment=environment; load();}

    public static String appVerificationToken;
    public static String botUserOauthAccessToken;
    public static String slackTeam;
    public static String slackTeamId;
    public static String debugChannelId;

    private static void load() {
        appVerificationToken    = environment.getRequiredProperty("appVerificationToken");
        botUserOauthAccessToken = environment.getRequiredProperty("botUserOauthAccessToken");
        slackTeam               = environment.getRequiredProperty("slackTeam");
        slackTeamId             = environment.getRequiredProperty("slackTeamId");
        debugChannelId          = environment.getRequiredProperty("debugChannelId");
    }
}
