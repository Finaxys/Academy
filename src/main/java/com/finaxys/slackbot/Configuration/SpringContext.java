package com.finaxys.slackbot.Configuration;

import com.finaxys.slackbot.BUL.Classes.*;
import com.finaxys.slackbot.BUL.Interfaces.*;
import com.finaxys.slackbot.BUL.Listeners.MessageListener;
import com.finaxys.slackbot.DAL.*;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration

@EnableTransactionManagement
@ComponentScan({"com.finaxys.slackbot.*"})
@PropertySource(value = "file:/site/wwwroot/bin/apache-tomcat-8.0.41/dataSourceInformation.properties")
public class SpringContext {

    @Autowired
    private Environment environment;

    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", environment.getRequiredProperty("hibernate.dialect"));
        properties.put("hibernate.show_sql", environment.getRequiredProperty("hibernate.show_sql"));
        properties.put("hibernate.format_sql", environment.getRequiredProperty("hibernate.format_sql"));
        properties.put("hibernate.hbm2ddl.auto", environment.getRequiredProperty("hibernate.hbm2ddl.auto"));
        return properties;
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getRequiredProperty("jdbc.driverClassName"));
        dataSource.setUrl(environment.getRequiredProperty("jdbc.url"));
        dataSource.setUsername(environment.getRequiredProperty("jdbc.username"));
        dataSource.setPassword(environment.getRequiredProperty("jdbc.password"));
        return dataSource;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan(new String[]{"com.finaxys.slackbot.*"});
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    @Bean
    @Autowired
    public HibernateTransactionManager transactionManager(SessionFactory s) {
        HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(s);
        return txManager;
    }

    @Bean
    public InnovateService innovateService() {
        return new InnovateServiceImpl();
    }

    @Bean
    public MessageListener messageListener() {
        return new MessageListener();
    }

    @Bean
    public Repository<FinaxysProfile, String> finaxysProfileRepository() {
        return new Repository<>(FinaxysProfile.class);
    }
    @Bean
    public Repository<Role, Integer> roleRepository() {
        return new Repository<>(Role.class);
    }

    @Bean
    public SlackBotCommandService slackBotCommandServiceImpl() {

        return new SlackBotCommandServiceImpl();
    }

    @Bean
    public ReactionAddedService reactionAddedService() {
        return new ReactionAddedServiceImpl();
    }

    @Bean
    public ReactionRemovedService reactionRemovedService() {
        return new ReactionRemovedServiceImpl();
    }

    @Bean
    public RealMessageReward realMessageReward() {
        return new RealMessageRewardImpl();
    }

    @Bean
    public ChannelLeftService channelLeftService() {
        return new ChannelLeftServiceImpl();
    }

    @Bean
    public NewTribeJoinedService newTribeJoinedService() {
        return new NewTribeJoinedServiceImpl();
    }

    @Bean
    public Repository<Challenge, Integer> challengeRepository() {
        return new Repository<>(Challenge.class);
    }

    @Bean
    public Repository<FinaxysProfile_Challenge, FinaxysProfile_Challenge_PK> finaxysProfileChallengeRepository() {
        return new Repository<>(FinaxysProfile_Challenge.class);
    }

    @Bean
    public Repository<EventScore, String> eventScoreRepository() {
        return new Repository<>(EventScore.class);
    }
    
    @Bean
    public com.finaxys.slackbot.BUL.Listeners.UserChangedListener userChangedListener() {
        return new com.finaxys.slackbot.BUL.Listeners.UserChangedListener();
    }

    @Bean
    public com.finaxys.slackbot.BUL.Listeners.ChannelCreatedListener channelCreatedListener() {
        return new com.finaxys.slackbot.BUL.Listeners.ChannelCreatedListener();
    }


    @Bean
    public com.finaxys.slackbot.BUL.Listeners.ReactionRemovedListener reactionRemovedListener() {
        return new com.finaxys.slackbot.BUL.Listeners.ReactionRemovedListener();
    }

    @Bean
    public com.finaxys.slackbot.BUL.Listeners.ReactionAddedListener reactionAddedListener() {
        return new com.finaxys.slackbot.BUL.Listeners.ReactionAddedListener();
    }

}