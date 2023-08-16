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
        
        filterChain.doFilter(request, response);
    }

	private void storeDomainContext(HttpServletRequest request) {
		String domain = request.getServerName();
        log.info("Determined domain: " + domain);
        if (domain != null) {
            ThreadContext.set(ThreadContext.DOMAIN, domain);
        }
	}

}
