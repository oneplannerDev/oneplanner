package com.ontide.oneplanner.obj;

import java.io.Serializable;

public class ScheduleInfo implements Serializable {

	private static final long serialVersionUID = 8365553157067135034L;
	String scheduleId;
	String scheduleName;
	String userId;
	String startDate;
	String endDate;
	String memo;
	String taskId;
	String participants;
	String location;
	String locationUrl;
	String alarmYn;
	String completeYn;
	String days;
	String weeks;
	String startTime;
	String endTime;
	String deleteYn;
	String groupId;
	String alarmPeriod;
	String repeatEndDate;
 
	public ScheduleInfo() {	}
		
	public String getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(String scheduleId) {
		this.scheduleId = scheduleId;
	}

	public String getScheduleName() {
		return scheduleName;
	}

	public void setScheduleName(String scheduleName) {
		this.scheduleName = scheduleName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getParticipants() {
		return participants;
	}

	public void setParticipants(String participants) {
		this.participants = participants;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLocationUrl() {
		return locationUrl;
	}

	public void setLocationUrl(String locationUrl) {
		this.locationUrl = locationUrl;
	}

	public String getAlarmYn() {
		return alarmYn;
	}

	public void setAlarmYn(String alarmYn) {
		this.alarmYn = alarmYn;
	}

	public String getCompleteYn() {
		return completeYn;
	}

	public void setCompleteYn(String completeYn) {
		this.completeYn = completeYn;
	}

	public String getDeleteYn() {
		return deleteYn;
	}

	public void setDeleteYn(String deleteYn) {
		this.deleteYn = deleteYn;
	}

	public String getDays() {
		return days;
	}

	public void setDays(String days) {
		this.days = days;
	}

	public String getWeeks() {
		return weeks;
	}

	public void setWeeks(String weeks) {
		this.weeks = weeks;
	}
	

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getAlarmPeriod() {
		return alarmPeriod;
	}

	public void setAlarmPeriod(String alarmPeriod) {
		this.alarmPeriod = alarmPeriod;
	}

	public String getRepeatEndDate() {
		return repeatEndDate;
	}

	public void setRepeatEndDate(String repeatEndDate) {
		this.repeatEndDate = repeatEndDate;
	}

	public String toString() {
		return String.format("scheduleId[%s]scheduleName[%s]userId[%s]startDate[%s]endDate[%s]memo[%s]taskId[%s]participants[%s]location[%s]locationUrl[%s]alarmYn[%s]completeYn[%s]deleteYn[%s]days[%s]weeks[%s]startTime[%s]endTime[%s]groupId[%s]alarmPeriod[%s]repeatEndDate[%s]"
				,scheduleId,scheduleName,userId,startDate,endDate,memo,taskId,participants,location,locationUrl,alarmYn,completeYn,deleteYn,days, weeks,startTime,endTime, groupId, alarmPeriod,repeatEndDate);
	}
}
