package com.rest.aem.core.models;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.annotation.PostConstruct;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = Resource.class)
public class HeroRestCompModel {

	private String message;
	private String restResult;

	@PostConstruct
	protected void init() {
		message = "Swati's Message!";
		restResult = getString();
	}

	private String getString() {

		try {

			/* Steps:
			 * 	1. Create the client.
			 *  2. Mention the rest uri and the method and get the response.
			 *  3. Add headers if needed.
			 *  4. Check if the status code is correct. 
			 *  5. Get the content from the response.
			 *  6. Return the response.
			 *  */
			HttpClient client = HttpClientBuilder.create().build();
			HttpResponse httpResponse = client
					.execute(new HttpGet("http://localhost:8081/jerseyws/restapi/myresource"));
			httpResponse.addHeader("accept", "application/json");
			if (httpResponse.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException(
						"Failed : HTTP error code : " + httpResponse.getStatusLine().getStatusCode());
			}
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader((httpResponse.getEntity().getContent())));

			String output;
			String finalResult = "";
			while ((output = bufferedReader.readLine()) != null) {
				finalResult = finalResult + output;
			}

			return finalResult;
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public String getMessage() {
		return message;
	}

	public String getRestResult() {
		return restResult;
	}

}
