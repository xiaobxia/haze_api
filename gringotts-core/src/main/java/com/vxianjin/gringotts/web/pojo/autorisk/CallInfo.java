package com.vxianjin.gringotts.web.pojo.autorisk;

/**
 * Created by Phi on 2017/12/6.
 */
public class CallInfo {
    private String callNumber;

    private String inOrOut;

    private String time;

    private int Duration;

    //夜间活跃为true
    private Boolean nightFlag;


    public String getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

    public String getInOrOut() {
        return inOrOut;
    }

    public void setInOrOut(String inOrOut) {
        this.inOrOut = inOrOut;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getDuration() {
        return Duration;
    }

    public void setDuration(int duration) {
        Duration = duration;
    }

    public Boolean getNightFlag() {
        return nightFlag;
    }

    public void setNightFlag(Boolean nightFlag) {
        this.nightFlag = nightFlag;
    }
}
