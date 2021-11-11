package com.rest.aem.core.models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;

import com.google.gson.Gson;
import com.rest.aem.core.bean.Patient;

@Model(adaptables = Resource.class)
public class HeroRestJsonPost {

	private String message;
	private static final String URI = "http://localhost:7070/restws/services/patientservice/patients";
	private String log;
	private String jsonData;

	@Inject
	@Optional
	private String patientName;

	@PostConstruct
	public void init() throws ClientProtocolException, IOException {
		message = "This is Post Example!";
		log = "Start";
		jsonData = postName();
	}

	String postName() throws ClientProtocolException, IOException {

		HttpClient client = HttpClientBuilder.create().build();
		HttpPost postMethod = new HttpPost(URI);

		Patient patient = new Patient();
		if (patientName != null) {
			patient.setName(patientName);
		} else {
			patient.setName("No Name");
		}
		Gson gson = new Gson();
		StringEntity ent = new StringEntity(gson.toJson(patient));

		postMethod.setEntity(ent);
		postMethod.setHeader("Content-Type", "application/json");
		HttpResponse httpResponse = client.execute(postMethod);
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader((httpResponse.getEntity().getContent())));
		if (httpResponse.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + httpResponse.getStatusLine().getStatusCode());
		}
		String output;
		String jsonData = "";
		while ((output = bufferedReader.readLine()) != null) {
			jsonData = jsonData + output;
		}

		log += "Execution Done.";
		return jsonData;
	}

	public String getPatientName() {
		return patientName;
	}

	public String getLog() {
		return log;
	}

	public String getMessage() {
		return message;
	}

	public String getJsonData() {
		return jsonData;
	}

}
