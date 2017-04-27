package com.ontide.oneplanner.obj;

import java.io.Serializable;

public class TaskInfo implements Serializable {

	private static final long serialVersionUID = -8720266753932626980L;
	protected String taskId;
	protected String userId;
	protected String taskName;
	protected String taskType;
	protected String startDate;
	protected String endDate;
	protected int frequencyCnt;
	protected String frequencyPeriod;
	protected int progress;
	protected String participants;
	protected int color;
	protected String days;
	protected String weeks;
	protected String startTime;
	protected String endTime;
	protected String deleteYn;
	
	public TaskInfo() {	}
		
	public String getTaskId() {
		return taskId;
	}
 
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
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

	public int getFrequencyCnt() {
		return frequencyCnt;
	}

	public void setFrequencyCnt(int frequencyCnt) {
		this.frequencyCnt = frequencyCnt;
	}

	public String getFrequencyPeriod() {
		return frequencyPeriod;
	}

	public void setFrequencyPeriod(String frequencyPeriod) {
		this.frequencyPeriod = frequencyPeriod;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public String getParticipants() {
		return participants;
	}

	public void setParticipants(String participants) {
		this.participants = participants;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
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

	public String getDeleteYn() {
		return deleteYn;
	}

	public void setDeleteYn(String deleteYn) {
		this.deleteYn = deleteYn;
	}

	public String toString() {
		return String.format("taskId[%s]userId[%s]taskName[%s]taskType[%s]startDate[%s]endDate[%s]frequencyCnt[%d]frequencyPeriod[%s]progress[%d]participants[%s]color[%d]days[%s]weeks[%s]startTime[%s]endTime[%s]deleteYn[%s]"
				,taskId,userId,taskName,taskType,startDate,endDate,frequencyCnt,frequencyPeriod,progress,participants,color, days, weeks, startTime, endTime, deleteYn);
	}
}
