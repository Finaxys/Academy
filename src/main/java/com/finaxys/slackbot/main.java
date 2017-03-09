package com.finaxys.slackbot;

import com.finaxys.slackbot.DAL.Interfaces.GenericRepository;
import com.finaxys.slackbot.Configuration.Classes.SpringContext;
import com.finaxys.slackbot.Domains.Challenge;
import com.finaxys.slackbot.Domains.FinaxysProfile;
import com.finaxys.slackbot.Domains.FinaxysProfile_Challenge;
import com.finaxys.slackbot.Domains.FinaxysProfile_Challenge_PK;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

/**
 * Created by inesnefoussi on 3/6/17.
 */
public class main {

    public static void main(String... args) {

        AbstractApplicationContext context = new AnnotationConfigApplicationContext(SpringContext.class);

        FinaxysProfile profile1 = new FinaxysProfile();
        profile1.setId("U123451555");
        profile1.setScore(10);

        FinaxysProfile profile2 = new FinaxysProfile();
        profile2.setId("U12347555");
        profile2.setScore(10);

        Challenge challenge1 = new Challenge();
        challenge1.setId(12348555);
        challenge1.setName("Challenge 1");

        Challenge challenge2 = new Challenge();
        challenge2.setId(12347555);
        challenge2.setName("Challenge 2");

        FinaxysProfile_Challenge finaxysProfile_challenge1 = new FinaxysProfile_Challenge(10,challenge1,profile1);
        FinaxysProfile_Challenge finaxysProfile_challenge2 = new FinaxysProfile_Challenge(20,challenge1,profile2);
        FinaxysProfile_Challenge finaxysProfile_challenge3 = new FinaxysProfile_Challenge(5,challenge2,profile2);

        GenericRepository<FinaxysProfile,String> myGenericRepo1 = (GenericRepository<FinaxysProfile,String>) context.getBean("myGenericRepo1") ;

        myGenericRepo1.addEntity(profile1);
        myGenericRepo1.addEntity(profile2);


        GenericRepository<Challenge,Integer> myGenericRepo2 = (GenericRepository<Challenge, Integer>) context.getBean("myGenericRepo2") ;


        myGenericRepo2.addEntity(challenge1);
        myGenericRepo2.addEntity(challenge2);

        GenericRepository<FinaxysProfile_Challenge,FinaxysProfile_Challenge_PK> myGenericRepo3 = (GenericRepository<FinaxysProfile_Challenge, FinaxysProfile_Challenge_PK>) context.getBean("myGenericRepo3") ;
        myGenericRepo3.addEntity(finaxysProfile_challenge1);
        myGenericRepo3.addEntity(finaxysProfile_challenge2);
        myGenericRepo3.addEntity(finaxysProfile_challenge3);
    }
}
