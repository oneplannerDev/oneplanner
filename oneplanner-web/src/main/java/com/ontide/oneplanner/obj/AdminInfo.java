package com.ontide.oneplanner.obj;

import java.io.Serializable;

public class AdminInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	String userId;
	String passwd;
	String email;
	String accessDate;
	String updateDate;
 
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
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
	public String getAccessDate() {
		return accessDate;
	}
	public void setAccessDate(String accessDate) {
		this.accessDate = accessDate;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	public AdminInfo() {	}
		public String toString() {
		return String.format("userId[%s]passwd[%s]email[%s]accessDate[%s]updateDate[%s]"
				,userId,passwd,email,accessDate,updateDate);
	}
}
