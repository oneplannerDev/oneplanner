package com.ontide.oneplanner.io;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResultObj<T> implements Serializable {
	private static final Logger logger = LoggerFactory.getLogger(ResultObj.class);
	private static final long serialVersionUID = 1L;
	String resultCode;
	String resultMsg;
	T item;//userNum,callingNum,callingName,registerDate,expiredDate,jsonMsg,durationType,downloadCnt,updateDate;

	public ResultObj() {
		// TODO Auto-generated constructor stub
	}
		
	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}
	
	public T getItem() {
		return item;
	}

	public void setItem(T item) {
		this.item = item;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append(String.format("[%s][%s]", resultCode, resultMsg));
		if (item != null)
			sb.append(String.format("{%s}", item.toString()));
		
		return sb.toString();
	}
}
