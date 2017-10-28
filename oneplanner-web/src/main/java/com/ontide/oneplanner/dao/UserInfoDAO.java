package com.ontide.oneplanner.dao;

import java.util.List;
import java.util.Map;

import com.ontide.oneplanner.obj.UserInfo;
import com.ontide.oneplanner.obj.UserInfoWeb;

public interface UserInfoDAO {
	public void create(UserInfoWeb userInfo) throws Exception ;
	public int update(UserInfo userInfo) throws Exception ;
	public int update(UserInfoWeb userInfo) throws Exception ;
	public int delete(String userId) throws Exception ;
	public UserInfo get(String userId) throws Exception ;
	public UserInfoWeb getEx(String userId) throws Exception ;
	public List<UserInfo> getList(Map<String,String> params) throws Exception ;
	public List<UserInfoWeb> getListEx(Map<String,String> params) throws Exception ;
	public int getCnt(Map<String,String> params) throws Exception ;
}
