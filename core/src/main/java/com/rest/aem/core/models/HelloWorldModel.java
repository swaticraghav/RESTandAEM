/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.rest.aem.core.models;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = Resource.class)
public class HelloWorldModel {

	private String message;
	private String restResult;

	/*
	 * @Inject String code;
	 */

	@PostConstruct
	protected void init() {
		message = "Hello World! Welcome to AEM.";
		restResult = getJSON("");
	}

	private String getJSON(String code) {

		try {

			DefaultHttpClient httpClient = new DefaultHttpClient();

			HttpGet getRequest = new HttpGet("http://localhost:8080/jerseyws/restapi/myresource");
			getRequest.addHeader("accept", "application/json");

			HttpResponse response = httpClient.execute(getRequest);

			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

			String output;
			String myJSON = "";
			while ((output = br.readLine()) != null) {
				// System.out.println(output);
				myJSON = myJSON + output;
			}

			httpClient.getConnectionManager().shutdown();
			return myJSON;
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
