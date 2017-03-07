package com.payment.dao.impl;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.logging.LogFactory;
import org.apache.tomcat.util.codec.binary.Base64;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	//secret key for encryption and decryption
	private static final String key = "PAYLATERPAYLATERPAYLAT";
	
	private static Logger LOG = LoggerFactory.getLogger(PaymentDao.class);

	@Autowired
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	//retrieve user by token
	@Transactional
	public User getUserByToken(String token) {
		User user = null;
		try {
			Session session = sessionFactory.openSession();
			session.beginTransaction();
			//decrypt the token
			token = new String(doCrypto(Cipher.DECRYPT_MODE, key, null, token));
			Query query = session.createQuery("FROM User WHERE token = :token");
			query.setParameter("token", token);
			user = (User) query.list().get(0);
			session.getTransaction().commit();
			session.close();
		} catch (Exception e) {
			LOG.error("Exception Occured while retrieving user from token: "+token);
		}
		return user;
	}

	//save user generating unique UUID as token
	@Transactional
	public String saveUser(User user) {
		String authToken = null;
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		//ensuring unique key in return
		String token = UUID.randomUUID().toString();
		user.setToken(token);

		// user.getTransactions().add(transactions);
		try {
			//token generated and encrypted with secret key
			authToken = new String(doCrypto(Cipher.ENCRYPT_MODE, key, token.getBytes(), null));
			Integer userId = (Integer) session.save(user);
			LOG.info("User created with Id : " + userId);
		} catch (Exception e) {
			LOG.error("Exception occured while creating user: " + e.getMessage());
		} finally {
			session.getTransaction().commit();
			session.close();
		}
		return authToken;
	}
	//return balance of user using the token
	@Transactional
	public User getBalance(String token) {
		User user = null;
		try {
			user = getUserByToken(token);
			return user != null ? user : null;
		} catch (Exception e) {
			LOG.error("Exception occured while retrieving balance for token: "+token+""+e.getMessage());
		}
		return null;
	}

	// to retrieve the transactions of the user 
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
			LOG.error("Exception occured: "+e.getMessage());
		}finally{
			session.getTransaction().commit();
			session.close();
		}
		return list;
	}

	//encrypting the token with algorith AES and secret key
	private static String doCrypto(int cipherMode, String key, byte[] inputBytes, String decodeText) {
		try {
			if (cipherMode == Cipher.DECRYPT_MODE) {
				byte[] raw = Base64.decodeBase64(key);
				Key secretKey = new SecretKeySpec(raw, ALGORITHM);
				Cipher cipher = Cipher.getInstance(TRANSFORMATION);
				byte[] encryptText = Base64.decodeBase64(decodeText);
				cipher = Cipher.getInstance(ALGORITHM);
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
	public Integer UserCount() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String extractToken(String encryptedText) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	//to save transaction done by user
	@Override
	public void saveTransactions(Transactions transactions) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		try {
			session.persist(transactions);
		} catch (Exception e) {
			LOG.error("Exception occured while creating user: " + e.getMessage());
		} finally {
			session.getTransaction().commit();
			session.close();
		}

	}

}
