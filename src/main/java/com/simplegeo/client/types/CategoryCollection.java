package com.simplegeo.client.types;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Collection of {@link com.simplegeo.client.types.Category} objects
 * 
 * @author Pranil Kanderi
 *
 */
public class CategoryCollection {

	private ArrayList<Category> categories;
	
	public CategoryCollection() {
	}
	
	public CategoryCollection(ArrayList<Category> categories) {
		this.categories = categories;
	}

	public ArrayList<Category> getCategories() {
		return categories;
	}

	public void setCategories(ArrayList<Category> categories) {
		this.categories = categories;
	}

	public static Object fromJSONString(String response) throws JSONException {		
		JSONArray jsonArray = new JSONArray(response);
		
		CategoryCollection categoryCollection = new CategoryCollection();
		ArrayList<Category> categories = new ArrayList<Category>();
		
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject json = jsonArray.getJSONObject(i);
			Category category = new Category(json.getString("category_id"),
					json.getString("category"), json.getString("type"),
					json.getString("subcategory"));

			categories.add(category);
		}
		categoryCollection.setCategories(categories);
		
		return categoryCollection;
	}
	
	
}
