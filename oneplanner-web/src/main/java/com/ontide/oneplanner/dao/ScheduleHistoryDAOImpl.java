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

import com.ontide.oneplanner.obj.ScheduleHistory;

public class ScheduleHistoryDAOImpl implements ScheduleHistoryDAO {
	private static final Logger logger = LoggerFactory.getLogger(ScheduleHistoryDAOImpl.class);

	private JdbcTemplate jdbcTemplate;
	
	public ScheduleHistoryDAOImpl(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public void create(ScheduleHistory scheduleHistory) throws Exception {
		if (!scheduleHistory.getUserId().equals("")) {
			//insert
			String sql = "INSERT INTO schedule_history "
					+ "(user_id"
					+ ",schedule_id"
					+ ",now_date"
					+ ",start_date"
					+ ",end_date"
					+ ",complete_yn"
					+ ",delete_yn)"
					+ " VALUES(?,?,?,?,?,?,?)";
			logger.debug(String.format("insert:[%s] sql[%s]", scheduleHistory, sql));
			jdbcTemplate.update(sql, scheduleHistory.getUserId()
					, scheduleHistory.getScheduleId()
					, scheduleHistory.getNowDate()
					, scheduleHistory.getStartDate()
					, scheduleHistory.getEndDate()
					, scheduleHistory.getCompleteYn()
					, scheduleHistory.getDeleteYn()
					);	
		} else {
			throw new Exception(String.format("No key value userId[%s]scheduleId[%s]"
					, scheduleHistory.getUserId(), scheduleHistory.getScheduleId()));
		}
	}
	
	@Override
	public int update(ScheduleHistory scheduleHistory) throws Exception {
		if (!scheduleHistory.getUserId().equals("")
				&& !scheduleHistory.getScheduleId().equals("")
				&& !scheduleHistory.getNowDate().equals("")) {
			//update
			String sql = " UPDATE schedule_history "
					+ " SET complete_yn = ?"
					+ " ,start_date = ?"
					+ " ,end_date = ?"
					+ " ,delete_yn = ?"
					+ " WHERE user_id = ? AND schedule_id = ? AND now_date = ?";
			logger.debug(String.format("update:[%s] sql[%s]", scheduleHistory, sql));
			return jdbcTemplate.update(sql, scheduleHistory.getCompleteYn(), scheduleHistory.getStartDate()
					, scheduleHistory.getEndDate(), scheduleHistory.getDeleteYn()
					, scheduleHistory.getUserId()
					, scheduleHistory.getScheduleId(), scheduleHistory.getNowDate()
					);
		} else {
			throw new Exception(String.format("No key value userId[%s]scheduleId[%s]nowDate[%s]"
					, scheduleHistory.getUserId(), scheduleHistory.getScheduleId(), scheduleHistory.getNowDate()));
		}
	}

	@Override
	public int delete(ScheduleHistory scheduleHistory) throws Exception {
		if (!scheduleHistory.getUserId().equals("")
				&& !scheduleHistory.getScheduleId().equals("")
				&& !scheduleHistory.getNowDate().equals("")) {
			String sql = "Delete from schedule_history WHERE user_id = ? AND schedule_id = ? AND now_date = ? ";
			logger.debug(String.format("delete:UserId[%s]ScheduleId[%s]nowDate[%s] sql[%s]"
					, scheduleHistory.getUserId(), scheduleHistory.getScheduleId()
					, scheduleHistory.getNowDate(), sql));
			return jdbcTemplate.update(sql, scheduleHistory.getUserId()
		               , scheduleHistory.getScheduleId(), scheduleHistory.getNowDate());
		} else {
			throw new Exception(String.format("No key value userId[%s]scheduleId[%s]nowDate[%s]"
					, scheduleHistory.getUserId(), scheduleHistory.getScheduleId(), scheduleHistory.getNowDate()));
		}
	}

	@Override
	public ScheduleHistory get(ScheduleHistory scheduleHistory) {
		String sql = " SELECT user_id,schedule_id,now_date,start_date,end_date,complete_yn,delete_yn"
				+" FROM schedule_history WHERE user_id = ? AND schedule_id = ? AND now_date = ? ";
		logger.info("get: sql:"+sql);
		return jdbcTemplate.query(sql, new ResultSetExtractor<ScheduleHistory>(){
			@Override
			public ScheduleHistory extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					ScheduleHistory scheduleHistory = new ScheduleHistory();
					scheduleHistory.setUserId(rs.getString("user_id"));
					scheduleHistory.setScheduleId(rs.getString("schedule_id"));
					scheduleHistory.setNowDate(rs.getString("now_date"));
					scheduleHistory.setStartDate(rs.getString("start_date"));
					scheduleHistory.setEndDate(rs.getString("end_date"));
					scheduleHistory.setCompleteYn(rs.getString("complete_yn"));
					scheduleHistory.setDeleteYn(rs.getString("delete_yn"));
					return scheduleHistory;
				}
				return null;
			}
			
		}, scheduleHistory.getUserId()
        , scheduleHistory.getScheduleId(), scheduleHistory.getNowDate());
	}
	
	/**
	 * 
	 */
	public List<ScheduleHistory> getListWeb(Map<String,String> params) throws Exception {
		String orderBy = "";
		int recordCntPerPage = 0;
		int pageIndex = 0;
		String sql = "SELECT @rownum:=@rownum + 1 as row_number,t.*  FROM (" 
				+" SELECT user_id,schedule_id,now_date,start_date,end_date,complete_yn,delete_yn"
				+" FROM schedule_history b where user_id = ? ";
		String userId = "";
		for (Entry<String, String> entry : params.entrySet()) {
			if  ("".equals(entry.getValue())) continue;
			if (entry.getKey().equals("userId")) 
				userId = entry.getValue();
			if (entry.getKey().equals("taskId")) 
				sql += "and exists (select * from schedule_info a where a.user_id = b.user_id" 
						+ "and a.schedule_id = b.schedule_id" 
						+ "and a.task_id = "+entry.getValue()+")";
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
		
		List<ScheduleHistory> listscheduleHistory = jdbcTemplate.query(sql, new RowMapper<ScheduleHistory>(){
			@Override
			public ScheduleHistory mapRow(ResultSet rs, int rowNum) throws SQLException {
				ScheduleHistory scheduleHistory = new ScheduleHistory();
				scheduleHistory.setUserId(rs.getString("user_id"));
				scheduleHistory.setScheduleId(rs.getString("schedule_id"));
				scheduleHistory.setNowDate(rs.getString("now_date"));
				scheduleHistory.setStartDate(rs.getString("start_date"));
				scheduleHistory.setEndDate(rs.getString("end_date"));
				scheduleHistory.setCompleteYn(rs.getString("complete_yn"));
				scheduleHistory.setDeleteYn(rs.getString("delete_yn"));
				return scheduleHistory;
			}
		},userId);
		return listscheduleHistory;

	}

	public List<ScheduleHistory> getList(Map<String,String> params) throws Exception {
		String sql = " SELECT user_id,schedule_id,now_date,start_date,end_date,complete_yn,delete_yn"
				+" FROM schedule_history b where user_id = ? ";
		String userId = "";
		for (Entry<String, String> entry : params.entrySet()) {
			if  ("".equals(entry.getValue())) continue;
			if (entry.getKey().equals("userId")) 
				userId = entry.getValue();
			if (entry.getKey().equals("taskId")) 
				sql += "and exists (select * from schedule_info a where a.user_id = b.user_id" 
						+ "and a.schedule_id = b.schedule_id" 
						+ "and a.task_id = "+entry.getValue()+")";
		}
		logger.debug("getList: userId["+userId+"]sql:"+sql);
		
		List<ScheduleHistory> listscheduleHistory = jdbcTemplate.query(sql, new RowMapper<ScheduleHistory>(){
			@Override
			public ScheduleHistory mapRow(ResultSet rs, int rowNum) throws SQLException {
				ScheduleHistory scheduleHistory = new ScheduleHistory();
				scheduleHistory.setUserId(rs.getString("user_id"));
				scheduleHistory.setScheduleId(rs.getString("schedule_id"));
				scheduleHistory.setNowDate(rs.getString("now_date"));
				scheduleHistory.setStartDate(rs.getString("start_date"));
				scheduleHistory.setEndDate(rs.getString("end_date"));
				scheduleHistory.setCompleteYn(rs.getString("complete_yn"));
				scheduleHistory.setDeleteYn(rs.getString("delete_yn"));
				return scheduleHistory;
			}
		},userId);
		return listscheduleHistory;

	}
	
	@Override
	public void create(List<ScheduleHistory> scheduleHistoryList)
			throws Exception {
		String sql = "INSERT INTO schedule_history "
				+ "(user_id"
				+ ",schedule_id"
				+ ",now_date"
				+ ",start_date"
				+ ",end_date"
				+ ",complete_yn"
				+ ",delete_yn)"
				+ " VALUES(?,?,?,?,?,?,?)";
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for (ScheduleHistory scheduleHistory :  scheduleHistoryList) {
			if ("".equals(scheduleHistory.getUserId())||scheduleHistory.getUserId() == null) {
				throw new Exception(String.format("No key value userId[%s]scheduleId[%s]"
						, scheduleHistory.getUserId(), scheduleHistory.getScheduleId()));
			}
			Object[] arrParam = { scheduleHistory.getUserId()
					, scheduleHistory.getScheduleId()
					, scheduleHistory.getNowDate()
					, scheduleHistory.getStartDate()
					, scheduleHistory.getEndDate()
					, scheduleHistory.getCompleteYn()
					, scheduleHistory.getDeleteYn()
			};
			batchArgs.add(arrParam);
			logger.info(String.format("arrParam[%s][%s][%s][%s][%s][%s][%s]"
					, scheduleHistory.getUserId()
					, scheduleHistory.getScheduleId()
					, scheduleHistory.getNowDate()
					, scheduleHistory.getStartDate()
					, scheduleHistory.getEndDate()
					, scheduleHistory.getCompleteYn()
					, scheduleHistory.getDeleteYn()));
		}	
		logger.debug(String.format("insert:sql[%s]", sql));
		jdbcTemplate.batchUpdate(sql, batchArgs);	
	}

	@Override
	public int setScheduleHistoriesDeleteFlag(Map<String, String> params)
			throws Exception {
		String sql = " UPDATE schedule_history "
				+ " SET delete_yn = 'Y'"
				+ ", update_date = now()"
				+ " WHERE user_id  = ?";
		String userId = "";
		String scheduleList = "";
		for (Entry<String, String> entry : params.entrySet()) {
			if  ("".equals(entry.getValue())) continue;
			if (entry.getKey().equals("userId"))
				userId = entry.getValue();
			if (entry.getKey().equals("scheduleHistoryList")) 
				scheduleList = entry.getValue();
		}
		sql += " and schedule_id in ("+scheduleList+")";

		logger.debug(String.format("setScheduleHistoriesDeleteFlag:[%s] sql[%s]", userId, sql));
		return jdbcTemplate.update(sql, userId);
	}

	@Override
	public int deleteFromScheduleHistories(Map<String, String> params) throws Exception {
		String sql = "delete from schedule_history "
				+ " WHERE user_id  = ?";
		String userId = "";
		String scheduleList = "";
		for (Entry<String, String> entry : params.entrySet()) {
			if  ("".equals(entry.getValue())) continue;
			if (entry.getKey().equals("userId"))
				userId = entry.getValue();
			if (entry.getKey().equals("scheduleHistoryList")) 
				scheduleList = entry.getValue();
		}
		sql += " and schedule_id in ("+scheduleList+")";
		logger.debug(String.format("deleteFromScheduleHistories:[%s] sql[%s]", userId, sql));
		return jdbcTemplate.update(sql, userId);	}

	@Override
	public int getCnt(Map<String, String> params) throws Exception {
		String sql = "SELECT count(*) "
				+" FROM schedule_history where user_id = ?";
		String userId = "";
		for (Entry<String, String> entry : params.entrySet()) {
			if  ("".equals(entry.getValue())) continue;
			if (entry.getKey().equals("userId")) 
				userId = entry.getValue();
			if (entry.getKey().equals("deleteYn"))
				sql += " and delete_yn = '"+entry.getValue()+"'";
			if (entry.getKey().equals("scheduleHistoryList")) {
				sql += " and schedule_id in ("+entry.getValue()+")";
			}
		}
		logger.debug("getCnt: userId["+userId+"]sql:"+sql);
		
		return  jdbcTemplate.queryForObject(sql, Integer.class, userId);	}


}
