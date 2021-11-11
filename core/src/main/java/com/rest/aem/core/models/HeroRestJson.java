package com.rest.aem.core.models;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Model(adaptables = Resource.class)
public class HeroRestJson {

	private static final String URI = "http://localhost:7070/restws/services/patientservice/patients/";

	private String patientName;
	private String jsonDataAsIs;

	private String message;

	@Inject
	private String id;

	@PostConstruct
	public void init() throws Exception {

		message = "Inside JSon";
		jsonDataAsIs = getName(Integer.parseInt(id));
		patientName = parse(getName(Integer.parseInt(id)));
	}

	private String getName(int i) throws Exception {
		HttpClient client = HttpClientBuilder.create().build();
		HttpResponse httpResponse = client.execute(new HttpGet(URI + i));
		httpResponse.addHeader("accept", "application/json");
		if (httpResponse.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + httpResponse.getStatusLine().getStatusCode());
		}
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader((httpResponse.getEntity().getContent())));

		String output;
		String jsonData = "";
		while ((output = bufferedReader.readLine()) != null) {
			jsonData = jsonData + output;
		}
		return jsonData;
	}

	// Parse the JSON to get back the Country name
	public static String parse(String jsonData) {
		JsonParser jParser = new JsonParser();
		JsonObject jobject = jParser.parse(jsonData).getAsJsonObject();
		return jobject.getAsJsonPrimitive("name").getAsString();
	}

	public String getPatientName() {
		return patientName;
	}

	public String getMessage() {
		return message;
	}

	public String getJsonDataAsIs() {
		return jsonDataAsIs;
	}
}
