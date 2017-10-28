package com.ontide.oneplanner.obj;

import java.io.Serializable;

public class TodayInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String today;
	String contSeq;
	String title;
	String content;
	String imageType;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImageType() {
		return imageType;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
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
		return String.format("today[%s]contSeq[%s]title[%s]imageType[%s]imageUrl[%s]content[%s]createDate[%s]updateDate[%s]"
				,today,  contSeq, title, imageType, imageUrl, content, createDate, updateDate);
	}
}
