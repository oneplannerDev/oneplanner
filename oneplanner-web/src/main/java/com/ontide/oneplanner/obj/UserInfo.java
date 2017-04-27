package com.ontide.oneplanner.obj;

import java.io.Serializable;

public class UserInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String userId;
	String userName;	
	String userType;
	String passwd;
	String email;
	String sex;
	String birthDate;
	String calType;
	String calViewType;
	String startWeek;
	String timeZone;
	String langCode;
	String widgetOption; 
	String authYn;
	String deleteYn;
	
	public UserInfo() {	}
		
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getCalType() {
		return calType;
	}

	public void setCalType(String calType) {
		this.calType = calType;
	}

	public String getCalViewType() {
		return calViewType;
	}

	public void setCalViewType(String calViewType) {
		this.calViewType = calViewType;
	}

	public String getStartWeek() {
		return startWeek;
	}

	public void setStartWeek(String startWeek) {
		this.startWeek = startWeek;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public String getLangCode() {
		return langCode;
	}

	public void setLangCode(String langCode) {
		this.langCode = langCode;
	}

	public String getWidgetOption() {
		return widgetOption;
	}

	public void setWidgetOption(String widgetOption) {
		this.widgetOption = widgetOption;
	}

	public String getAuthYn() {
		return authYn;
	}

	public void setAuthYn(String authYn) {
		this.authYn = authYn;
	}

	public String getDeleteYn() {
		return deleteYn;
	}

	public void setDeleteYn(String deleteYn) {
		this.deleteYn = deleteYn;
	}

	public String toString() {
		return String.format("userId[%s]userName[%s]userType[%s]passwd[%s]email[%s]sex[%s]birthDate[%s]calType[%s]calViewType[%s]startWeek[%s]timeZone[%s]langCode[%s]widgetOption[%s]authYn[%s]deleteYn[%s]"
				,userId,userName,userType,passwd,email,sex,birthDate,calType,calViewType,startWeek,timeZone,langCode,widgetOption,authYn,deleteYn);
	}
}
