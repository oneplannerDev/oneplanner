package com.ontide.oneplanner.dao;

import java.util.List;
import java.util.Map;

import com.ontide.oneplanner.obj.TodayInfo;

public interface TodayInfoDAO {
	public void create(TodayInfo todayInfo) throws Exception ;
	public int update(TodayInfo todayInfo) throws Exception ;
	public int delete(String today, String contSeq) throws Exception ;
	public TodayInfo get(String today, String contSeq) throws Exception ;
	public List<TodayInfo> getList(Map<String,String> params) throws Exception ;
}
