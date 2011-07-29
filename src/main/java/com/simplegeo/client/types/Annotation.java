package com.simplegeo.client.types;

import org.json.JSONException;
import org.json.JSONObject;

public class Annotation {
	
	private boolean isPrivate;
	private JSONObject annotations;
	
	public Annotation(boolean isPrivate, JSONObject annotations) {
		this.isPrivate = isPrivate;
		this.annotations = annotations;
	}

	public boolean isPrivate() {
		return isPrivate;
	}

	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}

	public JSONObject getAnnotations() {
		return annotations;
	}

	public void setAnnotations(JSONObject annotations) {
		this.annotations = annotations;
	}
	
	public JSONObject toJSON() throws JSONException {
		JSONObject json = new JSONObject();
		json.put("private", isPrivate);
		json.put("annotations", annotations);
		return json;
	}
	
	public String toJSONString() throws JSONException {
		return this.toJSON().toString();
	}
	
	public static Annotation fromJSON(JSONObject json) throws JSONException {
		return new Annotation(json.getBoolean("private"), json.getJSONObject("annotations"));
	}
	
}
