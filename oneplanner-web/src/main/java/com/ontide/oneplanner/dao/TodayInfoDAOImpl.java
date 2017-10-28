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
			String sql = "INSERT INTO today_info (today,cont_seq,title, image_type,image_url,content)"
					+ " VALUES (?,?,?,?,?,?)";
			logger.debug(String.format("insert:[%s] sql[%s]", todayInfo, sql));
			jdbcTemplate.update(sql, todayInfo.getToday(), todayInfo.getContSeq(), todayInfo.getTitle(), 
					todayInfo.getImageType(), todayInfo.getImageUrl(), todayInfo.getContent());	
		} else {
			throw new Exception(String.format("No key value today[%s]contSeq[%s]"
					, todayInfo.getToday(), todayInfo.getContSeq()));
		}
	}
	
	@Override
	public int update(TodayInfo todayInfo) throws Exception {
		if (!todayInfo.getToday().equals("")||!todayInfo.getContSeq().equals("")) {
			//update
			String sql = "UPDATE today_info SET title = ?, image_type = ?, content = ?, image_url = ? "
					+ " , update_date = now()"
					+ "WHERE today = ? and cont_seq = ? ";
				
			logger.debug(String.format("update:[%s] sql[%s]", todayInfo, sql));
			return jdbcTemplate.update(sql, todayInfo.getTitle(), todayInfo.getImageType(), todayInfo.getContent(), todayInfo.getImageUrl()
					,todayInfo.getToday(), todayInfo.getContSeq());	
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
		String sql = "SELECT today, cont_seq, title, image_type, image_url, content"
				+" FROM today_info where today ='"+today+"' and cont_seq = '"+contSeq+"'";
		logger.info("get: sql:"+sql);
		return jdbcTemplate.query(sql, new ResultSetExtractor<TodayInfo>(){
			@Override
			public TodayInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					TodayInfo todayInfo = new TodayInfo();
					todayInfo.setToday(rs.getString("today"));
					todayInfo.setContSeq(rs.getString("cont_seq"));
					todayInfo.setTitle(rs.getString("title"));
					todayInfo.setImageType(rs.getString("image_type"));
					todayInfo.setImageUrl(rs.getString("image_url"));
					todayInfo.setContent(rs.getString("content"));
					return todayInfo;
				}
				return null;
			}
			
		});
	}
	
	public List<TodayInfo> getList(Map<String,String> params) throws Exception {
		String sql = "SELECT today, cont_seq, content"
				+" FROM today_info where 1 = 1";

		for (Entry<String, String> entry : params.entrySet()) {
			if  ("".equals(entry.getValue())) continue;
			if (entry.getKey().equals("today")) {
				if (!"all".equals(entry.getValue())) {
					sql += " and today like '%"+entry.getValue().replaceAll("-", "")+"%'";
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
				todayInfo.setContent(rs.getString("content"));
				return todayInfo;
			}
		});
		return listTodayInfo;

	}
	
	public List<TodayInfo> getListWeb(Map<String,String> params) throws Exception {
		String orderBy = "";
		int recordCntPerPage = 0;
		int pageIndex = 0;
		String dateFrom = "";
		String dateTo = "";

		String sql = "SELECT @rownum:=@rownum + 1 as row_number,t.*  FROM (" 
				+"SELECT today, cont_seq, title, image_type, image_url, content"
				+" FROM today_info where 1 = 1";

		for (Entry<String, String> entry : params.entrySet()) {
			if  ("".equals(entry.getValue())) continue;
			if (entry.getKey().equals("searchWord")) {
				sql += " and (title like '%"+entry.getValue()+"%' or content like '%"+entry.getValue()+"%')";
			}
			if (entry.getKey().equals("dateFrom"))
				dateFrom = entry.getValue().replaceAll("-", "");
			if (entry.getKey().equals("dateTo"))
				dateTo = entry.getValue().replaceAll("-", "");
			
			if (entry.getKey().equals("orderBy")&&!entry.getValue().trim().equals(""))
				orderBy = entry.getValue();
			if (entry.getKey().equals("recCntPerPage")&&!String.valueOf(entry.getValue()).trim().equals(""))
				recordCntPerPage= Integer.parseInt(String.valueOf(entry.getValue()));
			if (entry.getKey().equals("pageIndex")&&!String.valueOf(entry.getValue()).trim().equals(""))
				pageIndex = Integer.parseInt(String.valueOf(entry.getValue()));
		}
		if (!dateFrom.equals("")) {
			sql += " and today between '"+dateFrom+"' and '"+dateTo+"'" ;
		}
		
		sql +=" ) t, (SELECT @rownum := 0) r ";

		if (!"".equals(orderBy))
			sql += " order by "+orderBy;
		sql += "limit "+recordCntPerPage+" offset "+((pageIndex-1)*recordCntPerPage);
		logger.info("getList: sql:"+sql);
		
		List<TodayInfo> listTodayInfo = jdbcTemplate.query(sql, new RowMapper<TodayInfo>(){
			@Override
			public TodayInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				TodayInfo todayInfo = new TodayInfo();
				todayInfo.setToday(rs.getString("today"));
				todayInfo.setContSeq(rs.getString("cont_seq"));
				todayInfo.setTitle(rs.getString("title"));
				todayInfo.setImageType(rs.getString("image_type"));
				todayInfo.setImageUrl(rs.getString("image_url"));
				todayInfo.setContent(rs.getString("content"));
				return todayInfo;
			}
		});
		return listTodayInfo;

	}

	
	@Override
	public int getCnt(Map<String, String> params) throws Exception {
		String dateFrom = "";
		String dateTo = "";

		String sql = "SELECT count(*)"
				+" FROM today_info where 1 = 1";

		for (Entry<String, String> entry : params.entrySet()) {
			if  ("".equals(entry.getValue())) continue;
			if (entry.getKey().equals("searchWord")) {
				sql += " and (content like '%"+entry.getValue()+"%')";
			}
			if (entry.getKey().equals("dateFrom"))
				dateFrom = entry.getValue().replaceAll("-", "");
			if (entry.getKey().equals("dateTo"))
				dateTo = entry.getValue().replaceAll("-", "");
		}
		if (!dateFrom.equals("")) {
			sql += " and today between '"+dateFrom+"' and '"+dateTo+"'" ;
		}

		logger.info("getCnt: sql:"+sql);
		return  jdbcTemplate.queryForObject(sql, Integer.class);
	}
	
	@Override
	public int getMaxSeq(String today) throws Exception {
		String sql = "SELECT ifnull(max(cont_seq)+1,1)"
				+" FROM today_info where today = '"+today+"'";
		logger.info("getMaxSeq: sql:"+sql);
		return  jdbcTemplate.queryForObject(sql, Integer.class);
	}
}
