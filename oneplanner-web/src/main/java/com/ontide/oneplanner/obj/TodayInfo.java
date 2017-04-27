package com.ontide.oneplanner.obj;

import java.io.Serializable;

public class TodayInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String today;
	String contSeq;
	String content;
	String imageUrl;
	String createDate;
	String updateDate;
 
	public TodayInfo() {	}
		
	public String getToday() {
		return today;
	}

	public void setToday(String today) {
		this.today = today;
	}

	public String getContSeq() {
		return contSeq;
	}

	public void setContSeq(String contSeq) {
		this.contSeq = contSeq;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public String toString() {
		return String.format("today[%s]contSeq[%s]imageUrl[%s]content[%s]createDate[%s]updateDate[%s]"
				,today,  contSeq, imageUrl, content, createDate, updateDate);
	}
}
