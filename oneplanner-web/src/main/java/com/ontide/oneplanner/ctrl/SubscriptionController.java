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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ontide.oneplanner.dao.AuthInfoDAO;
import com.ontide.oneplanner.dao.EnvironmentBean;
import com.ontide.oneplanner.dao.MailingHistoryDAO;
import com.ontide.oneplanner.dao.ScheduleInfoDAO;
import com.ontide.oneplanner.dao.TaskInfoDAO;
import com.ontide.oneplanner.dao.UserInfoDAO;
import com.ontide.oneplanner.etc.Constant;
import com.ontide.oneplanner.etc.ReturnCode;
import com.ontide.oneplanner.etc.Utils;
import com.ontide.oneplanner.io.ResultObj;
import com.ontide.oneplanner.obj.AuthInfo;
import com.ontide.oneplanner.obj.MailingHistory;
import com.ontide.oneplanner.obj.UserInfo;
import com.ontide.oneplanner.obj.UserInfoWeb;
import com.ontide.oneplanner.service.EmailSender;
import com.ontide.oneplanner.service.FileService;

@Controller
@RequestMapping("subscr")
public class SubscriptionController {
	private static final Logger logger = LoggerFactory.getLogger(SubscriptionController.class);

	@Autowired
	private UserInfoDAO userInfoDAO;
	
	@Autowired
	private AuthInfoDAO authInfoDAO;
	
	@Autowired
	private TaskInfoDAO taskInfoDAO;

	@Autowired
	private ScheduleInfoDAO scheduleInfoDAO;

	@Autowired
	private MailingHistoryDAO mailingHistoryDAO;

	@Autowired
	//private DataSourceTransactionManager transactionManager;
	private PlatformTransactionManager transactionManager;
	
	@Autowired
	private FileService fileService;

	@Autowired
	private EmailSender emailSender;

	@Autowired
	private EnvironmentBean environmentBean;
	
	/**
	 * 로그인 관련 id중복 확인      
	 */
	@RequestMapping(value="/checkdup", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody ResultObj checkIdDuplication(@RequestBody UserInfo userInfo) {
		long startMilSec = (new Date()).getTime();
		logger.info("checkIdDuplication:"+userInfo);
		//1.overwrite여부
		//overwrite Y이면 신규 생성
		//overwrite N이면 존재여부 확인
		ResultObj result =  new ResultObj();
		try {
			logger.debug("userInfo.getUserId()="+userInfo.getUserId());;
			UserInfo resultInfo = userInfoDAO.get(userInfo.getUserId());
			logger.debug("resultInfo="+resultInfo);;
			if (resultInfo != null) {
				result.setResultCode(ReturnCode.ALREADY_EXISTS.get());
				result.setResultMsg(ReturnCode.STR_ALREADY_EXISTS.get());
			} else {
				result.setResultCode(ReturnCode.SUCCESS.get());
				result.setResultMsg(ReturnCode.STR_SUCCESS.get());
			}
			logger.debug("result="+result);;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("checkIdDuplication fail:"+userInfo, e);
			result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
			result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
		}
		logger.info("checkIdDuplication:end:"+((new Date()).getTime()-startMilSec)+":"+result+":"+userInfo);
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
	@RequestMapping(value="/subscribe", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody ResultObj<String> subscribe(@RequestBody UserInfo userInfo) {
		
		logger.info("subscribe:"+userInfo);
		long startMilSec = (new Date()).getTime();
		ResultObj<String> result =  new ResultObj<String>();
		AuthInfo authInfo = new AuthInfo();
		MailingHistory mailingHistory = new MailingHistory();
		TransactionDefinition txDef = new DefaultTransactionDefinition();
		TransactionStatus txStatus = null;
		String url ="";
		try {
			String langCode = Constant.LANG_EN.get().equals(userInfo.getLangCode())?userInfo.getLangCode():Constant.LANG_KO.get();

			if (!Constant.USER_TYPE_GOOGLE.get().equals(userInfo.getUserType())) {
				String authId = Utils.getBase64String(userInfo.getUserId()+userInfo.getPasswd()+Utils.getRandomPassword());
				authInfo.setUserId(userInfo.getUserId());
				authInfo.setAuthId(authId);
				authInfo.setAuthMode("A");
				mailingHistory.setUserId(userInfo.getUserId());
				mailingHistory.setAuthId(authId);
				mailingHistory.setAuthMode("A");
				//http://123.12312.1234.12:8080/subscr/request/emailauth?authId=.....
				url =  String.format(environmentBean.getMailSubscAuthUrl()
						,environmentBean.getDomainIp()
						,environmentBean.getPort()
						,authId, userInfo.getUserId()
						,langCode);
				String title =  environmentBean.getMailSubscAuthTitle(langCode);
				String content = String.format(environmentBean.getMailSubscAuthContent(langCode), userInfo.getUserName(),url);
				logger.info("environmentBean.getMailSubscAuthContent():"+environmentBean.getMailSubscAuthContent(langCode));
				logger.info("content:"+content);
				emailSender.send(userInfo.getEmail(), title, content);
				userInfo.setAuthYn("N");
			} else {
				userInfo.setAuthYn("Y");
			}
			UserInfoWeb userInfoWeb = new UserInfoWeb(userInfo);
			userInfoWeb.setAuthMode("A");
			UserInfo resultUserInfo = userInfoDAO.get(userInfo.getUserId());
			boolean alreadyExists = (resultUserInfo != null && resultUserInfo.getUserId().equals(userInfo.getUserId()));
			
			txStatus = transactionManager.getTransaction(txDef);
			if (alreadyExists) {
				userInfoWeb.setDeleteYn("N");
				userInfoDAO.update(userInfoWeb);
			} else {
				userInfoDAO.create(userInfoWeb);
			}
			if (!Constant.USER_TYPE_GOOGLE.get().equals(userInfo.getUserType())) {
				authInfoDAO.create(authInfo);
				mailingHistoryDAO.create(mailingHistory);
			}
			transactionManager.commit(txStatus);

			if (alreadyExists) {
				result.setResultCode(ReturnCode.OK_USER_UPDATED.get());
				result.setResultMsg(ReturnCode.STR_OK_USER_UPDATED.get());
			} else {
				result.setResultCode(ReturnCode.SUCCESS.get());
				result.setResultMsg(ReturnCode.STR_SUCCESS.get());
			}
			if (!Constant.USER_TYPE_GOOGLE.get().equals(userInfo.getUserType())) {
				result.setItem(url);
			}

		} catch (MessagingException me) {
			logger.error("subscribe failed.",me);
			transactionManager.rollback(txStatus);
			result.setResultCode(ReturnCode.ERROR_MAIL_SEND.get());
			result.setResultMsg(ReturnCode.STR_ERROR_MAIL_SEND.get());
		} catch (Exception e) {
			logger.error("subscribe failed.",e);
			transactionManager.rollback(txStatus);
			result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
			result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
		}
		logger.info("subscribe:end:"+((new Date()).getTime()-startMilSec)+":"+result+":"+userInfo);
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
	@RequestMapping(value="/request/authconfirm", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody ResultObj<String> sendConfirmMail(@RequestBody UserInfo userInfo) {
		
		logger.info("sendConfirmMail:"+userInfo);
		long startMilSec = (new Date()).getTime();
		ResultObj<String> result =  new ResultObj<String>();
		AuthInfo authInfo = new AuthInfo();
		MailingHistory mailingHistory = new MailingHistory();
		TransactionDefinition txDef = new DefaultTransactionDefinition();
		TransactionStatus txStatus = null;
		String url ="";
		try {
			String langCode = Constant.LANG_EN.get().equals(userInfo.getLangCode())?userInfo.getLangCode():Constant.LANG_KO.get();

			UserInfo resultUserInfo = userInfoDAO.get(userInfo.getUserId());
			if (resultUserInfo == null || !resultUserInfo.getUserId().equals(userInfo.getUserId())) {
				result.setResultCode(ReturnCode.USER_NOT_FOUND.get());
				result.setResultMsg(ReturnCode.STR_USER_NOT_FOUND.get());
				logger.error("sendConfirmMail:"+result+":"+userInfo);
				return result;
			}

			if (!Constant.USER_TYPE_GOOGLE.get().equals(resultUserInfo.getUserType())) {
				String authId = Utils.getBase64String(resultUserInfo.getUserId()+resultUserInfo.getPasswd()+Utils.getRandomPassword());
				authInfo.setUserId(resultUserInfo.getUserId());
				authInfo.setAuthId(authId);
				authInfo.setAuthMode("A");
				mailingHistory.setUserId(resultUserInfo.getUserId());
				mailingHistory.setAuthId(authId);
				mailingHistory.setAuthMode("A");
				//http://123.12312.1234.12:8080/subscr/request/emailauth?authId=.....
				url =  String.format(environmentBean.getMailSubscAuthUrl()
						,environmentBean.getDomainIp()
						,environmentBean.getPort()
						,authId, resultUserInfo.getUserId()
						,langCode);
				String title =  environmentBean.getMailSubscAuthTitle(langCode);
				String content = String.format(environmentBean.getMailSubscAuthContent(langCode), resultUserInfo.getUserName(),url);
				logger.info("environmentBean.getMailSubscAuthContent():"+environmentBean.getMailSubscAuthContent(langCode));
				logger.info("content:"+content);
				emailSender.send(resultUserInfo.getEmail(), title, content);
				resultUserInfo.setAuthYn("N");
			} else {
				resultUserInfo.setAuthYn("Y");
			}

			UserInfoWeb userInfoWeb = new UserInfoWeb(resultUserInfo);
			userInfoWeb.setAuthMode("A");
			userInfoWeb.setDeleteYn("N");
			
			txStatus = transactionManager.getTransaction(txDef);
			userInfoDAO.update(userInfoWeb);
			if (!Constant.USER_TYPE_GOOGLE.get().equals(userInfo.getUserType())) {
				authInfoDAO.create(authInfo);
				mailingHistoryDAO.create(mailingHistory);
			}
			transactionManager.commit(txStatus);

			result.setResultCode(ReturnCode.SUCCESS.get());
			result.setResultMsg(ReturnCode.STR_SUCCESS.get());

			if (!Constant.USER_TYPE_GOOGLE.get().equals(userInfo.getUserType())) {
				result.setItem(url);
			}

		} catch (MessagingException me) {
			logger.error("sendConfirmMail failed.",me);
			transactionManager.rollback(txStatus);
			result.setResultCode(ReturnCode.ERROR_MAIL_SEND.get());
			result.setResultMsg(ReturnCode.STR_ERROR_MAIL_SEND.get());
		} catch (Exception e) {
			logger.error("sendConfirmMail failed.",e);
			transactionManager.rollback(txStatus);
			result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
			result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
		}
		logger.info("sendConfirmMail:end:"+((new Date()).getTime()-startMilSec)+":"+result+":"+userInfo);
		return result;
	}

	/**
	 *	사용자 정보 조회
	 */
	@RequestMapping(value="/userinfo/get", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody ResultObj<UserInfo> getUserInfo(@RequestBody UserInfo userInfo) {
		long startMilSec = (new Date()).getTime();
		logger.info("getUserInfo:"+userInfo);
		ResultObj<UserInfo> result =  new ResultObj<UserInfo>();
		try {
			UserInfo resultUserInfo = userInfoDAO.get(userInfo.getUserId());
			if (resultUserInfo == null || !resultUserInfo.getUserId().equals(userInfo.getUserId())) {
				result.setResultCode(ReturnCode.USER_NOT_FOUND.get());
				result.setResultMsg(ReturnCode.STR_USER_NOT_FOUND.get());
				logger.error("getUserInfo:"+result+":"+userInfo);
				return result;
			}
			result.setResultCode(ReturnCode.SUCCESS.get());
			result.setResultMsg(ReturnCode.STR_SUCCESS.get());
			result.setItem(resultUserInfo);
		} catch (Exception e) {
			logger.error("getUserInfo failed.",e);
			result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
			result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
		}
		logger.info("getUserInfo:end:"+((new Date()).getTime()-startMilSec)+":"+result+":"+userInfo);
		return result;
	}
	
	/**
	 *1-4. 비번 초기화 
	 * 임시비번 생성
	 * 이메일 전송
	*/
	@RequestMapping(value="/request/passwdreset", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody ResultObj<String> requestPasswordReset(@RequestBody UserInfo userInfo) {
		long startMilSec = (new Date()).getTime();
		logger.info("requestPasswordReset:"+userInfo);
		ResultObj<String> result =  new ResultObj<String>();
		AuthInfo authInfo = new AuthInfo();
		MailingHistory mailingHistory = new MailingHistory();
		TransactionDefinition txDef = new DefaultTransactionDefinition();
		TransactionStatus txStatus = null;
		try {
			//find ring info by user num.
			//set returnCode.
			String langCode = Constant.LANG_EN.get().equals(userInfo.getLangCode())?userInfo.getLangCode():Constant.LANG_KO.get();

			UserInfo tempUserInfo = userInfoDAO.get(userInfo.getUserId());
			if (tempUserInfo == null || !tempUserInfo.getUserId().equals(userInfo.getUserId())) {
				result.setResultCode(ReturnCode.USER_NOT_FOUND.get());
				result.setResultMsg(ReturnCode.STR_USER_NOT_FOUND.get());
				logger.error("requestPasswordReset:"+result+":"+userInfo);
				return result;
			}			
			
			String authId = Utils.getBase64String(tempUserInfo.getUserId()+tempUserInfo.getPasswd()+Utils.getRandomPassword());
			authInfo.setUserId(tempUserInfo.getUserId());
			authInfo.setAuthId(authId);
			authInfo.setAuthMode("R");
			authInfo.setComfirmYn("N");
			mailingHistory.setUserId(tempUserInfo.getUserId());
			mailingHistory.setAuthId(authId);
			mailingHistory.setAuthMode("R");
			UserInfoWeb userInfoWeb = new UserInfoWeb(tempUserInfo);
			userInfoWeb.setAuthMode("R");
			
			String title =  environmentBean.getMailPasswordResetTitle(langCode);
			String url =  String.format(environmentBean.getMailPasswdResetUrl()
					,environmentBean.getDomainIp()
					,environmentBean.getPort()
					,authId, tempUserInfo.getUserId()
					,langCode);
			String content = String.format(environmentBean.getMailpasswordResetContent(langCode), tempUserInfo.getUserName(),url);
			emailSender.send(tempUserInfo.getEmail(), title, content);
			
			txStatus = transactionManager.getTransaction(txDef);
			authInfoDAO.create(authInfo);
			mailingHistoryDAO.create(mailingHistory);
			userInfoDAO.update(userInfoWeb);
			transactionManager.commit(txStatus);
			result.setResultCode(ReturnCode.SUCCESS.get());
			result.setResultMsg(ReturnCode.STR_SUCCESS.get());
		} catch (MessagingException me) {
			logger.error("requestPasswordReset failed.",me);
			transactionManager.rollback(txStatus);
			result.setResultCode(ReturnCode.ERROR_MAIL_SEND.get());
			result.setResultMsg(ReturnCode.STR_ERROR_MAIL_SEND.get());
		} catch (Exception e) {
			logger.error("requestPasswordReset failed.",e);
			transactionManager.rollback(txStatus);
			result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
			result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
		}
		logger.info("requestPasswordReset:end:"+((new Date()).getTime()-startMilSec)+":"+result+":"+userInfo);
		return result;	
	}
	

	/**
	 *1-5. 인증 url 클릭시 인증처리후 실제 가입처리 
	 * 1. authId & userId를 확인
	 * 2. 맞으면 authId confirm=Y
	 * 3. 승인결과 userInfo 리턴
	 * 
	*/
	@RequestMapping(value="/request/passwdreset/confirm", method=RequestMethod.GET)
	//public @ResponseBody ResultObj<UserInfo> requestEmailAuthentication(@RequestParam("authId") String authId,@RequestParam String userId) {
	public ModelAndView confirmPasswdReset(@RequestParam("authId") String authId,@RequestParam String userId
			,@RequestParam String langCode) {
		//TODO  return 을 json말고 html로 변경
		long startMilSec = (new Date()).getTime();
		logger.info("confirmPasswdReset:"+authId+":"+userId+":"+langCode);
		ResultObj<UserInfo> result =  new ResultObj<UserInfo>();
		TransactionDefinition txDef = new DefaultTransactionDefinition();
		TransactionStatus txStatus = null;
		ModelAndView model = new ModelAndView();
		try {
			//find ring info by user num.
			//set returnCode.
			AuthInfo authInfo = authInfoDAO.get(userId, Utils.getBase64StringRecover(authId));
			if (authInfo == null || !authInfo.getUserId().equals(userId)) {
				result.setResultCode(ReturnCode.INVALID_AUTH.get());
				result.setResultMsg(ReturnCode.STR_INVALID_AUTH.get());
				logger.error("confirmPasswdReset:"+authId+":"+result+":"+userId+":"+langCode);
				return  getViewFailed(langCode);
			}			
			authInfo.setComfirmYn("Y");
			UserInfo tempUserInfo = userInfoDAO.get(userId);
			if (tempUserInfo == null || !tempUserInfo.getUserId().equals(userId)) {
				result.setResultCode(ReturnCode.USER_NOT_FOUND.get());
				result.setResultMsg(ReturnCode.STR_USER_NOT_FOUND.get());
				logger.error("confirmPasswdReset:"+result+":"+userId+":"+langCode);
				return  getViewFailed(langCode);
			}			
			UserInfoWeb userInfo = new UserInfoWeb(tempUserInfo);
			userInfo.setAuthMode("S");
			//String langCode = Constant.LANG_EN.get().equals(tempUserInfo.getLangCode())?tempUserInfo.getLangCode():Constant.LANG_KO.get();

			String newPassword = Utils.getRandomPassword();
			userInfo.setPasswd(newPassword);
			MailingHistory mailingHistory = new MailingHistory();
			mailingHistory.setUserId(userInfo.getUserId());
			mailingHistory.setAuthId(authInfo.getAuthId());
			mailingHistory.setAuthMode("S");
			
			txStatus = transactionManager.getTransaction(txDef);
			authInfoDAO.update(authInfo);
			userInfoDAO.update(userInfo);
			mailingHistoryDAO.create(mailingHistory);
			transactionManager.commit(txStatus);
			
			result.setResultCode(ReturnCode.SUCCESS.get());
			result.setResultMsg(ReturnCode.STR_SUCCESS.get());
			result.setItem(tempUserInfo);
			model = getViewPasswdResetSuccess(langCode, tempUserInfo.getUserName(),newPassword);
		} catch (Exception e) {
			logger.error("confirmPasswdReset failed.",e);
			transactionManager.rollback(txStatus);
			result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
			result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
			model = getViewFailed(langCode);
		}
		logger.info("confirmPasswdReset:end:"+((new Date()).getTime()-startMilSec)+":"+result+":"+authId+":"+userId+":"+langCode);
		return model;	
	}
	

	/**
	 *1-5. 인증 url 클릭시 인증처리후 실제 가입처리 
	 * 1. authId & userId를 확인
	 * 2. 맞으면 authId confirm=Y
	 * 3. 승인결과 userInfo 리턴
	 * 
	*/
	@RequestMapping(value="/request/emailauth", method=RequestMethod.GET)
	//public @ResponseBody ResultObj<UserInfo> requestEmailAuthentication(@RequestParam("authId") String authId,@RequestParam String userId) {
	public ModelAndView requestEmailAuthentication(@RequestParam("authId") String authId,@RequestParam String userId
			,@RequestParam String langCode) {
		//TODO  return 을 json말고 html로 변경
		long startMilSec = (new Date()).getTime();
		logger.info("requestEmailAuthentication:"+authId+":"+userId+":"+langCode);
		ResultObj<UserInfo> result =  new ResultObj<UserInfo>();
		TransactionDefinition txDef = new DefaultTransactionDefinition();
		TransactionStatus txStatus = null;
		ModelAndView model = new ModelAndView();
		try {
			//find ring info by user num.
			//set returnCode.
			AuthInfo authInfo = authInfoDAO.get(userId, Utils.getBase64StringRecover(authId));
			if (authInfo == null || !authInfo.getUserId().equals(userId)) {
				result.setResultCode(ReturnCode.INVALID_AUTH.get());
				result.setResultMsg(ReturnCode.STR_INVALID_AUTH.get());
				logger.error("requestEmailAuthentication:"+authId+":"+result+":"+userId+":"+langCode);
				return  getViewFailed(langCode);
			}			
			authInfo.setComfirmYn("Y");
			UserInfo userInfo = userInfoDAO.get(userId);
			if (userInfo == null || !userInfo.getUserId().equals(userId)) {
				result.setResultCode(ReturnCode.USER_NOT_FOUND.get());
				result.setResultMsg(ReturnCode.STR_USER_NOT_FOUND.get());
				logger.error("requestEmailAuthentication:"+result+":"+userId+":"+langCode);
				return  getViewFailed(langCode);
			}			
			userInfo.setAuthYn("Y");
			UserInfoWeb tempUserInfo = new UserInfoWeb(userInfo);
			tempUserInfo.setAuthMode("Y");
			tempUserInfo.setConfirmDate(Utils.yyyyMMddHHmmss());
			MailingHistory mailingHistory = new MailingHistory();
			mailingHistory.setUserId(userInfo.getUserId());
			mailingHistory.setAuthId(authInfo.getAuthId());
			mailingHistory.setAuthMode("Y");

			txStatus = transactionManager.getTransaction(txDef);
			authInfoDAO.update(authInfo);
			userInfoDAO.update(tempUserInfo);
			mailingHistoryDAO.create(mailingHistory);
			transactionManager.commit(txStatus);
			
			result.setResultCode(ReturnCode.SUCCESS.get());
			result.setResultMsg(ReturnCode.STR_SUCCESS.get());
			result.setItem(tempUserInfo);
			model = getViewSuccess(langCode);
		} catch (Exception e) {
			logger.error("requestEmailAuthentication failed.",e);
			transactionManager.rollback(txStatus);
			result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
			result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
			model = getViewFailed(langCode);
		}
		logger.info("requestEmailAuthentication:end:"+((new Date()).getTime()-startMilSec)+":"+result+":"+authId+":"+userId+":"+langCode);
		return model;	
	}
	
	/**
	 *1-6. 사용자정보 변경사항
	*/
	@RequestMapping(value="/userinfo/update", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody ResultObj<UserInfo> updateUserInfo(@RequestBody UserInfo userInfo) {
		long startMilSec = (new Date()).getTime();
		logger.info("updateUserInfo:"+userInfo);
		ResultObj<UserInfo> result =  new ResultObj<UserInfo>();
		try {
			//find ring info by user num.
			//set returnCode.
			UserInfo tempUserInfo = userInfoDAO.get(userInfo.getUserId());
			if (tempUserInfo == null || !tempUserInfo.getUserId().equals(userInfo.getUserId())) {
				result.setResultCode(ReturnCode.USER_NOT_FOUND.get());
				result.setResultMsg(ReturnCode.STR_USER_NOT_FOUND.get());
				logger.error("updateUserInfo:"+result+":"+userInfo);
				return result;
			}			
			userInfoDAO.update(userInfo);
			result.setResultCode(ReturnCode.SUCCESS.get());
			result.setResultMsg(ReturnCode.STR_SUCCESS.get());
			result.setItem(userInfo);
		} catch (Exception e) {
			logger.error("updateUserInfo failed.",e);
			result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
			result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
		}
		logger.info("updateUserInfo:end:"+((new Date()).getTime()-startMilSec)+":"+result+":"+userInfo);
		return result;	
	}

	/**
	 * 사용자 해지
	 * 1. delete subscription
	 * 2. todo: cleanse task,schedule -> user 
	 */
	@RequestMapping(value="/unsubscribe", method=RequestMethod.POST, consumes={"application/json"})
	public @ResponseBody ResultObj unsubscribe(@RequestBody UserInfo userInfo) {
		
		logger.info("unsubscribe:"+userInfo);
		long startMilSec = (new Date()).getTime();
		ResultObj result =  new ResultObj();

		try {
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("userId",userInfo.getUserId());
			paramMap.put("deleteYn","N");
			if (taskInfoDAO.getCnt(paramMap) > 0) {
				result.setResultCode(ReturnCode.TASK_EXISTS.get());
				result.setResultMsg(ReturnCode.STR_TASK_EXISTS.get());
				logger.error("unsubscribe:"+result+":"+userInfo);
				return result;
			}	
			userInfo.setDeleteYn("Y");
			if (userInfoDAO.update(userInfo) > 0) {
				result.setResultCode(ReturnCode.SUCCESS.get());
				result.setResultMsg(ReturnCode.STR_SUCCESS.get());
			} else {
				result.setResultCode(ReturnCode.USER_NOT_FOUND.get());
				result.setResultMsg(ReturnCode.STR_USER_NOT_FOUND.get());
			}
		} catch (Exception e) {
			logger.error("unsubscribe failed.",e);
			result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
			result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
		}
		logger.info("unsubscribe:end:"+((new Date()).getTime()-startMilSec)+":"+result+":"+userInfo);
		return result;
	}

	private ModelAndView getViewPasswdResetSuccess(String langCode, String userName, String passwd) {
		ModelAndView model;
		if (Constant.LANG_EN.get().equals(langCode)) {
			model = new ModelAndView("passwdreset_en");
		} else {
			model = new ModelAndView("passwdreset_ko");
		}
		model.addObject("userName", userName);
		model.addObject("passwd",passwd);
		return model;
	}
	
	private ModelAndView getViewSuccess(String langCode) {
		ModelAndView model = new ModelAndView("authresult");
		if (Constant.LANG_EN.get().equals(langCode)) {
			model.addObject(Constant.AUTH_RESULT_TITLE.get(), Constant.AUTH_SUCCESS_TITLE_EN.get());
			model.addObject(Constant.AUTH_RESULT_MESSAGE.get(), Constant.AUTH_SUCCESS_MSG_EN.get());
			model.addObject(Constant.AUTH_RESULT_BUTTON.get(), Constant.AUTH_SUCCESS_BUTTON_EN.get());
		} else {
			model.addObject(Constant.AUTH_RESULT_TITLE.get(), Constant.AUTH_SUCCESS_TITLE_KO.get());
			model.addObject(Constant.AUTH_RESULT_MESSAGE.get(), Constant.AUTH_SUCCESS_MSG_KO.get());
			model.addObject(Constant.AUTH_RESULT_BUTTON.get(), Constant.AUTH_SUCCESS_BUTTON_KO.get());
		}
		return model;
	}
	private ModelAndView getViewFailed(String langCode) {
		ModelAndView model = new ModelAndView("authresult");
		if (Constant.LANG_EN.get().equals(langCode)) {
			model.addObject(Constant.AUTH_RESULT_TITLE.get(), Constant.AUTH_FAIL_TITLE_EN.get());
			model.addObject(Constant.AUTH_RESULT_MESSAGE.get(), Constant.AUTH_FAIL_MSG_EN.get());
			model.addObject(Constant.AUTH_RESULT_BUTTON.get(), Constant.AUTH_FAIL_BUTTON_EN.get());
		} else {
			model.addObject(Constant.AUTH_RESULT_TITLE.get(), Constant.AUTH_FAIL_TITLE_KO.get());
			model.addObject(Constant.AUTH_RESULT_MESSAGE.get(), Constant.AUTH_FAIL_MSG_KO.get());
			model.addObject(Constant.AUTH_RESULT_BUTTON.get(), Constant.AUTH_FAIL_BUTTON_KO.get());
		}
		return model;
	}

}
