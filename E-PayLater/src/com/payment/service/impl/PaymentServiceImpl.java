package com.payment.service.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import com.payment.dao.PaymentDao;
import com.payment.entity.Transactions;
import com.payment.entity.User;
import com.payment.service.PaymentService;


@Service

public class PaymentServiceImpl implements PaymentService {
	@Autowired
	private PaymentDao paymentDao;

	public void setPaymentDao(PaymentDao paymentDao) {
		this.paymentDao = paymentDao;
	}
 // it creates a default user with default amount and indian currencyCode
	public Authentication createUser() {
		User user = new User();
		user.setId(1);
		String authToken = paymentDao.saveUser(user);
		Transactions transactions = new Transactions();
		transactions.setAmount(100L);
		transactions.setCurrencyCode("INR");
		transactions.setDate(new Date());
		transactions.setDescription("default amount added by system");
		transactions.setUserId(user.getId());
		paymentDao.saveTransactions(transactions);
		Authentication auth = new UsernamePasswordAuthenticationToken(authToken ,user.getId());
		return auth;
	}
	//get Balance using the token
	public User getBalance(String token) {
		return paymentDao.getBalance(token);
	}

	public Long getBalance1() {
		return paymentDao.getBalance1();
	}
	//get List of transactions using user Id
	@Override
	public List<Transactions> getTransactions(String token) {
		return paymentDao.getTransactions(token);
	}

	//addd transaction done by the user
	@Override
	public void addTransaction(String token, Transactions transaction) {
		User user = paymentDao.getUserByToken(token);
		user.setBalance(user.getBalance()+transaction.getAmount());
		transaction.setUserId(user.getId());
		paymentDao.saveTransactions(transaction);
		paymentDao.saveUser(user);
	}

}
