package com.rest.aem.core.models;

import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = Resource.class)
public class HeroRestFileUpload {

	private static final String IMAGE_FILE_LOCATION = "C:/Users/swaragha/Desktop/Swati.jpg";

	private static final String URI = "http://localhost:7070/restattachment/services/fileservice/upload";

	private String message;
	private String log;

	@PostConstruct
	public void init() throws ClientProtocolException, IOException {
		message = "File Upload Example";
		log = "Hey, This " + getJson();
	}

	private String getJson() throws ClientProtocolException, IOException {
		String message = "This is a multipart post";
		
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost postMethod = new HttpPost(URI);
		File file = new File(IMAGE_FILE_LOCATION);
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		builder.addBinaryBody("upfile", file, ContentType.DEFAULT_BINARY, "filename=Swati.jpg");
		builder.addTextBody("text", message, ContentType.TEXT_PLAIN);
		HttpEntity entity = builder.build();
		postMethod.setEntity(entity);

		HttpResponse httpResponse = client.execute(postMethod);
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
