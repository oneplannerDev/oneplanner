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









import com.ontide.oneplanner.etc.Utils;
import com.ontide.oneplanner.obj.AdminInfo;
import com.ontide.oneplanner.obj.AuthInfo;
import com.ontide.oneplanner.obj.StatusInfo;
import com.ontide.oneplanner.obj.UserInfoWeb;

public class AdminInfoDAOImpl implements AdminInfoDAO {
	private static final Logger logger = LoggerFactory.getLogger(AdminInfoDAOImpl.class);

	private JdbcTemplate jdbcTemplate;
	
	public AdminInfoDAOImpl(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public int create(AdminInfo adminInfo) throws Exception {
		int result = 0;
		if (!adminInfo.getUserId().equals("")) {
			//insert
			String sql = "INSERT INTO admin_info (user_id,passwd)"
					+ " VALUES (?,?)";
			logger.debug(String.format("insert:[%s] sql[%s]", adminInfo, sql));
			result = jdbcTemplate.update(sql, adminInfo.getUserId(), adminInfo.getPasswd());	
		} else {
			throw new Exception(String.format("No key value userId[%s]passwd[%s]"
					, adminInfo.getUserId(), adminInfo.getPasswd()));
		}
		return result;
	}
	
	@Override
	public int update(AdminInfo adminInfo) throws Exception {
		int result = 0;
		if (!adminInfo.getUserId().equals("")) {
			//update
			String sql = "UPDATE admin_info SET passwd = ? , email = ? "
					+ " , update_date = now()"
					+ "WHERE user_id = ? ";
			logger.debug(String.format("update:[%s] sql[%s]", adminInfo, sql));
			result = jdbcTemplate.update(sql, adminInfo.getPasswd(),adminInfo.getEmail(), adminInfo.getUserId());	
		} else {
			throw new Exception(String.format("No key value userId[%s]"
					, adminInfo.getUserId()));
		}
		return result;
	}

	@Override
	public int updateAccessTime(AdminInfo adminInfo) throws Exception {
		int result = 0;
		if (!adminInfo.getUserId().equals("")) {
			//update
			String sql = "UPDATE admin_info SET access_date = now()"
					+ "WHERE user_id = ? and passwd = ? ";
			logger.debug(String.format("updateAccessTime:[%s] sql[%s]", adminInfo, sql));
			result = jdbcTemplate.update(sql, adminInfo.getUserId(), adminInfo.getPasswd());	
		} else {
			throw new Exception(String.format("No key value userId[%s]passwd[%s]"
					, adminInfo.getUserId(), adminInfo.getPasswd()));
		}
		return result;
	}

	@Override
	public AdminInfo get(String userId) {
		String sql = "SELECT user_id, passwd, email, access_date, update_date"
				+" FROM admin_info where user_id ='"+userId+"'";
		logger.info("get: sql:"+sql);
		return jdbcTemplate.query(sql, new ResultSetExtractor<AdminInfo>(){
			@Override
			public AdminInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					AdminInfo adminInfo = new AdminInfo();
					adminInfo.setUserId(rs.getString("user_id"));
					adminInfo.setPasswd(rs.getString("passwd"));
					adminInfo.setEmail(rs.getString("email"));
					adminInfo.setAccessDate(rs.getString("access_date"));
					adminInfo.setUpdateDate(rs.getString("update_date"));
					return adminInfo;
				}
				return null;
			}
			
		});
	}
	
	@Override
	public StatusInfo getStatus() {
		String sql = "select ("
				+getTotalUserCntSql(true)            +") totalUserCnt, ("
				+getEmailUserCntSql(true)            +") emailUserCnt,("
				+getGoogleUserCntSql(true)           +") googleUserCnt, ("
				+getAuthUnconfirmCntSql(true)        +") authUnconfirmCnt, ("
				+getResetUnconfirmCntSql(true)       +") resetUnconfirmCnt, ("
				+getRecentUserCntByMonthSql(true)    +") recentUserCntByMonth, ("
				+getRecentUserCntByWeekSql(true)     +") recentUserCntByWeek, ("
				+getRecentUserCntByDaySql(true)      +") recentUserCntByDay, ("
				+getUnconfirmedUserCntByWeekSql(true)+") unconfirmedUserCntByWeek ";
		logger.info("getStatus: sql:"+sql);
		return jdbcTemplate.query(sql, new ResultSetExtractor<StatusInfo>(){
			@Override
			public StatusInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					StatusInfo statusInfo = new StatusInfo();
					statusInfo.setTotalUserCnt(rs.getInt("totalUserCnt"));
					statusInfo.setEmailUserCnt(rs.getInt("emailUserCnt"));
					statusInfo.setGoogleUserCnt(rs.getInt("googleUserCnt"));
					statusInfo.setAuthUnconfirmCnt(rs.getInt("authUnconfirmCnt"));
					statusInfo.setResetUnconfirmCnt(rs.getInt("resetUnconfirmCnt"));
					statusInfo.setRecentUserCntByMonth(rs.getInt("recentUserCntByMonth"));
					statusInfo.setRecentUserCntByWeek(rs.getInt("recentUserCntByWeek"));
					statusInfo.setRecentUserCntByDay(rs.getInt("recentUserCntByDay"));
					statusInfo.setUnconfirmedUserCntByWeek(rs.getInt("unconfirmedUserCntByWeek"));

					return statusInfo;
				}
				return null;
			}
			
		});
	}
	
	/** 전체가입자 */
	private String getTotalUserCntSql(boolean isCnt) {
		StringBuffer sb = new StringBuffer();
		sb.append(getCommonCntSql(isCnt));
		sb.append(" from user_info b where auth_yn = 'Y'");
		return sb.toString();
	}

	/** 직접가입자 */
	private String getEmailUserCntSql(boolean isCnt) {
		StringBuffer sb = new StringBuffer();
		sb.append(getCommonCntSql(isCnt));
		sb.append(" from user_info b where auth_yn = 'Y' and user_type <> 'G'");
		return sb.toString();
	}
	
	/** 구글가입자 */
	private String getGoogleUserCntSql(boolean isCnt) {
		StringBuffer sb = new StringBuffer();
		sb.append(getCommonCntSql(isCnt));
		sb.append(" from user_info b where auth_yn = 'Y' and user_type = 'G'");
		return sb.toString();
	}
	
	/** 1달내 추가 가입완료 */
	private String getRecentUserCntByMonthSql(boolean isCnt) {
		StringBuffer sb = new StringBuffer();
		sb.append(getCommonCntSql(isCnt));
		sb.append(" from user_info b where auth_yn = 'Y' and "); 
		sb.append(" exists (select * from auth_info a where confirm_yn = 'Y' and auth_mode= 'A' and "); 
		sb.append(" update_date >= DATE_SUB(NOW(), INTERVAL 1 MONTH) and a.user_id = b.user_id)");
		return sb.toString();
	}
	
	/** 1주내 추가 가입완료 */
	private String getRecentUserCntByWeekSql(boolean isCnt) {
		StringBuffer sb = new StringBuffer();
		sb.append(getCommonCntSql(isCnt));
		sb.append(" from user_info b where auth_yn = 'Y' and ");
		sb.append(" exists (select * from auth_info a where confirm_yn = 'Y' and auth_mode= 'A' and "); 
		sb.append(" update_date >= DATE_SUB(NOW(), INTERVAL 7 day)  and a.user_id = b.user_id)");
		return sb.toString();
	}

	private String getCommonCntSql(boolean isCnt) {
		StringBuffer sb = new StringBuffer();
		sb.append("select ");
		if (isCnt) sb.append(" count(*) "); 
		else { 
			sb.append("user_id,user_name,user_type,passwd,email,sex,birth_date ");
			sb.append(",cal_type,cal_view_type,start_week,time_zone,lang_code,widget_option,auth_yn,delete_yn,auth_mode,confirm_date,DATE_FORMAT(create_date, \"%Y%m%d%H%i%s\") create_date,DATE_FORMAT(update_date, \"%Y%m%d%H%i%s\") update_date");
			sb.append(",(select DATE_FORMAT(update_date, \"%Y-%m-%d %H:%i:%s\") from auth_info a where a.user_id =b.user_id order by update_date desc limit 1) send_date ");
		}
		return sb.toString();
	}
	
	/* 인증 미컨펌 */
	private String getAuthUnconfirmCntSql(boolean isCnt) {
		StringBuffer sb = new StringBuffer();
		sb.append(getCommonCntSql(isCnt));
		sb.append(" from  user_info b where auth_yn = 'N'");
		return sb.toString();
	}
	
	/** 리셋미컨펌*/
	private String getResetUnconfirmCntSql(boolean isCnt) {
		StringBuffer sb = new StringBuffer();
		sb.append(getCommonCntSql(isCnt));
		sb.append(" from  user_info b where auth_mode = 'R'");
		return sb.toString();
	}
	
	/** 1일내 추가 가입오나료 */
	private String getRecentUserCntByDaySql(boolean isCnt) {
		StringBuffer sb = new StringBuffer();
		sb.append(getCommonCntSql(isCnt));
		sb.append(" from  user_info b where auth_yn = 'Y' and "); 
		sb.append(" exists (select * from auth_info a where confirm_yn = 'Y' and auth_mode= 'A' and "); 
		sb.append(" update_date >= DATE_SUB(NOW(), INTERVAL 1 day) and a.user_id = b.user_id)");
		return sb.toString();
	}
	
	/** 1주내 인증미컨펌 */
	private String getUnconfirmedUserCntByWeekSql(boolean isCnt) {
		StringBuffer sb = new StringBuffer();
		sb.append(getCommonCntSql(isCnt));
		sb.append(" from  user_info b where auth_yn = 'N' and "); 
		sb.append(" exists (select * from auth_info a where confirm_yn = 'N' and auth_mode= 'A' and "); 
		sb.append(" update_date >= DATE_SUB(NOW(), INTERVAL 7 day) and a.user_id = b.user_id)");
		return sb.toString();
	}

	@Override
	public List<UserInfoWeb> getUserInfoWebList(Map<String,String> params) throws Exception {
		String sql = "";
		String mode = params.get("mode").toString();
		if (mode.equals("totalUserCnt")) sql = getTotalUserCntSql(false)            ;
	     else if (mode.equals("emailUserCnt")) sql = getEmailUserCntSql(false)            ;
	     else if (mode.equals("googleUserCnt")) sql = getGoogleUserCntSql(false)           ;
	     else if (mode.equals("authUnconfirmCnt")) sql = getAuthUnconfirmCntSql(false)        ;
	     else if (mode.equals("resetUnconfirmCnt")) sql = getResetUnconfirmCntSql(false)       ;
	     else if (mode.equals("recentUserCntByMonth")) sql = getRecentUserCntByMonthSql(false)    ;
	     else if (mode.equals("recentUserCntByWeek")) sql = getRecentUserCntByWeekSql(false)     ;
	     else if (mode.equals("recentUserCntByDay")) sql = getRecentUserCntByDaySql(false)      ;
	     else if (mode.equals("unconfirmedUserCntByWeek")) sql = getUnconfirmedUserCntByWeekSql(false);
	     
		
		String orderBy = "";
		int recordCntPerPage = 0;
		int pageIndex = 0;
		String dateFrom = "";
		String dateTo = "";
		
		sql = "SELECT @rownum:=@rownum + 1 as row_number,t.*  FROM (" 
				+sql;

		for (Entry<String, String> entry : params.entrySet()) {
			if  ("".equals(entry.getValue())) continue;

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
		
		sql +=" ) t, (SELECT @rownum := 0) r ";

		if (!"".equals(orderBy))
			sql += " order by "+orderBy;
		sql += "limit "+recordCntPerPage+" offset "+((pageIndex-1)*recordCntPerPage);

		logger.info("getUserInfoWebList: sql:"+sql);

		
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
	
}
