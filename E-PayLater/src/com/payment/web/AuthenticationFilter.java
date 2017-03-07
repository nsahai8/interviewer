package com.payment.web;

import java.io.IOException;
import java.util.Base64;
import java.util.StringTokenizer;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.payment.dao.PaymentDao;
import com.payment.service.impl.TokenAuthenticationProvider;

public class AuthenticationFilter extends OncePerRequestFilter {
	
	@Autowired
	PaymentDao paymentDao;
	
	private static Logger LOG = LoggerFactory.getLogger(AuthenticationFilter.class);
	
    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String xAuth = request.getHeader("Authorization");
        Authentication authentication = null;
        if(xAuth != null){
        	
    		// header value format will be "Basic encodedstring" for Basic
    		// authentication. 
    		final String encodedUserToken = xAuth.replaceFirst("Basic"
    				+ " ", "");
    		String userToken = null;
    		try {
    			byte[] decodedBytes = Base64.getDecoder().decode(
    					encodedUserToken);
    			userToken = new String(decodedBytes, "UTF-8");
    		} catch (IOException e) {
    			LOG.error("Authentication failed for token: "+authentication.getPrincipal());
    		}
    		final StringTokenizer tokenizer = new StringTokenizer(
    				userToken, ":");
    		final String userAuthToken = tokenizer.nextToken();
    		final String userId = tokenizer.nextToken();

    		authentication = new UsernamePasswordAuthenticationToken(userAuthToken ,userId);
    		SecurityContextHolder.getContext().setAuthentication(authentication);
    		
    		LOG.info("Token received : "+userAuthToken);
        }
        
        filterChain.doFilter(request, response);
    }

}
