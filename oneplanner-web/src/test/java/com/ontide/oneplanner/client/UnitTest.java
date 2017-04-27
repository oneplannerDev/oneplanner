package com.ontide.oneplanner.client;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.ExpectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontide.oneplanner.etc.Constant;
import com.ontide.oneplanner.etc.ReturnCode;
import com.ontide.oneplanner.io.RequestObj;
import com.ontide.oneplanner.io.ResultObj;
import com.ontide.oneplanner.obj.ScheduleHistory;
import com.ontide.oneplanner.obj.ScheduleInfo;
import com.ontide.oneplanner.obj.TaskInfo;
import com.ontide.oneplanner.obj.TaskInfoOut;
import com.ontide.oneplanner.obj.TodayInfo;
import com.ontide.oneplanner.obj.UserInfo;

public class UnitTest {
	//private static final Logger logger = LoggerFactory.getLogger(UnitTest.class);
	LogWrapper logger = new LogWrapper(UnitTest.class);
	UserInfo userA;
	TaskInfo taskAA;
	TaskInfo taskAB;
	TaskInfo taskAC;

	TaskInfo taskBA;
	TaskInfo taskBB;
	TaskInfo taskBC;

	ScheduleInfo scheduleAA;
	ScheduleInfo scheduleAB;
	ScheduleInfo scheduleBA;
	ScheduleInfo scheduleBB;
	
	ScheduleInfo scheduleCA;
	ScheduleInfo scheduleCB;
	ScheduleInfo scheduleCC;

	ScheduleHistory scheduleHistoryAA;
	ScheduleHistory scheduleHistoryAB;
	ScheduleHistory scheduleHistoryBA;
	ScheduleHistory scheduleHistoryBB;
	
	ScheduleHistory scheduleHistoryCA;
	ScheduleHistory scheduleHistoryCB;
	ScheduleHistory scheduleHistoryCC;
	
	TodayInfo todayA;
	TodayInfo todayB;
	String url;
	String authUrl;
	JsonHandler jsonHandler;
	
	void init() throws Exception{ 
		TestObj testObj = new TestObj();
		this.userA = testObj.getUserA();
		this.taskAA = testObj.getTaskAA(userA);
		this.taskAB = testObj.getTaskAB(userA);
		this.taskAC = testObj.getTaskAC(userA);

		this.taskBA = testObj.getTaskBA(userA);
		this.taskBB = testObj.getTaskBB(userA);
		this.taskBC = testObj.getTaskBC(userA);

		
		this.scheduleAA = testObj.getScheduleAA(taskAA);
		this.scheduleAB = testObj.getScheduleAB(taskAA);
		this.scheduleBA = testObj.getScheduleBA(taskAB);
		this.scheduleBB = testObj.getScheduleBB(taskAB);

		this.scheduleCA = testObj.getScheduleCA(taskBA);
		this.scheduleCB = testObj.getScheduleCB(taskBA);
		this.scheduleCC = testObj.getScheduleCC(taskBA);

		this.scheduleHistoryAA = testObj.getScheduleHistoryAA(taskAA);
		this.scheduleHistoryAB = testObj.getScheduleHistoryAB(taskAA);
		this.scheduleHistoryBA = testObj.getScheduleHistoryBA(taskAB);
		this.scheduleHistoryBB = testObj.getScheduleHistoryBB(taskAB);

		this.scheduleHistoryCA = testObj.getScheduleHistoryCA(taskBA);
		this.scheduleHistoryCB = testObj.getScheduleHistoryCB(taskBA);
		this.scheduleHistoryCC = testObj.getScheduleHistoryCC(taskBA);
		
		this.todayA = testObj.getTodayA();
		this.todayB = testObj.getTodayB();
		this.url = testObj.URL;
		jsonHandler = new JsonHandler();
	}

	@Before
	public void setUp() throws Exception {
		init();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test 
	//@Category(com.ontide.oneplanner.client.ScenarioTestDev.class)
	public void testFlowShort() {
		logger.info("###################################################################");
		logger.info("testFlowShort STARTS.");
		logger.info("###################################################################");
//		userA.setUserId("testukFiImur");
//		taskAA.setUserId("testOVuI6T5f");
//		testGetUser();
		
		/**
		 * 고객등록: 
		 * 1. 최초 user id 중복발생 확인
		 * 2. 고객등록및 인증
		 * 3. 고객정보변경
		 * 4. 비번 변경 및 메일확인
		 * 5. 고객조회
		 */
		
		testCheckDup();
		testSubscribe();
		testUpdateUser();
		testPasswordReset();
		testGetUser();
		
		testRegisterTaskAA();
		testRegisterTaskAA();

		logger.info("###################################################################");
		logger.info("testFlowShort END.");
		logger.info("###################################################################");
	}


	@Test 
	//@Category(com.ontide.oneplanner.client.ScenarioTestDev.class)
	public void testFlowGoogleUser() {
		logger.info("###################################################################");
		logger.info("testFlowGoogleUser STARTS.");
		logger.info("###################################################################");
//		userA.setUserId("testukFiImur");
//		taskAA.setUserId("testOVuI6T5f");
//		testGetUser();
		
		/**
		 * 고객등록: 
		 * 1. 최초 user id 중복발생 확인
		 * 2. 구글 고객등록및 인증
		 * 3. 고객정보변경
		 * 5. 고객조회
		 */
		
//		testCheckDup();
		testSubscribeGoogle();
		testGetUser();
//		testUpdateUser();
//		testGetUser();

		logger.info("###################################################################");
		logger.info("testFlowGoogleUser END.");
		logger.info("###################################################################");
	}
	
	@Test 
	//@Category(com.ontide.oneplanner.client.ScenarioTestDev.class)
	public void testFlowNormal() {
		logger.info("###################################################################");
		logger.info("testFlowNormal STARTS.");
		logger.info("###################################################################");
//		userA.setUserId("testukFiImur");
//		taskAA.setUserId("testOVuI6T5f");
//		testGetUser();
		
		/**
		 * 고객등록: 
		 * 1. 최초 user id 중복발생 확인
		 * 2. 고객등록및 인증
		 * 3. 고객정보변경
		 * 4. 비번 변경 및 메일확인
		 * 5. 고객조회
		 */
		
		testCheckDup();
		testSubscribe();
		testUpdateUser();
		testPasswordReset();
		testGetUser();
		
		/**
		 * 태스크 등록
		 * 
		 * userA -> TaskAA, TaskAB, TaskAC
		 * 
		 * TaskAA -> ScheduleAA,AB
		 * TaskAB -> ScheduleBA,BB
		 * TaskAC -> None
		 * 
		 * 순으로 등록한다.
		 */
		testRegisterTaskAA();
		testRegisterTaskAB();
		testRegisterTaskAC();
		testGetTaskAA();
		testGetTaskAB();
		testGetTaskAC();
		testUpdateTask();
		testCheckoutTask();
		testRegisterScheduleAA();
		testRegisterScheduleAB();
		testRegisterScheduleBA();
		testRegisterScheduleBB();
		
		/* 
		 * bulk insert테스트
		 */
		testRegisterTaskList();
		testCheckoutTaskAfterBulkInsert();
		testRegisterScheduleList();
		testCheckoutScheduleAfterBulkInsert();
		testRegisterScheduleHistList();
		testCheckoutScheduleHistoryAfterBulkInsert();
		/**
		 * 태스크 조회
		 * 태스크 전체조회
		 * 스케쥴AA의 history 등록
		 * 등록후 변경
		 * history 조회
		 * 스케쥴 전체조회
		 */
		testGetTaskAA();
		testCheckoutTask();
		testGetScheduleAA();
		testRegisterScheduleHistoryAA();
		testGetScheduleHistoryAA();
		testUpdateScheduleAA();
		testUpdateScheduleHistoryAA();
		testGetScheduleHistoryAA();
		testCheckoutSchedule();
		
		/**
		 * 스케쥴 없는 태스크 삭제
		 * 
		 */
		testSetTasksDeleteFlagAC();
		testGetTaskACAfterSetDelete();
		testDeleteFromTasksAC();
		testGetTaskACAfterDeleteFrom();
		
		/** 
		 * 하위스케쥴이 있는 태스크를 삭제시도
		 * 결과는 스케쥴있음오류
		 */
		testSetTasksDeleteFlagABWithSchedule();
		//testGetTaskABAfterDelete();
		testDeleteFromTasksABWithSchedule();
		//testGetTaskABAfterDelete();
		
		/**
		 * 스케쥴 삭제 
		 * 결과는 정상
		 */
		testSetSchedulesDeleteFlagBB();
		testGetScheduleBBAfterSetDelete();
		testDeleteFromSchedulesBB();
		testGetScheduleBBAfterDeleteFrom();

		/**
		 * 스케쥴 히스토리 삭제 
		 * 결과는 정상
		 */
		testSetScheduleHistoriesDeleteFlagCA();
		testGetScheduleHistoryCAAfterSetDelete();
		testDeleteFromScheduleHistoriesCA();
		testGetScheduleHistoryCAAfterDeleteFrom();

		/**
		 * 사용자 삭제
		 * 태스크 있음오류발생
		 */
		testGetUser();
		testUnsubscribeWithTasks();

		/**
		 * 스케쥴 전체삭제
		 * 1. 플랙변경
		 * 2. 실제 삭제
		 * 3. 스케쥴조회 -> 결과 없음
		 */
		testSetSchedulesDeleteFlagAll();
		testDeleteFromSchedulesAll();

		testCheckoutScheduleWithNone();
		
		/**
		 * 태스크 전체삭제
		 * 1. 플랙변경
		 * 2. 실제삭제
		 * 3. 태스크조회 -> 결과 없음
		 */
		testSetTasksDeleteFlagAll();
		testDeleteFromTasksAll();
		testCheckoutTaskWithNone();

		/**
		 * 사용자 삭제
		 * 태스크 삭제되어 정상처리됨
		 */
		testUnsubscribeWithNoTasks();

		logger.info("###################################################################");
		logger.info("testFlowNormal END.");
		logger.info("###################################################################");
	}

	@Test 
	//@Category(com.ontide.oneplanner.ScenarioTest.class)
	public void testFlowToday() {
		logger.info("###################################################################");
		logger.info("testFlowToday STARTS.");
		logger.info("###################################################################");
		testRemoveToday();
		testRemoveTodayB();
		testRegisterToday();
		testRegisterTodayB();
		testUpdateToday();
		testGetToday();
		testCheckoutToday();
		testGetTodayNotFound();
		logger.info("###################################################################");
		logger.info("testFlowToday END.");
		logger.info("###################################################################");
	}

	@Test 
	//@Category(com.ontide.oneplanner.ScenarioTest.class)
	public void testFlowTodaySimple() {
		logger.info("###################################################################");
		logger.info("testFlowTodaySimple STARTS.");
		logger.info("###################################################################");
		testRemoveToday();
		testRegisterToday();
		testGetToday();
		logger.info("###################################################################");
		logger.info("testFlowToday END.");
		logger.info("###################################################################");
	}
	
	@Test
	public void testCheckDup()  {
		ResultObj result01 = jsonHandler.callCheckDup(userA);
		if (result01.getResultCode().equals(ReturnCode.ALREADY_EXISTS.get())) {
			fail(ReturnCode.ALREADY_EXISTS.get());
		} else if (result01.getResultCode().equals(ReturnCode.SUCCESS.get())) {
			assert(true);
		} else {
			fail("etc error");
		}
	}
	@Test
	public void testSubscribe()  {
		ResultObj<String> result01 = jsonHandler.callSubscribe(userA);
		
		if (!result01.getResultCode().equals(ReturnCode.SUCCESS.get())) {
			fail("Fail on subscribe");
		}
		
		//String url = "http://localhost:8081/oneplanner/subscr/request/emailauth?authId=dGVzdDBITWtpdWVYeW91a25vd25vdGhpbmc=&userId=test0HMkiueX";
		jsonHandler.callEmailAuthentication(/*this.authUrl*/result01.getItem());//);
		logger.info("testSubscribe end");
		assert(true);
	}

	@Test
	public void testSubscribeGoogle()  {
		userA.setUserType(Constant.USER_TYPE_GOOGLE.get());

		ResultObj<String> result01 = jsonHandler.callSubscribe(userA);
		
		if (!result01.getResultCode().equals(ReturnCode.SUCCESS.get())) {
			fail("Fail on subscribe");
		}
		
		logger.info("testSubscribe end");
		assert(true);
	}

	@Test
	public void testUpdateUser()  {
		userA.setSex("F");
		userA.setUserType(Constant.USER_TYPE_FACEBOOK.get());
		userA.setBirthDate("20170116");
		userA.setUserName("LittleFinger");
		ResultObj<UserInfo> result = jsonHandler.callUpdateUser(userA);
		
		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
			fail();
		}
		assert(true);
	}
	
	@Test
	public void testPasswordReset()  {
		ResultObj<String> result = jsonHandler.callPasswordReset(userA);
		
		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
			fail();
		}
		assert(true);
	}
	
	@Test
	public void testGetUser()  {
		UserInfo userInfo = new UserInfo();
		userInfo.setUserId(userA.getUserId());
		ResultObj<UserInfo> result = (ResultObj<UserInfo>)jsonHandler.callGetUser(userInfo);
		UserInfo info = (UserInfo)result.getItem();
		logger.info("result.getItem():"+info);
		logger.info("result.getItem():"+result.getItem().getClass());
		logger.info("result.getItem():"+info.getEmail());
		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
			fail();
		}
		assert(true);
	}
	
	@Test
	public void testRegisterTaskAA()  {
		ResultObj<TaskInfo> result = jsonHandler.callRegisterTask(taskAA);
		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
			fail();
		}
		assert(true);
	}

	@Test
	public void testRegisterTaskAADup()  {
		ResultObj<TaskInfo> result = jsonHandler.callRegisterTask(taskAA);
		if (!result.getResultCode().equals(ReturnCode.ALREADY_EXISTS.get())) {
			fail();
		}
		assert(true);
	}

	@Test
	public void testRegisterTaskAB()  {
		ResultObj<TaskInfo> result = jsonHandler.callRegisterTask(taskAB);
		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
			fail();
		}
		assert(true);
	}

	@Test
	public void testRegisterTaskAC()  {
		ResultObj<TaskInfo> result = jsonHandler.callRegisterTask(taskAC);
		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
			fail();
		}
		assert(true);
	}
	@Test
	public void testRegisterTaskList()  {
		List<TaskInfo> taskList = Collections.synchronizedList(new ArrayList<TaskInfo>());
		taskList.add(this.taskBA);
		taskList.add(this.taskBB);
		taskList.add(this.taskBC);
		
		ResultObj<TaskInfo> result = jsonHandler.callRegisterTaskList(taskList);
		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
			fail();
		}
		assert(true);
	}
	
	@Test
	public void testGetTaskAA()  {
		TaskInfo  taskInfo = new TaskInfo();
		taskInfo.setUserId(taskAA.getUserId());
		taskInfo.setTaskId(taskAA.getTaskId());
		ResultObj<TaskInfoOut> result = jsonHandler.callGetTask(taskInfo);
		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
			fail();
		}
		assert(true);
	}
	
	@Test
	public void testGetTaskAB()  {
		TaskInfo  taskInfo = new TaskInfo();
		taskInfo.setUserId(taskAB.getUserId());
		taskInfo.setTaskId(taskAB.getTaskId());
		ResultObj<TaskInfoOut> result = jsonHandler.callGetTask(taskInfo);
		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
			fail();
		}
		assert(true);
	}
	@Test
	public void testGetTaskAC()  {
		TaskInfo  taskInfo = new TaskInfo();
		taskInfo.setUserId(taskAC.getUserId());
		taskInfo.setTaskId(taskAC.getTaskId());
		ResultObj<TaskInfoOut> result = jsonHandler.callGetTask(taskInfo);
		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
			fail();
		}
		assert(true);
	}
	@Test
	public void testUpdateTask()  {
		taskAA.setEndDate("20170116");
		taskAA.setTaskName(taskAA.getTaskName()+"1");
		taskAA.setParticipants(taskAA.getParticipants()+"AA");
		ResultObj<TaskInfoOut> result = jsonHandler.callUpdateTask(taskAA);
		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
			fail();
		}
		assert(true);
	}
	
	@Test
	public void testCheckoutTask()  {
		ResultObj<List<TaskInfoOut>> result = jsonHandler.callCheckoutTask(userA);
		for (TaskInfoOut item : result.getItem()) {
			logger.info("testCheckoutTask out: "+item.getTaskName());
		}
		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
			fail();
		}
		assert(true);
	}

	@Test
	public void testCheckoutTaskAfterBulkInsert()  {
		ResultObj<List<TaskInfoOut>> result = jsonHandler.callCheckoutTask(userA);
		for (TaskInfoOut item : result.getItem()) {
			logger.info("testCheckoutTask out: "+item.getTaskName());
		}
		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())
				||result.getItem().size()!=6) {
			fail();
		}
		assert(true);
	}

	@Test
	public void testRegisterScheduleAA()  {
		ResultObj<ScheduleInfo> result = jsonHandler.callRegisterSchedule(scheduleAA);
		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
			fail();
		}
		assert(true);
	}

	@Test
	public void testRegisterScheduleAADup()  {
		ResultObj<ScheduleInfo> result = jsonHandler.callRegisterSchedule(scheduleAA);
		if (!result.getResultCode().equals(ReturnCode.ALREADY_EXISTS.get())) {
			fail();
		}
		assert(true);
	}
	
	@Test
	public void testRegisterScheduleHistoryAA()  {
		ScheduleHistory history = new ScheduleHistory();
		history.setUserId(scheduleAA.getUserId());
		history.setScheduleId(scheduleAA.getScheduleId());
		history.setNowDate("201703010900");
		history.setStartDate(scheduleAA.getStartDate());
		history.setEndDate(scheduleAA.getEndDate());
		history.setCompleteYn("N");
		history.setDeleteYn("N");
		ResultObj<ScheduleHistory> result = jsonHandler.callRegisterScheduleHistory(history);
		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
			fail();
		}
		assert(true);
	}
	
	@Test
	public void testRegisterScheduleHistList()  {
		List<ScheduleHistory> historyList = new ArrayList<ScheduleHistory>();
		historyList.add(scheduleHistoryCA);
		historyList.add(scheduleHistoryCB);
		historyList.add(scheduleHistoryCC);
		
		ResultObj<ScheduleHistory> result = jsonHandler.callRegisterScheduleHistList(historyList);
		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
			fail();
		}
		assert(true);
	}
	
	@Test
	public void testRegisterScheduleAB()  {
		ResultObj<ScheduleInfo> result = jsonHandler.callRegisterSchedule(scheduleAB);
		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
			fail();
		}
		assert(true);
	}
	
	@Test
	public void testRegisterScheduleBA()  {
		ResultObj<ScheduleInfo> result = jsonHandler.callRegisterSchedule(scheduleBA);
		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
			fail();
		}
		assert(true);
	}
	
	@Test
	public void testRegisterScheduleBB()  {
		ResultObj<ScheduleInfo> result = jsonHandler.callRegisterSchedule(scheduleBB);
		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
			fail();
		}
		assert(true);
	}

	@Test
	public void testRegisterScheduleList()  {
		List<ScheduleInfo> scheduleList = new ArrayList<ScheduleInfo>();
		scheduleList.add(scheduleCA);
		scheduleList.add(scheduleCB);
		scheduleList.add(scheduleCC);
		
		ResultObj<ScheduleInfo> result = jsonHandler.callRegisterScheduleList(scheduleList);
		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
			fail();
		}
		assert(true);
	}
	
	@Test
	public void testGetScheduleAA()  {
		ScheduleInfo  scheduleInfo = new ScheduleInfo();
		scheduleInfo.setUserId(scheduleAA.getUserId());
		scheduleInfo.setScheduleId(scheduleAA.getScheduleId());
		ResultObj<ScheduleInfo> result = jsonHandler.callGetSchedule(scheduleInfo);
		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
			fail();
		}
		assert(true);
	}
	
	@Test
	public void testGetScheduleHistoryAA()  {
		ScheduleHistory  scheduleHistory = new ScheduleHistory();
		scheduleHistory.setUserId(scheduleAA.getUserId());
		scheduleHistory.setScheduleId(scheduleAA.getScheduleId());
		scheduleHistory.setNowDate("201703010900");
		ResultObj<ScheduleHistory> result = jsonHandler.callGetScheduleHistory(scheduleHistory);
		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
			fail();
		}
		assert(true);
	}
	
	@Test
	public void testGetScheduleBB()  {
		ScheduleInfo  scheduleInfo = new ScheduleInfo();
		scheduleInfo.setUserId(scheduleBB.getUserId());
		scheduleInfo.setScheduleId(scheduleBB.getScheduleId());
		ResultObj<ScheduleInfo> result = jsonHandler.callGetSchedule(scheduleInfo);
		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
			fail();
		}
		assert(true);
	}
	
	@Test
	public void testUpdateScheduleAA()  {
		scheduleAA.setEndDate("20170116");
		scheduleAA.setScheduleName(scheduleAA.getScheduleName()+"1");
		scheduleAA.setParticipants(scheduleAA.getParticipants()+"AA");
		ResultObj<ScheduleInfo> result = jsonHandler.callUpdateSchedule(scheduleAA);
		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
			fail();
		}
		assert(true);
	}
	
	@Test
	public void testUpdateScheduleHistoryAA()  {
		ScheduleHistory history = new ScheduleHistory();
		history.setUserId(scheduleAA.getUserId());
		history.setScheduleId(scheduleAA.getScheduleId());
		history.setNowDate("201703010900");
		history.setStartDate(scheduleAA.getStartDate());
		history.setEndDate(scheduleAA.getEndDate());
		history.setCompleteYn("N");
		ResultObj<ScheduleHistory> result = jsonHandler.callUpdateScheduleHistory (history);
		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
			fail();
		}
		assert(true);
	}

	@Test
	public void testCheckoutSchedule()  {
		ResultObj<List<ScheduleInfo>> result = jsonHandler.callCheckoutSchedule(userA);
		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
			fail();
		}
		assert(true);
	}
	
	@Test
	public void testCheckoutScheduleAfterBulkInsert()  {
		ResultObj<List<ScheduleInfo>> result = jsonHandler.callCheckoutSchedule(userA);
		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())
			||result.getItem().size()!=7	) {
			fail();
		}
		assert(true);
	}
	
	@Test
	public void testCheckoutScheduleHistory()  {
		ResultObj<List<ScheduleHistory>> result = jsonHandler.callCheckoutScheduleHistory(userA);
		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
			fail();
		}
		assert(true);
	}
	
	@Test
	public void testCheckoutScheduleHistoryAfterBulkInsert()  {
		ResultObj<List<ScheduleHistory>> result = jsonHandler.callCheckoutScheduleHistory(userA);
		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())
			||result.getItem().size()!=3	) {
			fail();
		}
		assert(true);
	}
	
	@Test
	public void testSetSchedulesDeleteFlagAA()  {
		RequestObj req = new RequestObj();
		req.setUserId(scheduleAA.getUserId());
		List<String> scheduleList = new ArrayList<String>();
		scheduleList.add(scheduleAA.getScheduleId());
		req.setKeyList(scheduleList);
		ResultObj result = jsonHandler.callSetSchedulesDeleteFlag(req);
		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
			fail();
		}
		assert(true);
	}

	@Test
	public void testDeleteFromSchedulesAA()  {
		RequestObj req = new RequestObj();
		req.setUserId(scheduleAA.getUserId());
		List<String> scheduleList = new ArrayList<String>();
		scheduleList.add(scheduleAA.getScheduleId());
		req.setKeyList(scheduleList);
		ResultObj result = jsonHandler.callDeleteFromSchedules(req);
		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
			fail();
		}
		assert(true);
	}

	
	@Test
	public void testSetSchedulesDeleteFlagBB()  {
		RequestObj req = new RequestObj();
		req.setUserId(scheduleBB.getUserId());
		List<String> scheduleList = new ArrayList<String>();
		scheduleList.add(scheduleBB.getScheduleId());
		req.setKeyList(scheduleList);
		ResultObj result = jsonHandler.callSetSchedulesDeleteFlag(req);
		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
			fail();
		}
		assert(true);
	}

	@Test
	public void testDeleteFromSchedulesBB()  {
		RequestObj req = new RequestObj();
		req.setUserId(scheduleBB.getUserId());
		List<String> scheduleList = new ArrayList<String>();
		scheduleList.add(scheduleBB.getScheduleId());
		req.setKeyList(scheduleList);
		ResultObj result = jsonHandler.callDeleteFromSchedules(req);
		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
			fail();
		}
		assert(true);
	}
	
	/**
	 * 정상삭제로 delete_yn='Y'라야 함
	 */
	@Test
	public void testGetScheduleBBAfterSetDelete()  {
		ScheduleInfo  scheduleInfo = new ScheduleInfo();
		scheduleInfo.setUserId(scheduleBB.getUserId());
		scheduleInfo.setScheduleId(scheduleBB.getScheduleId());
		ResultObj<ScheduleInfo> result = jsonHandler.callGetSchedule(scheduleInfo);
		ScheduleInfo resultInfo = result.getItem();
		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())||!"Y".equals(resultInfo.getDeleteYn())) {
			fail();
		}
		assert(true);
	}
	
	/**
	 * 정상삭제로 not found
	 */
	@Test
	public void testGetScheduleBBAfterDeleteFrom()  {
		ScheduleInfo  scheduleInfo = new ScheduleInfo();
		scheduleInfo.setUserId(scheduleBB.getUserId());
		scheduleInfo.setScheduleId(scheduleBB.getScheduleId());
		ResultObj<ScheduleInfo> result = jsonHandler.callGetSchedule(scheduleInfo);
		ScheduleInfo resultInfo = result.getItem();
		if (!result.getResultCode().equals(ReturnCode.SCHEDULE_NOT_FOUND.get())) {
			fail();
		}
		assert(true);
	}
	//TODO:
	@Test
	public void testSetScheduleHistoriesDeleteFlagCA()  {
		RequestObj req = new RequestObj();
		req.setUserId(scheduleHistoryCA.getUserId());
		List<String> scheduleList = new ArrayList<String>();
		scheduleList.add(scheduleHistoryCA.getScheduleId());
		scheduleList.add(scheduleHistoryCB.getScheduleId());
		scheduleList.add(scheduleHistoryCC.getScheduleId());
		req.setKeyList(scheduleList);
		ResultObj result = jsonHandler.callSetScheduleHistoriesDeleteFlag(req);
		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
			fail();
		}
		assert(true);
	}

	@Test
	public void testDeleteFromScheduleHistoriesCA()  {
		RequestObj req = new RequestObj();
		req.setUserId(scheduleHistoryCA.getUserId());
		List<String> scheduleList = new ArrayList<String>();
		scheduleList.add(scheduleHistoryCA.getScheduleId());
		scheduleList.add(scheduleHistoryCB.getScheduleId());
		scheduleList.add(scheduleHistoryCC.getScheduleId());
		req.setKeyList(scheduleList);
		ResultObj result = jsonHandler.callDeleteFromScheduleHistories(req);
		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
			fail();
		}
		assert(true);
	}
	
	/**
	 * 정상삭제로 delete_yn='Y'라야 함
	 */
	@Test
	public void testGetScheduleHistoryCAAfterSetDelete()  {
		ScheduleHistory  scheduleHistory = new ScheduleHistory();
		scheduleHistory.setUserId(scheduleHistoryCA.getUserId());
		scheduleHistory.setScheduleId(scheduleHistoryCA.getScheduleId());
		scheduleHistory.setNowDate(scheduleHistoryCA.getNowDate());
		ResultObj<ScheduleHistory> result = jsonHandler.callGetScheduleHistory(scheduleHistory);
		ScheduleHistory resultHistory = result.getItem();
		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())||!"Y".equals(resultHistory.getDeleteYn())) {
			fail();
		}
		assert(true);
	}
	
	/**
	 * 정상삭제로 not found
	 */
	@Test
	public void testGetScheduleHistoryCAAfterDeleteFrom()  {
		ScheduleHistory  scheduleHistory = new ScheduleHistory();
		scheduleHistory.setUserId(scheduleHistoryCA.getUserId());
		scheduleHistory.setScheduleId(scheduleHistoryCA.getScheduleId());
		scheduleHistory.setNowDate(scheduleHistoryCA.getNowDate());
		ResultObj<ScheduleHistory> result = jsonHandler.callGetScheduleHistory(scheduleHistory);
		ScheduleHistory resultHistory = result.getItem();
		if (!result.getResultCode().equals(ReturnCode.SCHEDULE_NOT_FOUND.get())) {
			fail();
		}
		assert(true);
	}
	
	
	
	@Test
	public void testSetTasksDeleteFlagABWithSchedule()  {
		RequestObj req = new RequestObj();
		req.setUserId(taskAB.getUserId());
		List<String> taskList = new ArrayList<String>();
		taskList.add(taskAB.getTaskId());
		req.setKeyList(taskList);
		ResultObj result = jsonHandler.callSetTasksDeleteFlag(req);
		if (!result.getResultCode().equals(ReturnCode.SCHEDULE_EXISTS.get())) {
			fail();
		}
		assert(true);
	}

	@Test
	public void testDeleteFromTasksABWithSchedule()  {
		RequestObj req = new RequestObj();
		req.setUserId(taskAB.getUserId());
		List<String> taskList = new ArrayList<String>();
		taskList.add(taskAB.getTaskId());
		req.setKeyList(taskList);
		ResultObj result = jsonHandler.callDeleteFromTasks(req);
		if (!result.getResultCode().equals(ReturnCode.SCHEDULE_EXISTS.get())) {
			fail();
		}
		assert(true);
	}

	@Test
	public void testSetTasksDeleteFlagABWithNoSchedule()  {
		RequestObj req = new RequestObj();
		req.setUserId(taskAB.getUserId());
		List<String> taskList = new ArrayList<String>();
		taskList.add(taskAB.getTaskId());
		req.setKeyList(taskList);
		ResultObj result = jsonHandler.callSetTasksDeleteFlag(req);
		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
			fail();
		}
		assert(true);
	}

	@Test
	public void testDeleteFromTasksABWithNoSchedule()  {
		RequestObj req = new RequestObj();
		req.setUserId(taskAB.getUserId());
		List<String> taskList = new ArrayList<String>();
		taskList.add(taskAB.getTaskId());
		req.setKeyList(taskList);
		ResultObj result = jsonHandler.callDeleteFromTasks(req);
		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
			fail();
		}
		assert(true);
	}
	
	@Test
	public void testSetTasksDeleteFlagAC()  {
		RequestObj req = new RequestObj();
		req.setUserId(taskAC.getUserId());
		List<String> taskList = new ArrayList<String>();
		taskList.add(taskAC.getTaskId());
		req.setKeyList(taskList);
		ResultObj result = jsonHandler.callSetTasksDeleteFlag(req);
		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
			fail();
		}
		assert(true);
	}

	/**
	 * 삭제오류로 deleteYn="N"로 변경없어야 함.
	 */
	@Test
	public void testGetTaskABAfterDelete()  {
		TaskInfo  taskInfo = new TaskInfo();
		taskInfo.setUserId(taskAB.getUserId());
		taskInfo.setTaskId(taskAB.getTaskId());
		ResultObj<TaskInfoOut> result = jsonHandler.callGetTask(taskInfo);
		TaskInfo resultInfo = result.getItem();
		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())||!"N".equals(resultInfo.getDeleteYn())) {
			fail();
		}
		assert(true);
	}

	/**
	 * 정상삭제로 deleteYn = 'Y'
	 */
	@Test
	public void testGetTaskACAfterSetDelete()  {
		TaskInfo  taskInfo = new TaskInfo();
		taskInfo.setUserId(taskAC.getUserId());
		taskInfo.setTaskId(taskAC.getTaskId());
		ResultObj<TaskInfoOut> result = jsonHandler.callGetTask(taskInfo);
		TaskInfo resultInfo = result.getItem();
		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())||!"Y".equals(resultInfo.getDeleteYn())) {
			fail();
		}
		assert(true);
	}
		
	/**
	 * 정상삭제로 NOT_FOUND
	 */
	@Test
	public void testGetTaskACAfterDeleteFrom()  {
		TaskInfo  taskInfo = new TaskInfo();
		taskInfo.setUserId(taskAC.getUserId());
		taskInfo.setTaskId(taskAC.getTaskId());
		ResultObj<TaskInfoOut> result = jsonHandler.callGetTask(taskInfo);
		TaskInfo resultInfo = result.getItem();
		if (!result.getResultCode().equals(ReturnCode.TASK_NOT_FOUND.get())) {
			fail();
		}
		assert(true);
	}
		
	@Test
	public void testDeleteFromTasksAC()  {
		RequestObj req = new RequestObj();
		req.setUserId(taskAC.getUserId());
		List<String> taskList = new ArrayList<String>();
		taskList.add(taskAC.getTaskId());
		req.setKeyList(taskList);
		ResultObj result = jsonHandler.callDeleteFromTasks(req);
		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
			fail();
		}
		assert(true);
	}


	/**
	 * 모든 스케쥴 삭제 delete_yn = 'Y'
	 */
	@Test
	public void testSetSchedulesDeleteFlagAll()  {
		RequestObj req = new RequestObj();
		req.setUserId(scheduleBB.getUserId());
		List<String> scheduleList = new ArrayList<String>();
		scheduleList.add(scheduleAA.getScheduleId());
		scheduleList.add(scheduleAB.getScheduleId());
		scheduleList.add(scheduleBA.getScheduleId());
		scheduleList.add(scheduleBB.getScheduleId());
		scheduleList.add(scheduleCA.getScheduleId());
		scheduleList.add(scheduleCB.getScheduleId());
		scheduleList.add(scheduleCC.getScheduleId());
		req.setKeyList(scheduleList);
		ResultObj result = jsonHandler.callSetSchedulesDeleteFlag(req);
		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
			fail();
		}
		assert(true);
	}

	@Test
	public void testDeleteFromSchedulesAll()  {
		RequestObj req = new RequestObj();
		req.setUserId(scheduleBB.getUserId());
		List<String> scheduleList = new ArrayList<String>();
		scheduleList.add(scheduleAA.getScheduleId());
		scheduleList.add(scheduleAB.getScheduleId());
		scheduleList.add(scheduleBA.getScheduleId());
		scheduleList.add(scheduleBB.getScheduleId());
		scheduleList.add(scheduleCA.getScheduleId());
		scheduleList.add(scheduleCB.getScheduleId());
		scheduleList.add(scheduleCC.getScheduleId());
		req.setKeyList(scheduleList);
		ResultObj result = jsonHandler.callDeleteFromSchedules(req);
		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
			fail();
		}
		assert(true);
	}
	
	@Test
	public void testSetTasksDeleteFlagAll()  {
		RequestObj req = new RequestObj();
		req.setUserId(userA.getUserId());
		List<String> taskList = new ArrayList<String>();
		taskList.add(taskAA.getTaskId());
		taskList.add(taskAB.getTaskId());
		taskList.add(taskBA.getTaskId());
		taskList.add(taskBB.getTaskId());
		taskList.add(taskBC.getTaskId());
		req.setKeyList(taskList);
		ResultObj result = jsonHandler.callSetTasksDeleteFlag(req);
		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
			fail();
		}
		assert(true);
	}
	
	@Test
	public void testDeleteFromTasksAll()  {
		RequestObj req = new RequestObj();
		req.setUserId(userA.getUserId());
		List<String> taskList = new ArrayList<String>();
		taskList.add(taskAA.getTaskId());
		taskList.add(taskAB.getTaskId());
		taskList.add(taskBA.getTaskId());
		taskList.add(taskBB.getTaskId());
		taskList.add(taskBC.getTaskId());
		req.setKeyList(taskList);
		ResultObj result = jsonHandler.callDeleteFromTasks(req);
		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
			fail();
		}
		assert(true);
	}
	
	@Test
	public void testCheckoutScheduleWithNone()  {
		ResultObj<List<ScheduleInfo>> result = jsonHandler.callCheckoutSchedule(userA);
		if (!result.getResultCode().equals(ReturnCode.SCHEDULE_NOT_FOUND.get())) {
			fail();
		}
		assert(true);
	}
	
	
	@Test
	public void testCheckoutTaskWithNone()  {
		ResultObj<List<TaskInfoOut>> result = jsonHandler.callCheckoutTask(userA);
		if (!result.getResultCode().equals(ReturnCode.TASK_NOT_FOUND.get())) {
			fail();
		}
		
//		for (TaskInfoOut item : result.getItem()) {
//			logger.info("testCheckoutTask out: "+item.getTaskName());
//		}
		logger.info("testCheckoutTaskWithNone: result.getResultCode"+result.getResultCode());
		logger.info("testCheckoutTaskWithNone: ReturnCode.TASK_NOT_FOUND"+ReturnCode.TASK_NOT_FOUND.get());
		assert(true);
	}
	
	@Test
	public void testUnsubscribeWithTasks() {
		ResultObj result = jsonHandler.callUnsubscribe(userA);
		if (!result.getResultCode().equals(ReturnCode.TASK_EXISTS.get())) {
			fail();
		}
		assert(true);
	}

	@Test
	public void testUnsubscribeWithNoTasks() {
		ResultObj result = jsonHandler.callUnsubscribe(userA);
		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
			fail();
		}
		assert(true);
	}
	
	@Test
	public void testRegisterToday()  {
		String filePath = "C:\\Users\\gencode\\Dropbox\\Photos\\test2.jpg";
		ResultObj<String> result = jsonHandler.callRegisterToday(todayA,filePath);
		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
			fail();
		}
		assert(true);
	}
	
	@Test
	public void testRegisterTodayB()  {
		String filePath = "C:\\Users\\gencode\\Dropbox\\Photos\\test2.jpg";
		ResultObj<String> result = jsonHandler.callRegisterToday(todayB,filePath);
		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
			fail();
		}
		assert(true);
	}
	
	@Test
	public void testUpdateToday()  {
		todayA.setContent(todayA.getContent()+"==>Updated.");
		ResultObj<String> result = jsonHandler.callUpdateToday(todayA);
		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
			fail();
		}
		assert(true);
	}
	
	@Test
	public void testGetToday()  {
		TodayInfo todayInfo = new TodayInfo();
		todayInfo.setToday(todayA.getToday());
		todayInfo.setContSeq(todayA.getContSeq());
		ResultObj<TodayInfo> result = jsonHandler.callGetToday(todayInfo);
		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
			fail();
		}
		assert(true);
	}

	@Test
	public void testGetTodayNotFound()  {
		TodayInfo todayInfo = new TodayInfo();
		todayInfo.setToday(todayA.getToday());
		todayInfo.setContSeq(todayA.getContSeq());
		ResultObj<TodayInfo> result = jsonHandler.callGetToday(todayInfo);
		if (!result.getResultCode().equals(ReturnCode.TODAY_NOT_FOUND.get())) {
			fail();
		}
		assert(true);
	}

	@Test
	public void testCheckoutToday()  {
		TodayInfo todayInfo = new TodayInfo();
		todayInfo.setToday(todayA.getToday());
		ResultObj<List<TodayInfo>> result = jsonHandler.callCheckoutToday(todayInfo);
		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
			fail();
		}
		assert(true);
	}
	
	@Test
	public void testRemoveToday()  {
		TodayInfo todayInfo = new TodayInfo();
		todayInfo.setToday(todayA.getToday());
		todayInfo.setContSeq(todayA.getContSeq());
		ResultObj<TodayInfo> result = jsonHandler.callRemoveToday(todayInfo);
//		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
//			fail();
//		}
		assert(true);
	}
	
	@Test
	public void testRemoveTodayB()  {
		TodayInfo todayInfo = new TodayInfo();
		todayInfo.setToday(todayB.getToday());
		todayInfo.setContSeq(todayB.getContSeq());
		ResultObj<TodayInfo> result = jsonHandler.callRemoveToday(todayInfo);
		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
			fail();
		}
		assert(true);
	}

}
