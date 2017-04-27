//package com.ontide.oneplanner.client;
//
//import static org.junit.Assert.*;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.experimental.categories.Category;
//import org.junit.rules.ExpectedException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.ontide.oneplanner.etc.Constant;
//import com.ontide.oneplanner.etc.ReturnCode;
//import com.ontide.oneplanner.io.RequestObj;
//import com.ontide.oneplanner.io.ResultObj;
//import com.ontide.oneplanner.obj.ScheduleHistory;
//import com.ontide.oneplanner.obj.ScheduleInfo;
//import com.ontide.oneplanner.obj.TaskInfo;
//import com.ontide.oneplanner.obj.TaskInfoOut;
//import com.ontide.oneplanner.obj.TodayInfo;
//import com.ontide.oneplanner.obj.UserInfo;
//
//public class StressTest {
//	//private static final Logger logger = LoggerFactory.getLogger(UnitTest.class);
//	LogWrapper logger = new LogWrapper(StressTest.class);
//	
//	final String MODE_NORMAL = "-";
//	final String MODE_DELFLAG = "Y";
//	final String MODE_DELETE = "D"; 
//
//	UserInfo userA;
//	TaskInfo taskAA;
//	TaskInfo taskAB;
//	TaskInfo taskAC;
//	ScheduleInfo scheduleAA;
//	ScheduleInfo scheduleAB;
//	ScheduleInfo scheduleBA;
//	ScheduleInfo scheduleBB;
//
//	String url;
//	String authUrl;
//	JsonHandler jsonHandler;
//	
//	int finalScheduleId = 0;
//	int finalTaskId = 0;
//	int taskCapacity =  0;
//	int MAX_SCHEDULE_ID = 100;
//	int MAX_TASK_ID = 50;
//	Map<String, String> mapTasksDeleted = new HashMap<String, String>();
//	Map<String, String> mapTasksDelFlag = new HashMap<String, String>();
//	Map<String, String> mapTasksNormal = new HashMap<String, String>();
//	
//
//	Map<String, String> mapSchedulesDeleted = new HashMap<String, String>();
//	Map<String, String> mapSchedulesDelFlag = new HashMap<String, String>();
//	Map<String, String> mapSchedulesNormal = new HashMap<String, String>();
//	
//	/**
//	 * task가 관리하는 schedule의 처리현황
//	 * 현재 목록과 제한 갯수
//	 * 등록된 태스크와  해당 스케줄 삭제모드
//	 * @author gencode
//	 *
//	 */
//	class ScheduleProgress {
//
//		int scheduleCapacity = 0;
//		Map<String, String> mapSchedules = new HashMap<String, String>();
//		int scheduleDeletedCount = 0;
//		int scheduleDelFlagCount = 0;
//		
//		public ScheduleProgress() {
//			scheduleCapacity = TestUtil.getRandomNumber(MAX_SCHEDULE_ID);
//		}
//		
//		public boolean isEmpty() {
//			return ((mapSchedules.size() - scheduleDeletedCount - scheduleDelFlagCount)==0);
//		}
//		
//		public boolean canAddSchedule() {
//			return (scheduleCapacity - mapSchedules.size() > 0);
//		}
//		
//		public void addSchedule(String scheduleId) {
//			mapSchedules.put(scheduleId, MODE_NORMAL);
//		}
//		
//		public void setDeleteSchedule(String scheduleId) {
//			if (MODE_NORMAL.equals(mapSchedules.get(scheduleId))) {
//				scheduleDelFlagCount ++;
//				mapSchedulesDeleted.put(scheduleId, MODE_DELFLAG);
//			}
//			mapSchedules.put(scheduleId, MODE_DELFLAG);
//		}
//		
//		public void deleteFromSchedule(String scheduleId) {
//			if (MODE_DELFLAG.equals(mapSchedules.get(scheduleId))) {
//				scheduleDelFlagCount --;
//				mapSchedulesDelFlag.remove(scheduleId);
//			}
//			scheduleDeletedCount++;
//			mapSchedulesDeleted.put(scheduleId, MODE_DELETE);
//			
//			mapSchedules.put(scheduleId, MODE_DELETE);
//		}
//		
//		List<String> getScheduleIdListForDelete() {
//			List<String> list = new ArrayList<String>();
//			
//			for (Map.Entry<String, String> entry : mapSchedules.entrySet()) {
//				if (!MODE_DELETE.equals(entry.getValue())) {
//					list.add(entry.getKey());
//				}
//			}
//			return list;
//		}
//		
//		List<String> getScheduleIdListForDelFlag() {
//			List<String> list = new ArrayList<String>();
//			
//			for (Map.Entry<String, String> entry : mapSchedules.entrySet()) {
//				if (!MODE_DELETE.equals(entry.getValue())&&!MODE_DELFLAG.equals(entry.getValue())) {
//					list.add(entry.getKey());
//				}
//			}
//			return list;
//		}
//	}
//	
//	//class 
//	
//	Map<String, ScheduleProgress> mapTasks = new HashMap<String, ScheduleProgress>();
//	
//	int stepToGo = 0; 
//	
//	void init() throws Exception{ 
//		TestObj testObj = new TestObj();
//		this.userA = testObj.getUserA();
//		this.url = testObj.URL;
//		mapTasks.clear();
//		taskCapacity = TestUtil.getRandomNumber(MAX_TASK_ID);
//		jsonHandler = new JsonHandler();
//	}
//
//	@Before
//	public void setUp() throws Exception {
//		init();
//	}
//
//	@After
//	public void tearDown() throws Exception {
//	}
//	
//	String getNewTaskId() {
//		return String.valueOf(finalTaskId+1);
//	}
//	
//	/**
//	 * 등록된 task id중 아무거나.
//	 * @return
//	 */
//	String getCurTaskId() {
//		return String.valueOf(TestUtil.getRandomNumber(finalTaskId));
//	}
//	
//	String getNewScheduleId() {
//		return String.valueOf(finalScheduleId+1);
//	}
//
//	/**
//	 * 등록된 schedule id중 아무거나.
//	 * @return
//	 */
//	String getCurScheduleId() {
//		return String.valueOf(TestUtil.getRandomNumber(finalScheduleId));
//	}
//
//	boolean isTaskDeleted(String taskId) {
//		return mapTasksDeleted.containsKey(taskId);
//	}
//	
//	boolean isTaskDelFlagged(String taskId) {
//		return mapTasksDelFlag.containsKey(taskId);
//	}
//	
//	boolean isScheduleDeleted(String taskId) {
//		return mapSchedulesDeleted.containsKey(taskId);
//	}
//	
//	boolean isScheduleDelFlagged(String taskId) {
//		return mapSchedulesDelFlag.containsKey(taskId);
//	}
//	
//	String getTaskIdForUpdate() {
//		String taskId = null;
//		if (taskCapacity - (mapTasksDeleted.size()+mapTasksDelFlag.size()) <= 0) return null;
//		while (true) {
//			taskId = getCurTaskId();
//			if (isTaskDeleted(taskId))  continue;
//			if (isTaskDelFlagged(taskId))  continue;
//			break;
//		}
//		return taskId;
//	}
//	
//	String getTaskIdForDelete() {
//		String taskId = null;
//		if (taskCapacity - mapTasksDeleted.size() <= 0) return null;
//		while (true) {
//			taskId = getCurTaskId();
//			if (isTaskDeleted(taskId))  continue;
//			break;
//		}
//		return taskId;
//	}
//	
//	String getScheduleIdForDelete() {
//		String scheduleId = null;
//		for (int i = 0; i < this.taskCapacity; i++) {
//			scheduleId = getCurScheduleId();
//			if (isScheduleDeleted(scheduleId))  continue;
//			break;
//		}
//		return scheduleId;
//	}
//	
//	String getScheduleIdForUpdate() {
//		String scheduleId = null;
//		for (int i = 0; i < this.taskCapacity; i++) {
//			scheduleId = getCurScheduleId();
//			if (isScheduleDeleted(scheduleId))  continue;
//			if (isScheduleDelFlagged(scheduleId))  continue;
//			break;
//		}
//		return scheduleId;
//	}
//	
//	List<String> getTaskIdListForDelete(int boundary) {
//		List<String> list = new ArrayList<String>();
//		for (int i = boundary+1; i <=finalTaskId; i++) {
//			String taskId = String.valueOf(i);
//			if (isTaskDeleted(taskId))  continue;
//			list.add(taskId);
//
//		}
//		return list;
//	}
//	
//	List<String> getTaskIdListForDelFlag(int boundary) {
//		List<String> list = new ArrayList<String>();
//		for (int i = 0; i <= boundary; i++) {
//			String taskId = String.valueOf(i);
//			if (isTaskDeleted(taskId))  continue;
//			if (isTaskDelFlagged(taskId))  continue;
//			list.add(taskId);
//		}
//		return list;
//	}
//	
//	List<String> getScheduleIdListAllForDelete(int boundary) {
//		List<String> list = new ArrayList<String>();
//		for (int i = boundary+1; i < finalScheduleId; i++) {
//			String scheduleId = String.valueOf(i);
//			if (isScheduleDeleted(scheduleId))  continue;
//			list.add(scheduleId);
//		}
//		return list;
//	}
//	
//	List<String> getScheduleIdListAllForDelFlag(int boundary) {
//		List<String> list = new ArrayList<String>();
//		for (int i = 0; i <= boundary; i++) {
//			String scheduleId = String.valueOf(i);
//			if (isScheduleDeleted(scheduleId))  continue;
//			if (isScheduleDelFlagged(scheduleId))  continue;
//			list.add(scheduleId);
//		}
//		return list;
//	}
//
////	void addTask() {
////		String taskId = getString.valueOf(++finalTaskId);
////		TaskProgress progress = new TaskProgress();
////		mapTasks.put(taskId,progress);		
////	}
//	
//	void deleteTask(String taskId) {
//		mapTasksDeleted.put(taskId,taskId);
//		mapTasksDelFlag.remove(taskId);
//	}
//	
//	void delFlagTask(String taskId) {
//		mapTasksDelFlag.put(taskId,taskId);		
//	}
//	
//	void deleteTask(List<String> taskIdList) {
//		for (String taskId : taskIdList) {
//			deleteTask(taskId);	
//		}
//	}
//	void delFlagTask(List<String> taskIdList) {
//		for (String taskId : taskIdList) {
//			delFlagTask(taskId);	
//		}
//	}
//	
//	void addSchedule(String taskId) {
//		String scheduleId = String.valueOf(++finalScheduleId);
//		ScheduleProgress progress = new ScheduleProgress();
//		//progress
//		mapTasks.put(taskId,progress);		
//	}
//	
//	void deleteSchedule() {
//		
//	}
//	
//	void delFlagSchedule() {
//		
//	}
//	
//	@Test 
//	@Category(com.ontide.oneplanner.client.ScenarioTestDev.class)
//	public void testFlowNormal() {
//		logger.info("###################################################################");
//		logger.info("testFlowNormal STARTS.");
//		logger.info("###################################################################");
////		userA.setUserId("testukFiImur");
////		taskAA.setUserId("testOVuI6T5f");
////		testGetUser();
//		//
//		
//		
//		/**
//		 * 고객등록: 
//		 * 1. 최초 user id 중복발생 확인
//		 * 2. 고객등록및 인증
//		 * 3. 고객정보변경
//		 * 4. 비번 변경 및 메일확인
//		 * 5. 고객조회
//		 */
//		
//		testCheckDup();
//		testSubscribe();
//		testUpdateUser();
//		testPasswordReset();
//		testGetUser();
//		
//		/**
//		 * 태스크 등록
//		 * 
//		 * userA -> TaskAA, TaskAB, TaskAC
//		 * 
//		 * TaskAA -> ScheduleAA,AB
//		 * TaskAB -> ScheduleBA,BB
//		 * TaskAC -> None
//		 * 
//		 * 순으로 등록한다.
//		 */
//
//		
//		/**
//		 * 태스크 조회
//		 * 태스크 전체조회
//		 * 스케쥴AA의 history 등록
//		 * 등록후 변경
//		 * history 조회
//		 * 스케쥴 전체조회
//		 */
//		
//		
//		/**
//		 * 스케쥴 없는 태스크 삭제
//		 * 
//		 */
//
//		
//		/** 
//		 * 하위스케쥴이 있는 태스크를 삭제시도
//		 * 결과는 스케쥴있음오류
//		 */
//		
//		/**
//		 * 스케쥴 삭제 
//		 * 결과는 정상
//		 */
//	
//		/**
//		 * 사용자 삭제
//		 * 태스크 있음오류발생
//		 */
//		testGetUser();
//		testUnsubscribeWithTasks();
//
//		/**
//		 * 스케쥴 전체삭제
//		 * 1. 플랙변경
//		 * 2. 실제 삭제
//		 * 3. 스케쥴조회 -> 결과 없음
//		 */
//		testSetSchedulesDeleteFlagAll();
//		testDeleteFromSchedulesAll();
//
//		testCheckoutScheduleWithNone();
//		
//		/**
//		 * 태스크 전체삭제
//		 * 1. 플랙변경
//		 * 2. 실제삭제
//		 * 3. 태스크조회 -> 결과 없음
//		 */
//		testSetTasksDeleteFlagAll();
//		testDeleteFromTasksAll();
//		testCheckoutTaskWithNone();
//
//		/**
//		 * 사용자 삭제
//		 * 태스크 삭제되어 정상처리됨
//		 */
//		testUnsubscribeWithNoTasks();
//
//		logger.info("###################################################################");
//		logger.info("testFlowNormal END.");
//		logger.info("###################################################################");
//	}
//
//	@Test
//	public void testCheckDup()  {
//		ResultObj result01 = jsonHandler.callCheckDup(userA);
//		if (result01.getResultCode().equals(ReturnCode.ALREADY_EXISTS.get())) {
//			fail(ReturnCode.ALREADY_EXISTS.get());
//		} else if (result01.getResultCode().equals(ReturnCode.SUCCESS.get())) {
//			assert(true);
//		} else {
//			fail("etc error");
//		}
//	}
//	@Test
//	public void testSubscribe()  {
//		ResultObj<String> result01 = jsonHandler.callSubscribe(userA);
//		
//		if (!result01.getResultCode().equals(ReturnCode.SUCCESS.get())) {
//			fail("Fail on subscribe");
//		}		
//		//String url = "http://localhost:8081/oneplanner/subscr/request/emailauth?authId=dGVzdDBITWtpdWVYeW91a25vd25vdGhpbmc=&userId=test0HMkiueX";
//		jsonHandler.callEmailAuthentication(/*this.authUrl*/result01.getItem());//);
//		logger.info("testSubscribe end");
//		assert(true);
//	}
//	
//	@Test
//	public void testUpdateUser()  {
//		userA.setSex("F");
//		userA.setUserType(Constant.USER_TYPE_FACEBOOK.get());
//		userA.setBirthDate("20170116");
//		userA.setUserName("LittleFinger");
//		ResultObj<UserInfo> result = jsonHandler.callUpdateUser(userA);
//		
//		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
//			fail();
//		}
//		assert(true);
//	}
//	
//	@Test
//	public void testPasswordReset()  {
//		ResultObj<String> result = jsonHandler.callPasswordReset(userA);
//		
//		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
//			fail();
//		}
//		assert(true);
//	}
//	
//	@Test
//	public void testGetUser()  {
//		UserInfo userInfo = new UserInfo();
//		userInfo.setUserId(userA.getUserId());
//		ResultObj<UserInfo> result = (ResultObj<UserInfo>)jsonHandler.callGetUser(userInfo);
//		UserInfo info = (UserInfo)result.getItem();
//		logger.info("result.getItem():"+info);
//		logger.info("result.getItem():"+result.getItem().getClass());
//		logger.info("result.getItem():"+info.getEmail());
//		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
//			fail();
//		}
//		assert(true);
//	}
//	
//	
//	
//	@Test
//	public void testRegisterTask()  {
//		
//		ResultObj<TaskInfo> result = jsonHandler.callRegisterTask(taskAA);
//		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
//			fail();
//		}
//		assert(true);
//	}
//
//
//	@Test
//	public void testGetTaskAA()  {
//		TaskInfo  taskInfo = new TaskInfo();
//		taskInfo.setUserId(taskAA.getUserId());
//		taskInfo.setTaskId(taskAA.getTaskId());
//		ResultObj<TaskInfoOut> result = jsonHandler.callGetTask(taskInfo);
//		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
//			fail();
//		}
//		assert(true);
//	}
//	
//	@Test
//	public void testGetTaskAB()  {
//		TaskInfo  taskInfo = new TaskInfo();
//		taskInfo.setUserId(taskAB.getUserId());
//		taskInfo.setTaskId(taskAB.getTaskId());
//		ResultObj<TaskInfoOut> result = jsonHandler.callGetTask(taskInfo);
//		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
//			fail();
//		}
//		assert(true);
//	}
//	@Test
//	public void testGetTaskAC()  {
//		TaskInfo  taskInfo = new TaskInfo();
//		taskInfo.setUserId(taskAC.getUserId());
//		taskInfo.setTaskId(taskAC.getTaskId());
//		ResultObj<TaskInfoOut> result = jsonHandler.callGetTask(taskInfo);
//		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
//			fail();
//		}
//		assert(true);
//	}
//	@Test
//	public void testUpdateTask()  {
//		taskAA.setEndDate("20170116");
//		taskAA.setTaskName(taskAA.getTaskName()+"1");
//		taskAA.setParticipants(taskAA.getParticipants()+"AA");
//		ResultObj<TaskInfoOut> result = jsonHandler.callUpdateTask(taskAA);
//		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
//			fail();
//		}
//		assert(true);
//	}
//	
//	@Test
//	public void testCheckoutTask()  {
//		ResultObj<List<TaskInfoOut>> result = jsonHandler.callCheckoutTask(userA);
//		for (TaskInfoOut item : result.getItem()) {
//			logger.info("testCheckoutTask out: "+item.getTaskName());
//		}
//		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
//			fail();
//		}
//		assert(true);
//	}
//	
//
//	@Test
//	public void testRegisterScheduleAA()  {
//		ResultObj<ScheduleInfo> result = jsonHandler.callRegisterSchedule(scheduleAA);
//		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
//			fail();
//		}
//		assert(true);
//	}
//
//	@Test
//	public void testRegisterScheduleHistoryAA()  {
//		ScheduleHistory history = new ScheduleHistory();
//		history.setUserId(scheduleAA.getUserId());
//		history.setScheduleId(scheduleAA.getScheduleId());
//		history.setNowDate("201703010900");
//		history.setStartDate(scheduleAA.getStartDate());
//		history.setEndDate(scheduleAA.getEndDate());
//		history.setCompleteYn("N");
//		ResultObj<ScheduleHistory> result = jsonHandler.callRegisterScheduleHistory(history);
//		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
//			fail();
//		}
//		assert(true);
//	}
//	
//	@Test
//	public void testRegisterScheduleAB()  {
//		ResultObj<ScheduleInfo> result = jsonHandler.callRegisterSchedule(scheduleAB);
//		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
//			fail();
//		}
//		assert(true);
//	}
//	
//	@Test
//	public void testRegisterScheduleBA()  {
//		ResultObj<ScheduleInfo> result = jsonHandler.callRegisterSchedule(scheduleBA);
//		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
//			fail();
//		}
//		assert(true);
//	}
//	
//	@Test
//	public void testRegisterScheduleBB()  {
//		ResultObj<ScheduleInfo> result = jsonHandler.callRegisterSchedule(scheduleBB);
//		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
//			fail();
//		}
//		assert(true);
//	}
//
//	@Test
//	public void testGetScheduleAA()  {
//		ScheduleInfo  scheduleInfo = new ScheduleInfo();
//		scheduleInfo.setUserId(scheduleAA.getUserId());
//		scheduleInfo.setScheduleId(scheduleAA.getScheduleId());
//		ResultObj<ScheduleInfo> result = jsonHandler.callGetSchedule(scheduleInfo);
//		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
//			fail();
//		}
//		assert(true);
//	}
//	
//	@Test
//	public void testGetScheduleHistoryAA()  {
//		ScheduleHistory  scheduleHistory = new ScheduleHistory();
//		scheduleHistory.setUserId(scheduleAA.getUserId());
//		scheduleHistory.setScheduleId(scheduleAA.getScheduleId());
//		scheduleHistory.setNowDate("201703010900");
//		ResultObj<ScheduleHistory> result = jsonHandler.callGetScheduleHistory(scheduleHistory);
//		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
//			fail();
//		}
//		assert(true);
//	}
//	
//	@Test
//	public void testGetScheduleBB()  {
//		ScheduleInfo  scheduleInfo = new ScheduleInfo();
//		scheduleInfo.setUserId(scheduleBB.getUserId());
//		scheduleInfo.setScheduleId(scheduleBB.getScheduleId());
//		ResultObj<ScheduleInfo> result = jsonHandler.callGetSchedule(scheduleInfo);
//		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
//			fail();
//		}
//		assert(true);
//	}
//	
//	@Test
//	public void testUpdateScheduleAA()  {
//		scheduleAA.setEndDate("20170116");
//		scheduleAA.setScheduleName(scheduleAA.getScheduleName()+"1");
//		scheduleAA.setParticipants(scheduleAA.getParticipants()+"AA");
//		ResultObj<ScheduleInfo> result = jsonHandler.callUpdateSchedule(scheduleAA);
//		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
//			fail();
//		}
//		assert(true);
//	}
//	
//	@Test
//	public void testUpdateScheduleHistoryAA()  {
//		ScheduleHistory history = new ScheduleHistory();
//		history.setUserId(scheduleAA.getUserId());
//		history.setScheduleId(scheduleAA.getScheduleId());
//		history.setNowDate("201703010900");
//		history.setStartDate(scheduleAA.getStartDate());
//		history.setEndDate(scheduleAA.getEndDate());
//		history.setCompleteYn("N");
//		ResultObj<ScheduleHistory> result = jsonHandler.callUpdateScheduleHistory (history);
//		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
//			fail();
//		}
//		assert(true);
//	}
//
//	@Test
//	public void testCheckoutSchedule()  {
//		ResultObj<List<ScheduleInfo>> result = jsonHandler.callCheckoutSchedule(userA);
//		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
//			fail();
//		}
//		assert(true);
//	}
//	
//	@Test
//	public void testSetSchedulesDeleteFlagAA()  {
//		RequestObj req = new RequestObj();
//		req.setUserId(scheduleAA.getUserId());
//		List<String> scheduleList = new ArrayList<String>();
//		scheduleList.add(scheduleAA.getScheduleId());
//		req.setKeyList(scheduleList);
//		ResultObj result = jsonHandler.callSetSchedulesDeleteFlag(req);
//		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
//			fail();
//		}
//		assert(true);
//	}
//
//	@Test
//	public void testDeleteFromSchedulesAA()  {
//		RequestObj req = new RequestObj();
//		req.setUserId(scheduleAA.getUserId());
//		List<String> scheduleList = new ArrayList<String>();
//		scheduleList.add(scheduleAA.getScheduleId());
//		req.setKeyList(scheduleList);
//		ResultObj result = jsonHandler.callDeleteFromSchedules(req);
//		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
//			fail();
//		}
//		assert(true);
//	}
//
//	
//	@Test
//	public void testSetSchedulesDeleteFlagBB()  {
//		RequestObj req = new RequestObj();
//		req.setUserId(scheduleBB.getUserId());
//		List<String> scheduleList = new ArrayList<String>();
//		scheduleList.add(scheduleBB.getScheduleId());
//		req.setKeyList(scheduleList);
//		ResultObj result = jsonHandler.callSetSchedulesDeleteFlag(req);
//		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
//			fail();
//		}
//		assert(true);
//	}
//
//	@Test
//	public void testDeleteFromSchedulesBB()  {
//		RequestObj req = new RequestObj();
//		req.setUserId(scheduleBB.getUserId());
//		List<String> scheduleList = new ArrayList<String>();
//		scheduleList.add(scheduleBB.getScheduleId());
//		req.setKeyList(scheduleList);
//		ResultObj result = jsonHandler.callDeleteFromSchedules(req);
//		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
//			fail();
//		}
//		assert(true);
//	}
//	
//	/**
//	 * 정상삭제로 delete_yn='Y'라야 함
//	 */
//	@Test
//	public void testGetScheduleBBAfterSetDelete()  {
//		ScheduleInfo  scheduleInfo = new ScheduleInfo();
//		scheduleInfo.setUserId(scheduleBB.getUserId());
//		scheduleInfo.setScheduleId(scheduleBB.getScheduleId());
//		ResultObj<ScheduleInfo> result = jsonHandler.callGetSchedule(scheduleInfo);
//		ScheduleInfo resultInfo = result.getItem();
//		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())||!"Y".equals(resultInfo.getDeleteYn())) {
//			fail();
//		}
//		assert(true);
//	}
//	
//	/**
//	 * 정상삭제로 delete_yn='Y'라야 함
//	 */
//	@Test
//	public void testGetScheduleBBAfterDeleteFrom()  {
//		ScheduleInfo  scheduleInfo = new ScheduleInfo();
//		scheduleInfo.setUserId(scheduleBB.getUserId());
//		scheduleInfo.setScheduleId(scheduleBB.getScheduleId());
//		ResultObj<ScheduleInfo> result = jsonHandler.callGetSchedule(scheduleInfo);
//		ScheduleInfo resultInfo = result.getItem();
//		if (!result.getResultCode().equals(ReturnCode.SCHEDULE_NOT_FOUND.get())) {
//			fail();
//		}
//		assert(true);
//	}
//	
//	
//	@Test
//	public void testSetTasksDeleteFlagABWithSchedule()  {
//		RequestObj req = new RequestObj();
//		req.setUserId(taskAB.getUserId());
//		List<String> taskList = new ArrayList<String>();
//		taskList.add(taskAB.getTaskId());
//		req.setKeyList(taskList);
//		ResultObj result = jsonHandler.callSetTasksDeleteFlag(req);
//		if (!result.getResultCode().equals(ReturnCode.SCHEDULE_EXISTS.get())) {
//			fail();
//		}
//		assert(true);
//	}
//
//	@Test
//	public void testDeleteFromTasksABWithSchedule()  {
//		RequestObj req = new RequestObj();
//		req.setUserId(taskAB.getUserId());
//		List<String> taskList = new ArrayList<String>();
//		taskList.add(taskAB.getTaskId());
//		req.setKeyList(taskList);
//		ResultObj result = jsonHandler.callDeleteFromTasks(req);
//		if (!result.getResultCode().equals(ReturnCode.SCHEDULE_EXISTS.get())) {
//			fail();
//		}
//		assert(true);
//	}
//
//	@Test
//	public void testSetTasksDeleteFlagABWithNoSchedule()  {
//		RequestObj req = new RequestObj();
//		req.setUserId(taskAB.getUserId());
//		List<String> taskList = new ArrayList<String>();
//		taskList.add(taskAB.getTaskId());
//		req.setKeyList(taskList);
//		ResultObj result = jsonHandler.callSetTasksDeleteFlag(req);
//		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
//			fail();
//		}
//		assert(true);
//	}
//
//	@Test
//	public void testDeleteFromTasksABWithNoSchedule()  {
//		RequestObj req = new RequestObj();
//		req.setUserId(taskAB.getUserId());
//		List<String> taskList = new ArrayList<String>();
//		taskList.add(taskAB.getTaskId());
//		req.setKeyList(taskList);
//		ResultObj result = jsonHandler.callDeleteFromTasks(req);
//		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
//			fail();
//		}
//		assert(true);
//	}
//	
//	@Test
//	public void testSetTasksDeleteFlagAC()  {
//		RequestObj req = new RequestObj();
//		req.setUserId(taskAC.getUserId());
//		List<String> taskList = new ArrayList<String>();
//		taskList.add(taskAC.getTaskId());
//		req.setKeyList(taskList);
//		ResultObj result = jsonHandler.callSetTasksDeleteFlag(req);
//		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
//			fail();
//		}
//		assert(true);
//	}
//
//	/**
//	 * 삭제오류로 deleteYn="N"로 변경없어야 함.
//	 */
//	@Test
//	public void testGetTaskABAfterDelete()  {
//		TaskInfo  taskInfo = new TaskInfo();
//		taskInfo.setUserId(taskAB.getUserId());
//		taskInfo.setTaskId(taskAB.getTaskId());
//		ResultObj<TaskInfoOut> result = jsonHandler.callGetTask(taskInfo);
//		TaskInfo resultInfo = result.getItem();
//		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())||!"N".equals(resultInfo.getDeleteYn())) {
//			fail();
//		}
//		assert(true);
//	}
//
//	/**
//	 * 정상삭제로 deleteYn = 'Y'
//	 */
//	@Test
//	public void testGetTaskACAfterSetDelete()  {
//		TaskInfo  taskInfo = new TaskInfo();
//		taskInfo.setUserId(taskAC.getUserId());
//		taskInfo.setTaskId(taskAC.getTaskId());
//		ResultObj<TaskInfoOut> result = jsonHandler.callGetTask(taskInfo);
//		TaskInfo resultInfo = result.getItem();
//		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())||!"Y".equals(resultInfo.getDeleteYn())) {
//			fail();
//		}
//		assert(true);
//	}
//		
//	/**
//	 * 정상삭제로 NOT_FOUND
//	 */
//	@Test
//	public void testGetTaskACAfterDeleteFrom()  {
//		TaskInfo  taskInfo = new TaskInfo();
//		taskInfo.setUserId(taskAC.getUserId());
//		taskInfo.setTaskId(taskAC.getTaskId());
//		ResultObj<TaskInfoOut> result = jsonHandler.callGetTask(taskInfo);
//		TaskInfo resultInfo = result.getItem();
//		if (!result.getResultCode().equals(ReturnCode.TASK_NOT_FOUND.get())) {
//			fail();
//		}
//		assert(true);
//	}
//		
//	@Test
//	public void testDeleteFromTasksAC()  {
//		RequestObj req = new RequestObj();
//		req.setUserId(taskAC.getUserId());
//		List<String> taskList = new ArrayList<String>();
//		taskList.add(taskAC.getTaskId());
//		req.setKeyList(taskList);
//		ResultObj result = jsonHandler.callDeleteFromTasks(req);
//		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
//			fail();
//		}
//		assert(true);
//	}
//
//
//	/**
//	 * 모든 스케쥴 삭제 delete_yn = 'Y'
//	 */
//	@Test
//	public void testSetSchedulesDeleteFlagAll()  {
//		RequestObj req = new RequestObj();
//		req.setUserId(scheduleBB.getUserId());
//		List<String> scheduleList = new ArrayList<String>();
//		scheduleList.add(scheduleAA.getScheduleId());
//		scheduleList.add(scheduleAB.getScheduleId());
//		scheduleList.add(scheduleBA.getScheduleId());
//		scheduleList.add(scheduleBB.getScheduleId());
//		req.setKeyList(scheduleList);
//		ResultObj result = jsonHandler.callSetSchedulesDeleteFlag(req);
//		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
//			fail();
//		}
//		assert(true);
//	}
//
//	@Test
//	public void testDeleteFromSchedulesAll()  {
//		RequestObj req = new RequestObj();
//		req.setUserId(scheduleBB.getUserId());
//		List<String> scheduleList = new ArrayList<String>();
//		scheduleList.add(scheduleAA.getScheduleId());
//		scheduleList.add(scheduleAB.getScheduleId());
//		scheduleList.add(scheduleBA.getScheduleId());
//		scheduleList.add(scheduleBB.getScheduleId());
//		req.setKeyList(scheduleList);
//		ResultObj result = jsonHandler.callDeleteFromSchedules(req);
//		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
//			fail();
//		}
//		assert(true);
//	}
//	
//	@Test
//	public void testSetTasksDeleteFlagAll()  {
//		RequestObj req = new RequestObj();
//		req.setUserId(userA.getUserId());
//		List<String> taskList = new ArrayList<String>();
//		taskList.add(taskAA.getTaskId());
//		taskList.add(taskAB.getTaskId());
//		req.setKeyList(taskList);
//		ResultObj result = jsonHandler.callSetTasksDeleteFlag(req);
//		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
//			fail();
//		}
//		assert(true);
//	}
//	
//	@Test
//	public void testDeleteFromTasksAll()  {
//		RequestObj req = new RequestObj();
//		req.setUserId(userA.getUserId());
//		List<String> taskList = new ArrayList<String>();
//		taskList.add(taskAA.getTaskId());
//		taskList.add(taskAB.getTaskId());
//		req.setKeyList(taskList);
//		ResultObj result = jsonHandler.callDeleteFromTasks(req);
//		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
//			fail();
//		}
//		assert(true);
//	}
//	
//	@Test
//	public void testCheckoutScheduleWithNone()  {
//		ResultObj<List<ScheduleInfo>> result = jsonHandler.callCheckoutSchedule(userA);
//		if (!result.getResultCode().equals(ReturnCode.SCHEDULE_NOT_FOUND.get())) {
//			fail();
//		}
//		assert(true);
//	}
//	
//	
//	@Test
//	public void testCheckoutTaskWithNone()  {
//		ResultObj<List<TaskInfoOut>> result = jsonHandler.callCheckoutTask(userA);
//		if (!result.getResultCode().equals(ReturnCode.TASK_NOT_FOUND.get())) {
//			fail();
//		}
//		
////		for (TaskInfoOut item : result.getItem()) {
////			logger.info("testCheckoutTask out: "+item.getTaskName());
////		}
//		logger.info("testCheckoutTaskWithNone: result.getResultCode"+result.getResultCode());
//		logger.info("testCheckoutTaskWithNone: ReturnCode.TASK_NOT_FOUND"+ReturnCode.TASK_NOT_FOUND.get());
//		assert(true);
//	}
//	
//	@Test
//	public void testUnsubscribeWithTasks() {
//		ResultObj result = jsonHandler.callUnsubscribe(userA);
//		if (!result.getResultCode().equals(ReturnCode.TASK_EXISTS.get())) {
//			fail();
//		}
//		assert(true);
//	}
//
//	@Test
//	public void testUnsubscribeWithNoTasks() {
//		ResultObj result = jsonHandler.callUnsubscribe(userA);
//		if (!result.getResultCode().equals(ReturnCode.SUCCESS.get())) {
//			fail();
//		}
//		assert(true);
//	}
//}
