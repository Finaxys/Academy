package com.finaxys.slackbot.Utilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;


import java.util.Properties;

/**
 * Created by Bannou on 07/03/2017.
 */
@PropertySource(value = "classpath:credentials.properties")
@Component
public class PropertyLoader {
    @Autowired
    private Environment environment;
    public  Properties loadSlackBotProperties() {
        Properties properties = new Properties();
        properties.put("token", environment.getRequiredProperty("token"));
        properties.put("verification_token", environment.getRequiredProperty("verification_token"));
        properties.put("finaxys_team_name", environment.getRequiredProperty("finaxys_team_name"));
        return properties;
    }
}
