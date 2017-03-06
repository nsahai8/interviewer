package com.payment.service;

import java.util.List;

import org.springframework.security.core.Authentication;

import com.payment.entity.Transactions;
import com.payment.entity.User;

public interface PaymentService {
	Authentication createUser();

	User getBalance(String token);

	List<Transactions> getTransactions(String token);

	void addTransaction(String token, Transactions transaction);

	Long getBalance1();
	
}
