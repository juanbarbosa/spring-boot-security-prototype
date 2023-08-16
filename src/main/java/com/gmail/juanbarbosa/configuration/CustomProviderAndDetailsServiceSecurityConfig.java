package com.gmail.juanbarbosa.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

import com.gmail.juanbarbosa.custom.ClearThreadContextFilter;
import com.gmail.juanbarbosa.custom.MyUserDetailsService;
import com.gmail.juanbarbosa.custom.MyUsernamePasswordAuthenticationProvider;
import com.gmail.juanbarbosa.custom.ThreadContextFilter;

import lombok.extern.slf4j.Slf4j;

@Profile("custom-provider-userdetailsservice")
@Configuration
@EnableWebSecurity @Slf4j
public class CustomProviderAndDetailsServiceSecurityConfig {
    @Autowired
    private MyUsernamePasswordAuthenticationProvider usernamePasswordAuthenticationProvider;
//    @Autowired
//    private MyJwtAuthenticationProvider jwtAuthenticationProvider;
    
    @Autowired
    private MyUserDetailsService customUserDetailsService;
    
    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
    	log.info("###### authManager");
        AuthenticationManagerBuilder authenticationManagerBuilder = 
            http.getSharedObject(AuthenticationManagerBuilder.class);
        return authenticationManagerBuilder.build();
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	log.info("###### WebSecurityConfig");
    	AuthenticationManager myAuthManager = authManager(http);
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
    		.authenticationManager(myAuthManager)
    		.formLogin(loginCustomizer -> loginCustomizer
				.loginPage("/login") // Do not configure to get the basic login page ;)
                .permitAll()
    		)
    		.logout(logout -> logout
				.logoutUrl("/logout") // Do not configure to get the basic logout page ;)
			    .invalidateHttpSession(true)
			    .deleteCookies("*")
			    .logoutSuccessUrl("/")
			    .permitAll()
			)
////            .addFilterBefore(new MyJwtAuthenticationFilter(authMan), MyUsernamePasswordAuthenticationFilter.class)
////    		.authenticationProvider(jwtAuthenticationProvider)
    		.addFilterBefore(new ThreadContextFilter(), UsernamePasswordAuthenticationFilter.class)
    		.addFilterAfter(new ClearThreadContextFilter(), ExceptionTranslationFilter.class)
    		.authenticationProvider(usernamePasswordAuthenticationProvider)
    		.rememberMe(rememberMe -> rememberMe
				.key("yourRememberMeKey") // A secret key for token generation
                .tokenValiditySeconds(60 * 60 * 24 * 30) // 30d * 24h/d * 60m/h * 60s/m, 30 day remember me
                .rememberMeParameter("remember-me") // Name of the checkbox in the login form
                .rememberMeCookieName("remember-me-cookie") // Name of the cookie
                .userDetailsService(customUserDetailsService)
            )
    	;
    	return http.build();
    }
}