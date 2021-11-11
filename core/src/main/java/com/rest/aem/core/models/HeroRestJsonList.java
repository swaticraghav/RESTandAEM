package com.rest.aem.core.models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.json.JSONArray;
import org.json.JSONException;

import com.rest.aem.core.bean.PatientDetail;

@Model(adaptables = Resource.class)
public class HeroRestJsonList {

	private static final String URI = "http://localhost:7070/restws/services/patientservice/patients";

	private String jsonData;

	private List<PatientDetail> listPatients;

	private List<String> listNames;

	@PostConstruct
	public void init() throws ClientProtocolException, IOException, JSONException {
		jsonData = getJson();
		listNames = getListOfNames(getJson());
		listPatients = getListOfPatients(getJson());
	}

	private String getJson() throws ClientProtocolException, IOException {
		HttpClient client = HttpClientBuilder.create().build();
		HttpResponse httpResponse = client.execute(new HttpGet(URI));
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

	private List<PatientDetail> getListOfPatients(String json) throws JSONException {

		JSONArray jsonArray = new JSONArray(json);
		List<PatientDetail> list = new ArrayList<PatientDetail>();
		for (int i = 0; i < jsonArray.length(); i++) {
			PatientDetail patientDetail = new PatientDetail();
			patientDetail.setId(jsonArray.getJSONObject(i).getInt("id"));
			patientDetail.setName(jsonArray.getJSONObject(i).getString("name"));
			list.add(patientDetail);
		}
		return list;
	}

	private List<String> getListOfNames(String json) throws JSONException {

		JSONArray jsonArray = new JSONArray(json);
		List<String> idnames = new ArrayList<>();
		for (int i = 0; i < jsonArray.length(); i++) {
			idnames.add(jsonArray.getJSONObject(i).getString("name"));
		}
		return idnames;
	}

	public String getJsonData() {
		return jsonData;
	}

	public List<PatientDetail> getListPatients() {
		return listPatients;
	}

	public List<String> getListNames() {
		return listNames;
	}

}
