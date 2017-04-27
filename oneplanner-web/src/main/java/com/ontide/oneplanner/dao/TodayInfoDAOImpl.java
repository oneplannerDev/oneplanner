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

import com.ontide.oneplanner.obj.TodayInfo;

public class TodayInfoDAOImpl implements TodayInfoDAO {
	private static final Logger logger = LoggerFactory.getLogger(TodayInfoDAOImpl.class);

	private JdbcTemplate jdbcTemplate;
	
	public TodayInfoDAOImpl(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public void create(TodayInfo todayInfo) throws Exception {
		if (!todayInfo.getToday().equals("")||!todayInfo.getContSeq().equals("")) {
			//insert
			String sql = "INSERT INTO today_info (today,cont_seq,image_url,content)"
					+ " VALUES (?,?,?,?)";
			logger.debug(String.format("insert:[%s] sql[%s]", todayInfo, sql));
			jdbcTemplate.update(sql, todayInfo.getToday(), todayInfo.getContSeq(), todayInfo.getImageUrl(), todayInfo.getContent());	
		} else {
			throw new Exception(String.format("No key value today[%s]contSeq[%s]"
					, todayInfo.getToday(), todayInfo.getContSeq()));
		}
	}
	
	@Override
	public int update(TodayInfo todayInfo) throws Exception {
		if (!todayInfo.getToday().equals("")||!todayInfo.getContSeq().equals("")) {
			//update
			String sql = "UPDATE today_info SET content = ? "
					+ " , update_date = now()"
					+ "WHERE today = ? and cont_seq = ? ";
			logger.debug(String.format("update:[%s] sql[%s]", todayInfo, sql));
			return jdbcTemplate.update(sql, todayInfo.getContent(), todayInfo.getToday(), todayInfo.getContSeq());	
		} else {
			throw new Exception(String.format("No key value today[%s]contSeq[%s]"
					, todayInfo.getToday(), todayInfo.getContSeq()));
		}
	}

	@Override
	public int delete(String today, String contSeq) throws Exception {
		if (!today.equals("")||!contSeq.equals("")) {
			String sql = "Delete from today_info where today = ? and cont_seq = ? ";
			logger.debug(String.format("delete:today[%s]contSeq[%s] sql[%s]", today, contSeq, sql));
			return jdbcTemplate.update(sql, today, contSeq);
		} else {
			throw new Exception(String.format("No key value today[%s]contSeq[%s]"
					 , today, contSeq));
		}
	}

	@Override
	public TodayInfo get(String today, String contSeq) {
		String sql = "SELECT today, cont_seq, image_url, content"
				+" FROM today_info where today ='"+today+"' and cont_seq = '"+contSeq+"'";
		logger.info("get: sql:"+sql);
		return jdbcTemplate.query(sql, new ResultSetExtractor<TodayInfo>(){
			@Override
			public TodayInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					TodayInfo todayInfo = new TodayInfo();
					todayInfo.setToday(rs.getString("today"));
					todayInfo.setContSeq(rs.getString("cont_seq"));
					todayInfo.setImageUrl(rs.getString("image_url"));
					todayInfo.setContent(rs.getString("content"));
					return todayInfo;
				}
				return null;
			}
			
		});
	}
	
	public List<TodayInfo> getList(Map<String,String> params) throws Exception {
		String sql = "SELECT today, cont_seq, image_url, content"
				+" FROM today_info where 1 = 1";

		for (Entry<String, String> entry : params.entrySet()) {
			if  ("".equals(entry.getValue())) continue;
			if (entry.getKey().equals("today")) {
				if (!"all".equals(entry.getValue())) {
					sql += " and today like '%"+entry.getValue()+"%'";
				}
			} 
		}
		logger.debug("getList: sql:"+sql);
		
		List<TodayInfo> listTodayInfo = jdbcTemplate.query(sql, new RowMapper<TodayInfo>(){
			@Override
			public TodayInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				TodayInfo todayInfo = new TodayInfo();
				todayInfo.setToday(rs.getString("today"));
				todayInfo.setContSeq(rs.getString("cont_seq"));
				todayInfo.setImageUrl(rs.getString("image_url"));
				todayInfo.setContent(rs.getString("content"));
				return todayInfo;
			}
		});
		return listTodayInfo;

	}

}
