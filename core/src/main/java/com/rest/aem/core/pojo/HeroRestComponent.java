package com.rest.aem.core.pojo;

import com.adobe.cq.sightly.WCMUsePojo;

public class HeroRestComponent extends WCMUsePojo {

	private String message;

	@Override
	public void activate() throws Exception {
		message = "Hello";

	}

	public String getMessage() {
		return message;
	}

}