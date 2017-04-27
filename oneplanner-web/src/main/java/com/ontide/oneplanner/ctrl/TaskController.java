package com.ontide.oneplanner.ctrl;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;

import org.json.JSONObject;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ontide.oneplanner.dao.EnvironmentBean;
import com.ontide.oneplanner.dao.ScheduleInfoDAO;
import com.ontide.oneplanner.dao.TaskInfoDAO;
import com.ontide.oneplanner.dao.UserInfoDAO;
import com.ontide.oneplanner.etc.ReturnCode;
import com.ontide.oneplanner.etc.Utils;
import com.ontide.oneplanner.io.RequestObj;
import com.ontide.oneplanner.io.ResultObj;
import com.ontide.oneplanner.obj.ScheduleInfo;
import com.ontide.oneplanner.obj.TaskInfo;
import com.ontide.oneplanner.obj.TaskInfoOut;
import com.ontide.oneplanner.obj.UserInfo;

@Controller
@RequestMapping("task")
public class TaskController {
	private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

	@Autowired
	private UserInfoDAO userInfoDAO;
	
	@Autowired
	private TaskInfoDAO taskInfoDAO;
	
	@Autowired
	private ScheduleInfoDAO scheduleInfoDAO;

	@Autowired
	private DataSourceTransactionManager transactionManager;
	
	@Autowired
	private EnvironmentBean environmentBean;
	
	/**
	 * 태스크 등록      
	 */
	@RequestMapping(value="/register", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody ResultObj<TaskInfo> registerTask(@RequestBody TaskInfo taskInfo) {
		long startMilSec = (new Date()).getTime();
		logger.info("registerTask:"+taskInfo);
		//1.overwrite여부
		//overwrite Y이면 신규 생성
		//overwrite N이면 존재여부 확인
		ResultObj<TaskInfo> result =  new ResultObj<TaskInfo>();
		TransactionDefinition txDef = new DefaultTransactionDefinition();
		TransactionStatus txStatus = transactionManager.getTransaction(txDef);

		try {
			taskInfoDAO.create(taskInfo);
			result.setResultCode(ReturnCode.SUCCESS.get());
			result.setResultMsg(ReturnCode.STR_SUCCESS.get());
			//result.setItem(taskInfo);
			transactionManager.commit(txStatus);
		} catch (DuplicateKeyException de){
			logger.error("registerTask failed.",de);
			transactionManager.rollback(txStatus);
			result.setResultCode(ReturnCode.ALREADY_EXISTS.get());
			result.setResultMsg(ReturnCode.STR_ALREADY_EXISTS.get());
		} catch (Exception e) {
			logger.error("registerTask failed.",e);
			transactionManager.rollback(txStatus);
			result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
			result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
		}
		logger.info("registerTask:end:"+((new Date()).getTime()-startMilSec)+":"+result+":"+taskInfo);
		return result;
	}
	
	/**
	 * 태스크 등록      
	 */
	@RequestMapping(value="/register/list", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody ResultObj<TaskInfo> registerTaskList(@RequestBody List<TaskInfo> taskList) {
		long startMilSec = (new Date()).getTime();
		logger.info("registerTaskList:"+Utils.getListToString(taskList));
		//1.overwrite여부
		//overwrite Y이면 신규 생성
		//overwrite N이면 존재여부 확인
		ResultObj<TaskInfo> result =  new ResultObj<TaskInfo>();
		TransactionDefinition txDef = new DefaultTransactionDefinition();
		TransactionStatus txStatus = transactionManager.getTransaction(txDef);

		try {
			taskInfoDAO.create(taskList);
			result.setResultCode(ReturnCode.SUCCESS.get());
			result.setResultMsg(ReturnCode.STR_SUCCESS.get());
			//result.setItem(taskInfo);
			transactionManager.commit(txStatus);
		} catch (DuplicateKeyException de){
			logger.error("registerTaskList failed.",de);
			transactionManager.rollback(txStatus);
			result.setResultCode(ReturnCode.ALREADY_EXISTS.get());
			result.setResultMsg(ReturnCode.STR_ALREADY_EXISTS.get());
		} catch (Exception e) {
			logger.error("registerTaskList failed.",e);
			transactionManager.rollback(txStatus);
			result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
			result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
		}
		logger.info("registerTaskList:end:"+((new Date()).getTime()-startMilSec)+":"+result+":"+Utils.getListToString(taskList));
		return result;
	}
	
		
	/**
	 * 태스크 업데이트
	 */
	@RequestMapping(value="/update", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody ResultObj<TaskInfo> updateTask(@RequestBody TaskInfo taskInfo) {
		long startMilSec = (new Date()).getTime();
		logger.info("updateTask:"+taskInfo);
		//1.overwrite여부
		//overwrite Y이면 신규 생성
		//overwrite N이면 존재여부 확인
		ResultObj<TaskInfo> result =  new ResultObj<TaskInfo>();
		TransactionDefinition txDef = new DefaultTransactionDefinition();
		TransactionStatus txStatus = transactionManager.getTransaction(txDef);

		try {
			if (taskInfoDAO.update(taskInfo)> 0) {
				result.setResultCode(ReturnCode.SUCCESS.get());
				result.setResultMsg(ReturnCode.STR_SUCCESS.get());
			} else {
				result.setResultCode(ReturnCode.TASK_NOT_FOUND.get());
				result.setResultMsg(ReturnCode.STR_TASK_NOT_FOUND.get());
			}
			//result.setItem(taskInfo);
			transactionManager.commit(txStatus);
		} catch (Exception e) {
			logger.error("updateTask failed.",e);
			transactionManager.rollback(txStatus);
			result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
			result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
		}
		logger.info("updateTask:end:"+((new Date()).getTime()-startMilSec)+":"+result+":"+taskInfo);
		return result;
	}
	/**
	 * 태스크 삭제     delete_yn = 'Y'
	 */
	@RequestMapping(value="/remove/setflag", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody ResultObj<String> setTasksDeleteFlag(@RequestBody RequestObj reqObj) {
		long startMilSec = (new Date()).getTime();
		logger.info("setTasksDeleteFlag:"+reqObj);
		//1.overwrite여부
		//overwrite Y이면 신규 생성
		//overwrite N이면 존재여부 확인
		ResultObj<String> result =  new ResultObj<String>();
		TransactionDefinition txDef = new DefaultTransactionDefinition();
		TransactionStatus txStatus = null;

		try {
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("userId",reqObj.getUserId());
			paramMap.put("deleteYn","N");
			paramMap.put("taskList", Utils.getListForQuery(reqObj.getKeyList()));
			if (scheduleInfoDAO.getCnt(paramMap) > 0) {
				result.setResultCode(ReturnCode.SCHEDULE_EXISTS.get());
				result.setResultMsg(ReturnCode.STR_SCHEDULE_EXISTS.get());
				logger.error("setTasksDeleteFlag:"+result+":"+reqObj);
				return result;
			}	
			txStatus = transactionManager.getTransaction(txDef);
			int returnCount = taskInfoDAO.setTasksDeleteFlag(paramMap);
			if (returnCount  > 0) {
				result.setResultCode(ReturnCode.SUCCESS.get());
				result.setResultMsg(ReturnCode.STR_SUCCESS.get());
			} else {
				result.setResultCode(ReturnCode.TASK_NOT_FOUND.get());
				result.setResultMsg(ReturnCode.STR_TASK_NOT_FOUND.get());
			}
			result.setItem("delete count:"+returnCount);
			transactionManager.commit(txStatus);
		} catch (Exception e) {
			logger.error("setTasksDeleteFlag failed.",e);
			transactionManager.rollback(txStatus);
			result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
			result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
		}
		logger.info("setTasksDeleteFlag:end:"+((new Date()).getTime()-startMilSec)+":"+result+":"+reqObj);
		return result;
	}
	
	/**
	 * 태스크 삭제      
	 */
	@RequestMapping(value="/remove/delete", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody ResultObj<String> deleteFromTasks(@RequestBody RequestObj reqObj) {
		long startMilSec = (new Date()).getTime();
		logger.info("deleteFromTasks:"+reqObj);

		ResultObj<String> result =  new ResultObj<String>();
		TransactionDefinition txDef = new DefaultTransactionDefinition();
		TransactionStatus txStatus = null;

		try {
			int scheduleCnt = 0;
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("userId",reqObj.getUserId());
			paramMap.put("taskList", Utils.getListForQuery(reqObj.getKeyList()));
			if (scheduleInfoDAO.getCnt(paramMap) > 0) {
				result.setResultCode(ReturnCode.SCHEDULE_EXISTS.get());
				result.setResultMsg(ReturnCode.STR_SCHEDULE_EXISTS.get());
				logger.error("deleteFromTasks:"+result+":"+reqObj);
				return result;
			}	

			txStatus = transactionManager.getTransaction(txDef);
			int returnCount = taskInfoDAO.deleteFromTasks(paramMap);
			if (returnCount  > 0) {
				result.setResultCode(ReturnCode.SUCCESS.get());
				result.setResultMsg(ReturnCode.STR_SUCCESS.get());
			} else {
				result.setResultCode(ReturnCode.TASK_NOT_FOUND.get());
				result.setResultMsg(ReturnCode.STR_TASK_NOT_FOUND.get());
			}
			result.setItem("delete count:"+returnCount);
			transactionManager.commit(txStatus);
		} catch (Exception e) {
			logger.error("deleteFromTasks failed.",e);
			transactionManager.rollback(txStatus);
			result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
	 		result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
		}
		logger.info("deleteFromTasks:end:"+((new Date()).getTime()-startMilSec)+":"+result+":"+reqObj);
		return result;
	}
	
	/**
	 *	태스크 조회(task id로 조회)
	 */
	@RequestMapping(value="/get", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody ResultObj<TaskInfoOut> getTaskInfo(@RequestBody TaskInfo taskInfo) {
		long startMilSec = (new Date()).getTime();
		logger.info("getTaskInfo:"+taskInfo);
		ResultObj<TaskInfoOut> result =  new ResultObj<TaskInfoOut>();
		
		try {
			TaskInfoOut resultTaskInfo = (TaskInfoOut)taskInfoDAO.get(taskInfo.getUserId(), taskInfo.getTaskId());
			if (resultTaskInfo == null || !resultTaskInfo.getTaskId().equals(taskInfo.getTaskId())) {
				result.setResultCode(ReturnCode.TASK_NOT_FOUND.get());
				result.setResultMsg(ReturnCode.STR_TASK_NOT_FOUND.get());
				logger.error("getTaskInfo:"+result+":"+taskInfo);
				return result;
			}
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("userId",taskInfo.getUserId());
			paramMap.put("taskId",taskInfo.getTaskId());
			paramMap.put("deleteYn","N");
			List<ScheduleInfo> scheduleList = scheduleInfoDAO.getList(paramMap);
			
			String jobIdList="";
			int  totalCntOfJob=scheduleList.size();
			int completedCntOfJob=0;
			
			for (ScheduleInfo info : scheduleList) {
				if (jobIdList.length() > 0) jobIdList+=";";
				jobIdList +=info.getScheduleId();
				if ("Y".equals(info.getCompleteYn())) completedCntOfJob++;
			}
			resultTaskInfo.setJobIdList(jobIdList);
			resultTaskInfo.setTotalCntOfJob(totalCntOfJob);
			resultTaskInfo.setCompletedCntOfJob(completedCntOfJob);
			
			result.setResultCode(ReturnCode.SUCCESS.get());
			result.setResultMsg(ReturnCode.STR_SUCCESS.get());
			result.setItem(resultTaskInfo);
		} catch (Exception e) {
			logger.error("getTaskInfo failed.",e);
			result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
			result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
		}
		logger.info("getTaskInfo:end:"+((new Date()).getTime()-startMilSec)+":"+result+":"+taskInfo);
		return result;
	}
	/**
	 *	태스크 전체 다운로드(user id별 조회)
	 */
	@RequestMapping(value="/checkout", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody ResultObj<List<TaskInfoOut>> getTaskList(@RequestBody UserInfo userInfo) {
		long startMilSec = (new Date()).getTime();
		logger.info("getTaskList:"+userInfo);
		ResultObj<List<TaskInfoOut>> result =  new ResultObj<List<TaskInfoOut>>();
		
		try {
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("userId",userInfo.getUserId());
			List<TaskInfoOut> taskList = taskInfoDAO.getList(paramMap);
			if (taskList == null || taskList.size() == 0) {
				result.setResultCode(ReturnCode.TASK_NOT_FOUND.get());
				result.setResultMsg(ReturnCode.STR_TASK_NOT_FOUND.get());
				logger.error("getTaskList:"+result+":"+userInfo);
				return result;
			}
			
			paramMap.put("deleteYn","N");
			List<ScheduleInfo> scheduleList = scheduleInfoDAO.getList(paramMap);
			
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
		logger.info("getTaskList:end:"+((new Date()).getTime()-startMilSec)+":"+result+":"+userInfo);
		return result;
	}
	
	
	
}
