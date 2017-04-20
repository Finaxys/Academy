package com.finaxys.slackbot.Configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import com.finaxys.slackbot.interceptors.TokenVerificationInterceptor;

@EnableWebMvc
@Configuration
@ComponentScan(basePackages = { "com.finaxys.slackbot.*" })
public class WebMvcConfig extends WebMvcConfigurerAdapter {

	public WebMvcConfig() {

	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		super.addInterceptors(registry);
		registry.addInterceptor(new TokenVerificationInterceptor());
		}
}