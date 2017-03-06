package com.finaxys.slackbot.configuration;

import com.finaxys.slackbot.DAL.GenericRepository;
import com.finaxys.slackbot.DAL.GenericRepositoryImpl;
import com.finaxys.slackbot.domains.Challenge;
import com.finaxys.slackbot.domains.FinaxysProfile;
import com.finaxys.slackbot.domains.FinaxysProfile_Challenge;
import com.finaxys.slackbot.domains.FinaxysProfile_Challenge_PK;
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

/**
 * Created by inesnefoussi on 3/6/17.
 */
@Configuration
@EnableTransactionManagement
@ComponentScan({ "com.finaxys.slackbot.domains" })
@PropertySource(value = "classpath:dataSourceInformation.properties")
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
        sessionFactory.setPackagesToScan(new String[] { "com.finaxys.slackbot.*" });
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
    public GenericRepository<FinaxysProfile,String> myGenericRepo1() {
        return new GenericRepositoryImpl<FinaxysProfile, String>(FinaxysProfile.class);
    }

    @Bean
    public GenericRepository<Challenge,Integer> myGenericRepo2() {
        return new GenericRepositoryImpl<Challenge, Integer>(Challenge.class);
    }

    @Bean
    public GenericRepository<FinaxysProfile_Challenge,FinaxysProfile_Challenge_PK> myGenericRepo3() {
        return new GenericRepositoryImpl<FinaxysProfile_Challenge, FinaxysProfile_Challenge_PK>(FinaxysProfile_Challenge.class);
    }


}
