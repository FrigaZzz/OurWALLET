package com.mfr.model.Response.Parsed;

public class Budget {

	private String name;

	private long id;

	private long amount;

	public Budget(String name, long id, long amount) {
		this.name = name;
		this.id = id;
		this.amount = amount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}
}
