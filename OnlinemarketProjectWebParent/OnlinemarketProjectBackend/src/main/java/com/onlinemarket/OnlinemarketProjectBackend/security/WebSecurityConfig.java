package com.onlinemarket.OnlinemarketProjectBackend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

    @Bean
    UserDetailsService userDetailsService() {
        return new OnlinemarketUserDetailsService();
    }
	
    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }


    @Bean
    SecurityFilterChain configureHttp(HttpSecurity http) throws Exception {
        http.authenticationProvider(authenticationProvider());
        
        // return http.authorizeHttpRequests((auth) -> auth.anyRequest().permitAll()).build();

        http.authorizeHttpRequests((auth) -> auth
            .requestMatchers("/users/**", "/settings/**").hasAuthority("Admin")
            .requestMatchers("/categories/**", "/brands/**").hasAnyAuthority("Admin","Editor")
            .requestMatchers("/products", "/products/", "/products/detail/**", "/products/page/**").hasAnyAuthority("Admin","Editor","Salesperson","Shipper")
            .requestMatchers("/products/new","/products/delete/**").hasAnyAuthority("Admin","Editor")
            .requestMatchers("/products/edit","/products/save/**","/products/check_unique").hasAnyAuthority("Admin","Editor","Salesperson")
            .requestMatchers("/products/**").hasAnyAuthority("Admin","Editor")
            .anyRequest().authenticated())
        .formLogin(form -> form
            .loginPage("/login")
            .usernameParameter("email")
            .permitAll())
        .logout(logout -> logout.permitAll())
        .rememberMe( rem -> rem
            .key("AbcdefghijklMNOPQRS_1234567890") //fixing cookie key
            .tokenValiditySeconds(7*24*60*60));
        
        return http.build();
    }

    @Bean
    WebSecurityCustomizer configureWebSecurity() throws Exception{
        return (web) -> web.ignoring().requestMatchers("/images/**","/js/**","/webjars/**");
    }
}
