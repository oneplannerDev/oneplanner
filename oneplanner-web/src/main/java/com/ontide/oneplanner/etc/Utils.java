package com.ontide.oneplanner.etc;

import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;

public class Utils {
	public static String inItems(List<String> itemList) {
		StringBuffer sb = new StringBuffer();
		sb.append("(");
		int i = 0;
		for (String user : itemList) {
			if (i == 0)
				sb.append("'"+user+"'");
			else
				sb.append(",'"+user+"'");
			i++;						
		}
		if (i ==  0) {
			sb.append("'')");
		} else {
			sb.append(")");
		}
		return sb.toString();
	}
	
    public static String getBase64String(String str) {
        byte[] bytesEncoded = Base64.encodeBase64(str.getBytes());
        String encodedStr = new String(bytesEncoded).replaceAll("(?:\\r\\n|\\n\\r|\\n|\\r)", "");
        System.out.println("Encoded value: "+encodedStr);
        return encodedStr;
    }
    
	public static String getBase64StringRecover(String str) {
    	if (str.length() % 4 != 0) 
    		str += new String(new char[(4 - str.length() % 4)]).replace('\0','=');
    	return str;
    }
	
	public static String getRandomPassword() {
    	return RandomStringUtils.randomAlphanumeric(8);
    }
	
	public static String getListForQuery(List<String> strList) {
		StringBuffer sb =  new StringBuffer();
		for (String key : strList) {
			if (sb.length()==0) {
				sb.append("'"+key+"'");
			} else {
				sb.append(",'"+key+"'");
			}
		}
		return sb.toString();
	}
	
	public static <T> String getListToString(List<T> theList) {
		StringBuffer  sb =  new StringBuffer();
		for (T item : theList) {
			sb.append("["+item.toString()+"]");
		}
		return sb.toString();
	}

	public static String YYYYMMDD(){
		return new java.text.SimpleDateFormat("yyyyMMdd").format(new Date());
	}
	public static String yyyyMMddHHmmss(){
		return new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	}
	
	public static String unCamel(String str) {
        String regex = "([a-z])([A-Z]+)";
        String replacement = "$1_$2";
        return str.replaceAll(regex, replacement)
                           .toLowerCase();
	}
}
