package com.mfr.model.Response.Parsed;

public class BudgetGraph {
	private long amount;
	private long spent;
	private Budget budget;

	public BudgetGraph(long amount,long spent, Budget budget) {
		this.amount = amount;
		this.spent = spent;
		this.budget = budget;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	public Budget getBudget() {
		return budget;
	}

	public void setBudget(Budget budget) {
		this.budget = budget;
	}

	public long getSpent() {
		return spent;
	}

	public void setSpent(long spent) {
		this.spent = spent;
	}
}
