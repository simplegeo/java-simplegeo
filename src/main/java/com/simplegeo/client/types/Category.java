package com.simplegeo.client.types;

/**
 * Class that a represents a single category
 * 
 * @author Pranil Kanderi
 *
 */
public class Category {
	
	private String categoryId;
	private String category;
	private String type;
	private String subCategory;
	
	/**
	 * Default constructor
	 */
	public Category() {
	}
	
	/**
	 * Main constructor for creating a category
	 * 
	 * @param categoryId - Id of the category, ex: "10100100", "10100101" etc
	 * @param category - name of the category, ex: "Administrative", "Commercial Area" etc
	 * @param type - type of the category, ex: "Region", "Entertainment" etc
	 * @param subCategory - a sub category of this category, ex: "Consolidated City", "County" etc
	 */
	public Category (String categoryId, String category, String type, String subCategory) {
		this.categoryId = categoryId;
		this.category = category;
		this.type = type;
		this.subCategory = subCategory;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}
	
	
}
