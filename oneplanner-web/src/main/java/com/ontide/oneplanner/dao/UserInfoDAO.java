package com.ontide.oneplanner.dao;

import java.util.List;
import java.util.Map;

import com.ontide.oneplanner.obj.UserInfo;

public interface UserInfoDAO {
	public void create(UserInfo userInfo) throws Exception ;
	public int update(UserInfo userInfo) throws Exception ;
	public int delete(String userId) throws Exception ;
	public UserInfo get(String userId) throws Exception ;
	public List<UserInfo> getList(Map<String,String> params) throws Exception ;
}
