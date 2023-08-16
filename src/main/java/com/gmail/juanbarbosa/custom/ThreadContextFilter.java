package com.gmail.juanbarbosa.custom;

import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThreadContextFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        storeDomainContext(request);
        determineCookieSupport(request);
        
        filterChain.doFilter(request, response);
    }

	private void determineCookieSupport(HttpServletRequest request) {
		// Check if the cookie exists
        Cookie[] cookies = request.getCookies();
        boolean hasCookie = false;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("cookie_test".equals(cookie.getName())) {
                    hasCookie = true;
                    break;
                }
            }
        }

        // Add logic to handle the absence of the cookie
        if (!hasCookie) {
        	// TODO set warning message or something
            // You can set a warning message or redirect to a specific page
            // response.sendRedirect("/cookie-warning");
        }
	}

	private void storeDomainContext(HttpServletRequest request) {
		String domain = request.getServerName();
        log.info("Determined domain: " + domain);
        if (domain != null) {
            ThreadContext.set(ThreadContext.DOMAIN, domain);
        }
	}

}
