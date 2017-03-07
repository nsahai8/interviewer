package com.payment.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.payment.security.AuthFailureHandler;
import com.payment.security.AuthSuccessHandler;
import com.payment.security.HttpAuthenticationEntryPoint;
import com.payment.service.impl.TokenAuthenticationProvider;
import com.payment.web.AuthenticationFilter;

@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = { "com.payment.security" })
public class SecurityConfig extends WebSecurityConfigurerAdapter {


	@Autowired
	TokenAuthenticationProvider tokenAuthenticationProvider;

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/static/**").antMatchers("/js/**").antMatchers("/css/**");
	}

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {   
    	 System.out.println("in AuthenticationManagerBuilder");
    	 
        auth.authenticationProvider(tokenAuthenticationProvider);        
    } 
	@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(tokenAuthenticationProvider);   
    }
}
