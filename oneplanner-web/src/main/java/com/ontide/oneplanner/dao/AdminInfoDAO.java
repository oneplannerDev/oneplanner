package com.ontide.oneplanner.dao;

import java.util.List;
import java.util.Map;

import com.ontide.oneplanner.obj.AdminInfo;
import com.ontide.oneplanner.obj.StatusInfo;
import com.ontide.oneplanner.obj.UserInfoWeb;

public interface AdminInfoDAO {
	public int create(AdminInfo adminInfo) throws Exception ;
	public int update(AdminInfo adminInfo) throws Exception ;
	public int updateAccessTime(AdminInfo adminInfo) throws Exception ;
	public AdminInfo get(String userId) throws Exception ;
	public StatusInfo getStatus() throws Exception;
	public List<UserInfoWeb> getUserInfoWebList(Map<String,String> params) throws Exception;
}
