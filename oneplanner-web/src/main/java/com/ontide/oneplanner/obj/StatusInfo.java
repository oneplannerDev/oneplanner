package com.ontide.oneplanner.obj;

import java.io.Serializable;

public class StatusInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	int totalUserCnt;
	int emailUserCnt;
	int googleUserCnt;
	int authUnconfirmCnt;
	int resetUnconfirmCnt;
	int recentUserCntByMonth;
	int recentUserCntByWeek;
	int recentUserCntByDay;
	int unconfirmedUserCntByWeek;
	
	
	public int getTotalUserCnt() {
		return totalUserCnt;
	}


	public void setTotalUserCnt(int totalUserCnt) {
		this.totalUserCnt = totalUserCnt;
	}


	public int getEmailUserCnt() {
		return emailUserCnt;
	}


	public void setEmailUserCnt(int emailUserCnt) {
		this.emailUserCnt = emailUserCnt;
	}


	public int getGoogleUserCnt() {
		return googleUserCnt;
	}


	public void setGoogleUserCnt(int googleUserCnt) {
		this.googleUserCnt = googleUserCnt;
	}


	public int getAuthUnconfirmCnt() {
		return authUnconfirmCnt;
	}


	public void setAuthUnconfirmCnt(int authUnconfirmCnt) {
		this.authUnconfirmCnt = authUnconfirmCnt;
	}


	public int getResetUnconfirmCnt() {
		return resetUnconfirmCnt;
	}


	public void setResetUnconfirmCnt(int resetUnconfirmCnt) {
		this.resetUnconfirmCnt = resetUnconfirmCnt;
	}


	public int getRecentUserCntByMonth() {
		return recentUserCntByMonth;
	}


	public void setRecentUserCntByMonth(int recentUserCntByMonth) {
		this.recentUserCntByMonth = recentUserCntByMonth;
	}


	public int getRecentUserCntByWeek() {
		return recentUserCntByWeek;
	}


	public void setRecentUserCntByWeek(int recentUserCntByWeek) {
		this.recentUserCntByWeek = recentUserCntByWeek;
	}


	public int getRecentUserCntByDay() {
		return recentUserCntByDay;
	}


	public void setRecentUserCntByDay(int recentUserCntByDay) {
		this.recentUserCntByDay = recentUserCntByDay;
	}


	public int getUnconfirmedUserCntByWeek() {
		return unconfirmedUserCntByWeek;
	}


	public void setUnconfirmedUserCntByWeek(int unconfirmedUserCntByWeek) {
		this.unconfirmedUserCntByWeek = unconfirmedUserCntByWeek;
	}
	public String toString() {
		return String.format("totalUserCnt[%d]emailUserCnt[%d]googleUserCnt[%d]authUnconfirmCnt[%d]resetUnconfirmCnt[%d]recentUserCntByMonth[%d]"
				+ "recentUserCntByWeek[%d]recentUserCntByDay[%d]unconfirmedUserCntByWeek[%d]"
				,totalUserCnt,
				emailUserCnt,
				googleUserCnt,
				authUnconfirmCnt,
				resetUnconfirmCnt,
				recentUserCntByMonth,
				recentUserCntByWeek,
				recentUserCntByDay,
				unconfirmedUserCntByWeek);
	}
}
