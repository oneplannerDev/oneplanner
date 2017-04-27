package com.ontide.oneplanner.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpEncoding;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpMediaType;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.MultipartContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Key;
import com.google.api.client.util.StreamingContent;
import com.google.api.client.util.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ontide.oneplanner.io.ResultObj;
import com.ontide.oneplanner.obj.UserInfo;

public class RestClient<T> {
	LogWrapper logger = new LogWrapper(JsonHandler.class);

    public static void main(String[] args) {
    	String url = "http://localhost:8080/ringcatcher/json/register";
    	
    	String body = "{\"userId\":\"mytoken-id\",\"userNum\":\"01044445555\",\"userEmail\":\"jinnonspot@gmail.com\",\"recomId\":\"hisrecommend\"}";
    	RestClient json = new RestClient();
    	try {
			json.post(url, body);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
    
    public ResultObj<T> callJsonHttp(String url, Object jsonObj, Type type) {
    	String body;
    	if (jsonObj  instanceof List) {
    		//JSONArray jsonArray = new JSONArray(jsonObj);
    		body = new Gson().toJson(jsonObj);
    	} else {
    		JSONObject reqJson = new JSONObject(jsonObj);
    		body = reqJson.toString();
        }
    	
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("url:"+url);
    	logger.info("body:"+body);
    	String resultJson = post(url, body);
    	logger.info("resultJson:"+resultJson);

    	Gson gson = new Gson();
		ResultObj<T> result = (ResultObj<T>)gson.fromJson(resultJson, type);
		logger.info("result = "+result.toString());
    	return result;
    }
    
    public ResultObj<T> callMultipartHttp(String url, String filePath, Object jsonObj, Type type) {
    	String body;
    	if (jsonObj  instanceof List) {
    		//JSONArray jsonArray = new JSONArray(jsonObj);
    		body = new Gson().toJson(jsonObj);
    	} else {
    		JSONObject reqJson = new JSONObject(jsonObj);
    		body = reqJson.toString();
        }
    	
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	logger.info("url:"+url);
    	logger.info("body:"+body);
    	String resultJson = post(url, filePath, body);
    	logger.info("resultJson:"+resultJson);

    	Gson gson = new Gson();
		ResultObj<T> result = (ResultObj<T>)gson.fromJson(resultJson, type);
		logger.info("result = "+result.toString());
    	return result;
    }
    
    
    public ResultObj<T> callJsonHttp(String url, Object jsonObj) {
		Type type = new TypeToken<ResultObj<T>>() {}.getType();
		return callJsonHttp(url, jsonObj, type);
    }

    static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    static final JsonFactory JSON_FACTORY = new JacksonFactory();

    /** URL for Dailymotion API. */
    public static class OnePlannerUrl extends GenericUrl {

      public OnePlannerUrl(String encodedUrl) {
        super(encodedUrl);
      }

      @Key
      public String fields;
    }

    public String post(String urlStr, String body) {
		try {
	        HttpRequestFactory requestFactory =
	                HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
	                    @Override
	                  public void initialize(HttpRequest request) {
	                    request.setParser(new JsonObjectParser(JSON_FACTORY));
	                  }
	                });
			OnePlannerUrl url = new OnePlannerUrl(urlStr);
			HttpRequest request = requestFactory.buildPostRequest(url, ByteArrayContent.fromString("application/json", body));
			return request.execute().parseAsString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
    //C:\\Users\\gencode\\Dropbox\\Photos\\test1.jpg
    public String post(String urlStr, String filePath, String body) {
		try {
	        HttpRequestFactory requestFactory =
	                HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
	                    @Override
	                  public void initialize(HttpRequest request) {
	                    request.setParser(new JsonObjectParser(JSON_FACTORY));
	                  }
	                });
	        
	        File initialFile = new File(filePath);
	        InputStream fileContent = new FileInputStream(initialFile);
	        MultipartContent.Part partFile = new MultipartContent.Part()
            .setContent(new InputStreamContent("application/octet-stream", fileContent))
            .setHeaders(new HttpHeaders().set(
                    "Content-Disposition",
                    String.format("form-data; name=\"upfile\"; filename=\"%s\"", filePath) // TODO: escape fileName?
            ));
	        MultipartContent content = new MultipartContent()
            .setMediaType(new HttpMediaType("multipart/form-data").setParameter("boundary", UUID.randomUUID().toString()))
            .addPart(partFile);
    
	        MultipartContent.Part partJson = new MultipartContent.Part(
	        		ByteArrayContent.fromString("application/json", body));
	        partJson.setHeaders(new HttpHeaders().set(
	                "Content-Disposition", "form-data; name=\"json\""));
	        content.addPart(partJson);
	        
			OnePlannerUrl url = new OnePlannerUrl(urlStr);
			HttpRequest request = requestFactory.buildPostRequest(url, content);
			return request.execute().parseAsString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
    
    public void callGetHttp(String urlStr, Type type) {
		String resultHtml = "";

		try {
		    HttpRequestFactory requestFactory =
		            HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
		                @Override
		              public void initialize(HttpRequest request) {
		                request.setParser(new JsonObjectParser(JSON_FACTORY));
		              }
		            });
		    OnePlannerUrl url = new OnePlannerUrl(urlStr);
		    HttpRequest request = requestFactory.buildGetRequest(url);

		    com.google.api.client.http.HttpResponse response = request.execute();
		    resultHtml = response.parseAsString();
	    	logger.info("resultHtml:"+resultHtml);
	    } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}