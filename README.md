# Spring Boot 3 + Spring Security 6 prototype project

## Overview

I'm trying to learn how to create a custom solution for login in a multi tenant / client website hosting project. This is a barebones prototype that taught me all the fundamentals I needed to learn (and then some more) and I thought I'd share.

## How to build

Build it with Maven or run it within an IDE and run it. I use the Spring Tool Suite, and it works fine for me. I'll include the command line version for people that don't want to bother with an IDE.
 - mvn package

## How to run it

If you're trying to learn from this code base you need to keep aware of the three profiles you can use and the here's the command on how to run them:
- base : java -jar target/spring-boot-security-prototype-0.0.1-SNAPSHOT.jar --spring.profiles.active=base
- custom-login-logout-url : java -jar target/spring-boot-security-prototype-0.0.1-SNAPSHOT.jar --spring.profiles.active=custom-login-logout-url
- custom-provider-userdetailsservice : java -jar target/spring-boot-security-prototype-0.0.1-SNAPSHOT.jar --spring.profiles.active=custom-provider-userdetailsservice

The three profiles load a different security config

### base
This profile will load the security config stored in BaseWebSecurityConfig.

### custom-login-logout-url
This profile will load the security config stored in CustomLoginLogoutUrlSecurityConfig. 

### custom-provider-userdetailsservice
This profile will load the security config stored in CustomProviderAndDetailsServiceSecurityConfig. 

I went through an iterative approach with these configs because the more I fiddled with it the more it broke, so started from base and worked my way up when I got features that I wanted. The thymelaf pages in the code are not pretty and that's the point. I was trying to learn if you could logout directly at any point through a form or if I had to go through the link route.
More importantly and what I'm a little proud of are the filters. UserDetailsService is kinda messed up that it only takes one parameter... username. If you build a solution with multiple websites and clients then you can have a repeat of the same username across domains. You need the domain to serve as the context for your process. Point is that for basic UsernamePasswordAuthentication you can work around this by using a custom userDetailsService and using a CustomAuthenticationProvider to leverage that CustomUserDetailsService as the code shows. Main problem is incorporating the RememberMe feature, that sucker uses the UserDetailService.loadUserByUsername method which screws you over, hence the filter. I created a ThreadContextFilter class that figures out the domain and stores it in a variable. The variable is of the ThreadLocal<> type and stored in a static field. In a reactive application you'd be screwed becuase threads share requests but in a servlet application like this, threads will process a single request at a time so throughout the thread exceution the domain will always be the same and hence although RememberMe calls the crappy loadUserByUsername method, we can work around it by using the filter and getting the domain related data. I also included a Post Processing filter to clean up any ThreadContext stuff that may be laying around.

Anyway, study it and stuff and I'm sure you'll learn a bit. GLHF.
