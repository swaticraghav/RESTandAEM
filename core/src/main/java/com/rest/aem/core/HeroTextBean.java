package com.rest.aem.core;

/* Stores data returned from the Restful web service */
public class HeroTextBean {

	private String fullcountry;
	private String codecountry;

	public String getFullcountry() {
		return fullcountry;
	}

	public void setFullcountry(String fullcountry) {
		this.fullcountry = fullcountry;
	}

	public String getCodecountry() {
		return codecountry;
	}

	public void setCodecountry(String codecountry) {
		this.codecountry = codecountry;
	}

}