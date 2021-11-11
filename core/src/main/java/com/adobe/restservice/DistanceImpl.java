package com.adobe.restservice;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.osgi.service.component.annotations.Component;

import com.adobe.cq.sightly.WCMUsePojo;

@Component
public class DistanceImpl extends WCMUsePojo {

	String myJSON = "";
	
	@Override
	public void activate() throws Exception {

        try
        {
            DefaultHttpClient httpClient = new DefaultHttpClient();
              
            HttpGet getRequest = new HttpGet("http://maps.googleapis.com/maps/api/distancematrix/json?origins=Vancouver%20BC&destinations=San%20Francisco&sensor=false");
            getRequest.addHeader("accept", "application/json");
 
            HttpResponse response = httpClient.execute(getRequest);
 
            if (response.getStatusLine().getStatusCode() != 200) {
                            throw new RuntimeException("Failed : HTTP error code : "
                                    + response.getStatusLine().getStatusCode());
                        }
 
            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
 
            String output;
                while ((output = br.readLine()) != null) {
                    //System.out.println(output);
                    myJSON = myJSON + output;
                }
 
              
            httpClient.getConnectionManager().shutdown();
        }
         
        catch (Exception e)
        {
            e.printStackTrace() ; 
        }
	}

	public String getMyJSON() {
		return myJSON;
	}


}
