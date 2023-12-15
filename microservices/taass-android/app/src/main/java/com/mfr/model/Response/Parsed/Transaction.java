package com.mfr.model.Response.Parsed;

import com.mfr.model.Request.InputTransaction;
public class Transaction {
	private Long id;
	private String description;
	private long date;
	private Long amount;
	private Account account;
	private Account target;
	private Category category;


	public Transaction(Long id, String description, long date, Long amount, Account account, Account target, Category category) {
		this.id = id;
		this.description = description;
		this.date = date;
		this.amount = amount;
		this.account = account;
		this.target = target;
		this.category = category;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}


	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Account getTarget() {
		return target;
	}

	public void setTarget(Account target) {
		this.target = target;
	}
}
