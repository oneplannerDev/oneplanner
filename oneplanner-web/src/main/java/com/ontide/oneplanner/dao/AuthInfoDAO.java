package com.ontide.oneplanner.dao;

import java.util.List;
import java.util.Map;

import com.ontide.oneplanner.obj.AuthInfo;

public interface AuthInfoDAO {
	public void create(AuthInfo authInfo) throws Exception ;
	public void update(AuthInfo authInfo) throws Exception ;
	public void delete(String userId, String authId) throws Exception ;
	public AuthInfo get(String userId, String authId) throws Exception ;
	public List<AuthInfo> getList(Map<String,String> params) throws Exception ;
}
