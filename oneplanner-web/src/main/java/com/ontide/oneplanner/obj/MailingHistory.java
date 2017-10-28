package com.ontide.oneplanner.obj;

import java.io.Serializable;

public class MailingHistory implements Serializable {

	private static final long serialVersionUID = 1L;
	
	String userId;
	String sendDate;
	String authMode;
	String authId;
	String createDate;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getSendDate() {
		return sendDate;
	}
	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}
	public String getAuthMode() {
		return authMode;
	}
	public void setAuthMode(String authMode) {
		this.authMode = authMode;
	}
	public String getAuthId() {
		return authId;
	}
	public void setAuthId(String authId) {
		this.authId = authId;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public MailingHistory() {	}
		public String toString() {
		return String.format("userId[%s]sendDate[%s]authMode[%s]authId[%s]createDate[%s]"
				,userId,sendDate, authMode, authId, createDate);
	}
}
