package com.finaxys.slackbot.WebServices;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {	
	
	@RequestMapping("/test")
	public void test() {
	
	}
	
	@RequestMapping(value = "/test1", method = RequestMethod.GET)
	public String testFunc() {
		return "helloo";
	}
	
}
