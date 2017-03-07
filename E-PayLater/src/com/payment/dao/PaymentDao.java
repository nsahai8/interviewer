package com.payment.dao;

import java.util.List;

import com.payment.entity.Transactions;
import com.payment.entity.User;

public interface PaymentDao {
	String saveUser(User user) ;
	Integer UserCount();
	User getBalance(String token);
	List<Transactions> getTransactions(String token);
	User getUserByToken(String token);
	String extractToken(String encryptedText) throws Exception;
	void saveTransactions(Transactions transactions);
}
