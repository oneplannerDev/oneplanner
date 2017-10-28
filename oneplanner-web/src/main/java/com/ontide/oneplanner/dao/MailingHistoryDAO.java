package com.ontide.oneplanner.dao;

import java.util.List;
import java.util.Map;

import com.ontide.oneplanner.obj.MailingHistory;

public interface MailingHistoryDAO {
	public void create(MailingHistory mailingHistory) throws Exception ;
	public int delete(String userId, String sendDate) throws Exception ;
	public MailingHistory get(String userId, String sendDate) throws Exception ;
	public MailingHistory getRecent(String userId) throws Exception ;
	public List<MailingHistory> getList(Map<String,String> params) throws Exception ;
	public List<MailingHistory> getList(String userId) throws Exception ;
	public int getCnt(Map<String,String> params) throws Exception ;
}
