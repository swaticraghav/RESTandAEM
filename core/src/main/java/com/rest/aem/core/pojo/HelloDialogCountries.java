package com.rest.aem.core.pojo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.iterators.TransformIterator;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.sling.api.resource.ResourceMetadata;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.json.JSONArray;

import com.adobe.cq.sightly.WCMUsePojo;
import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.granite.ui.components.ds.ValueMapResource;

public class HelloDialogCountries extends WCMUsePojo {

	private static final String URI = "http://localhost:7070/restws/services/patientservice/patients";

	@Override
	public void activate() throws Exception {
		final ResourceResolver resolver = getResource().getResourceResolver();
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
		this.getRequest().setAttribute(DataSource.class.getName(), ds);
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

}