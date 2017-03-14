package com.finaxys.slackbot.Configuration.Classes;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@EnableWebMvc
@Configuration
@ComponentScan(basePackages={"com.finaxys.slackbot.*"})
public class WebMvcConfig  extends WebMvcConfigurerAdapter{

}