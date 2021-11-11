package com.rest.aem.core.pojo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import com.adobe.cq.sightly.WCMUsePojo;
import com.aem.community.core.bean.HelloWorldBean;

public class HelloWorldPojo extends WCMUsePojo {

	private static HelloWorldBean bean = null;

	/** Default log. */

	@Override
	public void activate() throws Exception {

		bean = new HelloWorldBean();
		bean.setTextField(getProperties().get("text", ""));
		bean.setDescriptionField(getProperties().get("description", ""));

		String restValue = returnJson();
		bean.setTextField(restValue);

	}

	private static String returnJson() throws ClientProtocolException, IOException {

		String finalResult = "";
		HttpClient client = HttpClientBuilder.create().build();
		HttpResponse response = client.execute(new HttpGet("http://localhost:8081/jerseyws/restapi/myresource"));
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode == HttpStatus.SC_OK) {
			BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
			String output;
			while ((output = br.readLine()) != null) {
				finalResult = finalResult + output;
			}
			return finalResult;
		}

		return finalResult;
	}

	public HelloWorldBean getBean() {
		return bean;
	}

	public static void main(String[] args) throws ClientProtocolException, IOException {

		String finalResult = "";
		HttpClient client = HttpClientBuilder.create().build();
		HttpResponse response = client.execute(new HttpGet("http://localhost:8081/jerseyws/restapi/myresource"));
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode == HttpStatus.SC_OK) {
			BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
			String output;
			while ((output = br.readLine()) != null) {
				finalResult = finalResult + output;
			}
		}

		System.err.println(finalResult);

	}

}
