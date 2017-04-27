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

import com.ontide.oneplanner.obj.ScheduleInfo;

public class ScheduleInfoDAOImpl implements ScheduleInfoDAO {
	private static final Logger logger = LoggerFactory.getLogger(ScheduleInfoDAOImpl.class);

	private JdbcTemplate jdbcTemplate;
	
	public ScheduleInfoDAOImpl(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public void create(ScheduleInfo scheduleInfo) throws Exception {
		if (!scheduleInfo.getUserId().equals("")) {
			//insert
			String sql = "INSERT INTO schedule_info "
					+ "(user_id"
					+ ",schedule_id"
					+ ",schedule_name"
					+ ",start_date"
					+ ",end_date"
					
					+ ",memo"
					+ ",task_id"
					+ ",participants"
					+ ",location"
					+ ",location_url"
					
					+ ",alarm_yn"
					+ ",complete_yn"
					+ ",delete_yn"
					+ ",days"
					+ ",weeks"
					
					+ ",start_time"
					+ ",end_time"
					+ ",group_id"
					+ ",alarm_period"
					+ ",repeat_end_date)"
					+ " VALUES(?,?,?,?,?  ,?,?,?,?,?   ,?,?,?,?,? ,?,?,?,?,?)";
			logger.debug(String.format("insert:[%s] sql[%s]", scheduleInfo, sql));
			jdbcTemplate.update(sql, scheduleInfo.getUserId()
					, scheduleInfo.getScheduleId()
					, scheduleInfo.getScheduleName()
					, scheduleInfo.getStartDate()
					, scheduleInfo.getEndDate()
					, scheduleInfo.getMemo()
					, scheduleInfo.getTaskId()
					, scheduleInfo.getParticipants()
					, scheduleInfo.getLocation()
					, scheduleInfo.getLocationUrl()
					, scheduleInfo.getAlarmYn()
					, scheduleInfo.getCompleteYn()
					, scheduleInfo.getDeleteYn()
					, scheduleInfo.getDays()
					, scheduleInfo.getWeeks()
					, scheduleInfo.getStartTime()
					, scheduleInfo.getEndTime()
					, scheduleInfo.getGroupId()
					, scheduleInfo.getAlarmPeriod()
					, scheduleInfo.getRepeatEndDate()
					);	
		} else {
			throw new Exception(String.format("No key value userId[%s]scheduleId[%s]"
					, scheduleInfo.getUserId(), scheduleInfo.getScheduleId()));
		}
	}
	
	@Override
	public int update(ScheduleInfo scheduleInfo) throws Exception {
		if (!scheduleInfo.getUserId().equals("")) {
			//update
			String sql = " UPDATE schedule_info "
					+ " SET "
					+ "  user_id = ?"
					+ " ,schedule_id = ?"
					+ " ,schedule_name = ?"
					+ " ,start_date = ?"
					+ " ,end_date = ?"
					+ " ,memo = ?"
					+ " ,task_id = ?"
					+ " ,participants = ?"
					+ " ,location = ?"
					+ " ,location_url = ?"
					+ " ,alarm_yn = ?"
					+ " ,complete_yn = ?"
					+ " ,delete_yn = ?"
					+ " ,days = ?"
					+ " ,weeks = ?"
					
					+ " ,start_time = ?"
					+ " ,end_time = ?"
					+ " ,group_id = ?"
					+ " ,alarm_period = ?"
					+ " ,repeat_end_date = ?"

					+ ", update_date = now()"
					+ " WHERE user_id = ? AND schedule_id = ?";
			logger.debug(String.format("update:[%s] sql[%s]", scheduleInfo, sql));
			return jdbcTemplate.update(sql, scheduleInfo.getUserId()
					               , scheduleInfo.getScheduleId()
					               , scheduleInfo.getScheduleName(), scheduleInfo.getStartDate()
					, scheduleInfo.getEndDate(), scheduleInfo.getMemo(), scheduleInfo.getTaskId(), scheduleInfo.getParticipants()
					, scheduleInfo.getLocation(), scheduleInfo.getLocationUrl()
					, scheduleInfo.getAlarmYn(), scheduleInfo.getCompleteYn(), scheduleInfo.getDeleteYn()
					, scheduleInfo.getDays(), scheduleInfo.getWeeks()
					, scheduleInfo.getStartTime(), scheduleInfo.getEndTime()
					, scheduleInfo.getGroupId(), scheduleInfo.getAlarmPeriod()
					, scheduleInfo.getRepeatEndDate()
					
					, scheduleInfo.getUserId(), scheduleInfo.getScheduleId()
					);
		} else {
			throw new Exception(String.format("No key value userId[%s]scheduleId[%s]"
					, scheduleInfo.getUserId(), scheduleInfo.getScheduleId()));
		}
	}

	@Override
	public int setSchedulesDeleteFlag(Map<String,String> params) throws Exception {
		String sql = " UPDATE schedule_info "
				+ " SET "
				+ "  delete_yn = 'Y'"
				+ ", update_date = now()"
				+ " WHERE user_id  = ?";
		String userId = "";
		String scheduleList = "";
		for (Entry<String, String> entry : params.entrySet()) {
			if  ("".equals(entry.getValue())) continue;
			if (entry.getKey().equals("userId"))
				userId = entry.getValue();
			if (entry.getKey().equals("scheduleList")) 
				scheduleList = entry.getValue();
		}
		sql += " and schedule_id in ("+scheduleList+")";

		logger.debug(String.format("setSchedulesDeleteFlag:[%s] sql[%s]", userId, sql));
		return jdbcTemplate.update(sql, userId);
	}
	
	
	@Override
	public int deleteFromSchedules(Map<String,String> params) throws Exception {
		String sql = "delete from schedule_info "
				+ " WHERE user_id  = ?";
		String userId = "";
		String scheduleList = "";
		for (Entry<String, String> entry : params.entrySet()) {
			if  ("".equals(entry.getValue())) continue;
			if (entry.getKey().equals("userId"))
				userId = entry.getValue();
			if (entry.getKey().equals("scheduleList")) 
				scheduleList = entry.getValue();
		}
		sql += " and schedule_id in ("+scheduleList+")";
		logger.debug(String.format("deleteFromSchedules:[%s] sql[%s]", userId, sql));
		return jdbcTemplate.update(sql, userId);
	}
	
	@Override
	public int delete(String userId, String scheduleId) throws Exception {
		if (!userId.equals("")||!scheduleId.equals("")) {
			String sql = "Delete from schedule_info where user_id = ? AND schedule_id = ?";
			logger.debug(String.format("delete:UserId[%s]ScheduleId[%s] sql[%s]", userId, scheduleId, sql));
			return jdbcTemplate.update(sql, userId, scheduleId);
		} else {
			throw new Exception(String.format("No key value userId[%s]scheduleId[%s]"
					, userId, scheduleId));
		}
	}

	@Override
	public int delete(String userId) throws Exception {
		if (!userId.equals("")) {
			String sql = "Delete from schedule_info where user_id = ? ";
			logger.debug(String.format("delete:UserId[%s]sql[%s]", userId, sql));
			return jdbcTemplate.update(sql, userId);
		} else {
			throw new Exception(String.format("No key value userId[%s]", userId));
		}
	}
	
	@Override
	public ScheduleInfo get(String userId, String scheduleId) {
		String sql = " SELECT user_id,schedule_id,schedule_name,start_date,end_date"
				+",memo,task_id,participants,location,location_url"
				+",alarm_yn,complete_yn,delete_yn, days, weeks"
				+",start_time, end_time, group_id, alarm_period, repeat_end_date"
				+" FROM schedule_info where user_id ='"+userId+"' AND schedule_id = '"+scheduleId+"'";
		logger.info("get: sql:"+sql);
		return jdbcTemplate.query(sql, new ResultSetExtractor<ScheduleInfo>(){
			@Override
			public ScheduleInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					ScheduleInfo scheduleInfo = new ScheduleInfo();
					scheduleInfo.setUserId(rs.getString("user_id"));
					scheduleInfo.setScheduleId(rs.getString("schedule_id"));
					scheduleInfo.setScheduleName(rs.getString("schedule_name"));
					scheduleInfo.setStartDate(rs.getString("start_date"));
					scheduleInfo.setEndDate(rs.getString("end_date"));
					scheduleInfo.setMemo(rs.getString("memo"));
					scheduleInfo.setTaskId(rs.getString("task_id"));
					scheduleInfo.setParticipants(rs.getString("participants"));
					scheduleInfo.setLocation(rs.getString("location"));
					scheduleInfo.setLocationUrl(rs.getString("location_url"));
					scheduleInfo.setAlarmYn(rs.getString("alarm_yn"));
					scheduleInfo.setCompleteYn(rs.getString("complete_yn"));
					scheduleInfo.setDeleteYn(rs.getString("delete_yn"));
					scheduleInfo.setDays(rs.getString("days"));
					scheduleInfo.setWeeks(rs.getString("weeks"));
					
					scheduleInfo.setStartTime(rs.getString("start_time"));
					scheduleInfo.setEndTime(rs.getString("end_time"));
					scheduleInfo.setGroupId(rs.getString("group_id"));
					scheduleInfo.setAlarmPeriod(rs.getString("alarm_period"));
					scheduleInfo.setRepeatEndDate(rs.getString("repeat_end_date"));
					return scheduleInfo;
				}
				return null;
			}
			
		});
	}
	
	public List<ScheduleInfo> getList(Map<String,String> params) throws Exception {
		String sql = " SELECT user_id,schedule_id,schedule_name,start_date,end_date"
				+",memo,task_id,participants,location,location_url"
				+",alarm_yn,complete_yn,delete_yn, days, weeks"
				+",start_time, end_time, group_id, alarm_period, repeat_end_date"
				+" FROM schedule_info where user_id = ? ";
		String userId = "";
		for (Entry<String, String> entry : params.entrySet()) {
			if  ("".equals(entry.getValue())) continue;
			if (entry.getKey().equals("userId")) 
				userId = entry.getValue();
			if (entry.getKey().equals("userName"))
				sql += " and user_name like '%"+entry.getValue()+"%'";
			if (entry.getKey().equals("taskId")) 
				sql += " and task_id like '%"+entry.getValue()+"%'";
			if (entry.getKey().equals("deleteYn"))
				sql += " and delete_yn = '"+entry.getValue()+"'";
		}
		logger.debug("getList: userId["+userId+"]sql:"+sql);
		
		List<ScheduleInfo> listScheduleInfo = jdbcTemplate.query(sql, new RowMapper<ScheduleInfo>(){
			@Override
			public ScheduleInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				ScheduleInfo scheduleInfo = new ScheduleInfo();
				scheduleInfo.setUserId(rs.getString("user_id"));
				scheduleInfo.setScheduleId(rs.getString("schedule_id"));
				scheduleInfo.setScheduleName(rs.getString("schedule_name"));
				scheduleInfo.setStartDate(rs.getString("start_date"));
				scheduleInfo.setEndDate(rs.getString("end_date"));
				scheduleInfo.setMemo(rs.getString("memo"));
				scheduleInfo.setTaskId(rs.getString("task_id"));
				scheduleInfo.setParticipants(rs.getString("participants"));
				scheduleInfo.setLocation(rs.getString("location"));
				scheduleInfo.setLocationUrl(rs.getString("location_url"));
				scheduleInfo.setAlarmYn(rs.getString("alarm_yn"));
				scheduleInfo.setCompleteYn(rs.getString("complete_yn"));
				scheduleInfo.setDeleteYn(rs.getString("delete_yn"));
				scheduleInfo.setDays(rs.getString("days"));
				scheduleInfo.setWeeks(rs.getString("weeks"));
				scheduleInfo.setStartTime(rs.getString("start_time"));
				scheduleInfo.setEndTime(rs.getString("end_time"));
				scheduleInfo.setGroupId(rs.getString("group_id"));
				scheduleInfo.setAlarmPeriod(rs.getString("alarm_period"));
				scheduleInfo.setRepeatEndDate(rs.getString("repeat_end_date"));
				return scheduleInfo;
			}
		}, userId);
		return listScheduleInfo;

	}

	
	public int getCnt(Map<String,String> params) throws Exception {
		String sql = "SELECT count(*) "
				+" FROM schedule_info where user_id = ?";
		String userId = "";
		for (Entry<String, String> entry : params.entrySet()) {
			if  ("".equals(entry.getValue())) continue;
			if (entry.getKey().equals("userId")) 
				userId = entry.getValue();
			if (entry.getKey().equals("userName"))
				sql += " and user_name like '%"+entry.getValue()+"%'";
			if (entry.getKey().equals("deleteYn"))
				sql += " and delete_yn = '"+entry.getValue()+"'";
			if (entry.getKey().equals("taskList")) {
				sql += " and task_id in ("+entry.getValue()+")";
			}
		}
		logger.debug("getCnt: userId["+userId+"]sql:"+sql);
		
		return  jdbcTemplate.queryForObject(sql, Integer.class, userId);
	}

	@Override
	public void create(List<ScheduleInfo> scheduleList) throws Exception {
		String sql = "INSERT INTO schedule_info "
				+ "(user_id,schedule_id,schedule_name,start_date,end_date"
				+ ",memo,task_id,participants,location,location_url"
				+ ",alarm_yn,complete_yn,delete_yn,days,weeks"
				+ ",start_time,end_time,group_id,alarm_period,repeat_end_date)"
				+ " VALUES(?,?,?,?,?  ,?,?,?,?,?   ,?,?,?,?,? ,?,?,?,?,?)";
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for (ScheduleInfo  scheduleInfo : scheduleList) {
			if ("".equals(scheduleInfo.getUserId())||scheduleInfo.getUserId() == null) {
				throw new Exception(String.format("No key value userId[%s]scheduleId[%s]"
						, scheduleInfo.getUserId(), scheduleInfo.getScheduleId()));
			}
			Object[] arrParam = {
					scheduleInfo.getUserId()
					, scheduleInfo.getScheduleId()
					, scheduleInfo.getScheduleName()
					, scheduleInfo.getStartDate()
					, scheduleInfo.getEndDate()
					, scheduleInfo.getMemo()
					, scheduleInfo.getTaskId()
					, scheduleInfo.getParticipants()
					, scheduleInfo.getLocation()
					, scheduleInfo.getLocationUrl()
					, scheduleInfo.getAlarmYn()
					, scheduleInfo.getCompleteYn()
					, scheduleInfo.getDeleteYn()
					, scheduleInfo.getDays()
					, scheduleInfo.getWeeks()
					, scheduleInfo.getStartTime()
					, scheduleInfo.getEndTime()
					, scheduleInfo.getGroupId()
					, scheduleInfo.getAlarmPeriod()
					, scheduleInfo.getRepeatEndDate()
			};
			batchArgs.add(arrParam);
			logger.info(String.format("arrParam[%s][%s][%s][%s][%s]"
					+ "[%s][%s][%s][%s][%s]"
					+ "[%s][%s][%s][%s][%s]"
					+ "[%s][%s][%s][%s][%s]"
					, scheduleInfo.getUserId()
					, scheduleInfo.getScheduleId()
					, scheduleInfo.getScheduleName()
					, scheduleInfo.getStartDate()
					, scheduleInfo.getEndDate()
					, scheduleInfo.getMemo()
					, scheduleInfo.getTaskId()
					, scheduleInfo.getParticipants()
					, scheduleInfo.getLocation()
					, scheduleInfo.getLocationUrl()
					, scheduleInfo.getAlarmYn()
					, scheduleInfo.getCompleteYn()
					, scheduleInfo.getDeleteYn()
					, scheduleInfo.getDays()
					, scheduleInfo.getWeeks()
					, scheduleInfo.getStartTime()
					, scheduleInfo.getEndTime()
					, scheduleInfo.getGroupId()
					, scheduleInfo.getAlarmPeriod()
					, scheduleInfo.getRepeatEndDate()));
		}
		logger.debug(String.format("insert:sql[%s]", sql));
		jdbcTemplate.batchUpdate(sql, batchArgs);	
	}
}
