package com.rest.aem.core;
import com.adobe.cq.sightly.WCMUsePojo;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
     
    
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
      
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
   
import javax.jcr.Node;
import javax.jcr.Session;
      
   
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
    
import com.google.gson.*;   
import org.apache.http.client.methods.HttpGet;  
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
   
import java.io.StringReader ;
import java.io.StringWriter;
   
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
   
     
public class HeroRestComponent
extends WCMUsePojo
{
     
     /** The hero text bean that stores values returned by the RestFul Web Service. */
    private HeroTextBean heroTextBean = null;
          
    /** Default log. */
    protected final Logger log = LoggerFactory.getLogger(this.getClass());
                      
       
    @Override
    public void activate() throws Exception {
   
     
        Node currentNode = getResource().adaptTo(Node.class);
             
        heroTextBean = new HeroTextBean();
            
                     
        //Get the values that the author entered into the AEM dialog and to pass to RESTFul call
        String code = getProperties().get("code", "");
        
        String myJSON = getJSON(code);
         
        String myCountry=parse(myJSON);
             
       
        //Set Bean to store the values
        heroTextBean.setCodecountry("Code");
        heroTextBean.setFullcountry("FullCountry");
                       
    }
          
          
          
    public HeroTextBean getHeroTextBean() {
        return this.heroTextBean;
    }
        
     
    //Parse the JSON to get back the Country name
    public static String parse(String jsonLine) {
        JsonElement jelement = new JsonParser().parse(jsonLine);
        JsonObject jobject = jelement.getAsJsonObject();
        jobject = jobject.getAsJsonObject("RestResponse");
        jobject = jobject.getAsJsonObject("result");
        String result = jobject.get("name").getAsString();
        return result;
    }       
  //Invokes a third party Restful Web Service and returns the results in a JSON String
    private String getJSON(String code)
    {
 
        try
        {
 
           
            DefaultHttpClient httpClient = new DefaultHttpClient();
 
            HttpGet getRequest = new HttpGet("http://services.groupkt.com/country/get/iso2code/"+code);
            getRequest.addHeader("accept", "application/json");
 
 
            HttpResponse response = httpClient.execute(getRequest);
 
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }
 
            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
 
            String output;
            String myJSON="" ;
            while ((output = br.readLine()) != null) {
                //System.out.println(output);
                myJSON = myJSON + output;
            }
 
 
            httpClient.getConnectionManager().shutdown();
           return myJSON ;
        }
 
        catch (Exception e)
        {
            e.printStackTrace() ;
        }
 
        return null;
    }
        
       
}