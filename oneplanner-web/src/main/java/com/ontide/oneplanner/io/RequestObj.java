package com.ontide.oneplanner.io;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestObj implements Serializable {
	private static final Logger logger = LoggerFactory.getLogger(RequestObj.class);
	private static final long serialVersionUID = 1L;
	String userId;
	List<String> keyList;

	public RequestObj() {
		// TODO Auto-generated constructor stub
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<String> getKeyList() {
		return keyList;
	}

	public void setKeyList(List<String> keyList) {
		this.keyList = keyList;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (String key : keyList) {
			sb.append("["+key+"]");
		}
		return String.format("userId[%s]keyList[%s]", userId, sb.toString());
	}
}
