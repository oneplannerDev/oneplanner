package com.ontide.oneplanner.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import com.ontide.oneplanner.obj.TaskInfo;
import com.ontide.oneplanner.obj.TaskInfoOut;

public class TaskInfoDAOImpl implements TaskInfoDAO {
	private static final Logger logger = LoggerFactory.getLogger(TaskInfoDAOImpl.class);

	private JdbcTemplate jdbcTemplate;
	
	public TaskInfoDAOImpl(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public void create(TaskInfo taskInfo) throws Exception {
		if (!taskInfo.getUserId().equals("")) {
			//insert
			String sql = "INSERT INTO task_info (user_id,task_id,task_name,task_type,start_date, end_date"
					+ ",frequency_cnt,frequency_period,progress,participants, color, days, weeks, start_time, end_time,delete_yn)"
					+ " VALUES(?,?,?,?,? ,?,?,?,?,?  ,?,?,?,?,?,?)";
			logger.debug(String.format("insert:[%s] sql[%s]", taskInfo, sql));
			jdbcTemplate.update(sql
					, taskInfo.getUserId()
					, taskInfo.getTaskId()
					, taskInfo.getTaskName()
					, taskInfo.getTaskType()
					, taskInfo.getStartDate()
					, taskInfo.getEndDate()
					, taskInfo.getFrequencyCnt()
					, taskInfo.getFrequencyPeriod()
					, taskInfo.getProgress()
					, taskInfo.getParticipants()
					, taskInfo.getColor()
					, taskInfo.getDays()
					, taskInfo.getWeeks()
					, taskInfo.getStartTime()
					, taskInfo.getEndTime()
					, taskInfo.getDeleteYn());	
		} else {
			throw new Exception(String.format("No key value userId[%s]taskId[%s]"
					, taskInfo.getUserId(), taskInfo.getTaskId()));
		}
	}
	
	@Override
	public int update(TaskInfo taskInfo) throws Exception {
		if (!taskInfo.getUserId().equals("")) {
			//update
			String sql = "UPDATE task_info SET"
					+ "  user_id = ?"
					+ ", task_id = ?"
					+ ", task_name = ?"
					+ ", task_type = ?"
					+ ", start_date = ?"
					+ ", end_date = ?"
					+ ", frequency_cnt = ?"
					+ ", frequency_period = ?"
					+ ", progress = ?"
					+ ", participants = ?"
					+ ", color = ?"
					+ ", days = ?"
					+ ", weeks = ?"
					+ ", start_time = ?"
					+ ", end_time = ?"
					+ ", delete_yn = ?"
					+ ", update_date = now()"
					+ " WHERE user_id = ? AND task_id = ?";
			logger.debug(String.format("update:[%s] sql[%s]", taskInfo, sql));
			return jdbcTemplate.update(sql, taskInfo.getUserId()
					, taskInfo.getTaskId()
					, taskInfo.getTaskName()
					, taskInfo.getTaskType()
					, taskInfo.getStartDate()
					, taskInfo.getEndDate()
					, taskInfo.getFrequencyCnt()
					, taskInfo.getFrequencyPeriod()
					, taskInfo.getProgress()
					, taskInfo.getParticipants()
					, taskInfo.getColor()
					, taskInfo.getDays()
					, taskInfo.getWeeks()
					, taskInfo.getStartTime()
					, taskInfo.getEndTime()
					, taskInfo.getDeleteYn()
					, taskInfo.getUserId()
					, taskInfo.getTaskId()
					);
		} else {
			throw new Exception(String.format("No key value userId[%s]taskId[%s]"
					, taskInfo.getUserId(), taskInfo.getTaskId()));
		}
	}

	@Override
	public int delete(String userId, String taskId) throws Exception {
		if (!userId.equals("")) {
			String sql = "Delete from task_info WHERE user_id = ? AND task_id = ?";
			logger.info(String.format("delete:userId[%s]taskId[%s] sql[%s]", userId, taskId, sql));
			return jdbcTemplate.update(sql, userId, taskId);
		} else {
			throw new Exception(String.format("No key value userId[%s]taskId[%s]"
					, userId, taskId));
		}
	}
	
	@Override
	public int setTasksDeleteFlag(Map<String,String> params) throws Exception {
		String sql = "UPDATE task_info "
				+ " SET "
				+ "  delete_yn = 'Y'"
				+ ", update_date = now()"
				+ " WHERE user_id  = ?";
		String userId = "";
		String taskList = "";
		for (Entry<String, String> entry : params.entrySet()) {
			if  ("".equals(entry.getValue())) continue;
			if (entry.getKey().equals("userId")) 
				userId = entry.getValue();
			if (entry.getKey().equals("taskList"))
				taskList = entry.getValue();
		}
		sql += " and task_id in ("+taskList+")";
		logger.info(String.format("setTasksDeleteFlag userId[%s] sql[%s]", userId, sql));
		
		if (!userId.equals("")) {
			return jdbcTemplate.update(sql, userId);
		} else {
			throw new Exception(String.format("No key value userId[%s]", userId));
		}
	} 
	
	@Override
	public int deleteFromTasks(Map<String,String> params) throws Exception {
		String sql = "delete from task_info "
				+ " WHERE user_id  = ?";
		String userId = "";
		String taskList = "";
		for (Entry<String, String> entry : params.entrySet()) {
			if  ("".equals(entry.getValue())) continue;
			if (entry.getKey().equals("userId")) 
				userId = entry.getValue();
			if (entry.getKey().equals("taskList"))
				taskList = entry.getValue();
		}
		sql += " and task_id in ("+taskList+")";
		logger.info(String.format("deleteFromTasks userId[%s] sql[%s]", userId, sql));
		
		if (!userId.equals("")) {
			return jdbcTemplate.update(sql, userId);
		} else {
			throw new Exception(String.format("No key value userId[%s]", userId));
		}
	} 
	
	@Override
	public int delete(String userId) throws Exception {
		if (!userId.equals("")) {
			String sql = "Delete from task_info WHERE user_id = ? ";
			logger.debug(String.format("delete:userId[%s] sql[%s]", userId, sql));
			return jdbcTemplate.update(sql, userId);
		} else {
			throw new Exception(String.format("No key value userId[%s]", userId));
		}
	}

	@Override
	public TaskInfoOut get(String userId, String taskId) {
		String sql = "SELECT user_id,task_id,task_name,task_type "
				+",start_date,end_date,frequency_cnt,frequency_period,progress,participants,color,days,weeks,start_time,end_time,delete_yn "
				+" FROM task_info where user_id ='"+userId+"' and task_id = '"+taskId+"'";
		logger.info("get: sql:"+sql);
		return jdbcTemplate.query(sql, new ResultSetExtractor<TaskInfoOut>(){
			@Override
			public TaskInfoOut extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					TaskInfoOut taskInfo = new TaskInfoOut();
					taskInfo.setUserId(rs.getString("user_id"));
					taskInfo.setTaskId(rs.getString("task_id"));
					taskInfo.setTaskName(rs.getString("task_name"));
					taskInfo.setTaskType(rs.getString("task_type"));
					taskInfo.setStartDate(rs.getString("start_date"));
					taskInfo.setEndDate(rs.getString("end_date"));
					taskInfo.setFrequencyCnt(rs.getInt("frequency_cnt"));
					taskInfo.setFrequencyPeriod(rs.getString("frequency_period"));
					taskInfo.setProgress(rs.getInt("progress"));
					taskInfo.setParticipants(rs.getString("participants"));
					taskInfo.setColor(rs.getInt("color"));
					taskInfo.setDays(rs.getString("days"));
					taskInfo.setWeeks(rs.getString("weeks"));
					taskInfo.setStartTime(rs.getString("start_time"));
					taskInfo.setEndTime(rs.getString("end_time"));
					taskInfo.setDeleteYn(rs.getString("delete_yn"));
					return taskInfo;
				}
				return null;
			}
			
		});
	}
	
	public List<TaskInfoOut> getListWeb(Map<String,String> params) throws Exception {
		String orderBy = "";
		int recordCntPerPage = 0;
		int pageIndex = 0;
		String sql = "SELECT @rownum:=@rownum + 1 as row_number,t.*  FROM (" 
				+"SELECT user_id,task_id,task_name,task_type "
				+",start_date,end_date,frequency_cnt,frequency_period,progress,participants,color,days,weeks,start_time,end_time,delete_yn "
				+" FROM task_info where user_id = ?";
		String userId = "";
		for (Entry<String, String> entry : params.entrySet()) {
			if  ("".equals(entry.getValue())) continue;
			if (entry.getKey().equals("userId")) 
				userId = entry.getValue();
			if (entry.getKey().equals("userName"))
				sql += " and user_name like '%"+entry.getValue()+"%'";
			if (entry.getKey().equals("deleteYn"))
				sql += " and delete_yn = '"+entry.getValue()+"'";
			if (entry.getKey().equals("orderBy")&&!entry.getValue().trim().equals(""))
				orderBy = entry.getValue();
			if (entry.getKey().equals("recCntPerPage")&&!entry.getValue().trim().equals(""))
				recordCntPerPage= Integer.parseInt(entry.getValue());
			if (entry.getKey().equals("pageIndex")&&!entry.getValue().trim().equals(""))
				pageIndex = Integer.parseInt(entry.getValue());
		}
		sql +=" ) t, (SELECT @rownum := 0) r ";

		if (!"".equals(orderBy))
			sql += " order by "+orderBy;
		sql += "limit "+recordCntPerPage+" offset "+pageIndex;

		logger.debug("getList: userId["+userId+"]sql:"+sql);
		
		List<TaskInfoOut> listTaskInfo = jdbcTemplate.query(sql, new RowMapper<TaskInfoOut>(){
			@Override
			public TaskInfoOut mapRow(ResultSet rs, int rowNum) throws SQLException {
				TaskInfoOut taskInfo = new TaskInfoOut();
				taskInfo.setUserId(rs.getString("user_id"));
				taskInfo.setTaskId(rs.getString("task_id"));
				taskInfo.setTaskName(rs.getString("task_name"));
				taskInfo.setTaskType(rs.getString("task_type"));
				taskInfo.setStartDate(rs.getString("start_date"));
				taskInfo.setEndDate(rs.getString("end_date"));
				taskInfo.setFrequencyCnt(rs.getInt("frequency_cnt"));
				taskInfo.setFrequencyPeriod(rs.getString("frequency_period"));
				taskInfo.setProgress(rs.getInt("progress"));
				taskInfo.setParticipants(rs.getString("participants"));
				taskInfo.setColor(rs.getInt("color"));
				taskInfo.setDays(rs.getString("days"));
				taskInfo.setWeeks(rs.getString("weeks"));
				taskInfo.setStartTime(rs.getString("start_time"));
				taskInfo.setEndTime(rs.getString("end_time"));
				taskInfo.setDeleteYn(rs.getString("delete_yn"));
				return taskInfo;
			}
		}, userId);
		return listTaskInfo;

	}
	
	public List<TaskInfoOut> getList(Map<String,String> params) throws Exception {
		String sql = "SELECT user_id,task_id,task_name,task_type "
				+",start_date,end_date,frequency_cnt,frequency_period,progress,participants,color,days,weeks,start_time,end_time,delete_yn "
				+" FROM task_info where user_id = ?";
		String userId = "";
		for (Entry<String, String> entry : params.entrySet()) {
			if  ("".equals(entry.getValue())) continue;
			if (entry.getKey().equals("userId")) 
				userId = entry.getValue();
			if (entry.getKey().equals("userName"))
				sql += " and user_name like '%"+entry.getValue()+"%'";
			if (entry.getKey().equals("deleteYn"))
				sql += " and delete_yn = '"+entry.getValue()+"'";
		}
		logger.debug("getList: userId["+userId+"]sql:"+sql);
		
		List<TaskInfoOut> listTaskInfo = jdbcTemplate.query(sql, new RowMapper<TaskInfoOut>(){
			@Override
			public TaskInfoOut mapRow(ResultSet rs, int rowNum) throws SQLException {
				TaskInfoOut taskInfo = new TaskInfoOut();
				taskInfo.setUserId(rs.getString("user_id"));
				taskInfo.setTaskId(rs.getString("task_id"));
				taskInfo.setTaskName(rs.getString("task_name"));
				taskInfo.setTaskType(rs.getString("task_type"));
				taskInfo.setStartDate(rs.getString("start_date"));
				taskInfo.setEndDate(rs.getString("end_date"));
				taskInfo.setFrequencyCnt(rs.getInt("frequency_cnt"));
				taskInfo.setFrequencyPeriod(rs.getString("frequency_period"));
				taskInfo.setProgress(rs.getInt("progress"));
				taskInfo.setParticipants(rs.getString("participants"));
				taskInfo.setColor(rs.getInt("color"));
				taskInfo.setDays(rs.getString("days"));
				taskInfo.setWeeks(rs.getString("weeks"));
				taskInfo.setStartTime(rs.getString("start_time"));
				taskInfo.setEndTime(rs.getString("end_time"));
				taskInfo.setDeleteYn(rs.getString("delete_yn"));
				return taskInfo;
			}
		}, userId);
		return listTaskInfo;

	}
	
	public int getCnt(Map<String,String> params) throws Exception {
		String sql = "SELECT count(*) "
				+" FROM task_info where user_id = ?";
		String userId = "";
		for (Entry<String, String> entry : params.entrySet()) {
			if  ("".equals(entry.getValue())) continue;
			if (entry.getKey().equals("userId")) 
				userId = entry.getValue();
			if (entry.getKey().equals("userName"))
				sql += " and user_name like '%"+entry.getValue()+"%'";
			if (entry.getKey().equals("deleteYn"))
				sql += " and delete_yn = '"+entry.getValue()+"'";
		}
		logger.debug("getCnt: userId["+userId+"]sql:"+sql);
		return  jdbcTemplate.queryForObject(sql, Integer.class,userId);
	}

	@Override
	public void create(List<TaskInfo> taskInfoList) throws Exception {

		//insert
		String sql = "INSERT INTO task_info (user_id,task_id,task_name,task_type,start_date, end_date"
				+ ",frequency_cnt,frequency_period,progress,participants, color, days, weeks, start_time, end_time, delete_yn)"
				+ " VALUES(?,?,?,?,? ,?,?,?,?,?  ,?,?,?,?,?,?)";
		List<Object[]> batchArgs = new ArrayList<Object[]>(); 
		for (TaskInfo taskInfo : taskInfoList) {
			
			if ("".equals(taskInfo.getUserId())||taskInfo.getUserId() == null) {
				throw new Exception(String.format("No key value userId[%s]taskId[%s]"
						, taskInfo.getUserId(), taskInfo.getTaskId()));
			}

			Object[] arrParam = {
					taskInfo.getUserId(),taskInfo.getTaskId(),taskInfo.getTaskName(), taskInfo.getTaskType()
					,taskInfo.getStartDate(), taskInfo.getEndDate(),taskInfo.getFrequencyCnt(), taskInfo.getFrequencyPeriod()
					,taskInfo.getProgress(), taskInfo.getParticipants(), taskInfo.getColor(), taskInfo.getDays(),taskInfo.getWeeks()
					,taskInfo.getStartTime(),taskInfo.getEndTime(),taskInfo.getDeleteYn()
			};
			batchArgs.add(arrParam);
			logger.info(String.format("arrParam userid[%s]taskId[%s]taskname[%s]"
					+ "tasktype[%s]startdate[%s]enddate[%s]freqcnt[%s]freqper[%s]prog[%s]parti[%s]color[%s]days[%s]weeks[%s]stime[%s]etime[%s]dyn[%s]"
					,taskInfo.getUserId(),taskInfo.getTaskId(),taskInfo.getTaskName(), taskInfo.getTaskType()
					,taskInfo.getStartDate(), taskInfo.getEndDate(),taskInfo.getFrequencyCnt(), taskInfo.getFrequencyPeriod()
					,taskInfo.getProgress(), taskInfo.getParticipants(), taskInfo.getColor(), taskInfo.getDays(),taskInfo.getWeeks()
					,taskInfo.getStartTime(),taskInfo.getEndTime(),taskInfo.getDeleteYn()));

		}
		logger.debug(String.format("insert:sql[%s]", sql));
		jdbcTemplate.batchUpdate(sql, batchArgs);	
	}
	
}
