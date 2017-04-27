package com.ontide.oneplanner.ctrl;

import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

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

import com.ontide.oneplanner.dao.EnvironmentBean;
import com.ontide.oneplanner.dao.ScheduleHistoryDAO;
import com.ontide.oneplanner.dao.ScheduleInfoDAO;
import com.ontide.oneplanner.dao.UserInfoDAO;
import com.ontide.oneplanner.etc.ReturnCode;
import com.ontide.oneplanner.etc.Utils;
import com.ontide.oneplanner.io.RequestObj;
import com.ontide.oneplanner.io.ResultObj;
import com.ontide.oneplanner.obj.ScheduleHistory;
import com.ontide.oneplanner.obj.ScheduleInfo;
import com.ontide.oneplanner.obj.UserInfo;

@Controller
@RequestMapping("schedule")
public class ScheduleController {
	private static final Logger logger = LoggerFactory.getLogger(ScheduleController.class);

	@Autowired
	private UserInfoDAO userInfoDAO;
	
	@Autowired
	private ScheduleInfoDAO scheduleInfoDAO;
	
	@Autowired
	private ScheduleHistoryDAO scheduleHistoryDAO;
	
	@Autowired
	private PlatformTransactionManager transactionManager;
	
	@Autowired
	private EnvironmentBean environmentBean;
	
	/**
	 * 스케쥴 등록      
	 */
	@RequestMapping(value="/register", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody ResultObj<ScheduleInfo> registerSchedule(@RequestBody ScheduleInfo scheduleInfo) {
		long startMilSec = (new Date()).getTime();
		logger.info("registerSchedule:"+scheduleInfo);
		ResultObj<ScheduleInfo> result =  new ResultObj<ScheduleInfo>();
		try {
			scheduleInfoDAO.create(scheduleInfo);
			result.setResultCode(ReturnCode.SUCCESS.get());
			result.setResultMsg(ReturnCode.STR_SUCCESS.get());
			//result.setItem(scheduleInfo);
		} catch (DuplicateKeyException de){
			logger.error("registerSchedule failed.",de);
			result.setResultCode(ReturnCode.ALREADY_EXISTS.get());
			result.setResultMsg(ReturnCode.STR_ALREADY_EXISTS.get());
		} catch (Exception e) {
			logger.error("registerSchedule failed.",e);
			result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
			result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
		}
		logger.info("registerSchedule:end:"+((new Date()).getTime()-startMilSec)+":"+result+":"+scheduleInfo);
		return result;
	}

	/**
	 * 스케쥴 등록      
	 */
	@RequestMapping(value="/register/list", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody ResultObj<ScheduleInfo> registerScheduleList(@RequestBody List<ScheduleInfo> scheduleInfoList) {
		long startMilSec = (new Date()).getTime();
		logger.info("registerScheduleList:"+Utils.getListToString(scheduleInfoList));
		ResultObj<ScheduleInfo> result =  new ResultObj<ScheduleInfo>();
		try {
			scheduleInfoDAO.create(scheduleInfoList);
			result.setResultCode(ReturnCode.SUCCESS.get());
			result.setResultMsg(ReturnCode.STR_SUCCESS.get());
			//result.setItem(scheduleInfo);
		} catch (DuplicateKeyException de){
			logger.error("registerScheduleList failed.",de);
			result.setResultCode(ReturnCode.ALREADY_EXISTS.get());
			result.setResultMsg(ReturnCode.STR_ALREADY_EXISTS.get());
		} catch (Exception e) {
			logger.error("registerScheduleList failed.",e);
			result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
			result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
		}
		logger.info("registerScheduleList:end:"+((new Date()).getTime()-startMilSec)+":"+result+":"+Utils.getListToString(scheduleInfoList));
		return result;
	}
	/**
	 * 스케쥴 업데이트
	 */
	@RequestMapping(value="/update", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody ResultObj<ScheduleInfo> updateSchedule(@RequestBody ScheduleInfo scheduleInfo) {
		long startMilSec = (new Date()).getTime();
		logger.info("updateSchedule:"+scheduleInfo);
		//1.overwrite여부
		//overwrite Y이면 신규 생성
		//overwrite N이면 존재여부 확인
		ResultObj<ScheduleInfo> result =  new ResultObj<ScheduleInfo>();

		try {
			if (scheduleInfoDAO.update(scheduleInfo) > 0) {
				result.setResultCode(ReturnCode.SUCCESS.get());
				result.setResultMsg(ReturnCode.STR_SUCCESS.get());
			} else {
				result.setResultCode(ReturnCode.SCHEDULE_NOT_FOUND.get());
				result.setResultMsg(ReturnCode.STR_SCHEDULE_NOT_FOUND.get());
			}
			//result.setItem(scheduleInfo);
		} catch (Exception e) {
			logger.error("updateSchedule failed.",e);
			result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
			result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
		}
		logger.info("updateSchedule:end:"+((new Date()).getTime()-startMilSec)+":"+result+":"+scheduleInfo);
		return result;
	}
	/**
	 * 스케쥴 삭제      
	 */
	@RequestMapping(value="/remove/setflag", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody ResultObj<String> setSchedulesDeleteFlag(@RequestBody RequestObj reqObj) {
		long startMilSec = (new Date()).getTime();
		logger.info("setSchedulesDeleteFlag:"+reqObj);
		//1.overwrite여부
		//overwrite Y이면 신규 생성
		//overwrite N이면 존재여부 확인
		ResultObj<String> result =  new ResultObj<String>();
		try {
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("userId",reqObj.getUserId());
			paramMap.put("scheduleList", Utils.getListForQuery(reqObj.getKeyList()));

			int  returnCount = scheduleInfoDAO.setSchedulesDeleteFlag(paramMap);
			if (returnCount  > 0) {
				result.setResultCode(ReturnCode.SUCCESS.get());
				result.setResultMsg(ReturnCode.STR_SUCCESS.get());
			} else {
				result.setResultCode(ReturnCode.SCHEDULE_NOT_FOUND.get());
				result.setResultMsg(ReturnCode.STR_SCHEDULE_NOT_FOUND.get());
			}
			result.setItem("delete count:"+returnCount);
		} catch (Exception e) {
			logger.error("setSchedulesDeleteFlag failed.",e);
			result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
			result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
		}
		logger.info("setSchedulesDeleteFlag:end:"+((new Date()).getTime()-startMilSec)+":"+result+":"+reqObj);
		return result;
	}

	/**
	 * 스케쥴 삭제      
	 */
	@RequestMapping(value="/remove/setflag/history", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody ResultObj<String> setScheduleHistDeleteFlag(@RequestBody RequestObj reqObj) {
		long startMilSec = (new Date()).getTime();
		logger.info("setScheduleHistDeleteFlag:"+reqObj);
		//1.overwrite여부
		//overwrite Y이면 신규 생성
		//overwrite N이면 존재여부 확인
		ResultObj<String> result =  new ResultObj<String>();
		try {
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("userId",reqObj.getUserId());
			paramMap.put("scheduleHistoryList", Utils.getListForQuery(reqObj.getKeyList()));

			int  returnCount = scheduleHistoryDAO.setScheduleHistoriesDeleteFlag(paramMap);
			if (returnCount  > 0) {
				result.setResultCode(ReturnCode.SUCCESS.get());
				result.setResultMsg(ReturnCode.STR_SUCCESS.get());
			} else {
				result.setResultCode(ReturnCode.SCHEDULE_NOT_FOUND.get());
				result.setResultMsg(ReturnCode.STR_SCHEDULE_NOT_FOUND.get());
			}
			result.setItem("delete count:"+returnCount);
		} catch (Exception e) {
			logger.error("setScheduleHistDeleteFlag failed.",e);
			result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
			result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
		}
		logger.info("setScheduleHistDeleteFlag:end:"+((new Date()).getTime()-startMilSec)+":"+result+":"+reqObj);
		return result;
	}

	/**
	 * 스케쥴 삭제      
	 */
	@RequestMapping(value="/remove/delete", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody ResultObj<String> deleteFromSchedules(@RequestBody RequestObj reqObj) {
		long startMilSec = (new Date()).getTime();
		logger.info("deleteFromSchedules:"+reqObj);
		//1.overwrite여부
		//overwrite Y이면 신규 생성
		//overwrite N이면 존재여부 확인
		ResultObj<String> result =  new ResultObj<String>();
		try {
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("userId",reqObj.getUserId());
			paramMap.put("scheduleList", Utils.getListForQuery(reqObj.getKeyList()));

			int  returnCount = scheduleInfoDAO.deleteFromSchedules(paramMap);
			if (returnCount  > 0) {
				result.setResultCode(ReturnCode.SUCCESS.get());
				result.setResultMsg(ReturnCode.STR_SUCCESS.get());
			} else {
				result.setResultCode(ReturnCode.SCHEDULE_NOT_FOUND.get());
				result.setResultMsg(ReturnCode.STR_SCHEDULE_NOT_FOUND.get());
			}
			result.setItem("delete count:"+returnCount);
		} catch (Exception e) {
			logger.error("deleteFromSchedules failed.",e);
			result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
			result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
		}
		logger.info("deleteFromSchedules:end:"+((new Date()).getTime()-startMilSec)+":"+result+":"+reqObj);
		return result;
	}

	/**
	 * 스케쥴 삭제      
	 */
	@RequestMapping(value="/remove/delete/history", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody ResultObj<String> deleteFromScheduleHist(@RequestBody RequestObj reqObj) {
		long startMilSec = (new Date()).getTime();
		logger.info("deleteFromScheduleHist:"+reqObj);
		//1.overwrite여부
		//overwrite Y이면 신규 생성
		//overwrite N이면 존재여부 확인
		ResultObj<String> result =  new ResultObj<String>();
		try {
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("userId",reqObj.getUserId());
			paramMap.put("scheduleHistoryList", Utils.getListForQuery(reqObj.getKeyList()));

			int  returnCount = scheduleHistoryDAO.deleteFromScheduleHistories(paramMap);
			if (returnCount  > 0) {
				result.setResultCode(ReturnCode.SUCCESS.get());
				result.setResultMsg(ReturnCode.STR_SUCCESS.get());
			} else {
				result.setResultCode(ReturnCode.SCHEDULE_NOT_FOUND.get());
				result.setResultMsg(ReturnCode.STR_SCHEDULE_NOT_FOUND.get());
			}
			result.setItem("delete count:"+returnCount);
		} catch (Exception e) {
			logger.error("deleteFromScheduleHist failed.",e);
			result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
			result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
		}
		logger.info("deleteFromScheduleHist:end:"+((new Date()).getTime()-startMilSec)+":"+result+":"+reqObj);
		return result;
	}	
	/**
	 *	스케쥴 조회(schedule id로 조회)
	 */
	@RequestMapping(value="/get", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody ResultObj<ScheduleInfo> getScheduleInfo(@RequestBody ScheduleInfo scheduleInfo) {
		long startMilSec = (new Date()).getTime();
		logger.info("getScheduleInfo:"+scheduleInfo);
		ResultObj<ScheduleInfo> result =  new ResultObj<ScheduleInfo>();

		try {
			ScheduleInfo resultScheduleInfo = scheduleInfoDAO.get(scheduleInfo.getUserId(), scheduleInfo.getScheduleId());
			if (resultScheduleInfo == null || !resultScheduleInfo.getScheduleId().equals(scheduleInfo.getScheduleId())) {
				result.setResultCode(ReturnCode.SCHEDULE_NOT_FOUND.get());
				result.setResultMsg(ReturnCode.STR_SCHEDULE_NOT_FOUND.get());
				logger.error("getScheduleInfo:"+result+":"+scheduleInfo);
				return result;
			}
			result.setResultCode(ReturnCode.SUCCESS.get());
			result.setResultMsg(ReturnCode.STR_SUCCESS.get());
			result.setItem(resultScheduleInfo);
		} catch (Exception e) {
			logger.error("getScheduleInfo failed.",e);
			result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
			result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
		}
		logger.info("getScheduleInfo:end:"+((new Date()).getTime()-startMilSec)+":"+result+":"+scheduleInfo);
		return result;
	}
	
	/**
	 *	스케쥴 이력 조회
	 */
	@RequestMapping(value="/get/history", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody ResultObj<ScheduleHistory> getScheduleHistory(@RequestBody ScheduleHistory scheduleHistory) {
		long startMilSec = (new Date()).getTime();
		logger.info("getScheduleHistory:"+scheduleHistory);
		ResultObj<ScheduleHistory> result =  new ResultObj<ScheduleHistory>();

		try {
			ScheduleHistory resultScheduleHistory = scheduleHistoryDAO.get(scheduleHistory);
			if (resultScheduleHistory == null || !resultScheduleHistory.getScheduleId().equals(scheduleHistory.getScheduleId())) {
				result.setResultCode(ReturnCode.SCHEDULE_NOT_FOUND.get());
				result.setResultMsg(ReturnCode.STR_SCHEDULE_NOT_FOUND.get());
				logger.error("getScheduleHistory:"+result+":"+scheduleHistory);
				return result;
			}
			result.setResultCode(ReturnCode.SUCCESS.get());
			result.setResultMsg(ReturnCode.STR_SUCCESS.get());
			result.setItem(resultScheduleHistory);
		} catch (Exception e) {
			logger.error("getScheduleHistory failed.",e);
			result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
			result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
		}
		logger.info("getScheduleHistory:end:"+((new Date()).getTime()-startMilSec)+":"+result+":"+scheduleHistory);
		return result;
	}
	/**
	 *	스케쥴 전체 다운로드(user id별 조회)
	 */
	@RequestMapping(value="/checkout", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody ResultObj<List<ScheduleInfo>> getScheduleList(@RequestBody UserInfo userInfo) {
		long startMilSec = (new Date()).getTime();
		logger.info("getScheduleList:"+userInfo);
		ResultObj<List<ScheduleInfo>> result =  new ResultObj<List<ScheduleInfo>>();

		try {
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("userId",userInfo.getUserId());
			List<ScheduleInfo> scheduleList = scheduleInfoDAO.getList(paramMap);
			if (scheduleList == null || scheduleList.size() == 0) {
				result.setResultCode(ReturnCode.SCHEDULE_NOT_FOUND.get());
				result.setResultMsg(ReturnCode.STR_SCHEDULE_NOT_FOUND.get());
				logger.error("getScheduleList:"+result+":"+userInfo);
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
		logger.info("getScheduleList:end:"+((new Date()).getTime()-startMilSec)+":"+result+":"+userInfo);
		return result;
	}
	
	/**
	 *  스케쥴 이력등록      
	 */
	@RequestMapping(value="/register/history", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody ResultObj<ScheduleHistory> registerScheduleHistory(@RequestBody ScheduleHistory scheduleHistory) {
		long startMilSec = (new Date()).getTime();
		logger.info("registerScheduleHistory:"+scheduleHistory);
		//1.overwrite여부
		//overwrite Y이면 신규 생성
		//overwrite N이면 존재여부 확인
		ResultObj<ScheduleHistory> result =  new ResultObj<ScheduleHistory>();
		try {
			scheduleHistoryDAO.create(scheduleHistory);
			result.setResultCode(ReturnCode.SUCCESS.get());
			result.setResultMsg(ReturnCode.STR_SUCCESS.get());
		} catch (DuplicateKeyException de){
			logger.error("registerScheduleHistory failed.",de);
			result.setResultCode(ReturnCode.ALREADY_EXISTS.get());
			result.setResultMsg(ReturnCode.STR_ALREADY_EXISTS.get());
		} catch (Exception e) {
			logger.error("registerScheduleHistory failed.",e);
			result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
			result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
		}
		logger.info("registerScheduleHistory:end:"+((new Date()).getTime()-startMilSec)+":"+result+":"+scheduleHistory);
		return result;
	}
	/**
	 *  스케쥴 이력등록      
	 */
	@RequestMapping(value="/register/history/list", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody ResultObj<ScheduleHistory> registerScheduleHistoryList(@RequestBody List<ScheduleHistory> scheduleHistoryList) {
		long startMilSec = (new Date()).getTime();
		logger.info("registerScheduleHistory:"+Utils.getListToString(scheduleHistoryList));
		//1.overwrite여부
		//overwrite Y이면 신규 생성
		//overwrite N이면 존재여부 확인
		ResultObj<ScheduleHistory> result =  new ResultObj<ScheduleHistory>();
		try {
			scheduleHistoryDAO.create(scheduleHistoryList);
			result.setResultCode(ReturnCode.SUCCESS.get());
			result.setResultMsg(ReturnCode.STR_SUCCESS.get());
		} catch (DuplicateKeyException de){
			logger.error("registerScheduleHistoryList failed.",de);
			result.setResultCode(ReturnCode.ALREADY_EXISTS.get());
			result.setResultMsg(ReturnCode.STR_ALREADY_EXISTS.get());
		} catch (Exception e) {
			logger.error("registerScheduleHistoryList failed.",e);
			result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
			result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
		}
		logger.info("registerScheduleHistoryList:end:"+((new Date()).getTime()-startMilSec)+":"+result+":"+Utils.getListToString(scheduleHistoryList));
		return result;
	}
	/**
	 * 스케쥴 이력 업데이트
	 */
	@RequestMapping(value="/update/history", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody ResultObj<ScheduleHistory> updateScheduleHistory(@RequestBody ScheduleHistory scheduleHistory) {
		long startMilSec = (new Date()).getTime();
		logger.info("updateScheduleHistory:"+scheduleHistory);
		//1.overwrite여부
		//overwrite Y이면 신규 생성
		//overwrite N이면 존재여부 확인
		ResultObj<ScheduleHistory> result =  new ResultObj<ScheduleHistory>();

		try {
			if  (scheduleHistoryDAO.update(scheduleHistory)>0) {
				result.setResultCode(ReturnCode.SUCCESS.get());
				result.setResultMsg(ReturnCode.STR_SUCCESS.get());
			} else {
				result.setResultCode(ReturnCode.SCHEDULE_NOT_FOUND.get());
				result.setResultMsg(ReturnCode.STR_SCHEDULE_NOT_FOUND.get());
			}
			//result.setItem(scheduleInfo);
		} catch (Exception e) {
			logger.error("updateScheduleHistory failed.",e);
			result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
			result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
		}
		logger.info("updateScheduleHistory:end:"+((new Date()).getTime()-startMilSec)+":"+result+":"+scheduleHistory);
		return result;
	}

	/**
	 *	스케쥴이력 전체 다운로드(user id별 조회)
	 */
	@RequestMapping(value="/checkout/history", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody ResultObj<List<ScheduleHistory>> getScheduleHistoryList(@RequestBody UserInfo userInfo) {
		long startMilSec = (new Date()).getTime();
		logger.info("getScheduleHistoryList:"+userInfo);
		ResultObj<List<ScheduleHistory>> result =  new ResultObj<List<ScheduleHistory>>();

		try {
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("userId",userInfo.getUserId());
			List<ScheduleHistory> scheduleList = scheduleHistoryDAO.getList(paramMap);
			if (scheduleList == null || scheduleList.size() == 0) {
				result.setResultCode(ReturnCode.SCHEDULE_NOT_FOUND.get());
				result.setResultMsg(ReturnCode.STR_SCHEDULE_NOT_FOUND.get());
				logger.error("getScheduleHistoryList:"+result+":"+userInfo);
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
		logger.info("getScheduleHistoryList:end:"+((new Date()).getTime()-startMilSec)+":"+result+":"+userInfo);
		return result;
	}
}
