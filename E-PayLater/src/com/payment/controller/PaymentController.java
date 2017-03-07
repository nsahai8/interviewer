package com.payment.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.jasper.tagplugins.jstl.core.ForEach;
import org.hibernate.type.TrueFalseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.payment.entity.Transactions;
import com.payment.entity.User;
import com.payment.service.PaymentService;

@Controller
public class PaymentController {
	
	@Autowired
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	PaymentService paymentService;

	@ResponseBody   
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Object login() {
    	Authentication auth = paymentService.createUser();
		return auth.getPrincipal();
    }
     
    @ResponseBody   
    @RequestMapping(value = "/balance", method = RequestMethod.GET)
    public ArrayNode getbalance() {
    	ArrayNode arrayNode = objectMapper.createArrayNode();
    	if(SecurityContextHolder.getContext().getAuthentication() == null)
    		return arrayNode;
    	String auth = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	User user = paymentService.getBalance(auth);
    	JsonNode node = objectMapper.createObjectNode();
    	((ObjectNode)node).put("Balance", user.getBalance());
    	((ObjectNode)node).put("Currency Code", user.getCurrencyCode());
    	arrayNode.add(node);
    	return arrayNode;
    }
    
    @ResponseBody   
    @RequestMapping(value = "/transactions", method = RequestMethod.GET)
    public List<Transactions> getTransactions() {
    	List<Transactions> list = new ArrayList<>();
    	String token = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	List<Transactions> transactions = paymentService.getTransactions(token);
    	transactions.forEach(transaction->{
    		list.add(transaction);
    	});
    	return transactions;
    }
    
	@ResponseBody   
    @RequestMapping(value = "/spend", method = RequestMethod.POST)
    public void setSpendings(@RequestBody JsonNode jsonString) {
    	String token = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	Transactions transaction = new Transactions();
    	transaction.setDate(new Date(jsonString.get("date").asText()));
    	transaction.setAmount(jsonString.get("amount").asLong());
    	transaction.setCurrencyCode(jsonString.get("currencyCode").asText());
    	transaction.setDescription(jsonString.get("description").asText());
    	paymentService.addTransaction(token,transaction);
    }
	
}

