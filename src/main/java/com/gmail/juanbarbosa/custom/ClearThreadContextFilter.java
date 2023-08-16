package com.gmail.juanbarbosa.custom;

import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClearThreadContextFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } finally {
        	log.info("###### ClearThreadContextFilter cleanup");
        	clearDomainContext();
        }
    }
    
	private void clearDomainContext() {
		ThreadContext.clear(ThreadContext.DOMAIN);
	}
}

