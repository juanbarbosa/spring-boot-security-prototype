package com.gmail.juanbarbosa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class JwtDemo {

	@GetMapping(value= {"secured/**"})
	public ModelAndView jwttTest(HttpServletRequest  request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("jwt-demo");
		mv.addObject("message", getCookieString(request));
		
		return mv;
	}
	
	@PostMapping(value= {"/jwt-demo"})
	public ModelAndView jwttTestReadCookieToSeeJwt(HttpServletRequest  request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("jwt-demo");
		mv.addObject("message", getCookieString(request));
		
		return mv;
	}
	
	public String getCookieString(HttpServletRequest  request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
            StringBuilder cookieInfo = new StringBuilder();
            for (Cookie cookie : cookies) {
                cookieInfo.append("Name: ").append(cookie.getName())
                        .append(", Value: ").append(cookie.getValue())
                        .append("\n");
            }
            return cookieInfo.toString();
        }
		return "No cookies set";
	}
}
