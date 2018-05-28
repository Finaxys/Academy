package com.finaxys.slackbot.Configuration;

import java.util.Properties;


import javax.sql.DataSource;

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

import com.finaxys.slackbot.BUL.Classes.ChannelLeftServiceImpl;
import com.finaxys.slackbot.BUL.Classes.InnovateServiceImpl;
import com.finaxys.slackbot.BUL.Classes.NewTribeJoinedServiceImpl;
import com.finaxys.slackbot.BUL.Classes.ReactionAddedServiceImpl;
import com.finaxys.slackbot.BUL.Classes.ReactionRemovedServiceImpl;
import com.finaxys.slackbot.BUL.Classes.RealMessageRewardImpl;
import com.finaxys.slackbot.BUL.Interfaces.ChannelLeftService;
import com.finaxys.slackbot.BUL.Interfaces.InnovateService;
import com.finaxys.slackbot.BUL.Interfaces.NewTribeJoinedService;
import com.finaxys.slackbot.BUL.Interfaces.ReactionAddedService;
import com.finaxys.slackbot.BUL.Interfaces.ReactionRemovedService;
import com.finaxys.slackbot.BUL.Interfaces.RealMessageReward;
import com.finaxys.slackbot.BUL.Listeners.MessageListener;
import com.finaxys.slackbot.DAL.Action;
import com.finaxys.slackbot.DAL.DebugMode;
import com.finaxys.slackbot.DAL.Event;
import com.finaxys.slackbot.DAL.Parameter;
import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.DAL.SlackUser;

@Configuration
@EnableTransactionManagement
@ComponentScan({"com.finaxys.slackbot.*"})
@PropertySource(value = "file:${catalina.home}/dataSourceInformationPostgres.properties")
public class SpringContext {

    @Autowired
    private Environment environment;
    
    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect"		,environment.getRequiredProperty("hibernate.dialect"));
        properties.put("hibernate.show_sql"		,environment.getRequiredProperty("hibernate.show_sql"));
        properties.put("hibernate.format_sql"	,environment.getRequiredProperty("hibernate.format_sql"));
        properties.put("hibernate.hbm2ddl.auto"	,environment.getRequiredProperty("hibernate.hbm2ddl.auto"));
        return properties;
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName	(environment.getRequiredProperty("jdbc.driverClassName"));
        dataSource.setUrl				(environment.getRequiredProperty("jdbc.url"));
        dataSource.setUsername			(environment.getRequiredProperty("jdbc.username"));
        dataSource.setPassword			(environment.getRequiredProperty("jdbc.password"));
        return dataSource;
    }
    
    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());sessionFactory.setPackagesToScan(new String[]{"com.finaxys.slackbot.*"});
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
    public Repository<SlackUser, String> finaxysProfileRepository() {
        return new Repository<>(SlackUser.class);
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
    public Repository<Event, Integer> eventRepository() {
        return new Repository<>(Event.class);
    }
    
    @Bean
    public Repository<Parameter, String> parameterRepository() {
        return new Repository<>(Parameter.class);
    }
    
    
    @Bean
    public Repository<Action, Long> actionRepository() {
        return new Repository<>(Action.class);
    }
    
    @Bean
    public Repository<DebugMode, Integer> debugmodeRepository() {
        return new Repository<>(DebugMode.class);
    }
    
    
    @Bean
    public com.finaxys.slackbot.BUL.Listeners.UserChangeListener userChangedListener() {
        return new com.finaxys.slackbot.BUL.Listeners.UserChangeListener();
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