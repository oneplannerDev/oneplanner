package com.ontide.oneplanner.dao;

import com.ontide.oneplanner.etc.Constant;

public class EnvironmentBean {
	String domainName;
	String domainIp;
	String port;
	String workPath;
	String mailUserName;
	String mailPassword;
	String mailSmtpHost;
	int mailSmtpPort;
	String mailFromMail;
	String mailSubscAuthUrl;
	String mailSubscAuthTitleEn;
	String mailSubscAuthTitleKo;
	String mailSubscAuthContentEn;
	String mailSubscAuthContentKo;
	String mailPasswordResetTitleEn;
	String mailPasswordResetTitleKo;
	String mailpasswordResetContentEn;
	String mailpasswordResetContentKo;
	/*		
	mail.subscr.auth.url
	mail.subscr.auth.title
	mail.subscr.auth.content
	mail.passwd.reset.title
	mail.subscr.auth.content
*/

	
	public String getDomainName() {
		return domainName;
	}
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	public String getDomainIp() {
		return domainIp;
	}
	public void setDomainIp(String domainIp) {
		this.domainIp = domainIp;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getWorkPath() {
		return workPath;
	}
	public void setWorkPath(String workPath) {
		this.workPath = workPath;
	}
	public String getMailUserName() {
		return mailUserName;
	}
	public void setMailUserName(String mailUserName) {
		this.mailUserName = mailUserName;
	}
	public String getMailPassword() {
		return mailPassword;
	}
	public void setMailPassword(String mailPassword) {
		this.mailPassword = mailPassword;
	}
	public String getMailSmtpHost() {
		return mailSmtpHost;
	}
	public void setMailSmtpHost(String mailSmtpHost) {
		this.mailSmtpHost = mailSmtpHost;
	}
	public int getMailSmtpPort() {
		return mailSmtpPort;
	}
	public void setMailSmtpPort(int mailSmtpPort) {
		this.mailSmtpPort = mailSmtpPort;
	}
	public String getMailFromMail() {
		return mailFromMail;
	}
	public void setMailFromMail(String mailFromMail) {
		this.mailFromMail = mailFromMail;
	}	
	public String getMailSubscAuthUrl() {
		return mailSubscAuthUrl;
	}
	public void setMailSubscAuthUrl(String mailSubscAuthUrl) {
		this.mailSubscAuthUrl = mailSubscAuthUrl;
	}
	public String getMailSubscAuthTitle(String langCode) {
		return Constant.LANG_EN.get().equals(langCode)?mailSubscAuthTitleEn:mailSubscAuthTitleKo;
	}
	public void setMailSubscAuthTitleEn(String mailSubscAuthTitleEn) {
		this.mailSubscAuthTitleEn = mailSubscAuthTitleEn;
	}
	public void setMailSubscAuthTitleKo(String mailSubscAuthTitleKo) {
		this.mailSubscAuthTitleKo = mailSubscAuthTitleKo;
	}
	public String getMailSubscAuthContent(String langCode) {
		return Constant.LANG_EN.get().equals(langCode)?mailSubscAuthContentEn:mailSubscAuthContentKo;
	}
	public void setMailSubscAuthContentEn(String mailSubscAuthContentEn) {
		this.mailSubscAuthContentEn = mailSubscAuthContentEn;
	}
	
	public void setMailSubscAuthContentKo(String mailSubscAuthContentKo) {
		this.mailSubscAuthContentKo = mailSubscAuthContentKo;
	}
	public String getMailPasswordResetTitle(String langCode) {
		return Constant.LANG_EN.get().equals(langCode)?mailPasswordResetTitleEn:mailPasswordResetTitleKo;
	}
	public void setMailPasswordResetTitleEn(String mailPasswordResetTitleEn) {
		this.mailPasswordResetTitleEn = mailPasswordResetTitleEn;
	}
	public void setMailPasswordResetTitleKo(String mailPasswordResetTitleKo) {
		this.mailPasswordResetTitleKo = mailPasswordResetTitleKo;
	}
	public String getMailpasswordResetContent(String langCode) {
		return Constant.LANG_EN.get().equals(langCode)?mailpasswordResetContentEn:mailpasswordResetContentKo;
	}
	public void setMailpasswordResetContentEn(String mailpasswordResetContentEn) {
		this.mailpasswordResetContentEn = mailpasswordResetContentEn;
	}
	public void setMailpasswordResetContentKo(String mailpasswordResetContentKo) {
		this.mailpasswordResetContentKo = mailpasswordResetContentKo;
	}

}
