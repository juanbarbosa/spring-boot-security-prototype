package com.gmail.juanbarbosa.custom;
//package com.codestewards.feprototype.login;
//
//import java.io.IOException;
//
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//public class TokenAuthenticationFilter extends OncePerRequestFilter {
//
//	@Override
//	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//			throws ServletException, IOException {
//        // Validate token and set authentication in SecurityContextHolder
//        // ...
//		log.info("###### TokenAuthenticationFilter");
//        filterChain.doFilter(request, response);
//
//	}
//
//}
