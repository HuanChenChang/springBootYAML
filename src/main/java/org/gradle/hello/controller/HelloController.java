package org.gradle.hello.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {
	
	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	public @ResponseBody String hello() {
		return "Hello World!!!";
	}
	
	@RequestMapping(value = "/hello/{name}", method = RequestMethod.GET)
	public @ResponseBody String helloByName( @PathVariable("name") String name) {
		return "Hello "+name+" !!!";
	}
}
