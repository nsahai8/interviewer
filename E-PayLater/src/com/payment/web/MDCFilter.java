/*package com.payment.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;

import com.payment.entity.User;
import com.payment.service.impl.TokenAuthenticationProvider;


public class MDCFilter implements Filter {

	public static final String AUTHENTICATION_HEADER = "Authorization";
	
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
    	System.out.println("-------------------here");
        if(SecurityContextHolder.getContext().getAuthentication()!=null){
            User appUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            
        }
        

        try {
            chain.doFilter(req, resp);
        } finally {
           
        }
    	System.out.println("in MDC filter");
    	if (req instanceof HttpServletRequest) {
			HttpServletRequest httpServletRequest = (HttpServletRequest) req;
			String authCredentials = httpServletRequest
					.getHeader(AUTHENTICATION_HEADER);

			// better injected
			

			boolean authenticationStatus = authenticationService
					.authenticate(authCredentials);

			if (authenticationStatus) {
				filter.doFilter(request, response);
			} else {
				if (response instanceof HttpServletResponse) {
					HttpServletResponse httpServletResponse = (HttpServletResponse) response;
					httpServletResponse
							.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				}
			}
		}
    }

    @Override
    public void destroy() {
    }

}*/