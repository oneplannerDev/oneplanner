package com.ontide.oneplanner.ctrl;

import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ontide.oneplanner.dao.EnvironmentBean;
import com.ontide.oneplanner.dao.TodayInfoDAO;
import com.ontide.oneplanner.dao.UserInfoDAO;
import com.ontide.oneplanner.etc.ReturnCode;
import com.ontide.oneplanner.etc.Utils;
import com.ontide.oneplanner.io.ResultObj;
import com.ontide.oneplanner.obj.TodayInfo;
import com.ontide.oneplanner.obj.UserInfo;
import com.ontide.oneplanner.service.FileService;

@Controller
@RequestMapping("today")
public class TodayController {
	private static final Logger logger = LoggerFactory.getLogger(TodayController.class);

	@Autowired
	private UserInfoDAO userInfoDAO;
	
	@Autowired
	private TodayInfoDAO todayInfoDAO;
	
	@Autowired
	private PlatformTransactionManager transactionManager;
	
	@Autowired
	private EnvironmentBean environmentBean;
	
	@Autowired
	private FileService fileService;

	/**
	 *  투데이 등록
	 */
	@RequestMapping(value="/admin/register", method=RequestMethod.POST, consumes={"multipart/form-data"})
	public @ResponseBody ResultObj<String> registerToday(@RequestPart("json") TodayInfo todayInfo,@RequestPart("upfile") MultipartFile multipartFile) {
		long startMilSec = (new Date()).getTime();
		logger.info("registerToday:"+todayInfo);
		//1.overwrite여부
		//overwrite Y이면 신규 생성
		//overwrite N이면 존재여부 확인
		ResultObj<String> result =  new ResultObj<String>();
		TransactionDefinition txDef = new DefaultTransactionDefinition();
		TransactionStatus txStatus = transactionManager.getTransaction(txDef);

		try {
//			logger.info("##################################################");
//			Map<String, String> env = System.getenv();
//	        for (String envName : env.keySet()) {
//	        	logger.info(String.format("%s=%s", envName, env.get(envName)));
//	        }
//	        //System.getProperty("work.path")
//			logger.info("##################################################");
//			Properties props = System.getProperties();
//	        for (Object envName : props.keySet()) {
//	        	logger.info(String.format("%s=%s", envName, System.getProperty((String)envName)));
//	        }
//			logger.info("##################################################");
			if (todayInfo.getToday() == null) {
				todayInfo.setToday(	Utils.YYYYMMDD());
			}
			if (todayInfo.getContSeq() == null) {
				int seq = todayInfoDAO.getMaxSeq(todayInfo.getToday());
				todayInfo.setContSeq(Integer.toString(seq));
			}
			// save file
			String uploadFileName = fileService.getSaveFileName(multipartFile, todayInfo.getToday(), todayInfo.getContSeq());
			String savedFilePath = fileService.getSaveFilePath(todayInfo.getToday(),uploadFileName);
			String savedFileUrl = fileService.getSaveFileUrl(todayInfo.getToday(),uploadFileName);

			
			//ReqUserInfo userInfo = new ReqUserInfo();
			long fileSize = multipartFile.getSize();
			String fileName = multipartFile.getOriginalFilename();
			System.out.println("imagefileup:fileName="+fileName);
			System.out.println("imagefileup:fileSize="+fileSize);
			
			if (!fileService.saveFile(multipartFile,todayInfo.getToday(),savedFilePath)) {
				result.setResultCode(ReturnCode.ERROR_FILEUP.get());
				result.setResultMsg(ReturnCode.STR_ERROR_FILEUP.get());
				logger.info("registerToday:end:"+((new Date()).getTime()-startMilSec)+":"+todayInfo+":"+result);
				return result;
			}			
			todayInfo.setImageUrl(savedFileUrl);
			todayInfoDAO.create(todayInfo);
			result.setResultCode(ReturnCode.SUCCESS.get());
			result.setResultMsg(ReturnCode.STR_SUCCESS.get());
			result.setItem(savedFileUrl);
			transactionManager.commit(txStatus);
		} catch (DuplicateKeyException de){
			logger.error("registerToday failed.",de);
			transactionManager.rollback(txStatus);
			result.setResultCode(ReturnCode.ALREADY_EXISTS.get());
			result.setResultMsg(ReturnCode.STR_ALREADY_EXISTS.get());
		} catch (Exception e) {
			logger.error("registerToday failed.",e);
			transactionManager.rollback(txStatus);
			result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
			result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
		}
		logger.info("registerToday:end:"+((new Date()).getTime()-startMilSec)+":"+result+":"+todayInfo);
		return result;
	}
	
	/**
	 * 투데이 변경
	 */
	@RequestMapping(value="/admin/register/json", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody ResultObj<TodayInfo> registerTodayJson(@RequestBody TodayInfo todayInfo) {
		long startMilSec = (new Date()).getTime();
		logger.info("registerTodayJson:"+todayInfo);

		TransactionDefinition txDef = new DefaultTransactionDefinition();
		TransactionStatus txStatus = transactionManager.getTransaction(txDef);

		ResultObj<TodayInfo> result =  new ResultObj<TodayInfo>();

		try {
			if (todayInfo.getToday() == null) {
				todayInfo.setToday(	Utils.YYYYMMDD());
			}
			if (todayInfo.getContSeq() == null) {
				int seq = todayInfoDAO.getMaxSeq(todayInfo.getToday());
				todayInfo.setContSeq(Integer.toString(seq));
			}

			todayInfoDAO.create(todayInfo);
			result.setResultCode(ReturnCode.SUCCESS.get());
			result.setResultMsg(ReturnCode.STR_SUCCESS.get());
			//result.setItem(todayInfo);
			transactionManager.commit(txStatus);
		} catch (DuplicateKeyException de){
			logger.error("registerTodayJson failed.",de);
			transactionManager.rollback(txStatus);
			result.setResultCode(ReturnCode.ALREADY_EXISTS.get());
			result.setResultMsg(ReturnCode.STR_ALREADY_EXISTS.get());
		} catch (Exception e) {
			logger.error("registerTodayJson failed.",e);
			transactionManager.rollback(txStatus);
			result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
			result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
		}
		logger.info("registerTodayJson:end:"+((new Date()).getTime()-startMilSec)+":"+result+":"+todayInfo);
		return result;
	}
		
	@RequestMapping(value="/admin/update", method=RequestMethod.POST, consumes={"multipart/form-data"})
	public @ResponseBody ResultObj<String> updateToday(@RequestPart("json") TodayInfo todayInfo,@RequestPart("upfile") MultipartFile multipartFile) {
		long startMilSec = (new Date()).getTime();
		logger.info("registerToday:"+todayInfo);
		//1.overwrite여부
		//overwrite Y이면 신규 생성
		//overwrite N이면 존재여부 확인
		ResultObj<String> result =  new ResultObj<String>();
		TransactionDefinition txDef = new DefaultTransactionDefinition();
		TransactionStatus txStatus = transactionManager.getTransaction(txDef);

		try {
//			logger.info("##################################################");
//			Map<String, String> env = System.getenv();
//	        for (String envName : env.keySet()) {
//	        	logger.info(String.format("%s=%s", envName, env.get(envName)));
//	        }
//	        //System.getProperty("work.path")
//			logger.info("##################################################");
//			Properties props = System.getProperties();
//	        for (Object envName : props.keySet()) {
//	        	logger.info(String.format("%s=%s", envName, System.getProperty((String)envName)));
//	        }
//			logger.info("##################################################");
			if (todayInfo.getToday() == null||todayInfo.getContSeq() == null) {
				throw new Exception("Invalid today key");
			}
			// save file
			String uploadFileName = fileService.getSaveFileName(multipartFile, todayInfo.getToday(), todayInfo.getContSeq());
			String savedFilePath = fileService.getSaveFilePath(todayInfo.getToday(),uploadFileName);
			String savedFileUrl = fileService.getSaveFileUrl(todayInfo.getToday(),uploadFileName);

			
			//ReqUserInfo userInfo = new ReqUserInfo();
			long fileSize = multipartFile.getSize();
			String fileName = multipartFile.getOriginalFilename();
			System.out.println("imagefileup:fileName="+fileName);
			System.out.println("imagefileup:fileSize="+fileSize);
			
			if (!fileService.saveFile(multipartFile,todayInfo.getToday(),savedFilePath)) {
				result.setResultCode(ReturnCode.ERROR_FILEUP.get());
				result.setResultMsg(ReturnCode.STR_ERROR_FILEUP.get());
				transactionManager.rollback(txStatus);
				logger.info("updateToday:end:"+((new Date()).getTime()-startMilSec)+":"+todayInfo+":"+result);
				return result;
			}			
			todayInfo.setImageUrl(savedFileUrl);
			todayInfoDAO.update(todayInfo);
			result.setResultCode(ReturnCode.SUCCESS.get());
			result.setResultMsg(ReturnCode.STR_SUCCESS.get());
			result.setItem(savedFileUrl);
			transactionManager.commit(txStatus);
		} catch (Exception e) {
			logger.error("updateToday failed.",e);
			transactionManager.rollback(txStatus);
			result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
			result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
		}
		logger.info("updateToday:end:"+((new Date()).getTime()-startMilSec)+":"+result+":"+todayInfo);
		return result;
	}
	/**
	 * 투데이 변경
	 */
	@RequestMapping(value="/admin/update/json", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody ResultObj<TodayInfo> updateTodayJson(@RequestBody TodayInfo todayInfo) {
		long startMilSec = (new Date()).getTime();
		logger.info("updateTodayJson:"+todayInfo);
		//1.overwrite여부
		//overwrite Y이면 신규 생성
		//overwrite N이면 존재여부 확인
		ResultObj<TodayInfo> result =  new ResultObj<TodayInfo>();
		TransactionDefinition txDef = new DefaultTransactionDefinition();
		TransactionStatus txStatus = transactionManager.getTransaction(txDef);

		try {
			if (todayInfoDAO.update(todayInfo) > 0) {
				result.setResultCode(ReturnCode.SUCCESS.get());
				result.setResultMsg(ReturnCode.STR_SUCCESS.get());
			} else {
				result.setResultCode(ReturnCode.TODAY_NOT_FOUND.get());
				result.setResultMsg(ReturnCode.STR_TODAY_NOT_FOUND.get());
			}
			//result.setItem(todayInfo);
			transactionManager.commit(txStatus);
		} catch (Exception e) {
			logger.error("updateTodayJson failed.",e);
			transactionManager.rollback(txStatus);
			result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
			result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
		}
		logger.info("updateTodayJson:end:"+((new Date()).getTime()-startMilSec)+":"+result+":"+todayInfo);
		return result;
	}
	/**
	 * 투데이 삭제      
	 */
	@RequestMapping(value="/admin/remove", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody ResultObj<TodayInfo> removeToday(@RequestBody TodayInfo todayInfo) {
		long startMilSec = (new Date()).getTime();
		logger.info("removeToday:"+todayInfo);
		//1.overwrite여부
		//overwrite Y이면 신규 생성
		//overwrite N이면 존재여부 확인
		ResultObj<TodayInfo> result =  new ResultObj<TodayInfo>();
		TransactionDefinition txDef = new DefaultTransactionDefinition();
		TransactionStatus txStatus = transactionManager.getTransaction(txDef);

		try {
			if (todayInfoDAO.delete(todayInfo.getToday(), todayInfo.getContSeq()) > 0) {
				result.setResultCode(ReturnCode.SUCCESS.get());
				result.setResultMsg(ReturnCode.STR_SUCCESS.get());
			} else {
				result.setResultCode(ReturnCode.TODAY_NOT_FOUND.get());
				result.setResultMsg(ReturnCode.STR_TODAY_NOT_FOUND.get());
			}
			//result.setItem(todayInfo);
			transactionManager.commit(txStatus);
		} catch (Exception e) {
			logger.error("removeToday failed.",e);
			transactionManager.rollback(txStatus);
			result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
			result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
		}
		logger.info("removeToday:end:"+((new Date()).getTime()-startMilSec)+":"+result+":"+todayInfo);
		return result;
	}
	/**
	 *	투데이  조회
	 */
	@RequestMapping(value="/get", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody ResultObj<TodayInfo> getTodayInfo(@RequestBody TodayInfo todayInfo) {
		long startMilSec = (new Date()).getTime();
		logger.info("getTodayInfo:"+todayInfo);
		ResultObj<TodayInfo> result =  new ResultObj<TodayInfo>();
		
		try {
			TodayInfo resultTodayInfo = todayInfoDAO.get(todayInfo.getToday(), todayInfo.getContSeq());
			if (resultTodayInfo == null || !resultTodayInfo.getToday().equals(todayInfo.getToday())) {
				result.setResultCode(ReturnCode.TODAY_NOT_FOUND.get());
				result.setResultMsg(ReturnCode.STR_TODAY_NOT_FOUND.get());
				logger.error("getTodayInfo:"+result+":"+todayInfo);
				return result;
			}
			result.setResultCode(ReturnCode.SUCCESS.get());
			result.setResultMsg(ReturnCode.STR_SUCCESS.get());
			result.setItem(resultTodayInfo);
		} catch (Exception e) {
			logger.error("getTodayInfo failed.",e);
			result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
			result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
		}
		logger.info("getTodayInfo:end:"+((new Date()).getTime()-startMilSec)+":"+result+":"+todayInfo);
		return result;
	}

	/**
	 *	오늘 전체 다운로드(user id별 조회)
	 */
	@RequestMapping(value="/checkout", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody ResultObj<List<TodayInfo>> getTodayList(@RequestBody TodayInfo todayInfo) {
		long startMilSec = (new Date()).getTime();
		logger.info("getTodayList:"+todayInfo);
		ResultObj<List<TodayInfo>> result =  new ResultObj<List<TodayInfo>>();
		
		try {
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("today",todayInfo.getToday());
			List<TodayInfo> todayList = todayInfoDAO.getList(paramMap);
			if (todayList == null || todayList.size() == 0) {
				result.setResultCode(ReturnCode.TODAY_NOT_FOUND.get());
				result.setResultMsg(ReturnCode.STR_TODAY_NOT_FOUND.get());
				logger.error("getTodayList:"+result+":"+todayInfo);
				return result;
			}
			result.setResultCode(ReturnCode.SUCCESS.get());
			result.setResultMsg(ReturnCode.STR_SUCCESS.get());
			result.setItem(todayList);
		} catch (Exception e) {
			logger.error("getTodayList failed.",e);
			result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
			result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
		}
		logger.info("getTodayList:end:"+((new Date()).getTime()-startMilSec)+":"+result+":"+todayInfo);
		return result;
	}
	
}
