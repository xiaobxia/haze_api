package com.vxianjin.gringotts.web.pojo.autorisk;

/**
 * Created by Phi on 2017/12/27.
 */
public class TdTopTen {
    private Integer rank;

    private String callInNumber;

    private String callInFrequency;

    private String callInPerson;

    private Boolean callInSameFlag;

    private String callOutNumber;

    private String callOutFrequency;

    private String callOutPerson;

    private Boolean callOutSameFlag;

    @Override
    public String toString() {
        return "TdTopTen{" +
                "rank=" + rank +
                ", callInNumber='" + callInNumber + '\'' +
                ", callInFrequency='" + callInFrequency + '\'' +
                ", callInPerson='" + callInPerson + '\'' +
                ", callInSameFlag=" + callInSameFlag +
                ", callOutNumber='" + callOutNumber + '\'' +
                ", callOutFrequency='" + callOutFrequency + '\'' +
                ", callOutPerson='" + callOutPerson + '\'' +
                ", callOutSameFlag=" + callOutSameFlag +
                '}';
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public String getCallInNumber() {
        return callInNumber;
    }

    public void setCallInNumber(String callInNumber) {
        this.callInNumber = callInNumber;
    }

    public String getCallInFrequency() {
        return callInFrequency;
    }

    public void setCallInFrequency(String callInFrequency) {
        this.callInFrequency = callInFrequency;
    }

    public String getCallInPerson() {
        return callInPerson;
    }

    public void setCallInPerson(String callInPerson) {
        this.callInPerson = callInPerson;
    }

    public Boolean getCallInSameFlag() {
        return callInSameFlag;
    }

    public void setCallInSameFlag(Boolean callInSameFlag) {
        this.callInSameFlag = callInSameFlag;
    }

    public String getCallOutNumber() {
        return callOutNumber;
    }

    public void setCallOutNumber(String callOutNumber) {
        this.callOutNumber = callOutNumber;
    }

    public String getCallOutFrequency() {
        return callOutFrequency;
    }

    public void setCallOutFrequency(String callOutFrequency) {
        this.callOutFrequency = callOutFrequency;
    }

    public String getCallOutPerson() {
        return callOutPerson;
    }

    public void setCallOutPerson(String callOutPerson) {
        this.callOutPerson = callOutPerson;
    }

    public Boolean getCallOutSameFlag() {
        return callOutSameFlag;
    }

    public void setCallOutSameFlag(Boolean callOutSameFlag) {
        this.callOutSameFlag = callOutSameFlag;
    }
}
