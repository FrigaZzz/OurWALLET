package com.mfr.model.Response.Parsed;

public class Category {
	private Long id;
	private String name;
	private Category SuperCategory;

	public Category(Long id, String name, Category SuperCategory) {
		this.id = id;
		this.name = name;
		this.SuperCategory = SuperCategory;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Category getSuperCategory() {
		return SuperCategory;
	}

	public void setSuperCategory(Category superCategory) {
		SuperCategory = superCategory;
	}
}
