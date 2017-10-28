package com.ontide.oneplanner.dao;

import java.util.List;
import java.util.Map;

import com.ontide.oneplanner.obj.TaskInfo;
import com.ontide.oneplanner.obj.TaskInfoOut;

public interface TaskInfoDAO {
	public void create(TaskInfo taskInfo) throws Exception ;
	public void create(List<TaskInfo> taskInfoList) throws Exception ;
	public int update(TaskInfo taskInfo) throws Exception ;
	public int delete(String userId, String taskId) throws Exception ;
	public int delete(String userId) throws Exception ;
	public int setTasksDeleteFlag(Map<String,String> params) throws Exception ;
	public int deleteFromTasks(Map<String,String> params) throws Exception ;
	public TaskInfoOut get(String userId, String taskId) throws Exception ;
	public List<TaskInfoOut> getList(Map<String,String> params) throws Exception ;
	public List<TaskInfoOut> getListWeb(Map<String,String> params) throws Exception ;
	public int getCnt(Map<String,String> params) throws Exception ;
}
