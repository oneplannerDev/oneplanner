package com.ontide.oneplanner.client;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.reflect.TypeToken;
import com.ontide.oneplanner.etc.Utils;
import com.ontide.oneplanner.io.RequestObj;
import com.ontide.oneplanner.io.ResultObj;
import com.ontide.oneplanner.obj.ScheduleHistory;
import com.ontide.oneplanner.obj.ScheduleInfo;
import com.ontide.oneplanner.obj.TaskInfo;
import com.ontide.oneplanner.obj.TaskInfoOut;
import com.ontide.oneplanner.obj.TodayInfo;
import com.ontide.oneplanner.obj.UserInfo;

public class JsonHandler {
	//private static final Logger logger = LoggerFactory.getLogger(JsonHandler.class);
	LogWrapper logger = new LogWrapper(JsonHandler.class);
	String url;//= "http://52.79.110.126:8080/oneplanner/";
	String authUrl;

	public JsonHandler() {
		TestObj testObj = new TestObj();
		this.url = testObj.URL;
	}
	/**
	 * 로그인전 id중복확인
	 * 	ResultCode:ResultMsg
	 * 	0001 Success
	 * 	9999 Unknown Error
	 * 	1001 Already Exists
	 * @param userInfo
	 * @return
	 */
	public ResultObj callCheckDup(UserInfo userInfo)  {
		String url = this.url + "/subscr/checkdup";
		RestClient caller = new RestClient();
		ResultObj result = caller.callJsonHttp(url, userInfo);
		logger.debug("testCheckDup userInfo["+userInfo+"]result["+result+"]");
		return result;
	}

	/**
	 * 사용자 가입 -> 인증 url 이메일 전송
	 * 	0001 Success
	 *  0103 Existing user updated
	 * 	5001 Mail Send Error
	 * 	9999 Unknown Error
	 * @param userInfo
	 * @return
	 */
	public ResultObj<String> callSubscribe(UserInfo userInfo) {
		String url = this.url + "/subscr/subscribe";
		RestClient<String> caller = new RestClient<String>();
		Type type = new TypeToken<ResultObj<String>>() {}.getType();
		ResultObj<String> result = caller.callJsonHttp(url, userInfo, type);
		this.authUrl = result.getItem();
		logger.info("testSubscribe authUrl["+authUrl+"]userInfo["+userInfo+"]result["+result.getResultCode()+"]["+result.getItem()+"]");
		return result;
	}

	/**
	 * 인증 url 클릭시 인증처리후 실제 가입처리
	 * 	0001 Success
	 *  4001 User Not Found
	 * 	4002 Invalid authentication id
	 * 	9999 Unknown Error
	 * @param url
	 * @return
	 */
	public void callEmailAuthentication(String url) {
		RestClient<UserInfo> caller = new RestClient<UserInfo>();
		Type type = new TypeToken<ResultObj<UserInfo>>() {}.getType();
		caller.callGetHttp(url, type);
		logger.debug("testEmailAuthentication url["+url+"]");
		return;
	}

	/**
	 * 사용자정보 조회
	 * 0001 Success
	 * 4001 User Not Found
	 * 9999 Unknown Error
	 * @param userInfo
	 * @return
	 */
	public ResultObj<UserInfo> callGetUser(UserInfo userInfo) {
		String url = this.url + "/subscr/userinfo/get";
		RestClient<UserInfo> caller = new RestClient<UserInfo>();
		Type type = new TypeToken<ResultObj<UserInfo>>() {}.getType();
		ResultObj<UserInfo> result = caller.callJsonHttp(url, userInfo, type);
		logger.info("callGetUser userInfo["+userInfo+"]result["+result+"]");
		return result;
	}

	/**
	 * 비번 초기화->임시비번 생성후 이메일 전송
	 * 0001 Success
	 * 4001 User Not Found
	 * 5001 Mail Send Error
	 * 9999 Unknown Error
	 * @param userInfo
	 * @return
	 */
	public ResultObj<String> callPasswordReset(UserInfo userInfo) {
		String url = this.url + "/subscr/request/passwdreset";
		RestClient<String> caller = new RestClient<String>();
		Type type = new TypeToken<ResultObj<String>>() {}.getType();
		ResultObj<String> result = caller.callJsonHttp(url, userInfo, type);
		logger.debug("callPasswordReset userInfo["+userInfo+"]result["+result+"]");
		return result;
	}
	/**
	 * 사용자정보 변경사항
	 * 0001 Success
	 * 4001 User Not Found
	 * 9999 Unknown Error
	 * @param userInfo
	 * @return
	 */
	public ResultObj<UserInfo> callUpdateUser(UserInfo userInfo) {
		String url = this.url + "/subscr/userinfo/update";
		RestClient<UserInfo> caller = new RestClient<UserInfo>();
		Type type = new TypeToken<ResultObj<UserInfo>>() {}.getType();
		ResultObj<UserInfo> result = caller.callJsonHttp(url, userInfo, type);
		logger.debug("callUpdateUser userInfo["+userInfo+"]result["+result+"]");
		return result;
	}
	/**
	 * 태스크 등록
	 * 0001 Success
	 * 1001 Already Exist
	 * 9999 Unknown Error
	 * @param taskInfo
	 * @return
	 */
	public ResultObj<TaskInfo> callRegisterTask(TaskInfo taskInfo) {
		String url = this.url + "/task/register";
		RestClient<TaskInfo> caller = new RestClient<TaskInfo>();
		Type type = new TypeToken<ResultObj<TaskInfo>>() {}.getType();
		ResultObj<TaskInfo> result = caller.callJsonHttp(url, taskInfo, type);
		logger.debug("callRegisterTask taskInfo["+taskInfo+"]result["+result+"]");
		return result;
	}

	/**
	 * 태스크 등록
	 * 0001 Success
	 * 1001 Already Exist
	 * 9999 Unknown Error
	 * @param taskInfo
	 * @return
	 */
	public ResultObj<TaskInfo> callRegisterTaskList(List<TaskInfo> taskInfoList) {
		String url = this.url + "/task/register/list";
		RestClient<TaskInfo> caller = new RestClient<TaskInfo>();
		Type type = new TypeToken<ResultObj<TaskInfo>>() {}.getType();
		ResultObj<TaskInfo> result = caller.callJsonHttp(url, taskInfoList, type);
		logger.debug("callRegisterTaskList taskInfoList["+taskInfoList+"]result["+result+"]");
		return result;
	}

	/**
	 * 태스크 조회(task id로 조회)
	 * 0001 Success
	 * 4101 Task Not Found
	 * 9999 Unknown Error 
	 * @param taskInfo
	 * @return
	 */
	public ResultObj<TaskInfoOut> callGetTask(TaskInfo  taskInfo) {
		String url = this.url + "/task/get";
		RestClient<TaskInfoOut> caller = new RestClient<TaskInfoOut>();
		Type type = new TypeToken<ResultObj<TaskInfoOut>>() {}.getType();
		ResultObj<TaskInfoOut> result = caller.callJsonHttp(url, taskInfo, type);
		logger.debug("callGetTask taskInfo["+taskInfo+"]result["+result+"]");
		return result;
	}
	/**
	 * 태스크 갱신. 변경사항 서버 반영
	 * 0001 Success
	 * 4101 Task Not Found
	 * 9999 Unknown Error
	 * @param taskInfo
	 * @return
	 */
	public ResultObj<TaskInfoOut> callUpdateTask(TaskInfo taskInfo) {
		String url = this.url + "/task/update";
		RestClient<TaskInfoOut> caller = new RestClient<TaskInfoOut>();
		Type type = new TypeToken<ResultObj<TaskInfoOut>>() {}.getType();
		ResultObj<TaskInfoOut> result = caller.callJsonHttp(url, taskInfo, type);
		logger.debug("callUpdateTask taskInfo["+taskInfo+"]result["+result+"]");
		return result;
	}

	/**
	 * 태스크 전체 다운로드
	 * 0001 Success
	 * 4101 Task Not Found
	 * 9999 Unknown Error
	 * @param userInfo
	 * @return
	 */
	public ResultObj<List<TaskInfoOut>> callCheckoutTask(UserInfo userInfo) {
		String url = this.url + "/task/checkout";
		RestClient<List<TaskInfoOut>> caller = new RestClient<List<TaskInfoOut>>();
		Type type = new TypeToken<ResultObj<List<TaskInfoOut>>>() {}.getType();
		ResultObj<List<TaskInfoOut>> result = caller.callJsonHttp(url, userInfo, type);
		logger.debug("callCheckoutTask userInfo["+userInfo+"]result["+result+"]");
		return result;
	}

	/**
	 * 스케쥴등록
	 * 0001 Success
 	 * 1001 Already Exist
	 * 9999 Unknown Error
	 * @param scheduleInfo
	 * @return
	 */
	public ResultObj<ScheduleInfo> callRegisterSchedule(ScheduleInfo scheduleInfo) {
		String url = this.url + "/schedule/register";
		RestClient<ScheduleInfo> caller = new RestClient<ScheduleInfo>();
		Type type = new TypeToken<ResultObj<ScheduleInfo>>() {}.getType();
		ResultObj<ScheduleInfo> result = caller.callJsonHttp(url, scheduleInfo, type);
		logger.debug("callRegisterSchedule scheduleInfo["+scheduleInfo+"]result["+result+"]");
		return result;
	}
	
	/**
	 * 스케쥴등록
	 * 0001 Success
 	 * 1001 Already Exist
	 * 9999 Unknown Error
	 * @param scheduleInfo
	 * @return
	 */
	public ResultObj<ScheduleInfo> callRegisterScheduleList(List<ScheduleInfo> scheduleInfoList) {
		String url = this.url + "/schedule/register/list";
		RestClient<ScheduleInfo> caller = new RestClient<ScheduleInfo>();
		Type type = new TypeToken<ResultObj<ScheduleInfo>>() {}.getType();
		ResultObj<ScheduleInfo> result = caller.callJsonHttp(url, scheduleInfoList, type);
		logger.debug("callRegisterScheduleList scheduleInfoList["+Utils.getListToString(scheduleInfoList)+"]result["+result+"]");
		return result;
	}

	/**
	 * 스케쥴조회(scheduleid로 조회)
	 * 0001 Success
	 * 4201 Schedule Not Found
	 * 9999 Unknown Error
	 * @param scheduleInfo
	 * @return
	 */
	public ResultObj<ScheduleInfo> callGetSchedule(ScheduleInfo scheduleInfo) {
		String url = this.url + "/schedule/get";
		RestClient<ScheduleInfo> caller = new RestClient<ScheduleInfo>();
		Type type = new TypeToken<ResultObj<ScheduleInfo>>() {}.getType();
		ResultObj<ScheduleInfo> result = caller.callJsonHttp(url, scheduleInfo, type);
		logger.debug("callGetSchedule scheduleInfo["+scheduleInfo+"]result["+result+"]");
		return result;
	}
	/**
	 * 스케쥴변경
	 * 0001 Success
	 * 4201 Schedule Not Found
	 * 9999 Unknown Error
	 * @param scheduleInfo
	 * @return
	 */
	public ResultObj<ScheduleInfo> callUpdateSchedule(ScheduleInfo scheduleInfo) {
		String url = this.url + "/schedule/update";
		RestClient<ScheduleInfo> caller = new RestClient<ScheduleInfo>();
		Type type = new TypeToken<ResultObj<ScheduleInfo>>() {}.getType();
		ResultObj<ScheduleInfo> result = caller.callJsonHttp(url, scheduleInfo, type);
		logger.debug("callUpdateSchedule scheduleInfo["+scheduleInfo+"]result["+result+"]");
		return result;
	}

	/**
	 * 스케쥴일정조회(userid, scheduleid, nowdate로 조회)
	 * 0001 Success
	 * 4201 Schedule Not Found
	 * 9999 Unknown Error
	 * @param scheduleHistory
	 * @return
	 */
	public ResultObj<ScheduleHistory> callGetScheduleHistory(ScheduleHistory scheduleHistory) {
		String url = this.url + "/schedule/get/history";
		RestClient<ScheduleHistory> caller = new RestClient<ScheduleHistory>();
		Type type = new TypeToken<ResultObj<ScheduleHistory>>() {}.getType();
		ResultObj<ScheduleHistory> result = caller.callJsonHttp(url, scheduleHistory, type);
		logger.debug("callGetScheduleHistory scheduleHistory["+scheduleHistory+"]result["+result+"]");
		return result;
	}
	/**
	 * 스케쥴 일정변경
	 * 0001 Success
	 * 4201 Schedule Not Found
	 * 9999 Unknown Error
	 * @param scheduleHistory
	 * @return
	 */
	public ResultObj<ScheduleHistory> callUpdateScheduleHistory(ScheduleHistory scheduleHistory) {
		String url = this.url + "/schedule/update/history";
		RestClient<ScheduleHistory> caller = new RestClient<ScheduleHistory>();
		Type type = new TypeToken<ResultObj<ScheduleHistory>>() {}.getType();
		ResultObj<ScheduleHistory> result = caller.callJsonHttp(url, scheduleHistory, type);
		logger.debug("callUpdateScheduleHistory scheduleHistory["+scheduleHistory+"]result["+result+"]");
		return result;
	}
	/**
	 * 스케쥴일정등록
	 * 	0001 Success
 	 *  1001 Already Exist
	 * 	9999 Unknown Error
	 * @param scheduleHistory
	 * @return
	 */
	public ResultObj<ScheduleHistory> callRegisterScheduleHistory(ScheduleHistory scheduleHistory) {
		String url = this.url + "/schedule/register/history";
		RestClient<ScheduleHistory> caller = new RestClient<ScheduleHistory>();
		Type type = new TypeToken<ResultObj<ScheduleHistory>>() {}.getType();
		ResultObj<ScheduleHistory> result = caller.callJsonHttp(url, scheduleHistory, type);
		logger.debug("callRegisterScheduleHistory scheduleHistory["+scheduleHistory+"]result["+result+"]");
		return result;
	}

	/**
	 * 스케쥴일정등록
	 * 	0001 Success
 	 *  1001 Already Exist
	 * 	9999 Unknown Error
	 * @param scheduleHistory
	 * @return
	 */
	public ResultObj<ScheduleHistory> callRegisterScheduleHistList(List<ScheduleHistory> scheduleHistoryList) {
		String url = this.url + "/schedule/register/history/list";
		RestClient<ScheduleHistory> caller = new RestClient<ScheduleHistory>();
		Type type = new TypeToken<ResultObj<ScheduleHistory>>() {}.getType();
		ResultObj<ScheduleHistory> result = caller.callJsonHttp(url, scheduleHistoryList, type);
		logger.debug("callRegisterScheduleHistList scheduleHistoryList["+scheduleHistoryList+"]result["+result+"]");
		return result;
	}
	
	/**
	 * 스케쥴삭제
	 * 0001 Success
	 * 4201 Schedule Not Found
	 * 9999 Unknown Error
	 * @param reqObj
	 * @return
	 */
	public ResultObj callSetSchedulesDeleteFlag(RequestObj reqObj) {
		String url = this.url + "/schedule/remove/setflag";
		RestClient<String> caller = new RestClient<String>();
		Type type = new TypeToken<ResultObj<String>>() {}.getType();
		ResultObj<String> result = caller.callJsonHttp(url, reqObj, type);
		logger.debug("callSetSchedulesDeleteFlag reqObj["+reqObj+"]result["+result+"]");
		return result;
	}

	/**
	 * 스케쥴삭제
	 * 0001 Success
	 * 4201 Schedule Not Found
	 * 9999 Unknown Error
	 * @param reqObj
	 * @return
	 */
	public ResultObj callDeleteFromSchedules(RequestObj reqObj) {
		String url = this.url + "/schedule/remove/delete";
		RestClient<String> caller = new RestClient<String>();
		Type type = new TypeToken<ResultObj<String>>() {}.getType();
		ResultObj<String> result = caller.callJsonHttp(url, reqObj, type);
		logger.debug("callDeleteFromSchedules reqObj["+reqObj+"]result["+result+"]");
		return result;
	}
	/**
	 * 스케쥴삭제
	 * 0001 Success
	 * 4201 Schedule Not Found
	 * 9999 Unknown Error
	 * @param reqObj
	 * @return
	 */
	public ResultObj callSetScheduleHistoriesDeleteFlag(RequestObj reqObj) {
		String url = this.url + "/schedule/remove/setflag/history";
		RestClient<String> caller = new RestClient<String>();
		Type type = new TypeToken<ResultObj<String>>() {}.getType();
		ResultObj<String> result = caller.callJsonHttp(url, reqObj, type);
		logger.debug("callSetScheduleHistoriesDeleteFlag reqObj["+reqObj+"]result["+result+"]");
		return result;
	}

	/**
	 * 스케쥴삭제
	 * 0001 Success
	 * 4201 Schedule Not Found
	 * 9999 Unknown Error
	 * @param reqObj
	 * @return
	 */
	public ResultObj callDeleteFromScheduleHistories(RequestObj reqObj) {
		String url = this.url + "/schedule/remove/delete/history";
		RestClient<String> caller = new RestClient<String>();
		Type type = new TypeToken<ResultObj<String>>() {}.getType();
		ResultObj<String> result = caller.callJsonHttp(url, reqObj, type);
		logger.debug("callDeleteFromScheduleHistories reqObj["+reqObj+"]result["+result+"]");
		return result;
	}
	/**
	 * 전체스케쥴 다운로드
	 * 0001 Success
	 * 4201 Schedule Not Found
	 * 9999 Unknown Error
	 * @param userInfo
	 * @return
	 */
	public ResultObj<List<ScheduleInfo>> callCheckoutSchedule(UserInfo userInfo) {
		String url = this.url + "/schedule/checkout";
		RestClient<List<ScheduleInfo>> caller = new RestClient<List<ScheduleInfo>>();
		Type type = new TypeToken<ResultObj<List<ScheduleInfo>>>() {}.getType();
		ResultObj<List<ScheduleInfo>> result = caller.callJsonHttp(url, userInfo, type);
		logger.debug("callCheckoutSchedule userInfo["+userInfo+"]result["+result+"]");
		return result;
	}

	/**
	 * 전체스케쥴 이력 다운로드
	 * 0001 Success
	 * 4201 Schedule Not Found
	 * 9999 Unknown Error
	 * @param userInfo
	 * @return
	 */
	public ResultObj<List<ScheduleHistory>> callCheckoutScheduleHistory(UserInfo userInfo) {
		String url = this.url + "/schedule/checkout/history";
		RestClient<List<ScheduleHistory>> caller = new RestClient<List<ScheduleHistory>>();
		Type type = new TypeToken<ResultObj<List<ScheduleHistory>>>() {}.getType();
		ResultObj<List<ScheduleHistory>> result = caller.callJsonHttp(url, userInfo, type);
		logger.debug("callCheckoutScheduleHistory userInfo["+userInfo+"]result["+result+"]");
		return result;
	}

	/**
	 * 태스크 삭제
	 * 0001 Success
	 * 1201 Schedules Exists
	 * 4101 Task Not Found
	 * 9999 Unknown Error
	 * @param reqObj
	 * @return
	 */
	public ResultObj callSetTasksDeleteFlag(RequestObj reqObj) {
		String url = this.url + "/task/remove/setflag";
		RestClient<String> caller = new RestClient<String>();
		Type type = new TypeToken<ResultObj<String>>() {}.getType();
		ResultObj<String> result = caller.callJsonHttp(url, reqObj, type);
		logger.debug("callSetTaskDeleteFlag reqObj["+reqObj+"]result["+result+"]");
		return result;
	}

	/**
	 * 태스크 삭제
	 * 0001 Success
	 * 1201 Schedules Exists
	 * 4101 Task Not Found
	 * 9999 Unknown Error
	 * @param reqObj
	 * @return
	 */
	public ResultObj callDeleteFromTasks(RequestObj reqObj) {
		String url = this.url + "/task/remove/delete";
		RestClient<String> caller = new RestClient<String>();
		Type type = new TypeToken<ResultObj<String>>() {}.getType();
		ResultObj<String> result = caller.callJsonHttp(url, reqObj, type);
		logger.debug("callDeleteFromTasks reqObj["+reqObj+"]result["+result+"]");
		return result;
	}
	/**
	 * 사용자정보 삭제
	 * 0001 Success
	 * 4001 User Not Found
	 * 1101 Tasks Exists
	 * 9999 Unknown Error
	 * @param userInfo
	 * @return
	 */
	public ResultObj callUnsubscribe(UserInfo userInfo) {
		String url = this.url + "/subscr/unsubscribe";
		RestClient<UserInfo> caller = new RestClient<UserInfo>();
		Type type = new TypeToken<ResultObj<UserInfo>>() {}.getType();
		ResultObj<UserInfo> result = caller.callJsonHttp(url, userInfo, type);
		logger.debug("callUnsubscribe userInfo["+userInfo+"]result["+result+"]");
		return result;
	}

	/**
	 * 투데이 등록
	 * 0001 Success
 	 * 1001 Already Exist
	 * 9999 Unknown Error
	 * @param today
	 * @return
	 */
	public ResultObj<String> callRegisterToday(TodayInfo today, String filePath) {
		String url = this.url + "/today/admin/register";
		RestClient<String> caller = new RestClient<String>();
		Type type = new TypeToken<ResultObj<String>>() {}.getType();
		ResultObj<String> result = caller.callMultipartHttp(url, filePath, today, type);
		logger.debug("callRegisterToday today["+today+"]result["+result+"]");
		return result;
	}

	/**
	 * 투데이 등록. image file업로드를 하지 않는 경우
	 * 0001 Success
 	 * 1001 Already Exist
	 * 9999 Unknown Error
	 * @param today
	 * @return
	 */
	public ResultObj<String> callRegisterToday(TodayInfo today) {
		String url = this.url + "/today/admin/register/json";
		RestClient<String> caller = new RestClient<String>();
		Type type = new TypeToken<ResultObj<String>>() {}.getType();
		ResultObj<String> result = caller.callJsonHttp(url, today, type);
		logger.debug("callRegisterToday today["+today+"]result["+result+"]");
		return result;
	}

	
	/**
	 * 투데이  조회
	 * 0001 Success
	 * 4301 Today Not Found
	 * 9999 Unknown Error
	 * @param today
	 * @return
	 */
	public ResultObj<TodayInfo> callGetToday(TodayInfo today) {
		String url = this.url + "/today/get";
		RestClient<TodayInfo> caller = new RestClient<TodayInfo>();
		Type type = new TypeToken<ResultObj<TodayInfo>>() {}.getType();
		ResultObj<TodayInfo> result = caller.callJsonHttp(url, today, type);
		logger.debug("callGetToday today["+today+"]result["+result+"]");
		return  result;
	}

	/**
	 * 투데이 전체다운로드
	 * 0001 Success
	 * 4301 Today Not Found
	 * 9999 Unknown Error
	 * @param today
	 * @return
	 */
	public ResultObj<List<TodayInfo>> callCheckoutToday(TodayInfo today) {
		String url = this.url + "/today/checkout";
		RestClient<List<TodayInfo>> caller = new RestClient<List<TodayInfo>>();
		Type type = new TypeToken<ResultObj<List<TodayInfo>>>() {}.getType();
		ResultObj<List<TodayInfo>> result = caller.callJsonHttp(url, today, type);
		logger.debug("callGetToday today["+today+"]result["+result+"]");
		return  result;
	}

	/**
	 * 투데이  변경
	 * 0001 Success
	 * 4301 Today Not Found
	 * 9999 Unknown Error
	 * @param today
	 * @return
	 */
	public ResultObj<String> callUpdateToday(TodayInfo today,String filePath) {
		String url = this.url + "/today/admin/update";
		RestClient<String> caller = new RestClient<String>();
		Type type = new TypeToken<ResultObj<String>>() {}.getType();
		ResultObj<String> result = caller.callMultipartHttp(url, filePath, today, type);
		logger.debug("callUpdateToday today["+today+"]result["+result+"]");
		return  result;
	}

	/**
	 * 투데이  변경. image file upload가 없는 경우
	 * 0001 Success
	 * 4301 Today Not Found
	 * 9999 Unknown Error
	 * @param today
	 * @return
	 */
	public ResultObj<String> callUpdateToday(TodayInfo today) {
		String url = this.url + "/today/admin/update/json";
		RestClient<String> caller = new RestClient<String>();
		Type type = new TypeToken<ResultObj<String>>() {}.getType();
		ResultObj<String> result = caller.callJsonHttp(url, today, type);
		logger.debug("callUpdateToday today["+today+"]result["+result+"]");
		return  result;
	}

	/**
	 * 투데이 삭제
	 * 0001 Success
	 * 4301 Today Not Found
	 * 9999 Unknown Error
	 * @param today
	 * @return
	 */
	public ResultObj<TodayInfo> callRemoveToday(TodayInfo today) {
		String url = this.url + "/today/admin/remove";
		RestClient<TodayInfo> caller = new RestClient<TodayInfo>();
		Type type = new TypeToken<ResultObj<TodayInfo>>() {}.getType();
		ResultObj<TodayInfo> result = caller.callJsonHttp(url, today, type);
		logger.debug("callRemoveToday today["+today+"]result["+result+"]");
		return  result;
	}


}
