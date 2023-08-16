package com.gmail.juanbarbosa.custom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component @SuppressWarnings("deprecation") @Slf4j
public class MyUsernamePasswordAuthenticationProvider implements AuthenticationProvider {

	@Autowired
    private MyUserDetailsService userDetailsService;
    
	private PasswordEncoder passwordEncoder;

	public MyUsernamePasswordAuthenticationProvider() {
		log.info("###### MyUsernamePasswordAuthenticationProvider constructor");
    	this.passwordEncoder = NoOpPasswordEncoder.getInstance();
    }

    @SuppressWarnings("serial")
	@Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    	log.info("###### MyUsernamePasswordAuthenticationProvider authenticate");
    	String domain = (String) ThreadContext.get(ThreadContext.DOMAIN);
    	
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserDetails userDetails = userDetailsService.loadUser(username, domain);

        if (passwordEncoder.matches(password, userDetails.getPassword())) {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
            log.info("###### MyUsernamePasswordAuthenticationProvider match found returning token");
			return usernamePasswordAuthenticationToken;
        } else {
        	log.info("###### MyUsernamePasswordAuthenticationProvider Invalid credentials");
            throw new AuthenticationException("Invalid credentials") {};
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}