package com.payment.dao.impl;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.tomcat.util.codec.binary.Base64;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.payment.dao.PaymentDao;
import com.payment.entity.Transactions;
import com.payment.entity.User;

@Repository
public class PaymentDaoImpl implements PaymentDao {

	private static final String ALGORITHM = "AES";
	private static final String TRANSFORMATION = "AES";
	private static final String key = "PAYLATERPAYLATERPAYLAT";

	@Autowired
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Transactional
	public User getUserByToken(String token) {
		User user = null;
		try {
			Session session = sessionFactory.openSession();
			session.beginTransaction();
			token = new String(doCrypto(Cipher.DECRYPT_MODE, key, null, token));
			Query query = session.createQuery("FROM User WHERE token = :token");
			query.setParameter("token", token);
			user = (User) query.list().get(0);
			session.getTransaction().commit();
			session.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return user;
	}

	@Transactional
	public String saveUser(User user) {
		String authToken = null;
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		String token = UUID.randomUUID().toString();
		user.setToken(token);

		// user.getTransactions().add(transactions);
		try {
			authToken = new String(doCrypto(Cipher.ENCRYPT_MODE, key, token.getBytes(), null));
			Integer personId = (Integer) session.save(user);
			System.out.println("User created with Id : " + personId);
		} catch (Exception e) {
			System.out.println("Exception occured while creating user: " + e.getMessage());
		} finally {
			session.getTransaction().commit();
			session.close();
		}
		return authToken;
	}

	@Transactional
	public User getBalance(String token) {
		User user = null;
		try {
			user = getUserByToken(token);
			return user != null ? user : null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Transactional
	public List<Transactions> getTransactions(String token) {
		List<Transactions> list =null;
		User user = null;
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		try {
			user = getUserByToken(token);
			Query query = session.createQuery("FROM Transactions WHERE userId = :userId");
			query.setParameter("userId", user.getId());
			list = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			session.getTransaction().commit();
			session.close();
		}
		return list;
	}

	private static String doCrypto(int cipherMode, String key, byte[] inputBytes, String decodeText) {
		try {
			if (cipherMode == Cipher.DECRYPT_MODE) {
				byte[] raw = Base64.decodeBase64(key);
				Key secretKey = new SecretKeySpec(raw, ALGORITHM);
				Cipher cipher = Cipher.getInstance(TRANSFORMATION);
				byte[] encryptText = Base64.decodeBase64(decodeText);
				cipher = Cipher.getInstance("AES");
				cipher.init(Cipher.DECRYPT_MODE, secretKey);
				return new String(cipher.doFinal(encryptText));

			}
			byte[] raw = Base64.decodeBase64(key);
			Key secretKey = new SecretKeySpec(raw, ALGORITHM);
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(cipherMode, secretKey);
			byte[] outputBytes = cipher.doFinal(inputBytes);
			if (cipherMode == Cipher.ENCRYPT_MODE) {
				return Base64.encodeBase64String(outputBytes);
			}

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return null;
	}

	@Override
	public Long getBalance1() {
		User user;
		try {
			Session session = sessionFactory.openSession();
			session.beginTransaction();

			Query query = session.createQuery("FROM User ");
			user = (User) query.list().get(0);
			session.getTransaction().commit();
			session.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return 100L;
	}

	@Override
	public Integer UserCount() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String extractToken(String encryptedText) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveTransactions(Transactions transactions) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		try {
			session.persist(transactions);
			System.out.println("transaction created  ");
		} catch (Exception e) {
			System.out.println("Exception occured while creating user: " + e.getMessage());
		} finally {
			session.getTransaction().commit();
			session.close();
		}

	}

}
