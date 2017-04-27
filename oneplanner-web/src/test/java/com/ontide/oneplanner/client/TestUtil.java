package com.ontide.oneplanner.client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class TestUtil {
	
	static String getRandomUserId() {
		return RandomStringUtils.randomAlphanumeric(8);
	}
	
	static int getRandomNumber(int limit) {
		return (new Random()).nextInt(limit);
	}
}
