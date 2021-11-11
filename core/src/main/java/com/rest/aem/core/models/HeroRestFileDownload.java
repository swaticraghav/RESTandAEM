package com.rest.aem.core.models;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = Resource.class)
public class HeroRestFileDownload {

	private static final String DOWNLOAD_FILE = "C:/Users/swaragha/Desktop/Training/1. API/Rest Files Attachments/downloaded.jpg";
	private static final String URI = "http://localhost:7070/restattachment/services/fileservice/download";

	private String message;
	private String log;

	@PostConstruct
	public void init() throws ClientProtocolException, IOException {
		message = "File Download Example";
		log = "Hey, This " + getJson();
	}

	private String getJson() throws ClientProtocolException, IOException {
		CloseableHttpClient client = HttpClientBuilder.create().build();
		HttpGet postMethod = new HttpGet(URI);
		HttpResponse httpResponse = client.execute(postMethod);
		HttpEntity entity = httpResponse.getEntity();
		InputStream is = entity.getContent();

		String filePath = DOWNLOAD_FILE;
		FileOutputStream fos = new FileOutputStream(new File(filePath));
		int inByte;
		while ((inByte = is.read()) != -1) {
			fos.write(inByte);
		}
		is.close();
		fos.close();
		client.close();
		log = "is done.";
		return log;
	}

	public String getMessage() {
		return message;
	}

	public String getLog() {
		return log;
	}

}
