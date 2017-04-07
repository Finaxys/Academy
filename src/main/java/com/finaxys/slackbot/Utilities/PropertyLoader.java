package com.finaxys.slackbot.Utilities;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Properties;

/**
 * Created by Bannou on 07/03/2017.
 */
public class PropertyLoader {
	
	
    public static Properties loadSlackBotProperties() {
    	
        Path credentialFilePath = Paths.get(".");
        File f = new File(".");
        System.out.println(Arrays.toString(f.list()));
        Properties properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream(credentialFilePath.toString())) {
            properties.load(fileInputStream);
        } catch (Exception e) {
            System.out.print(e.getMessage());
        } finally {
            return (properties);
        }
    }
}
