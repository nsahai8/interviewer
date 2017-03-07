package com.payment.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.payment.dao.PaymentDao;
import com.payment.entity.User;

@Service
public class TokenAuthenticationProvider implements AuthenticationProvider {
	
	@Autowired
	private PaymentDao paymentDao;
	
	public void setPaymentDao(PaymentDao paymentDao) {
		this.paymentDao = paymentDao;
	}
	
	@Override
	public boolean supports(Class<?> aClass) {
		return aClass.equals(UsernamePasswordAuthenticationToken.class);
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		UsernamePasswordAuthenticationToken usernamePasswordAuth = (UsernamePasswordAuthenticationToken) authentication;
		String token = usernamePasswordAuth.getName();
		System.out.println("In TokenAuthenticationProvider ");
		String userId = (String) usernamePasswordAuth.getCredentials();
		try {
			String tokenInDB = paymentDao.extractToken(token);
			User user = paymentDao.getUserByToken(tokenInDB);
			if(user.getId() == Integer.parseInt(userId))
				return new UsernamePasswordAuthenticationToken(userId, userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		throw new UsernameNotFoundException("User not found exception");
		
	}
}
