package com.ontide.oneplanner.etc;

public enum Constant {
	DEFAULT_USER_NUM("defaultnum"),
	STR_DEFAULT_USER_NUM("default user number"),
	CALLING_NUM("callingNum"),
	FILE_PATH("filePath"),
	EXPIRED_DATE("expiredDate"),
	DURATION_TYPE("durationType"),
	USER_NUM("userNum"),
	CALLING_NAME("callingName"),
	REGISTER_DATE("registerDate"),
	JSON_MSG("jsonMsg"),
	DOWNLOAD_CNT("downloadCnt"),
	UPDATE_DATE("updateDate"),
	
	DEFAULT_DURATION_TYPE("defaultDurationType"),
	DEFAULT_JSON_MESSAGE("defaultJsonMessage"),
	DEFAULT_EXPIRED_DATE("defaultExpiredDate"),
	JSON_MESSAGE("jsonMessage"),
	
	CALENDAR_TYPE_GOOGLE("G"),
	CALENDAR_TYPE_FACEBOOK("F"),
	CALENDAR_TYPE_NAVER("N"),
	CALENDAR_TYPE_DAUM("D"),
	CALENDAR_VIEW_TYPE_BIWEEK("B"),
	CALENDAR_VIEW_TYPE_MONTHLY("M"),
	CALENDAR_VIEW_TYPE_JOBS("J"),
	FREQUENCY_PERIOD_MONTH("M"),
	FREQUENCY_PERIOD_WEEK("W"),
	FREQUENCY_PERIOD_DAY("D"),
	START_WEEK_SUNDAY("S"),
	START_WEEK_MONDAY("M"),
	TASK_TYPE_GOAL("G"),
	TASK_TYPE_ROUTINE("R"),
	USER_TYPE_GOOGLE("G"),
	USER_TYPE_FACEBOOK("F"),
	USER_TYPE_NORMAL("N"),
	
	LANG_EN("en"),
	LANG_KO("ko"),
	/* mail auth message box */
	AUTH_RESULT_TITLE("authResultTitle"),
	AUTH_RESULT_MESSAGE("authResultMessage"),
	AUTH_RESULT_BUTTON("authResultButton"),
	AUTH_SUCCESS_TITLE_KO("등록 승인 완료"),
	AUTH_SUCCESS_TITLE_EN("Your subscription confirmed"),
	AUTH_FAIL_TITLE_KO("등록 승인 실패"),
	AUTH_FAIL_TITLE_EN("Your subscription failed"),
	AUTH_SUCCESS_MSG_KO("등록 요청이 승인되었습니다.<br/>one planner서비스를 하용해주셔서 감사합니다."),
	AUTH_SUCCESS_MSG_EN("Your subscription request is just confirmed.<br/>Thank you for using one planner service."),
	AUTH_FAIL_MSG_KO("등록 요청이  실해하였습니다.<br/>다시 등록 신청 부탁드립니다."),
	AUTH_FAIL_MSG_EN("Your subscription request is just failed.<br/>Please subscribe your account again."),
	AUTH_SUCCESS_BUTTON_KO("One Planner 열기"),
	AUTH_SUCCESS_BUTTON_EN("Open One Planner"),
	AUTH_FAIL_BUTTON_KO("One Planner 열기"),
	AUTH_FAIL_BUTTON_EN("Open One Planner"),
	;
	private Constant(String str) {
		value = str;
	}
	
	private Constant(int val) {
		intValue = val;
	}
	
	private int intValue;
	private String value;
	
	public boolean equalValues(String str) {
		return (str == null)?false:value.equals(str);
	}
	
	public String get() {
		return value;
	}
	
	public int val() {
		return intValue;
	}
}
