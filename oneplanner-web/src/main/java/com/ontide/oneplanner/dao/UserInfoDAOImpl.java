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
import com.ontide.oneplanner.obj.UserInfoWeb;

public class UserInfoDAOImpl implements UserInfoDAO {
	private static final Logger logger = LoggerFactory.getLogger(UserInfoDAOImpl.class);

	private JdbcTemplate jdbcTemplate;
	
	public UserInfoDAOImpl(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public void create(UserInfoWeb userInfo) throws Exception {
		if (!userInfo.getUserId().equals("")) {
			//insert
			String sql = "INSERT INTO user_info (user_id,user_name,user_type,passwd,email"
					+ ",sex,birth_date,cal_type,cal_view_type,start_week"
					+ ",time_zone,lang_code,widget_option,auth_yn, auth_mode)"
					+ " VALUES(?,?,?,?,?"
					+ ",?,?,?,?,?"
					+ ",?,?,?,?,?)";
			logger.debug(String.format("insert:[%s] sql[%s]", userInfo, sql));
			int ret = jdbcTemplate.update(sql, userInfo.getUserId()
					, userInfo.getUserName(), userInfo.getUserType(), userInfo.getPasswd(),userInfo.getEmail()
					, userInfo.getSex(), userInfo.getBirthDate(), userInfo.getCalType(), userInfo.getCalViewType()
					, userInfo.getStartWeek(),userInfo.getTimeZone(), userInfo.getLangCode(), userInfo.getWidgetOption()
					, userInfo.getAuthYn(), userInfo.getAuthMode());
			
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
					+ " ,cal_type = ?,cal_view_type = ?,start_week = ?,time_zone = ?,lang_code = ?,widget_option = ?, auth_yn = ?"
					+ " , delete_yn = ?, update_date = now()"
					+ "WHERE user_id = ?";
			logger.debug(String.format("update:[%s] sql[%s]", userInfo, sql));
			return jdbcTemplate.update(sql, userInfo.getUserId()
					, userInfo.getUserName(), userInfo.getUserType(), userInfo.getPasswd()
					, userInfo.getEmail(), userInfo.getSex(), userInfo.getBirthDate(), userInfo.getCalType()
					, userInfo.getCalViewType(), userInfo.getStartWeek(), userInfo.getTimeZone()
					, userInfo.getLangCode(), userInfo.getWidgetOption()
					, userInfo.getAuthYn(), userInfo.getDeleteYn() 
					, userInfo.getUserId()
					);
		} else {
			throw new Exception(String.format("No key value [%s]", userInfo.getUserId()));
		}
	}

	@Override
	public int update(UserInfoWeb userInfo) throws Exception {
		if (!userInfo.getUserId().equals("")) {
			//update
			String sql = "UPDATE user_info SET user_id = ? "
					+ " ,user_name = ?,user_type = ?,passwd = ?,email = ?,sex = ?,birth_date = ? "
					+ " ,cal_type = ?,cal_view_type = ?,start_week = ?,time_zone = ?,lang_code = ?,widget_option = ?, auth_yn = ?, auth_mode = ? "
					+ " , confirm_date= ?, delete_yn = ?, update_date = now()"
					+ "WHERE user_id = ?";
			logger.debug(String.format("update:[%s] sql[%s]", userInfo, sql));
			return jdbcTemplate.update(sql, userInfo.getUserId()
					, userInfo.getUserName(), userInfo.getUserType(), userInfo.getPasswd()
					, userInfo.getEmail(), userInfo.getSex(), userInfo.getBirthDate(), userInfo.getCalType()
					, userInfo.getCalViewType(), userInfo.getStartWeek(), userInfo.getTimeZone()
					, userInfo.getLangCode(), userInfo.getWidgetOption()
					, userInfo.getAuthYn(), userInfo.getAuthMode(), userInfo.getConfirmDate(), userInfo.getDeleteYn() 
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

	private String getQuery(String userId) {
		String sql = "SELECT user_id,user_name,user_type,passwd,email,sex,birth_date "
				+",cal_type,cal_view_type,start_week,time_zone,lang_code,widget_option,auth_yn,delete_yn,auth_mode,confirm_date,DATE_FORMAT(create_date, \"%Y%m%d%H%i%s\") create_date,DATE_FORMAT(update_date, \"%Y%m%d%H%i%s\") update_date"
				+" FROM user_info where user_id ='"+userId+"'";
		logger.info("get: sql:"+sql);
		return sql;
	}
	
	@Override
	public UserInfo get(String userId) {
		String sql = getQuery(userId);
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
					userInfo.setLangCode(rs.getString("lang_code"));
					userInfo.setWidgetOption(rs.getString("widget_option"));
					userInfo.setAuthYn(rs.getString("auth_yn"));
					userInfo.setDeleteYn(rs.getString("delete_yn"));
					return userInfo;
				}
				return null;
			}
			
		});
	}

	@Override
	public UserInfoWeb getEx(String userId) {
		String sql = getQuery(userId);
		return jdbcTemplate.query(sql, new ResultSetExtractor<UserInfoWeb>(){
			@Override
			public UserInfoWeb extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					UserInfoWeb userInfo = new UserInfoWeb();
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
					userInfo.setLangCode(rs.getString("lang_code"));
					userInfo.setWidgetOption(rs.getString("widget_option"));
					userInfo.setAuthYn(rs.getString("auth_yn"));
					userInfo.setDeleteYn(rs.getString("delete_yn"));
					userInfo.setAuthMode(rs.getString("auth_mode"));
					userInfo.setConfirmDate(rs.getString("confirm_date"));
					userInfo.setCreateDate(rs.getString("create_date"));
					userInfo.setUpdateDate(rs.getString("update_date"));
					
					return userInfo;
				}
				return null;
			}
			
		});
	}

	private String getListQuery(Map<String,String> params) throws Exception {
		String orderBy = "";
		int recordCntPerPage = 0;
		int pageIndex = 0;
		String dateFrom = "";
		String dateTo = "";
		
		String sql = "SELECT @rownum:=@rownum + 1 as row_number,t.*  FROM (" 
				+"SELECT user_id,user_name,user_type,passwd,email,sex,birth_date "
				+",cal_type,cal_view_type,start_week,time_zone,lang_code,widget_option,auth_yn,delete_yn,auth_mode,confirm_date,DATE_FORMAT(create_date, \"%Y%m%d%H%i%s\") create_date,DATE_FORMAT(update_date, \"%Y%m%d%H%i%s\") update_date"
				+",(select DATE_FORMAT(update_date, \"%Y-%m-%d %H:%i:%s\") from auth_info a where a.user_id =b.user_id order by update_date desc limit 1) send_date "
				+" FROM user_info b where 1 = 1";

		for (Entry<String, String> entry : params.entrySet()) {
			if  ("".equals(entry.getValue())) continue;
			if (entry.getKey().equals("userId")) 
				sql += " and user_id like '%"+entry.getValue()+"%'";
			if (entry.getKey().equals("userName"))
				sql += " and user_name like '%"+entry.getValue()+"%'";
			if (entry.getKey().equals("searchWord")) {
				sql += " and (user_id like '%"+entry.getValue()+"%' or user_name like '%"+entry.getValue()+"%' or email like '%"+entry.getValue()+"%')";
			}
			if (entry.getKey().equals("deleteYn"))
				sql += " and delete_yn = '"+entry.getValue()+"'";
			if (entry.getKey().equals("dateFrom"))
				dateFrom = entry.getValue().replaceAll("-", "");
			if (entry.getKey().equals("dateTo"))
				dateTo = entry.getValue().replaceAll("-", "");

			if (entry.getKey().equals("orderDesc")&&!entry.getValue().trim().equals(""))
				if (orderBy.equals(""))
					orderBy = Utils.unCamel(entry.getValue())+" desc ";
				else 
					orderBy += ","+Utils.unCamel(entry.getValue())+" desc ";
			if (entry.getKey().equals("orderAsc")&&!entry.getValue().trim().equals(""))
				if (orderBy.equals(""))
					orderBy = Utils.unCamel(entry.getValue())+" asc ";
				else 
					orderBy += ","+Utils.unCamel(entry.getValue())+" asc ";
			
			if (entry.getKey().equals("recCntPerPage")&&!String.valueOf(entry.getValue()).trim().equals(""))
				recordCntPerPage= Integer.parseInt(String.valueOf(entry.getValue()));
			if (entry.getKey().equals("pageIndex")&&!String.valueOf(entry.getValue()).trim().equals(""))
				pageIndex = Integer.parseInt(String.valueOf(entry.getValue()));
		}
		if (!dateFrom.equals("")) {
			sql += " and confirm_date between '"+dateFrom+"000000' and '"+dateTo+"235959'" ;
		}
		
		sql +=" ) t, (SELECT @rownum := 0) r ";

		if (!"".equals(orderBy))
			sql += " order by "+orderBy;
		sql += "limit "+recordCntPerPage+" offset "+((pageIndex-1)*recordCntPerPage);

		logger.info("getList: sql:"+sql);
		return sql;
	}
	public List<UserInfo> getList(Map<String,String> params) throws Exception {
		String sql = getListQuery(params);
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
				userInfo.setLangCode(rs.getString("lang_code"));
				userInfo.setWidgetOption(rs.getString("widget_option"));
				userInfo.setAuthYn(rs.getString("auth_yn"));
				userInfo.setDeleteYn(rs.getString("delete_yn"));
				return userInfo;
			}
		});
		return listUserInfo;
	}

	public List<UserInfoWeb> getListEx(Map<String,String> params) throws Exception {
		String sql = getListQuery(params);
		List<UserInfoWeb> listUserInfo = jdbcTemplate.query(sql, new RowMapper<UserInfoWeb>(){
			@Override
			public UserInfoWeb mapRow(ResultSet rs, int rowNum) throws SQLException {
				UserInfoWeb userInfo = new UserInfoWeb();
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
				userInfo.setLangCode(rs.getString("lang_code"));
				userInfo.setWidgetOption(rs.getString("widget_option"));
				userInfo.setAuthYn(rs.getString("auth_yn"));
				userInfo.setDeleteYn(rs.getString("delete_yn"));
				userInfo.setAuthMode(rs.getString("auth_mode"));
				userInfo.setConfirmDate(rs.getString("confirm_date"));
				userInfo.setCreateDate(rs.getString("create_date"));
				userInfo.setUpdateDate(rs.getString("update_date"));
				userInfo.setSendDate(rs.getString("send_date"));
				
				return userInfo;
			}
		});
		return listUserInfo;
	}
	
	/**
	 * 검색조건을 포함하여 전체건을 구한다.
	 */
	@Override
	public int getCnt(Map<String, String> params) throws Exception {
		String dateFrom = "";
		String dateTo = "";
		String sql = "SELECT count(*) "
				+" FROM user_info where 1 = 1";
		
		for (Entry<String, String> entry : params.entrySet()) {
			if  ("".equals(entry.getValue())) continue;
			if (entry.getKey().equals("userId")) 
				sql += " and user_id like '%"+entry.getValue()+"%'";
			if (entry.getKey().equals("userName"))
				sql += " and user_name like '%"+entry.getValue()+"%'";
			if (entry.getKey().equals("deleteYn"))
				sql += " and delete_yn = '"+entry.getValue()+"'";
			if (entry.getKey().equals("searchWord")) {
				sql += " and (user_id like '%"+entry.getValue()+"%' or user_name like '%"+entry.getValue()+"%' or email like '%"+entry.getValue()+"%')";
			}
			if (entry.getKey().equals("dateFrom"))
				dateFrom = entry.getValue().replaceAll("-", "");;
			if (entry.getKey().equals("dateTo"))
				dateTo = entry.getValue().replaceAll("-", "");;
		}
		if (!dateFrom.equals("")) {
			sql += " and confirm_date between '"+dateFrom+"000000' and '"+dateTo+"235959'" ;
		}
		logger.info("getCnt: sql:"+sql);
		return  jdbcTemplate.queryForObject(sql, Integer.class);
	}

}
