package com.example.cashcard;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests(request -> request.antMatchers("/cashcards/**").hasRole("CARD-OWNER"))
				.httpBasic(Customizer.withDefaults()).csrf(csrf -> csrf.disable());

		return http.build();
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	UserDetailsService testOnlyUsers(PasswordEncoder passwordEncoder) {
		User.UserBuilder users=User.builder();
		UserDetails bhanu=users.username("Bhanu").password(passwordEncoder.encode("Chowdary@1507")).roles("CARD-OWNER").build();
		UserDetails vishnu=users.username("Vishnu").password(passwordEncoder.encode("Vishnu@1702")).roles("NON-OWNER").build();
		UserDetails hemanth=users.username("Hemanth").password(passwordEncoder.encode("Hemanth@0912")).roles("CARD-OWNER").build();
		return new InMemoryUserDetailsManager(bhanu,vishnu,hemanth);
	}
}
