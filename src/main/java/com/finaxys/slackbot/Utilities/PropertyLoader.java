package com.finaxys.slackbot.Utilities;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Created by Bannou on 07/03/2017.
 */
public class PropertyLoader {
    public static Properties loadSlackBotProperties(){
        Path credentialFilePath = Paths.get("src/main/resources/credentials.properties");
        Properties properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream(credentialFilePath.toString())) {
            properties.load(fileInputStream);
        }
        catch (Exception e){
            System.out.print(e.getMessage());
        }finally {
            return (properties);
        }
    }
}
