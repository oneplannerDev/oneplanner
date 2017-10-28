package com.ontide.oneplanner.io;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map.Entry;

public class RequestMap extends HashMap<String, Object> implements Serializable {

	public RequestMap() {	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("RequestMap: ");
		for (Entry<String, Object> entry :  this.entrySet()) {
			
			if (entry.getValue() instanceof RequestMap) 
				sb.append(String.format("{%s, %s}",entry.getKey(), ((RequestMap)entry).toString()));
			else 
				sb.append(String.format("{%s, %s}",entry.getKey(), entry.getValue()));
		}
		return sb.toString();
	}
}
