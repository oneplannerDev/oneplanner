package com.ontide.oneplanner.client;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ontide.oneplanner.etc.ReturnCode;
import com.ontide.oneplanner.io.ResultObj;
import com.ontide.oneplanner.obj.UserInfo;

public class GsonTest {

	@Test
	public void testRunUserInfoGson() {
		TestObj testObj = new TestObj();
		List<UserInfo> list = new ArrayList<UserInfo>();
		list.add(testObj.getUserA());
		list.add(testObj.getUserB());
		ResultObj<List<UserInfo>> result = new ResultObj<List<UserInfo>>();
		result.setResultCode(ReturnCode.SUCCESS.get());
		result.setResultMsg(ReturnCode.STR_SUCCESS.get());
		result.setItem(list);
		Gson gson = new Gson();
		Type type = new TypeToken<ResultObj<List<UserInfo>>>() {}.getType();
		String json = gson.toJson(result, type);
		System.out.println(json);
		ResultObj<List<UserInfo>> fromJson = gson.fromJson(json, type);
		
		for (UserInfo item : fromJson.getItem()) {
			System.out.println("item:"+item.getAuthYn());
		}
	}
	
	@Test
	public void testRunUserInfoGson2() {
		TestObj testObj = new TestObj();
		ResultObj<UserInfo> result = new ResultObj<UserInfo>();
		result.setResultCode(ReturnCode.SUCCESS.get());
		result.setResultMsg(ReturnCode.STR_SUCCESS.get());
		result.setItem(testObj.getUserB());
		Gson gson = new Gson();
		Type type = new TypeToken<ResultObj<UserInfo>>() {}.getType();
		String json = gson.toJson(result, type);
		System.out.println(json);
		ResultObj<UserInfo> fromJson = gson.fromJson(json, type);
		
		UserInfo item = fromJson.getItem();
			System.out.println("item:"+fromJson.getItem().getEmail());
	}
	
	@Test
	public void testRunGson() {
		List<GsonTask> list = new ArrayList<GsonTask>();
		for (int i = 0; i < 20; i++) {
	    	list.add(new GsonTask(i, "Test1", "Test2", GsonTask.Status.ASSIGNED, 10));
		}
		Gson gson = new Gson();
		Type type = new TypeToken<List<GsonTask>>() {}.getType();
		String json = gson.toJson(list, type);
		System.out.println(json);
		List<GsonTask> fromJson = gson.fromJson(json, type);
		
		for (GsonTask task : fromJson) {
			System.out.println(task);
		}
	}
}
