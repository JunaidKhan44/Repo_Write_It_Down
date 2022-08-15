package com.theswiftvision.writeitdown.modelclasses;

public class AlarmDetails {

    private String mTitle, mNotes, mTime, mDate;
    private int mRequestId,mAlarmStatus;


    public AlarmDetails(String mTitle, String mNotes, String mTime, String mDate, int mAlarmStatus) {
        this.mTitle = mTitle;
        this.mNotes = mNotes;
        this.mTime = mTime;
        this.mDate = mDate;
        this.mAlarmStatus = mAlarmStatus;
    }

    public AlarmDetails() {
    }


    public int getmRequestId() {
        return mRequestId;
    }

    public void setmRequestId(int mRequestId) {
        this.mRequestId = mRequestId;
    }

    public int getmAlarmStatus() {
        return mAlarmStatus;
    }

    public void setmAlarmStatus(int mAlarmStatus) {
        this.mAlarmStatus = mAlarmStatus;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmNotes() {
        return mNotes;
    }

    public void setmNotes(String mNotes) {
        this.mNotes = mNotes;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }
}
