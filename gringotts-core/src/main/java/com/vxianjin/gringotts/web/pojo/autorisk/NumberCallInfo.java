package com.vxianjin.gringotts.web.pojo.autorisk;

/**
 * Created by Phi on 2017/12/6.
 */
public class NumberCallInfo {
    private String callNumber;

    private int duration;

    private int durationIn;

    private int durationOut;

    private int frequency;

    private int frequencyIn;

    private int frequencyOut;

    private int nightFrequency;

    private String netMark;

    private String location;

    public String getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDurationIn() {
        return durationIn;
    }

    public void setDurationIn(int durationIn) {
        this.durationIn = durationIn;
    }

    public int getDurationOut() {
        return durationOut;
    }

    public void setDurationOut(int durationOut) {
        this.durationOut = durationOut;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getFrequencyIn() {
        return frequencyIn;
    }

    public void setFrequencyIn(int frequencyIn) {
        this.frequencyIn = frequencyIn;
    }

    public int getFrequencyOut() {
        return frequencyOut;
    }

    public void setFrequencyOut(int frequencyOut) {
        this.frequencyOut = frequencyOut;
    }

    public String getNetMark() {
        return netMark;
    }

    public void setNetMark(String netMark) {
        this.netMark = netMark;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getNightFrequency() {
        return nightFrequency;
    }

    public void setNightFrequency(int nightFrequency) {
        this.nightFrequency = nightFrequency;
    }
}
