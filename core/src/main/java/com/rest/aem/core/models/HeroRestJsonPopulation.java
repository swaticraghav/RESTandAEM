package com.rest.aem.core.models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.iterators.TransformIterator;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceMetadata;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.json.JSONArray;
import org.json.JSONException;

import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.granite.ui.components.ds.ValueMapResource;

@Model(adaptables = {SlingHttpServletRequest.class})
public class HeroRestJsonPopulation {

	@Self
	SlingHttpServletRequest request;
	
	private String message;
	private String log;
	private static final String URI = "http://localhost:7070/restws/services/patientservice/patients";

	@PostConstruct
	public void init() throws ClientProtocolException, IOException, JSONException {
		message = "Swati Here! Look at the JSON: " + getJson();
		dialogPopulation();
	}


	private void dialogPopulation() throws ClientProtocolException, IOException, JSONException {
		
		if(request != null) {
			log = "Success";
			final ResourceResolver resolver = request.getResource().getResourceResolver();
			String jsondata = getJson();
			Map<String, String> patientMap = new LinkedHashMap<String, String>();
			JSONArray jsonArray = new JSONArray(jsondata);
			for (int i = 0; i < jsonArray.length(); i++) {
				patientMap.put(String.valueOf(jsonArray.getJSONObject(i).getInt("id")),
						jsonArray.getJSONObject(i).getString("name"));
			}
			
			@SuppressWarnings("unchecked")
			DataSource ds = new SimpleDataSource(new TransformIterator(patientMap.keySet().iterator(), new Transformer() {
				@Override
				public Object transform(Object o) {
					String patient = (String) o;
					ValueMap vm = new ValueMapDecorator(new HashMap<String, Object>());
					vm.put("value", patient);
					vm.put("text", patientMap.get(patient));
					return new ValueMapResource(resolver, new ResourceMetadata(), "nt:unstructured", vm);
				}
			}));
			request.setAttribute(DataSource.class.getName(), ds);
		}else {
			log = "Fail,,tannnn";
		}
	}


	public String getMessage() {
		return message;
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

	public String getLog() {
		return log;
	}

}
