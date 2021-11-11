package com.rest.aem.core.models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.annotation.PostConstruct;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = Resource.class)
public class HeroRestJsonBasicAuth {

	private String message;
	private String xmlContent;
	private static final String URI = "http://localhost:7070/restws/services/productservice/products";
	private static final String USERNAME = "admin";
	private static final String PASSWORD = "admin";

	@PostConstruct
	public void init() throws ClientProtocolException, IOException {
		message = "Basic Auth Example";
		xmlContent = getJson();
	}

	private String getJson() throws ClientProtocolException, IOException {

		CredentialsProvider credProvider = new BasicCredentialsProvider();
		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(USERNAME, PASSWORD);
		credProvider.setCredentials(AuthScope.ANY, (Credentials) credentials);

		HttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(credProvider).build();
		HttpResponse httpResponse = client.execute(new HttpGet(URI));
		httpResponse.addHeader("accept", "application/json");
		if (httpResponse.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + httpResponse.getStatusLine().getStatusCode());
		}
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader((httpResponse.getEntity().getContent())));

		String output;
		String xmlData = "";
		while ((output = bufferedReader.readLine()) != null) {
			xmlData = xmlData + output;
		}
		return xmlData;
	}

	public String getMessage() {
		return message;
	}

	public String getXmlContent() {
		return xmlContent;
	}

}
