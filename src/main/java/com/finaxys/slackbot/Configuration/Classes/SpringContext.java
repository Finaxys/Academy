package com.finaxys.slackbot.Configuration.Classes;

import java.util.Properties;

import javax.sql.DataSource;

import com.finaxys.slackbot.BUL.Classes.NewTributeJoinedServiceImpl;
import com.finaxys.slackbot.BUL.Interfaces.NewTributeJoinedService;
import com.finaxys.slackbot.DAL.Classes.Repository;
import com.finaxys.slackbot.Domains.FinaxysProfile;
import com.finaxys.slackbot.BUL.Listeners.MessageAppreciatedListener;
import com.finaxys.slackbot.BUL.Listeners.MessageListener;
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

import com.finaxys.slackbot.DAL.Classes.Repository;
import com.finaxys.slackbot.Domains.Challenge;
import com.finaxys.slackbot.Domains.FinaxysProfile;

/**
 * Created by inesnefoussi on 3/6/17.
 */
@Configuration
@EnableTransactionManagement
@ComponentScan({"com.finaxys.slackbot.*"})
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
	public Repository<FinaxysProfile, String> myGenericRepo1() {
		return new Repository<FinaxysProfile, String>(FinaxysProfile.class);
	}

	@Bean
	public Repository<Challenge, Integer> myGenericRepo2() {
		return new Repository<Challenge, Integer>(Challenge.class);
	}

	@Bean
	public MessageAppreciatedListener messageAppreciatedListener() {
		return new MessageAppreciatedListener();
	}

}
