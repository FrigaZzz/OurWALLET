package com.mfr.model.Response;

import com.mfr.model.Response.Parsed.Account;
import com.mfr.model.Response.Parsed.Category;
import com.mfr.model.Response.Parsed.Group;
/**
 *
 * @author matteo
 */
public class OutputTransaction {

	public OutputTransaction() {
	}

	public OutputTransaction(Long id, String description, Long amount, Group groupSender, Account accountSender, Account accountReceived, Category category, Long date) {
		this.id = id;
		this.description = description;
		this.amount = amount;
		this.groupSender = groupSender;
		this.accountSender = accountSender;
		this.accountReceived = accountReceived;
		this.category = category;
		this.date = date;
	}

	private Long id;

	private String description;

	private Long amount;

	private Group groupSender;

	private Account accountSender;

	private Account accountReceived;

	private Category category;

	private Long date;

	public Long getDate() {
		return date;
	}

	public void setDate(Long date) {
		this.date = date;
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


	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public Group getGroupSender() {
		return groupSender;
	}

	public void setGroupSender(Group groupSender) {
		this.groupSender = groupSender;
	}

	public Account getAccountSender() {
		return accountSender;
	}

	public void setAccountSender(Account accountSender) {
		this.accountSender = accountSender;
	}

	public Account getAccountReceived() {
		return accountReceived;
	}

	public void setAccountReceived(Account accountReceived) {
		this.accountReceived = accountReceived;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
}