package com.typesafe.akka.http.ext.controller.test.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Dummy {
	
	private String message;
	private String prefix;
	private int count;
	private boolean active;

	public Dummy() {
		
	}

	public Dummy(String message, String prefix, int count, boolean active) {
		this.message = message;
		this.prefix = prefix;
		this.count = count;
		this.active = active;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public String toString() {
		final ObjectMapper mapper = new ObjectMapper().enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);
		try { 
			return mapper.writeValueAsString(this); 
		} catch (JsonProcessingException cause) { 
			throw new RuntimeException(cause); 
		}
	}
}