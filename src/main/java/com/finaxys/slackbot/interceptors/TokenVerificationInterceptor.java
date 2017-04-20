package com.finaxys.slackbot.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.Utilities.Settings;

public class TokenVerificationInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		String token = request.getParameter("token");
		String slackTeam = request.getParameter("team_domain");

		if (token == null || !token.equals(Settings.appVerificationToken)) {
			response.getWriter().write(new ResponseEntity("Wrong app verification token !", HttpStatus.BAD_REQUEST).toString());
			return false;
		}

		if (slackTeam == null || !slackTeam.equals(Settings.slackTeam)) {
			response.getWriter().write(new ResponseEntity("Only for Finaxys members !", HttpStatus.BAD_REQUEST).toString());
			return false;
		}

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

}