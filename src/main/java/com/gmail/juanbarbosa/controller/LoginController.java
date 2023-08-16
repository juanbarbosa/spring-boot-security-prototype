package com.gmail.juanbarbosa.controller;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Profile("!base")
@Controller
public class LoginController {
	@GetMapping(value= {"/login"})
	public String login(HttpServletRequest  request, HttpServletResponse response) {
		
		return "login-test";
	}
	
	@GetMapping(value= {"/logout"})
	public String logout(HttpServletRequest  request, HttpServletResponse response) {
		
		return "logout-test";
	}
}
