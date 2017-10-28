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

import com.ontide.oneplanner.obj.MailingHistory;

public class MailingHistoryDAOImpl implements MailingHistoryDAO {
	private static final Logger logger = LoggerFactory.getLogger(MailingHistoryDAOImpl.class);

	private JdbcTemplate jdbcTemplate;
	
	public MailingHistoryDAOImpl(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public void create(MailingHistory mailingHistory) throws Exception {
		if (!mailingHistory.getUserId().equals("")||!mailingHistory.getSendDate().equals("")) {
			//insert
			String sql = "INSERT INTO mailing_history (user_id, send_date, auth_mode, auth_id)"
					+ " VALUES (?,DATE_FORMAT(NOW(), \"%Y%m%d%H%i%s\"),?,?)";
			logger.debug(String.format("insert:[%s] sql[%s]", mailingHistory, sql));
			jdbcTemplate.update(sql, mailingHistory.getUserId(), mailingHistory.getAuthMode(), 
					mailingHistory.getAuthId());	
		} else {
			throw new Exception(String.format("No key value userId[%s]sendDate[%s]"
					, mailingHistory.getUserId(), mailingHistory.getSendDate()));
		}
	}
	
	@Override
	public int delete(String userId, String sendDate) throws Exception {
		if (!userId.equals("")||!sendDate.equals("")) {
			String sql = "Delete from mailing_history where user_id = ? and send_date = ? ";
			logger.debug(String.format("delete:user_id[%s]send_date[%s] sql[%s]", userId, sendDate, sql));
			return jdbcTemplate.update(sql, userId, sendDate);
		} else {
			throw new Exception(String.format("No key value userId[%s]sendDate[%s]"
					, userId, sendDate));
		}
	}

	@Override
	public MailingHistory get(String userId, String sendDate) {
		String sql = "SELECT user_id, send_date, auth_mode, auth_id, create_date"
				+" FROM mailing_history where user_id ='"+userId+"' and send_date = '"+sendDate+"'";
		logger.info("get: sql:"+sql);
		return jdbcTemplate.query(sql, new ResultSetExtractor<MailingHistory>(){
			@Override
			public MailingHistory extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					MailingHistory mailingHistory = new MailingHistory();
					mailingHistory.setUserId(rs.getString("user_id"));
					mailingHistory.setSendDate(rs.getString("send_date"));
					mailingHistory.setAuthMode(rs.getString("auth_mode"));
					mailingHistory.setAuthId(rs.getString("auth_id"));
					mailingHistory.setCreateDate(rs.getString("create_date"));
					return mailingHistory;
				}
				return null;
			}
			
		});
	}
	
	@Override
	public MailingHistory getRecent(String userId) {
		String sql = "SELECT user_id, send_date, auth_mode, auth_id, create_date"
				+" FROM mailing_history where user_id ='"+userId+"' order by create_date desc limit 1";
		logger.info("get: sql:"+sql);
		return jdbcTemplate.query(sql, new ResultSetExtractor<MailingHistory>(){
			@Override
			public MailingHistory extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					MailingHistory mailingHistory = new MailingHistory();
					mailingHistory.setUserId(rs.getString("user_id"));
					mailingHistory.setSendDate(rs.getString("send_date"));
					mailingHistory.setAuthMode(rs.getString("auth_mode"));
					mailingHistory.setAuthId(rs.getString("auth_id"));
					mailingHistory.setCreateDate(rs.getString("create_date"));
					return mailingHistory;
				}
				return null;
			}
			
		});
	}
	
	
	public List<MailingHistory> getList(Map<String,String> params) throws Exception {
		String orderBy = "";
		int recordCntPerPage = 0;
		int pageIndex = 0;
		String dateFrom = "";
		String dateTo = "";

		String sql = "SELECT @rownum:=@rownum + 1 as row_number,t.*  FROM (" 
				+"SELECT user_id, send_date, auth_mode, auth_id, create_date"
				+" FROM mailing_history where 1 = 1";

		for (Entry<String, String> entry : params.entrySet()) {
			if  ("".equals(entry.getValue())) continue;
			if (entry.getKey().equals("searchWord")) {
				sql += " and user_id like '%"+entry.getValue()+"%')";
			}
			if (entry.getKey().equals("dateFrom"))
				dateFrom = entry.getValue();
			if (entry.getKey().equals("dateTo"))
				dateTo = entry.getValue();
			
			if (entry.getKey().equals("orderBy")&&!entry.getValue().trim().equals(""))
				orderBy = entry.getValue();
			if (entry.getKey().equals("recCntPerPage")&&!String.valueOf(entry.getValue()).trim().equals(""))
				recordCntPerPage= Integer.parseInt(String.valueOf(entry.getValue()));
			if (entry.getKey().equals("pageIndex")&&!String.valueOf(entry.getValue()).trim().equals(""))
				pageIndex = Integer.parseInt(String.valueOf(entry.getValue()));
		}
		if (!dateFrom.equals("")) {
			sql += " and send_date '"+dateFrom+"000000' and '"+dateTo+"235959'" ;
		}
		
		sql +=" ) t, (SELECT @rownum := 0) r ";

		if (!"".equals(orderBy))
			sql += " order by "+orderBy;
		sql += "limit "+recordCntPerPage+" offset "+((pageIndex-1)*recordCntPerPage);
		logger.debug("getList: sql:"+sql);
		
		List<MailingHistory> listMailingHistory = jdbcTemplate.query(sql, new RowMapper<MailingHistory>(){
			@Override
			public MailingHistory mapRow(ResultSet rs, int rowNum) throws SQLException {
				MailingHistory mailingHistory = new MailingHistory();
				mailingHistory.setUserId(rs.getString("user_id"));
				mailingHistory.setSendDate(rs.getString("send_date"));
				mailingHistory.setAuthMode(rs.getString("auth_mode"));
				mailingHistory.setAuthId(rs.getString("auth_id"));
				mailingHistory.setCreateDate(rs.getString("create_date"));
				return mailingHistory;
			}
		});
		return listMailingHistory;

	}

	@Override
	public int getCnt(Map<String, String> params) throws Exception {
		String dateFrom = "";
		String dateTo = "";

		String sql = "SELECT count(*)"
				+" FROM mailing_history where 1 = 1";

		for (Entry<String, String> entry : params.entrySet()) {
			if  ("".equals(entry.getValue())) continue;
			if (entry.getKey().equals("searchWord")) {
				sql += " and (user_id like '%"+entry.getValue()+"%')";
			}
			if (entry.getKey().equals("dateFrom"))
				dateFrom = entry.getValue();
			if (entry.getKey().equals("dateTo"))
				dateTo = entry.getValue();
		}
		if (!dateFrom.equals("")) {
			sql += " and send_date '"+dateFrom+"000000' and '"+dateTo+"235959'" ;
		}

		logger.info("getCnt: sql:"+sql);
		return  jdbcTemplate.queryForObject(sql, Integer.class);
	}

	@Override
	public List<MailingHistory> getList(String userId) throws Exception {
		String sql = "SELECT user_id, send_date, auth_mode, auth_id, create_date"
				+" FROM mailing_history where user_id = '"+userId+"'"
				+" order by create_date desc";

		logger.debug("getList: sql:"+sql);
		
		List<MailingHistory> listMailingHistory = jdbcTemplate.query(sql, new RowMapper<MailingHistory>(){
			@Override
			public MailingHistory mapRow(ResultSet rs, int rowNum) throws SQLException {
				MailingHistory mailingHistory = new MailingHistory();
				mailingHistory.setUserId(rs.getString("user_id"));
				mailingHistory.setSendDate(rs.getString("send_date"));
				mailingHistory.setAuthMode(rs.getString("auth_mode"));
				mailingHistory.setAuthId(rs.getString("auth_id"));
				mailingHistory.setCreateDate(rs.getString("create_date"));
				return mailingHistory;
			}
		});
		return listMailingHistory;	}	
}
