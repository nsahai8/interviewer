package com.payment.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="userTransactionDetails",schema="public")
public class Transactions {
	
	private Integer transId;
	private Date date;
	private String description;
	private Long amount;
	private String currencyCode;
	private Integer userId;

	@Id
	@Column(name="transId")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getTransId() {
		return this.transId;
	}
	public void setTransId(Integer transId) {
		this.transId = transId;
	}
	public Date getDate() {
		return this.date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getDescription() {
		return this.description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getAmount() {
		return this.amount;
	}
	public void setAmount(Long amount) {
		this.amount = amount;
	}
	public String getCurrencyCode() {
		return this.currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	/*@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "Id", nullable = false,insertable=false,updatable=false)
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}*/
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
}
