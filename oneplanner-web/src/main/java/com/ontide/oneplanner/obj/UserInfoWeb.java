package com.ontide.oneplanner.obj;

import java.io.Serializable;
import java.util.List;

public class UserInfoWeb extends UserInfo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String confirmDate;
	String authMode;
	String createDate;
	String updateDate;
	String sendDate;
	List<MailingHistory> item;
	public UserInfoWeb() {	}
		
	public UserInfoWeb(UserInfo userInfo) {
		userId = userInfo.getUserId();
		userName = userInfo.getUserName();	
		userType = userInfo.getUserType();
		passwd = userInfo.getPasswd();
		email = userInfo.getEmail();
		sex = userInfo.getSex();
		birthDate =userInfo.getBirthDate();
		calType = userInfo.getCalType();
		calViewType= userInfo.getCalViewType();
		startWeek= userInfo.getStartWeek();
		timeZone= userInfo.getTimeZone();
		langCode= userInfo.getLangCode();
		widgetOption= userInfo.getWidgetOption(); 
		authYn= userInfo.getAuthYn();
		deleteYn= userInfo.getDeleteYn();
	}
	
	public String getConfirmDate() {
		return confirmDate;
	}

	public void setConfirmDate(String confirmDate) {
		this.confirmDate = confirmDate;
	}

	public String getAuthMode() {
		return authMode;
	}

	public void setAuthMode(String authMode) {
		this.authMode = authMode;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	
	public String getSendDate() {
		return sendDate;
	}

	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}

	public List<MailingHistory> getItem() {
		return item;
	}

	public void setItem(List<MailingHistory> item) {
		this.item = item;
	}

	public String toString() {
		return String.format("userId[%s]userName[%s]userType[%s]passwd[%s]email[%s]sex[%s]birthDate[%s]calType[%s]calViewType[%s]startWeek[%s]timeZone[%s]langCode[%s]widgetOption[%s]authYn[%s]deleteYn[%s]authMode[%s]confirmDate[%s]createDate[%s]updateDate[%s]sendDate[%s]"
				,userId,userName,userType,passwd,email,sex,birthDate,calType,calViewType,startWeek,timeZone,langCode,widgetOption,authYn,deleteYn,authMode,confirmDate,createDate,updateDate,sendDate);
	}
}
