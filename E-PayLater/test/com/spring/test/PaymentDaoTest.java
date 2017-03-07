package com.spring.test;

import java.lang.reflect.Method;

import javax.crypto.Cipher;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.payment.dao.PaymentDao;
import com.payment.dao.impl.PaymentDaoImpl;

public class PaymentDaoTest {

	@Autowired
	PaymentDao paymentDao = new PaymentDaoImpl();
	
	@Test
	public void test(){
		Method []  methods = PaymentDao.class.getDeclaredMethods();
		String token ="Uo6Ok13dkpN3nUV5MH5HtlnvuIcFyR8EJOPxv/yTg9Njy0tMmIRCQALgVBRf9rOH";
		String tokenInDbExpected="d0bbfe0a-4df3-4652-99ae-a0efc47d27f9";
		for(Method method:methods){
			if(method.getName().equalsIgnoreCase("doCrypto")){
				try {
					method.setAccessible(true);
					String tokenInDBActual = (String) method.invoke(paymentDao, Cipher.DECRYPT_MODE,"PAYLATERPAYLATERPAYLAT",null,token);
					Assert.assertEquals(tokenInDbExpected, tokenInDBActual);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
