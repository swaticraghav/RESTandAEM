package com.rest.aem.core.models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;

import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

@Model(adaptables = Resource.class)
public class HeroRestXmlModel {

	private String message;
	private static final String URI = "http://localhost:7070/restws/services/patientservice/patients";
	private String serviceResponse;
	private String[] data;

	@PostConstruct
	public void init() throws ClientProtocolException, IOException, ParserConfigurationException, SAXException {
		message = "This is XML file.";
		serviceResponse = getJson();
		data = retrieveXml(getJson());
	}

	private String getJson() throws ClientProtocolException, IOException {
		HttpClient client = HttpClientBuilder.create().build();
		HttpResponse httpResponse = client.execute(new HttpGet(URI));
		httpResponse.addHeader("accept", "application/xml");
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

	public String[] getData() {
		return data;
	}

	String[] retrieveXml(String xmldata) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(xmldata));
		Document doc = dBuilder.parse(is);
		NodeList nodes = doc.getElementsByTagName("Patient");
		String[] names = new String[nodes.getLength()];
		String name = "";
		for (int i = 0; i < nodes.getLength(); i++) {
			Element element = (Element) nodes.item(i);
			NodeList patientName = element.getElementsByTagName("name");
			Element line = (Element) patientName.item(0);
			name = getCharacterDataFromElement(line);
			names[i] = name;
		}
		return names;
	}

	public static String getCharacterDataFromElement(Element e) {
		Node child = e.getFirstChild();
		if (child instanceof CharacterData) {
			CharacterData cd = (CharacterData) child;
			return cd.getData();
		}
		return "";
	}

	public String getServiceResponse() {
		return serviceResponse;
	}

	public String getMessage() {
		return message;
	}

}
