package com.ontide.oneplanner.client;

//import com.ontide.oneplanner.etc.Constant;
import com.ontide.oneplanner.etc.Constant;
import com.ontide.oneplanner.obj.ScheduleHistory;
import com.ontide.oneplanner.obj.ScheduleInfo;
import com.ontide.oneplanner.obj.TaskInfo;
import com.ontide.oneplanner.obj.TodayInfo;
import com.ontide.oneplanner.obj.UserInfo;

public class TestObj {
	//public final String URL = "http://localhost:8081/oneplanner/";
	public final String URL = "http://13.124.1.173:8080/oneplanner/";

	public UserInfo getUserA() {
		UserInfo userA = new UserInfo();
		userA.setUserId("test"+TestUtil.getRandomUserId());
		userA.setUserName("John Snow본명");
		userA.setPasswd("youknownothing");
		userA.setEmail("jinnonspot@gmail.com");
		userA.setUserType(Constant.USER_TYPE_NORMAL.get());
		userA.setSex("M");
		userA.setBirthDate("19810324");
		userA.setCalType(Constant.CALENDAR_TYPE_GOOGLE.get());
		userA.setCalViewType(Constant.CALENDAR_VIEW_TYPE_BIWEEK.get());
		userA.setStartWeek(Constant.START_WEEK_MONDAY.get());
		userA.setTimeZone("Seoul");
		userA.setLangCode(Constant.LANG_KO.get());
		userA.setWidgetOption("ss");

		return userA;
	}

	public UserInfo getUserB() {
		UserInfo userB = new UserInfo();
		userB.setUserId("test"+TestUtil.getRandomUserId());
		userB.setUserName("jimmy baration");
		userB.setPasswd("cerseiismine");
		userB.setEmail("jinnonsbox@gmail.com");
		userB.setUserType(Constant.USER_TYPE_FACEBOOK.get());
		userB.setSex("M");
		userB.setBirthDate("19910424");
		userB.setCalType(Constant.CALENDAR_TYPE_FACEBOOK.get());
		userB.setCalViewType(Constant.CALENDAR_VIEW_TYPE_JOBS.get());
		userB.setStartWeek(Constant.START_WEEK_SUNDAY.get());
		userB.setTimeZone("Tokyo");
		userB.setLangCode(Constant.LANG_KO.get());
		userB.setWidgetOption("tt");
		return userB;
	}

	public TaskInfo getTaskAA(UserInfo userInfo) {
		TaskInfo taskAA = new TaskInfo();
		taskAA.setUserId(userInfo.getUserId());
		taskAA.setTaskId("9223372036854775701");
		taskAA.setTaskName("헬스 계획");
		taskAA.setTaskType(Constant.TASK_TYPE_GOAL.get());
		taskAA.setStartDate("201702010000");
		taskAA.setEndDate("201703010000");
		taskAA.setFrequencyCnt(3);
		taskAA.setFrequencyPeriod(Constant.FREQUENCY_PERIOD_MONTH.get());
		taskAA.setProgress(3);
		taskAA.setParticipants("James;Jane;Jean");
		taskAA.setColor(1);
		taskAA.setDays("01000000");
		taskAA.setWeeks("10000");
		taskAA.setStartTime("1259");
		taskAA.setEndTime("1359");
		taskAA.setDeleteYn("N");
		return taskAA;
	}

	public TaskInfo getTaskAB(UserInfo userInfo) {
		TaskInfo taskAB = new TaskInfo();
		taskAB.setUserId(userInfo.getUserId());
		taskAB.setTaskId("9223372036854775702");
		taskAB.setTaskName("공부계획");
		taskAB.setTaskType(Constant.TASK_TYPE_ROUTINE.get());
		taskAB.setStartDate("201705010000");
		taskAB.setEndDate("201706010000");
		taskAB.setFrequencyCnt(1);
		taskAB.setFrequencyPeriod(Constant.FREQUENCY_PERIOD_MONTH.get());
		taskAB.setProgress(3);
		taskAB.setParticipants("Kim;Kane;Kleo");
		taskAB.setColor(2);
		taskAB.setDays("01100000");
		taskAB.setWeeks("11000");
		taskAB.setStartTime("1258");
		taskAB.setEndTime("1358");
		taskAB.setDeleteYn("N");

		return taskAB;
	}

	public TaskInfo getTaskAC(UserInfo userInfo) {
		TaskInfo taskAB = new TaskInfo();
		taskAB.setUserId(userInfo.getUserId());
		taskAB.setTaskId("9223372036854775703");
		taskAB.setTaskName("여행계획");
		taskAB.setTaskType(Constant.TASK_TYPE_ROUTINE.get());
		taskAB.setStartDate("201705010000");
		taskAB.setEndDate("201706010000");
		taskAB.setFrequencyCnt(1);
		taskAB.setFrequencyPeriod(Constant.FREQUENCY_PERIOD_MONTH.get());
		taskAB.setProgress(3);
		taskAB.setParticipants("Jamie;제이미;지미");
		taskAB.setColor(2);
		taskAB.setDays("01100000");
		taskAB.setWeeks("11000");
		taskAB.setStartTime("1257");
		taskAB.setEndTime("1357");
		taskAB.setDeleteYn("N");
		return taskAB;
	}

	public TaskInfo getTaskBA(UserInfo userInfo) {
		TaskInfo taskAB = new TaskInfo();
		taskAB.setUserId(userInfo.getUserId());
		taskAB.setTaskId("9223372036854775704");
		taskAB.setTaskName("여행계획");
		taskAB.setTaskType(Constant.TASK_TYPE_ROUTINE.get());
		taskAB.setStartDate("201705010000");
		taskAB.setEndDate("201706010000");
		taskAB.setFrequencyCnt(1);
		taskAB.setFrequencyPeriod(Constant.FREQUENCY_PERIOD_MONTH.get());
		taskAB.setProgress(3);
		taskAB.setParticipants("Jamie;제이미;지미");
		taskAB.setColor(2);
		taskAB.setDays("01100000");
		taskAB.setWeeks("11000");
		taskAB.setStartTime("1257");
		taskAB.setEndTime("1357");
		taskAB.setDeleteYn("N");
		return taskAB;
	}

	public TaskInfo getTaskBB(UserInfo userInfo) {
		TaskInfo taskAB = new TaskInfo();
		taskAB.setUserId(userInfo.getUserId());
		taskAB.setTaskId("9223372036854775705");
		taskAB.setTaskName("여행계획");
		taskAB.setTaskType(Constant.TASK_TYPE_ROUTINE.get());
		taskAB.setStartDate("201705010000");
		taskAB.setEndDate("201706010000");
		taskAB.setFrequencyCnt(1);
		taskAB.setFrequencyPeriod(Constant.FREQUENCY_PERIOD_MONTH.get());
		taskAB.setProgress(3);
		taskAB.setParticipants("Jamie;제이미;지미");
		taskAB.setColor(2);
		taskAB.setDays("01100000");
		taskAB.setWeeks("11000");
		taskAB.setStartTime("1257");
		taskAB.setEndTime("1357");
		taskAB.setDeleteYn("N");
		return taskAB;
	}
	
	public TaskInfo getTaskBC(UserInfo userInfo) {
		TaskInfo taskAB = new TaskInfo();
		taskAB.setUserId(userInfo.getUserId());
		taskAB.setTaskId("9223372036854775706");
		taskAB.setTaskName("여행계획");
		taskAB.setTaskType(Constant.TASK_TYPE_ROUTINE.get());
		taskAB.setStartDate("201705010000");
		taskAB.setEndDate("201706010000");
		taskAB.setFrequencyCnt(1);
		taskAB.setFrequencyPeriod(Constant.FREQUENCY_PERIOD_MONTH.get());
		taskAB.setProgress(3);
		taskAB.setParticipants("Jamie;제이미;지미");
		taskAB.setColor(2);
		taskAB.setDays("01100000");
		taskAB.setWeeks("11000");
		taskAB.setStartTime("1257");
		taskAB.setEndTime("1357");
		taskAB.setDeleteYn("N");
		return taskAB;
	}
//	public TaskInfo getNewTask(UserInfo userInfo, int ) {
//		TaskInfo taskAB = new TaskInfo();
//		taskAB.setUserId(userInfo.getUserId());
//		taskAB.setTaskId("3");
//		taskAB.setTaskName("여행계획");
//		taskAB.setTaskType(Constant.TASK_TYPE_ROUTINE.get());
//		taskAB.setStartDate("201705010000");
//		taskAB.setEndDate("201706010000");
//		taskAB.setFrequencyCnt(1);
//		taskAB.setFrequencyPeriod(Constant.FREQUENCY_PERIOD_MONTH.get());
//		taskAB.setProgress(3);
//		taskAB.setParticipants("Jamie;제이미;지미");
//		taskAB.setColor(2);
//		taskAB.setDays("01100000");
//		taskAB.setWeeks("11000");
//		return taskAB;
//	}
	
	public ScheduleInfo getScheduleAA(TaskInfo taskInfo) {
		ScheduleInfo schedule = new ScheduleInfo();
		schedule.setUserId(taskInfo.getUserId());
		schedule.setScheduleId("9223372036854775701");
		schedule.setScheduleName("윗몸일으키기");
		schedule.setStartDate("201702010900");
		schedule.setEndDate("201703091100");
		schedule.setMemo("윗몸 일으키기.");
		schedule.setTaskId(taskInfo.getTaskId());
		schedule.setParticipants("Park;Choi;최진원");
		schedule.setLocation("강남");
		schedule.setLocationUrl("www.naver.com");
		schedule.setAlarmYn("Y");
		schedule.setCompleteYn("N");
		schedule.setDeleteYn("N");
		schedule.setDays("01000000");
		schedule.setWeeks("10000");
		schedule.setStartTime("0150");
		schedule.setEndTime("1250");
		schedule.setGroupId("9223372036854775700");
		schedule.setAlarmPeriod("-1");
		schedule.setRepeatEndDate("201912312359");
		return schedule;
	}

	public ScheduleInfo getScheduleAB(TaskInfo taskInfo) {
		ScheduleInfo schedule = new ScheduleInfo();
		schedule.setUserId(taskInfo.getUserId());
		schedule.setScheduleId("9223372036854775702");
		schedule.setScheduleName("pushup");
		schedule.setStartDate("201702011300");
		schedule.setEndDate("201703091500");
		schedule.setMemo("You're awesome.");
		schedule.setTaskId(taskInfo.getTaskId());
		schedule.setParticipants("Park;Choi");
		schedule.setLocation("한강");
		schedule.setLocationUrl("www.naver.com");
		schedule.setAlarmYn("Y");
		schedule.setCompleteYn("N");
		schedule.setDeleteYn("N");
		schedule.setDays("01100000");
		schedule.setWeeks("11000");
		schedule.setStartTime("0150");
		schedule.setEndTime("1250");
		schedule.setGroupId("9223372036854775700");
		schedule.setAlarmPeriod("-1");
		schedule.setRepeatEndDate("201912312359");
		return schedule;
	}

	public ScheduleInfo getScheduleBA(TaskInfo taskInfo) {
		ScheduleInfo schedule = new ScheduleInfo();
		schedule.setUserId(taskInfo.getUserId());
		schedule.setScheduleId("9223372036854775703");
		schedule.setScheduleName("영어");
		schedule.setStartDate("201702011400");
		schedule.setEndDate("201703091600");
		schedule.setMemo("You're awesome.");
		schedule.setTaskId(taskInfo.getTaskId());
		schedule.setParticipants("Park;Choi");
		schedule.setLocation("종로");
		schedule.setLocationUrl("www.naver.com");
		schedule.setAlarmYn("Y");
		schedule.setCompleteYn("N");
		schedule.setDeleteYn("N");
		schedule.setDays("01110000");
		schedule.setWeeks("11100");
		schedule.setStartTime("0150");
		schedule.setEndTime("1250");
		schedule.setGroupId("9223372036854775700");
		schedule.setAlarmPeriod("-1");
		schedule.setRepeatEndDate("201912312359");
		return schedule;
	}

	public ScheduleInfo getScheduleBB(TaskInfo taskInfo) {
		ScheduleInfo schedule = new ScheduleInfo();
		schedule.setUserId(taskInfo.getUserId());
		schedule.setScheduleId("9223372036854775704");
		schedule.setScheduleName("수학");
		schedule.setStartDate("201702011700");
		schedule.setEndDate("201703091900");
		schedule.setMemo("You're awesome.");
		schedule.setTaskId(taskInfo.getTaskId());
		schedule.setParticipants("Park;Choi");
		schedule.setLocation("대치");
		schedule.setLocationUrl("www.naver.com");
		schedule.setAlarmYn("Y");
		schedule.setCompleteYn("N");
		schedule.setDeleteYn("N");
		schedule.setDays("01111000");
		schedule.setWeeks("11110");
		schedule.setStartTime("0150");
		schedule.setEndTime("1250");
		schedule.setGroupId("9223372036854775700");
		schedule.setAlarmPeriod("-1");
		schedule.setRepeatEndDate("201912312359");
		return schedule;
	}

	public ScheduleInfo getScheduleCA(TaskInfo taskInfo) {
		ScheduleInfo schedule = new ScheduleInfo();
		schedule.setUserId(taskInfo.getUserId());
		schedule.setScheduleId("9223372036854775705");
		schedule.setScheduleName("수학");
		schedule.setStartDate("201702011700");
		schedule.setEndDate("201703091900");
		schedule.setMemo("You're awesome.");
		schedule.setTaskId(taskInfo.getTaskId());
		schedule.setParticipants("Park;Choi");
		schedule.setLocation("대치");
		schedule.setLocationUrl("www.naver.com");
		schedule.setAlarmYn("Y");
		schedule.setCompleteYn("N");
		schedule.setDeleteYn("N");
		schedule.setDays("01111000");
		schedule.setWeeks("11110");
		schedule.setStartTime("0150");
		schedule.setEndTime("1250");
		schedule.setGroupId("9223372036854775700");
		schedule.setAlarmPeriod("-1");
		schedule.setRepeatEndDate("201912312359");
		return schedule;
	}
	public ScheduleInfo getScheduleCB(TaskInfo taskInfo) {
		ScheduleInfo schedule = new ScheduleInfo();
		schedule.setUserId(taskInfo.getUserId());
		schedule.setScheduleId("9223372036854775706");
		schedule.setScheduleName("수학");
		schedule.setStartDate("201702011700");
		schedule.setEndDate("201703091900");
		schedule.setMemo("You're awesome.");
		schedule.setTaskId(taskInfo.getTaskId());
		schedule.setParticipants("Park;Choi");
		schedule.setLocation("대치");
		schedule.setLocationUrl("www.naver.com");
		schedule.setAlarmYn("Y");
		schedule.setCompleteYn("N");
		schedule.setDeleteYn("N");
		schedule.setDays("01111000");
		schedule.setWeeks("11110");
		schedule.setStartTime("0150");
		schedule.setEndTime("1250");
		schedule.setGroupId("9223372036854775700");
		schedule.setAlarmPeriod("-1");
		schedule.setRepeatEndDate("201912312359");
		return schedule;
	}
	public ScheduleInfo getScheduleCC(TaskInfo taskInfo) {
		ScheduleInfo schedule = new ScheduleInfo();
		schedule.setUserId(taskInfo.getUserId());
		schedule.setScheduleId("9223372036854775707");
		schedule.setScheduleName("수학");
		schedule.setStartDate("201702011700");
		schedule.setEndDate("201703091900");
		schedule.setMemo("You're awesome.");
		schedule.setTaskId(taskInfo.getTaskId());
		schedule.setParticipants("Park;Choi");
		schedule.setLocation("대치");
		schedule.setLocationUrl("www.naver.com");
		schedule.setAlarmYn("Y");
		schedule.setCompleteYn("N");
		schedule.setDeleteYn("N");
		schedule.setDays("01111000");
		schedule.setWeeks("11110");
		schedule.setStartTime("0150");
		schedule.setEndTime("1250");
		schedule.setGroupId("9223372036854775700");
		schedule.setAlarmPeriod("-1");
		schedule.setRepeatEndDate("201912312359");
		return schedule;
	}

	public ScheduleHistory getScheduleHistoryAA(TaskInfo taskInfo) {
		ScheduleHistory schedule = new ScheduleHistory();
		schedule.setUserId(taskInfo.getUserId());
		schedule.setScheduleId("9223372036854775701");
		schedule.setNowDate("201702011330");
		schedule.setStartDate("201702011330");
		schedule.setEndDate("201703091230");
		schedule.setCompleteYn("Y");
		schedule.setDeleteYn("N");
		return schedule;
	}

	public ScheduleHistory getScheduleHistoryAB(TaskInfo taskInfo) {
		ScheduleHistory schedule = new ScheduleHistory();
		schedule.setUserId(taskInfo.getUserId());
		schedule.setScheduleId("9223372036854775702");
		schedule.setNowDate("201702011330");
		schedule.setStartDate("201702011430");
		schedule.setEndDate("201703091700");
		schedule.setCompleteYn("Y");
		schedule.setDeleteYn("N");
		return schedule;
	}

	public ScheduleHistory getScheduleHistoryBA(TaskInfo taskInfo) {
		ScheduleHistory schedule = new ScheduleHistory();
		schedule.setUserId(taskInfo.getUserId());
		schedule.setScheduleId("9223372036854775703");
		schedule.setNowDate("201702011330");
		schedule.setStartDate("201702011300");
		schedule.setEndDate("201703091450");
		schedule.setCompleteYn("Y");
		schedule.setDeleteYn("N");
		return schedule;
	}

	public ScheduleHistory getScheduleHistoryBB(TaskInfo taskInfo) {
		ScheduleHistory schedule = new ScheduleHistory();
		schedule.setUserId(taskInfo.getUserId());
		schedule.setScheduleId("9223372036854775704");
		schedule.setNowDate("201702011330");
		schedule.setStartDate("201702011510");
		schedule.setEndDate("201703091920");
		schedule.setCompleteYn("Y");
		schedule.setDeleteYn("N");
		return schedule;
	}

	public ScheduleHistory getScheduleHistoryCA(TaskInfo taskInfo) {
		ScheduleHistory schedule = new ScheduleHistory();
		schedule.setUserId(taskInfo.getUserId());
		schedule.setScheduleId("9223372036854775705");
		schedule.setNowDate("201702011330");
		schedule.setStartDate("201702011510");
		schedule.setEndDate("201703091920");
		schedule.setCompleteYn("Y");
		schedule.setDeleteYn("N");
		return schedule;
	}
	public ScheduleHistory getScheduleHistoryCB(TaskInfo taskInfo) {
		ScheduleHistory schedule = new ScheduleHistory();
		schedule.setUserId(taskInfo.getUserId());
		schedule.setScheduleId("9223372036854775706");
		schedule.setNowDate("201702011330");
		schedule.setStartDate("201702011510");
		schedule.setEndDate("201703091920");
		schedule.setCompleteYn("Y");
		schedule.setDeleteYn("N");
		return schedule;
	}
	public ScheduleHistory getScheduleHistoryCC(TaskInfo taskInfo) {
		ScheduleHistory schedule = new ScheduleHistory();
		schedule.setUserId(taskInfo.getUserId());
		schedule.setScheduleId("9223372036854775707");
		schedule.setNowDate("201702011330");
		schedule.setStartDate("201702011510");
		schedule.setEndDate("201703091920");
		schedule.setCompleteYn("Y");
		schedule.setDeleteYn("N");
		return schedule;
	}

	
	public TodayInfo getTodayA() {
		TodayInfo todayA  = new TodayInfo();
		todayA.setToday("20170105");
		todayA.setContSeq("1");
		todayA.setContent("한글테스트  content:A");
		return todayA;
	}	

	public TodayInfo getTodayB() {
		TodayInfo todayA  = new TodayInfo();
		todayA.setToday("20170105");
		todayA.setContSeq("2");
		todayA.setContent("test  content:B");
		return todayA;
	}	

}
