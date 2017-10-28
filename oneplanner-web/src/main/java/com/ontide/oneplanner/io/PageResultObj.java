package com.ontide.oneplanner.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PageResultObj<T> extends ResultObj<T> {
	private static final Logger logger = LoggerFactory.getLogger(PageResultObj.class);
	private static final long serialVersionUID = 1L;
	String totalCnt;

	public PageResultObj() {
		// TODO Auto-generated constructor stub
	}
	
	public String getTotalCnt() {
		return totalCnt;
	}

	public void setTotalCnt(String totalCnt) {
		this.totalCnt = totalCnt;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append(String.format("[%s][%s][%s]", resultCode, resultMsg, totalCnt));
		if (item != null)
			sb.append(String.format("{%s}", item.toString()));
		
		return sb.toString();
	}
}
