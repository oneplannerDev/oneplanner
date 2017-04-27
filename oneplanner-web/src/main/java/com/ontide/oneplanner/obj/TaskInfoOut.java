package com.ontide.oneplanner.obj;

public class TaskInfoOut extends TaskInfo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String jobIdList;
	int totalCntOfJob;
	int completedCntOfJob;
		
	public String getJobIdList() {
		return jobIdList;
	}


	public void setJobIdList(String jobIdList) {
		this.jobIdList = jobIdList;
	}


	public int getTotalCntOfJob() {
		return totalCntOfJob;
	}


	public void setTotalCntOfJob(int totalCntOfJob) {
		this.totalCntOfJob = totalCntOfJob;
	}


	public int getCompletedCntOfJob() {
		return completedCntOfJob;
	}


	public void setCompletedCntOfJob(int completedCntOfJob) {
		this.completedCntOfJob = completedCntOfJob;
	}


	public String toString() {
		return String.format("taskId[%s]userId[%s]taskName[%s]taskType[%s]startDate[%s]endDate[%s]frequencyCnt[%d]frequencyPeriod[%s]progress[%d]participants[%s]"
				+ "color[%s]days[%s]weeks[%s]startTime[%s]endTime[%s]deleteYn[%s]jobIdList[%s]totalCntOfJob[%d]completedCntOfJob[%d]"
				,taskId,userId,taskName,taskType,startDate,endDate,frequencyCnt,frequencyPeriod,progress,participants
				,color, days, weeks, startTime, endTime, deleteYn, jobIdList, totalCntOfJob, completedCntOfJob);
	}
}
