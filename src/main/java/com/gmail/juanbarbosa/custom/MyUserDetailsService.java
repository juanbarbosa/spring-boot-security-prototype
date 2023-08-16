package com.gmail.juanbarbosa.custom;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Profile("custom-provider-userdetailsservice")
@Service @Slf4j @SuppressWarnings("deprecation")
public class MyUserDetailsService implements UserDetailsService {
	
	private Map<String, UserDetails> repo;

	public MyUserDetailsService() {
		UserBuilder users = User.builder();
		repo = new HashMap<>();
		repo.put("user", users.username("user").password("password").passwordEncoder(NoOpPasswordEncoder.getInstance()::encode).roles("USER").build());
		repo.put("admin", users.username("admin").password("password").passwordEncoder(NoOpPasswordEncoder.getInstance()::encode).roles("USER","ADMIN").build());
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("###### Noooooooooooooooooooooo CustomUserDetailsService.loadUserByUsername");
		String domain = (String) ThreadContext.get(ThreadContext.DOMAIN);
		return loadUser(username, domain);
	}
	
	public UserDetails loadUser(String username, String domain) throws UsernameNotFoundException {
		log.info("###### CustomUserDetailsService.loadUser");
		if (repo.containsKey(username)) {
			UserDetails userDetails = repo.get(username);
			
			UserDetails copy = User.builder().username(username).password(userDetails.getPassword()).passwordEncoder(NoOpPasswordEncoder.getInstance()::encode).authorities(userDetails.getAuthorities()).build();
			log.info("###### CustomUserDetailsService.loadUser user found");
			return copy;
		}
		
		log.info("###### CustomUserDetailsService.loadUser not found");
		throw new UsernameNotFoundException("not found");
	}

}
