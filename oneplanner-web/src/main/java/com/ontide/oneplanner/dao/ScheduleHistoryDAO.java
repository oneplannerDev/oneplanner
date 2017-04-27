package com.ontide.oneplanner.dao;

import java.util.List;
import java.util.Map;

import com.ontide.oneplanner.obj.ScheduleHistory;

public interface ScheduleHistoryDAO {
	public void create(ScheduleHistory scheduleHistory) throws Exception ;
	public void create(List<ScheduleHistory> scheduleHistoryList) throws Exception ;
	public int update(ScheduleHistory scheduleInfo) throws Exception ;
	public int delete(ScheduleHistory scheduleHistory) throws Exception ;
	public int setScheduleHistoriesDeleteFlag(Map<String,String> params) throws Exception ;
	public int deleteFromScheduleHistories(Map<String,String> params) throws Exception ;
	public ScheduleHistory get(ScheduleHistory scheduleHistory) throws Exception ;
	public List<ScheduleHistory> getList(Map<String,String> params) throws Exception ;
	public int getCnt(Map<String,String> params) throws Exception ;
}
