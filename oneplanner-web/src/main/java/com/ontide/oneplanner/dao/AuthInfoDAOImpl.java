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


import com.ontide.oneplanner.obj.AuthInfo;

public class AuthInfoDAOImpl implements AuthInfoDAO {
	private static final Logger logger = LoggerFactory.getLogger(AuthInfoDAOImpl.class);

	private JdbcTemplate jdbcTemplate;
	
	public AuthInfoDAOImpl(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public void create(AuthInfo authInfo) throws Exception {
		if (!authInfo.getUserId().equals("")||!authInfo.getAuthId().equals("")) {
			//insert
			String sql = "INSERT INTO auth_info (user_id,auth_id,expired_date, confirm_yn)"
					+ " VALUES (?,?,DATE_FORMAT(NOW() + INTERVAL 1 DAY, \"%Y%m%d%H%i%s\"),?)";
			logger.debug(String.format("insert:[%s] sql[%s]", authInfo, sql));
			jdbcTemplate.update(sql, authInfo.getUserId(), authInfo.getAuthId(), authInfo.getComfirmYn());	
		} else {
			throw new Exception(String.format("No key value userId[%s]authId[%s]"
					, authInfo.getUserId(), authInfo.getAuthId()));
		}
	}
	
	@Override
	public void update(AuthInfo authInfo) throws Exception {
		if (!authInfo.getUserId().equals("")||!authInfo.getAuthId().equals("")) {
			//update
			String sql = "UPDATE auth_info SET confirm_yn = ? "
					+ " , update_date = now()"
					+ "WHERE user_id = ? and auth_id = ? ";
			logger.debug(String.format("update:[%s] sql[%s]", authInfo, sql));
			jdbcTemplate.update(sql, authInfo.getComfirmYn(), authInfo.getUserId(), authInfo.getAuthId());	
		} else {
			throw new Exception(String.format("No key value userId[%s]authId[%s]"
					, authInfo.getUserId(), authInfo.getAuthId()));
		}
	}

	@Override
	public void delete(String userId, String authId) throws Exception {
		if (!userId.equals("")||!authId.equals("")) {
			String sql = "Delete from auth_info where user_id = ? and auth_id = ? ";
			logger.debug(String.format("delete:userId[%s]authId[%] sql[%s]", userId,  authId, sql));
			jdbcTemplate.update(sql, userId,  authId);
		} else {
			throw new Exception(String.format("No key value userId[%s]authId[%s]"
					, userId, authId));
		}
	}

	@Override
	public AuthInfo get(String userId, String authId) {
		String sql = "SELECT user_id, auth_id,expired_date, confirm_yn"
				+" FROM auth_info where user_id ='"+userId+"' and auth_id = '"+authId+"'";
		logger.info("get: sql:"+sql);
		return jdbcTemplate.query(sql, new ResultSetExtractor<AuthInfo>(){
			@Override
			public AuthInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					AuthInfo authInfo = new AuthInfo();
					authInfo.setUserId(rs.getString("user_id"));
					authInfo.setAuthId(rs.getString("auth_id"));
					authInfo.setExpiredDate(rs.getString("expired_date"));
					authInfo.setComfirmYn(rs.getString("confirm_yn"));
					return authInfo;
				}
				return null;
			}
			
		});
	}
	
	public List<AuthInfo> getList(Map<String,String> params) throws Exception {
		String sql = "SELECT user_id, auth_id, expired_date, confirm_yn"
				+" FROM auth_info where 1 = 1";

		for (Entry<String, String> entry : params.entrySet()) {
			if  ("".equals(entry.getValue())) continue;
			if (entry.getKey().equals("userId")) 
				sql += " and user_id like '%"+entry.getValue()+"%'";
		}
		logger.debug("getList: sql:"+sql);
		
		List<AuthInfo> listAuthInfo = jdbcTemplate.query(sql, new RowMapper<AuthInfo>(){
			@Override
			public AuthInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				AuthInfo authInfo = new AuthInfo();
				authInfo.setUserId(rs.getString("user_id"));
				authInfo.setAuthId(rs.getString("auth_id"));
				authInfo.setExpiredDate(rs.getString("expired_date"));
				authInfo.setComfirmYn(rs.getString("confirm_yn"));
				return authInfo;
			}
		});
		return listAuthInfo;

	}

}
