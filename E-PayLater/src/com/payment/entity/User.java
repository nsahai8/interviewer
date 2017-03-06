package com.payment.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="USERPAYMENT",schema="public")
public class User {

	
	private Integer Id;
	private Long balance;
	
	private String currencyCode;
	private String token;
	//private Set<Transactions> transactions;
	
	public User() {
		this.balance = 100L;
		this.currencyCode = "INR";
		//Set<Transactions> transSet =  new HashSet<Transactions>();
		//this.transactions = transSet;
	}
	@Id
	@Column(name="Id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getId() {
		return this.Id;
	}
	public void setId(Integer id) {
		this.Id = id;
	}
	public Long getBalance() {
		return this.balance;
	}
	public void setBalance(Long balance) {
		this.balance = balance;
	}
	public String getCurrencyCode() {
		return this.currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getToken() {
		return this.token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	/*@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "USER_TRANSACTION", joinColumns = { @JoinColumn(name = "transId") }, 
	inverseJoinColumns = { @JoinColumn(name = "Id") })
	public Set<Transactions> getTransactions() {
		return this.transactions;
	}
	
	public void setTransactions(Set<Transactions> transactions) {
		this.transactions = transactions;
	}*/
	@Override
    public String toString() {
        return "User [id=" + Id + ", balance=" + balance + "currency=" + currencyCode + "]";
    }  
}
