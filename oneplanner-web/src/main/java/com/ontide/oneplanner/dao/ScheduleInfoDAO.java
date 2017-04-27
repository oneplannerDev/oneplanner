package com.ontide.oneplanner.dao;

import java.util.List;
import java.util.Map;

import com.ontide.oneplanner.obj.ScheduleInfo;

public interface ScheduleInfoDAO {
	public void create(ScheduleInfo scheduleInfo) throws Exception ;
	public void create(List<ScheduleInfo> scheduleList) throws Exception ;
	public int update(ScheduleInfo scheduleInfo) throws Exception ;
	public int delete(String userId, String scheduleId) throws Exception ;
	public int delete(String userId) throws Exception ;
	public int setSchedulesDeleteFlag(Map<String,String> params) throws Exception ;
	public int deleteFromSchedules(Map<String,String> params) throws Exception ;
	public ScheduleInfo get(String userId, String scheduleId) throws Exception ;
	public List<ScheduleInfo> getList(Map<String,String> params) throws Exception ;
	public int getCnt(Map<String,String> params) throws Exception ;
}
