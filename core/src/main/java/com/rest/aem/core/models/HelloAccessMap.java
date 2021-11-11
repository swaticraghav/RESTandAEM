package com.rest.aem.core.models;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = Resource.class)
public class HelloAccessMap {

	private String message;
	private Map<String, String> map = new HashMap<>();

	@PostConstruct
	public void init() {
		message = "Message is Displayed.";
		map.put("name", "Swati");
		map.put("frient", "Suprabhat Tiwari");
	}

	public String getMessage() {
		return message;
	}

	public Map<String, String> getMap() {
		return map;
	}

}
