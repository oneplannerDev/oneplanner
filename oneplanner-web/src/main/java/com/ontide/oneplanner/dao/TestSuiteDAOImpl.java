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

import com.ontide.oneplanner.etc.Constant;
import com.ontide.oneplanner.etc.Utils;
import com.ontide.oneplanner.obj.UserInfo;

public class TestSuiteDAOImpl implements UserInfoDAO {
	private static final Logger logger = LoggerFactory.getLogger(TestSuiteDAOImpl.class);

	private JdbcTemplate jdbcTemplate;
	
	public TestSuiteDAOImpl(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public void create(UserInfo userInfo) throws Exception {
		if (!userInfo.getUserId().equals("")) {
			//insert
			String sql = "INSERT INTO user_info (user_id,user_name,user_type,passwd,email"
					+ ",sex,birth_date,cal_type,cal_view_type,start_week"
					+ ",time_zone,widget_option)"
					+ " VALUES(?,?,?,?,?"
					+ ",?,?,?,?,?"
					+ ",?,?)";
			logger.debug(String.format("insert:[%s] sql[%s]", userInfo, sql));
			int ret = jdbcTemplate.update(sql, userInfo.getUserId()
					, userInfo.getUserName(), userInfo.getUserType(), userInfo.getPasswd(),userInfo.getEmail()
					, userInfo.getSex(), userInfo.getBirthDate(), userInfo.getCalType(), userInfo.getCalViewType()
					, userInfo.getStartWeek(),userInfo.getTimeZone(), userInfo.getWidgetOption());
			
			logger.info("userInfo.create ret="+ret);
		} else {
			throw new Exception(String.format("No key value [%s]", userInfo.getUserId()));
		}
	}
	
	@Override
	public int update(UserInfo userInfo) throws Exception {
		if (!userInfo.getUserId().equals("")) {
			//update
			String sql = "UPDATE user_info SET user_id = ? "
					+ " ,user_name = ?,user_type = ?,passwd = ?,email = ?,sex = ?,birth_date = ? "
					+ " ,cal_type = ?,cal_view_type = ?,start_week = ?,time_zone = ?,widget_option = ?, auth_yn = ? "
					+ " , delete_yn = ?, update_date = now()"
					+ "WHERE user_id = ?";
			logger.debug(String.format("update:[%s] sql[%s]", userInfo, sql));
			return jdbcTemplate.update(sql, userInfo.getUserId()
					, userInfo.getUserName(), userInfo.getUserType(), userInfo.getPasswd()
					, userInfo.getEmail(), userInfo.getSex(), userInfo.getBirthDate(), userInfo.getCalType()
					, userInfo.getCalViewType(), userInfo.getStartWeek(), userInfo.getTimeZone(), userInfo.getWidgetOption()
					, userInfo.getAuthYn(), userInfo.getDeleteYn() 
					, userInfo.getUserId()
					);
		} else {
			throw new Exception(String.format("No key value [%s]", userInfo.getUserId()));
		}
	}

	@Override
	public int delete(String userId) throws Exception {
		if (!userId.equals("")) {
			String sql = "Delete from user_info where user_id = ?";
			logger.debug(String.format("delete:[%s] sql[%s]", userId, sql));
			return jdbcTemplate.update(sql, userId);
		} else {
			throw new Exception(String.format("No key value [%s]", userId));
		}
	}

	@Override
	public UserInfo get(String userId) {
		String sql = "SELECT user_id,user_name,user_type,passwd,email,sex,birth_date "
				+",cal_type,cal_view_type,start_week,time_zone,widget_option,auth_yn,delete_yn"
				+" FROM user_info where user_id ='"+userId+"'";
		logger.info("get: sql:"+sql);
		return jdbcTemplate.query(sql, new ResultSetExtractor<UserInfo>(){
			@Override
			public UserInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					UserInfo userInfo = new UserInfo();
					userInfo.setUserId(rs.getString("user_id"));
					userInfo.setUserName(rs.getString("user_name"));
					userInfo.setUserType(rs.getString("user_type"));
					userInfo.setPasswd(rs.getString("passwd"));
					userInfo.setEmail(rs.getString("email"));
					userInfo.setSex(rs.getString("sex"));
					userInfo.setBirthDate(rs.getString("birth_date"));
					userInfo.setCalType(rs.getString("cal_type"));
					userInfo.setCalViewType(rs.getString("cal_view_type"));
					userInfo.setStartWeek(rs.getString("start_week"));
					userInfo.setTimeZone(rs.getString("time_zone"));
					userInfo.setWidgetOption(rs.getString("widget_option"));
					userInfo.setAuthYn(rs.getString("auth_yn"));
					userInfo.setDeleteYn(rs.getString("delete_yn"));
					return userInfo;
				}
				return null;
			}
			
		});
	}
	
	public List<UserInfo> getList(Map<String,String> params) throws Exception {
		String sql = "SELECT user_id,user_name,user_type,passwd,email,sex,birth_date "
				+",cal_type,cal_view_type,start_week,time_zone,widget_option,auth_yn,delete_yn"
				+" FROM user_info where 1 = 1";

		for (Entry<String, String> entry : params.entrySet()) {
			if  ("".equals(entry.getValue())) continue;
			if (entry.getKey().equals("userId")) 
				sql += " and user_id like '%"+entry.getValue()+"%'";
			if (entry.getKey().equals("userName"))
				sql += " and user_name like '%"+entry.getValue()+"%'";
			
		}
		logger.debug("getList: sql:"+sql);
		
		List<UserInfo> listUserInfo = jdbcTemplate.query(sql, new RowMapper<UserInfo>(){
			@Override
			public UserInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				UserInfo userInfo = new UserInfo();
				userInfo.setUserId(rs.getString("user_id"));
				userInfo.setUserName(rs.getString("user_name"));
				userInfo.setUserType(rs.getString("user_type"));
				userInfo.setPasswd(rs.getString("passwd"));
				userInfo.setEmail(rs.getString("email"));
				userInfo.setSex(rs.getString("sex"));
				userInfo.setBirthDate(rs.getString("birth_date"));
				userInfo.setCalType(rs.getString("cal_type"));
				userInfo.setCalViewType(rs.getString("cal_view_type"));
				userInfo.setStartWeek(rs.getString("start_week"));
				userInfo.setTimeZone(rs.getString("time_zone"));
				userInfo.setWidgetOption(rs.getString("widget_option"));
				userInfo.setAuthYn(rs.getString("auth_yn"));
				userInfo.setDeleteYn(rs.getString("delete_yn"));
				return userInfo;
			}
		});
		return listUserInfo;

	}

}
