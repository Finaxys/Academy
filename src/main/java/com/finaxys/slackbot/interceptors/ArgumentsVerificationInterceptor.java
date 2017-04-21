package com.finaxys.slackbot.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.finaxys.slackbot.Utilities.ArgumentsVerifier;

public class ArgumentsVerificationInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		ArgumentsVerifier verifier = new ArgumentsVerifier();

		String arguments = request.getParameter("text");
		String URL = request.getRequestURI();
		String command = "/" + URL.split("/")[URL.split("/").length - 1];

		System.out.println("==================================================================================");
		System.out.println(arguments);
		System.out.println(URL);
		System.out.println(command);

		if (!verifier.Verify(arguments, command)) {
			response.getWriter()
					.write(new ResponseEntity("The arguments didn't match the pattern : \n" + "It should have been : "
							+ verifier.commandPatternMap.get(command).toString(), HttpStatus.BAD_REQUEST).toString());
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