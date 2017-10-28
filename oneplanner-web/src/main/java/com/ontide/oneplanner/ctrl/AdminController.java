package com.ontide.oneplanner.ctrl;

import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ontide.oneplanner.io.PageResultObj;
import com.ontide.oneplanner.io.RequestMap;
import com.ontide.oneplanner.dao.AdminInfoDAO;
import com.ontide.oneplanner.dao.EnvironmentBean;
import com.ontide.oneplanner.dao.MailingHistoryDAO;
import com.ontide.oneplanner.dao.ScheduleHistoryDAO;
import com.ontide.oneplanner.dao.ScheduleInfoDAO;
import com.ontide.oneplanner.dao.TaskInfoDAO;
import com.ontide.oneplanner.dao.TodayInfoDAO;
import com.ontide.oneplanner.dao.UserInfoDAO;
import com.ontide.oneplanner.etc.Constant;
import com.ontide.oneplanner.etc.ReturnCode;
import com.ontide.oneplanner.io.ResultObj;
import com.ontide.oneplanner.obj.AdminInfo;
import com.ontide.oneplanner.obj.AuthInfo;
import com.ontide.oneplanner.obj.MailingHistory;
import com.ontide.oneplanner.obj.ScheduleHistory;
import com.ontide.oneplanner.obj.ScheduleInfo;
import com.ontide.oneplanner.obj.StatusInfo;
import com.ontide.oneplanner.obj.TaskInfoOut;
import com.ontide.oneplanner.obj.TodayInfo;
import com.ontide.oneplanner.obj.UserInfo;
import com.ontide.oneplanner.obj.UserInfoWeb;

@Controller
@RequestMapping("admin")
public class AdminController {
	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

	@Autowired
	private UserInfoDAO userInfoDAO;
	
	@Autowired
	private TaskInfoDAO taskInfoDAO;
	
	@Autowired
	private ScheduleInfoDAO scheduleInfoDAO;
	
	@Autowired
	private ScheduleHistoryDAO scheduleHistoryDAO;
	
	@Autowired
	private TodayInfoDAO todayInfoDAO;

	@Autowired
	private AdminInfoDAO adminInfoDAO;

	@Autowired
	private MailingHistoryDAO mailingHistoryDAO;

	@Autowired
	private PlatformTransactionManager transactionManager;
	
	@Autowired
	private EnvironmentBean environmentBean;

	/**
	 * admin계정 업데이트
	 */
	@RequestMapping(value="/admin/update", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody ResultObj<String> update(@RequestBody AdminInfo adminInfo) {
		
		logger.info("admin update:"+adminInfo);
		long startMilSec = (new Date()).getTime();
		ResultObj<String> result =  new ResultObj<String>();
		String url ="";
		try {
			int retCnt = adminInfoDAO.update(adminInfo);

			if (retCnt == 1) {
				result.setResultCode(ReturnCode.SUCCESS.get());
				result.setResultMsg(ReturnCode.STR_SUCCESS.get());
			} else {
				result.setResultCode(ReturnCode.USER_NOT_FOUND.get());
				result.setResultMsg(ReturnCode.STR_USER_NOT_FOUND.get());
			}
		} catch (Exception e) {
			logger.error("admin update failed.",e);
			result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
			result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
		}
		logger.info("admin update:end:"+((new Date()).getTime()-startMilSec)+":"+result+":"+adminInfo);
		return result;
	}
	
	/**
	 *	사용자 정보 조회
	 */
	@RequestMapping(value="/admin/get", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody ResultObj<AdminInfo> getAdminInfo(@RequestBody AdminInfo adminInfo) {
		long startMilSec = (new Date()).getTime();
		logger.info("getAdminInfo:"+adminInfo);
		ResultObj<AdminInfo> result =  new ResultObj<AdminInfo>();
		try {
			AdminInfo resultAdminInfo = adminInfoDAO.get(adminInfo.getUserId());
			if (resultAdminInfo == null || !resultAdminInfo.getUserId().equals(adminInfo.getUserId())) {
				result.setResultCode(ReturnCode.USER_NOT_FOUND.get());
				result.setResultMsg(ReturnCode.STR_USER_NOT_FOUND.get());
				logger.error("getAdminInfo:"+result+":"+adminInfo);
				return result;
			}
			result.setResultCode(ReturnCode.SUCCESS.get());
			result.setResultMsg(ReturnCode.STR_SUCCESS.get());
			result.setItem(resultAdminInfo);
		} catch (Exception e) {
			logger.error("getAdminInfo failed.",e);
			result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
			result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
		}
		logger.info("getAdminInfo:end:"+((new Date()).getTime()-startMilSec)+":"+result+":"+adminInfo);
		return result;
	}
	
	/**
	 *	admin login
	 */
	@RequestMapping(value="/admin/login", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody ResultObj<AdminInfo> loginAdmin(@RequestBody AdminInfo adminInfo) {
		long startMilSec = (new Date()).getTime();
		logger.info("loginAdmin:"+adminInfo);
		ResultObj<AdminInfo> result =  new ResultObj<AdminInfo>();
		TransactionDefinition txDef = new DefaultTransactionDefinition();
		TransactionStatus txStatus = null;

		try {
			txStatus = transactionManager.getTransaction(txDef);
			AdminInfo resultAdminInfo = adminInfoDAO.get(adminInfo.getUserId());
			if (resultAdminInfo == null || !resultAdminInfo.getUserId().equals(adminInfo.getUserId())){
				result.setResultCode(ReturnCode.USER_NOT_FOUND.get());
				result.setResultMsg(ReturnCode.STR_USER_NOT_FOUND.get());
				logger.error("loginAdmin:"+result+":"+adminInfo);
				transactionManager.rollback(txStatus);
				return result;
			}
			if (!resultAdminInfo.getPasswd().equals(adminInfo.getPasswd())) {
				result.setResultCode(ReturnCode.INVALID_PASSWD.get());
				result.setResultMsg(ReturnCode.STR_INVALID_PASSWD.get());
				logger.error("loginAdmin:"+result+":"+adminInfo);
				transactionManager.rollback(txStatus);
				return result;
			}
			if (adminInfoDAO.updateAccessTime(adminInfo) < 1) {
				result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
				result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
				logger.error("loginAdmin:"+result+":"+adminInfo);
				transactionManager.rollback(txStatus);
				return result;
			}
			result.setResultCode(ReturnCode.SUCCESS.get());
			result.setResultMsg(ReturnCode.STR_SUCCESS.get());
			result.setItem(resultAdminInfo);
			transactionManager.commit(txStatus);
		} catch (Exception e) {
			logger.error("loginAdmin failed.",e);
			result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
			result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
			transactionManager.rollback(txStatus);
		}
		logger.info("loginAdmin:end:"+((new Date()).getTime()-startMilSec)+":"+result+":"+adminInfo);
		return result;
	}
	
	/**
	 *	admin status
	 */
	@RequestMapping(value="/status/get", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody ResultObj<StatusInfo> getStatus() {
		long startMilSec = (new Date()).getTime();
		logger.info("getStatus");
		ResultObj<StatusInfo> result =  new ResultObj<StatusInfo>();

		try {
			StatusInfo resultStatusInfo = adminInfoDAO.getStatus();
			result.setResultCode(ReturnCode.SUCCESS.get());
			result.setResultMsg(ReturnCode.STR_SUCCESS.get());
			result.setItem(resultStatusInfo);
		} catch (Exception e) {
			logger.error("getStatus failed.",e);
			result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
			result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
		}
		logger.info("getStatus:end:"+((new Date()).getTime()-startMilSec)+":"+result);
		
		return result;
	}

	/**
	 *	admin status
	 */
	@RequestMapping(value="/status/get/list", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody ResultObj<List<UserInfoWeb>>  getStatusList(@RequestBody RequestMap requestMap) {
		long startMilSec = (new Date()).getTime();
		logger.info("getUserWebList:"+requestMap);
		ResultObj<List<UserInfoWeb>> result =  new ResultObj<List<UserInfoWeb>>();

		try {
			List<UserInfoWeb> userList = adminInfoDAO.getUserInfoWebList((HashMap)requestMap);
			if (userList == null || userList.size() == 0) {
				result.setResultCode(ReturnCode.USER_NOT_FOUND.get());
				result.setResultMsg(ReturnCode.STR_USER_NOT_FOUND.get());
				logger.error("getUserWebList:"+requestMap+"=>"+result);
				return result;
			}
			result.setResultCode(ReturnCode.SUCCESS.get());
			result.setResultMsg(ReturnCode.STR_SUCCESS.get());
			result.setItem(userList);
		} catch (Exception e) {
			logger.error("getUserWebList failed.",e);
			result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
			result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
		}
		logger.info("getUserWebList:end:"+((new Date()).getTime()-startMilSec)+":"+requestMap+"=>"+result);
		return result;
	}
	
	
	/**
	 * 사용자 가입 -> 인증 url 이메일 전송
	 * 1. insert subscription
	 * 2. generate auth id
	 * 3. send email
	 * 4. return
	 * 5. 
	 */
	@RequestMapping(value="/user/register", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody ResultObj<String> register(@RequestBody UserInfoWeb userInfo) {
		
		logger.info("register:"+userInfo);
		long startMilSec = (new Date()).getTime();
		ResultObj<String> result =  new ResultObj<String>();
		AuthInfo authInfo = new AuthInfo();
		TransactionDefinition txDef = new DefaultTransactionDefinition();
		TransactionStatus txStatus = null;
		String url ="";
		try {
			userInfo.setAuthYn("Y");
			userInfo.setAuthMode("A");
			UserInfo resultUserInfo = userInfoDAO.get(userInfo.getUserId());
			boolean alreadyExists = (resultUserInfo != null && resultUserInfo.getUserId().equals(userInfo.getUserId()));
			
			txStatus = transactionManager.getTransaction(txDef);
			if (alreadyExists) {
				userInfo.setDeleteYn("N");
				userInfoDAO.update(userInfo);
			} else {
				userInfoDAO.create(userInfo);
			}
			transactionManager.commit(txStatus);

			if (alreadyExists) {
				result.setResultCode(ReturnCode.OK_USER_UPDATED.get());
				result.setResultMsg(ReturnCode.STR_OK_USER_UPDATED.get());
			} else {
				result.setResultCode(ReturnCode.SUCCESS.get());
				result.setResultMsg(ReturnCode.STR_SUCCESS.get());
			}
		} catch (Exception e) {
			logger.error("register failed.",e);
			transactionManager.rollback(txStatus);
			result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
			result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
		}
		logger.info("register:end:"+((new Date()).getTime()-startMilSec)+":"+result+":"+userInfo);
		return result;
	}
	
	/**
	 *	사용자 정보 조회
	 */
	@RequestMapping(value="/userinfo/get", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody ResultObj<UserInfoWeb> getUserInfo(@RequestBody UserInfoWeb userInfo) {
		long startMilSec = (new Date()).getTime();
		logger.info("getUserInfoWeb:"+userInfo);
		ResultObj<UserInfoWeb> result =  new ResultObj<UserInfoWeb>();
		try {
			UserInfoWeb resultUserInfo = userInfoDAO.getEx(userInfo.getUserId());
			if (resultUserInfo == null || !resultUserInfo.getUserId().equals(userInfo.getUserId())) {
				result.setResultCode(ReturnCode.USER_NOT_FOUND.get());
				result.setResultMsg(ReturnCode.STR_USER_NOT_FOUND.get());
				logger.error("getUserInfoEx:"+result+":"+userInfo);
				return result;
			}
			List<MailingHistory> mailingHistoryList= mailingHistoryDAO.getList(userInfo.getUserId());
			resultUserInfo.setItem(mailingHistoryList);
			result.setResultCode(ReturnCode.SUCCESS.get());
			result.setResultMsg(ReturnCode.STR_SUCCESS.get());
			result.setItem(resultUserInfo);
		} catch (Exception e) {
			logger.error("getUserInfoEx failed.",e);
			result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
			result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
		}
		logger.info("getUserInfExo:end:"+((new Date()).getTime()-startMilSec)+":"+result+":"+userInfo);
		return result;
	}
	
	/**
	 *사용자 조회(user id별 조회)
	 */
	@RequestMapping(value="/user/list", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody PageResultObj<List<UserInfoWeb>> getUserWebList(@RequestBody RequestMap requestMap) {
		long startMilSec = (new Date()).getTime();
		logger.info("getUserWebList:"+requestMap);
		PageResultObj<List<UserInfoWeb>> result =  new PageResultObj<List<UserInfoWeb>>();

		try {
			List<UserInfoWeb> userList = userInfoDAO.getListEx((HashMap)requestMap);
			if (userList == null || userList.size() == 0) {
				result.setResultCode(ReturnCode.USER_NOT_FOUND.get());
				result.setResultMsg(ReturnCode.STR_USER_NOT_FOUND.get());
				logger.error("getUserWebList:"+requestMap+"=>"+result);
				return result;
			}
			int resultCnt = userInfoDAO.getCnt((HashMap)requestMap);
			result.setResultCode(ReturnCode.SUCCESS.get());
			result.setResultMsg(ReturnCode.STR_SUCCESS.get());
			result.setItem(userList);
			result.setTotalCnt(Integer.toString(resultCnt));
		} catch (Exception e) {
			logger.error("getUserWebList failed.",e);
			result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
			result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
		}
		logger.info("getUserWebList:end:"+((new Date()).getTime()-startMilSec)+":"+requestMap+"=>"+result);
		return result;
	}

	/**
	 *사용자 mailing list 조회(user id별 조회)
	 */
	@RequestMapping(value="/mailing/list", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody ResultObj<List<MailingHistory>> getMailingHistoryList(@RequestBody RequestMap requestMap) {
		long startMilSec = (new Date()).getTime();
		logger.info("getMailingHistoryList:"+requestMap);
		ResultObj<List<MailingHistory>> result =  new ResultObj<List<MailingHistory>>();

		try {
			List<MailingHistory> mailingHisotryList = mailingHistoryDAO.getList((HashMap)requestMap);
			if (mailingHisotryList == null || mailingHisotryList.size() == 0) {
				result.setResultCode(ReturnCode.USER_NOT_FOUND.get());
				result.setResultMsg(ReturnCode.STR_USER_NOT_FOUND.get());
				logger.error("getMailingHistoryList:"+requestMap+"=>"+result);
				return result;
			}
			result.setResultCode(ReturnCode.SUCCESS.get());
			result.setResultMsg(ReturnCode.STR_SUCCESS.get());
			result.setItem(mailingHisotryList);
		} catch (Exception e) {
			logger.error("getMailingHistoryList failed.",e);
			result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
			result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
		}
		logger.info("getMailingHistoryList:end:"+((new Date()).getTime()-startMilSec)+":"+requestMap+"=>"+result);
		return result;
	}
	
	/**
	 *	스케쥴 전체 다운로드(user id별 조회)
	 */
	@RequestMapping(value="/schedule/list", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody ResultObj<List<ScheduleInfo>> getScheduleList(@RequestBody RequestMap requestMap) {
		long startMilSec = (new Date()).getTime();
		logger.info("getScheduleList:"+requestMap);
		ResultObj<List<ScheduleInfo>> result =  new ResultObj<List<ScheduleInfo>>();

		try {
			List<ScheduleInfo> scheduleList = scheduleInfoDAO.getList((HashMap)requestMap);
			if (scheduleList == null || scheduleList.size() == 0) {
				result.setResultCode(ReturnCode.SCHEDULE_NOT_FOUND.get());
				result.setResultMsg(ReturnCode.STR_SCHEDULE_NOT_FOUND.get());
				logger.error("getScheduleList:"+requestMap+"=>"+result);
				return result;
			}
			result.setResultCode(ReturnCode.SUCCESS.get());
			result.setResultMsg(ReturnCode.STR_SUCCESS.get());
			result.setItem(scheduleList);
		} catch (Exception e) {
			logger.error("getScheduleList failed.",e);
			result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
			result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
		}
		logger.info("getScheduleList:end:"+((new Date()).getTime()-startMilSec)+":"+requestMap+"=>"+result);
		return result;
	}
	
	/**
	 *	스케쥴이력 전체 다운로드(user id별 조회)
	 */
	@RequestMapping(value="/scheduleHistory/list", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody ResultObj<List<ScheduleHistory>> getScheduleHistoryList(@RequestBody RequestMap requestMap) {
		long startMilSec = (new Date()).getTime();
		logger.info("getScheduleHistoryList:"+requestMap);
		ResultObj<List<ScheduleHistory>> result =  new ResultObj<List<ScheduleHistory>>();

		try {
			List<ScheduleHistory> scheduleList = scheduleHistoryDAO.getList((HashMap)requestMap);
			if (scheduleList == null || scheduleList.size() == 0) {
				result.setResultCode(ReturnCode.SCHEDULE_NOT_FOUND.get());
				result.setResultMsg(ReturnCode.STR_SCHEDULE_NOT_FOUND.get());
				logger.error("getScheduleHistoryList:"+requestMap+"=>"+result);
				return result;
			}
			result.setResultCode(ReturnCode.SUCCESS.get());
			result.setResultMsg(ReturnCode.STR_SUCCESS.get());
			result.setItem(scheduleList);
		} catch (Exception e) {
			logger.error("getScheduleHistoryList failed.",e);
			result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
			result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
		}
		logger.info("getScheduleHistoryList:end:"+((new Date()).getTime()-startMilSec)+":"+requestMap+"=>"+result);
		return result;
	}
	
	/**
	 *	태스크 전체 다운로드(user id별 조회)
	 */
	@RequestMapping(value="/task/list", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody ResultObj<List<TaskInfoOut>> getTaskList(@RequestBody RequestMap requestMap) {
		long startMilSec = (new Date()).getTime();
		logger.info("getTaskList:"+requestMap);
		ResultObj<List<TaskInfoOut>> result =  new ResultObj<List<TaskInfoOut>>();
		
		try {
			List<TaskInfoOut> taskList = taskInfoDAO.getList((HashMap)requestMap);
			if (taskList == null || taskList.size() == 0) {
				result.setResultCode(ReturnCode.TASK_NOT_FOUND.get());
				result.setResultMsg(ReturnCode.STR_TASK_NOT_FOUND.get());
				logger.error("getTaskList:"+requestMap+"=>"+result);
				return result;
			}
			
			requestMap.put("deleteYn","N");
			List<ScheduleInfo> scheduleList = scheduleInfoDAO.getList((HashMap)requestMap);
			
			for (TaskInfoOut task : taskList) {
				String jobIdList="";
				int  totalCntOfJob=scheduleList.size();
				int completedCntOfJob=0;
				
				for (ScheduleInfo info : scheduleList) {
					if (!task.getTaskId().equals(info.getTaskId())) continue;
					if (jobIdList.length() > 0) jobIdList+=";";
					jobIdList +=info.getScheduleId();
					if ("Y".equals(info.getCompleteYn())) completedCntOfJob++;
				}
				task.setJobIdList(jobIdList);
				task.setTotalCntOfJob(totalCntOfJob);
				task.setCompletedCntOfJob(completedCntOfJob);
			}
			
			result.setResultCode(ReturnCode.SUCCESS.get());
			result.setResultMsg(ReturnCode.STR_SUCCESS.get());
			result.setItem(taskList);
		} catch (Exception e) {
			logger.error("getTaskList failed.",e);
			result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
			result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
		}
		logger.info("getTaskList:end:"+((new Date()).getTime()-startMilSec)+":"+requestMap+"=>"+result);
		return result;
	}
	
	/**
	 *	오늘 전체 다운로드(user id별 조회)
	 */
	@RequestMapping(value="/today/list", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody PageResultObj<List<TodayInfo>> getTodayWebList(@RequestBody RequestMap requestMap) {
		long startMilSec = (new Date()).getTime();
		logger.info("getTodayWebList:"+requestMap);
		PageResultObj<List<TodayInfo>> result =  new PageResultObj<List<TodayInfo>>();
		
		try {
			List<TodayInfo> todayList = todayInfoDAO.getListWeb((HashMap)requestMap);
			if (todayList == null || todayList.size() == 0) {
				result.setResultCode(ReturnCode.TODAY_NOT_FOUND.get());
				result.setResultMsg(ReturnCode.STR_TODAY_NOT_FOUND.get());
				logger.error("getTodayWebList:"+requestMap+"=>"+result);
				return result;
			}
			int resultCnt = todayInfoDAO.getCnt((HashMap)requestMap);
			result.setResultCode(ReturnCode.SUCCESS.get());
			result.setResultMsg(ReturnCode.STR_SUCCESS.get());
			result.setItem(todayList);
			result.setTotalCnt(Integer.toString(resultCnt));
		} catch (Exception e) {
			logger.error("getTodayWebList failed.",e);
			result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
			result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
		}
		logger.info("getTodayWebList:end:"+((new Date()).getTime()-startMilSec)+":"+requestMap+"=>"+result);
		return result;
	}
	
	@RequestMapping("/index")
	public ModelAndView renderHtmlView() {
	    return new ModelAndView("index");
	}
	@RequestMapping("/test")
	public ModelAndView renderTestView() {
	    return new ModelAndView("test");
	}

}
