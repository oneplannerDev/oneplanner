package com.ontide.oneplanner.obj;

import java.io.Serializable;

public class ScheduleHistory implements Serializable {

	private static final long serialVersionUID = 8365553157067135034L;
	String scheduleId;
	String userId;
	String nowDate;
	String startDate;
	String endDate;
	String completeYn;
	String deleteYn;
	
	public ScheduleHistory() {	}
		
	public String getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(String scheduleId) {
		this.scheduleId = scheduleId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getNowDate() {
		return nowDate;
	}

	public void setNowDate(String nowDate) {
		this.nowDate = nowDate;
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

	public String toString() {
		return String.format("scheduleId[%s]userId[%s]nowDate[%s]startDate[%s]endDate[%s]completeYn[%s]deleteYn[%s]"
				,scheduleId,userId,nowDate,startDate,endDate,completeYn,deleteYn);
	}
}
