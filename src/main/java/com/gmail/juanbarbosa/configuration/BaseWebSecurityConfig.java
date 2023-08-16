package com.gmail.juanbarbosa.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

import lombok.extern.slf4j.Slf4j;

@Profile("base")
@Configuration @EnableWebSecurity @Slf4j
public class BaseWebSecurityConfig {
	@Bean @SuppressWarnings("deprecation")
	public UserDetailsService userDetailsService() {
		log.info("###### UserDetailsService");
		UserBuilder users = User.withDefaultPasswordEncoder();
		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
		manager.createUser(users.username("user").password("password").roles("USER").build());
		manager.createUser(users.username("admin").password("password").roles("USER","ADMIN").build());
		return manager;
	}
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	log.info("###### WebSecurityConfig");
    	http
    		.authorizeHttpRequests(authorizeCustomizer -> authorizeCustomizer
    			.requestMatchers("/secured/**").hasRole("USER")
	            .requestMatchers("/admin/**").hasRole("ADMIN")
	            .anyRequest().permitAll()
    		)
    		.csrf(csrf -> 
    			csrf.requireCsrfProtectionMatcher(
    				new OrRequestMatcher(
    					// universal login url
						new AntPathRequestMatcher("/login", "POST"),
						new AntPathRequestMatcher("/logout", "POST"),
    					
						// url for end users to post their data for the clients
						new AntPathRequestMatcher("/process/**", "POST"),
			   			new AntPathRequestMatcher("/process/**", "DELETE"),
		    			new AntPathRequestMatcher("/process/**", "PUT"),
		    			
		    			// url for client admins to post their processing requests
		    			new AntPathRequestMatcher("/admin/process/**", "POST"),
		    			new AntPathRequestMatcher("/admin/process/**", "DELETE"),
		    			new AntPathRequestMatcher("/admin/process/**", "PUT")
					)
				)
    		)
    		.formLogin(loginCustomizer -> loginCustomizer
//				.loginPage("/login") // Do not configure to get the basic login page ;)
                .permitAll()
    		)
    		.logout(logout -> logout
//				.logoutUrl("/logout") // Do not configure to get the basic logout page ;)
			    .invalidateHttpSession(true)
			    .deleteCookies("*")
			    .logoutSuccessUrl("/")
			    .permitAll()
			)
    		.rememberMe(rememberMe -> rememberMe
				.key("yourRememberMeKey") // A secret key for token generation
                .tokenValiditySeconds(60 * 60 * 24 * 30) // 30d * 24h/d * 60m/h * 60s/m, 30 day remember me
                .rememberMeParameter("remember-me") // Name of the checkbox in the login form
                .rememberMeCookieName("custom-remember-me-cookie") // Name of the cookie
                .userDetailsService(userDetailsService())
            )
    	;
    	return http.build();
    }
}