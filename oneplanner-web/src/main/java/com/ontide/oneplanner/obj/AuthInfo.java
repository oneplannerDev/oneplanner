package com.ontide.oneplanner.obj;

import java.io.Serializable;

public class AuthInfo implements Serializable {

	private static final long serialVersionUID = 1608563172112405778L;
	String userId;
	String authId;
	String authMode;
	String comfirmYn;
	String expiredDate;
	String createDate;
 
	public AuthInfo() {	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAuthId() {
		return authId;
	}

	public void setAuthId(String authId) {
		this.authId = authId;
	}

	public String getAuthMode() {
		return authMode;
	}

	public void setAuthMode(String authMode) {
		this.authMode = authMode;
	}

	public String getComfirmYn() {
		return comfirmYn;
	}

	public void setComfirmYn(String comfirmYn) {
		this.comfirmYn = comfirmYn;
	}

	public String getExpiredDate() {
		return expiredDate;
	}

	public void setExpiredDate(String expiredDate) {
		this.expiredDate = expiredDate;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String toString() {
		return String.format("userId[%s]authId[%s]authMode[%s]comfirmYn[%s]expiredDate[%s]createDate[%s]"
				,userId,authId,authMode,comfirmYn,expiredDate,createDate);
	}
}
