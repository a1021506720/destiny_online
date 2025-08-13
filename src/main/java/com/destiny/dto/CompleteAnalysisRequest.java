package com.destiny.dto;

/**
 * 完整命理分析请求
 */
public class CompleteAnalysisRequest {
    
    private String date;        // 出生日期 yyyy-MM-dd
    private String time;        // 出生时间 HH:mm
    private DateType dateType;  // 日期类型
    private boolean leapMonth;  // 是否闰月
    private Gender gender;      // 性别
    private int liunianYears;   // 流年分析年数，默认10年
    
    public enum DateType {
        SOLAR,  // 阳历
        LUNAR   // 农历
    }
    
    public enum Gender {
        MALE,   // 男
        FEMALE  // 女
    }
    
    // getter and setter
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public DateType getDateType() { return dateType; }
    public void setDateType(DateType dateType) { this.dateType = dateType; }
    public boolean isLeapMonth() { return leapMonth; }
    public void setLeapMonth(boolean leapMonth) { this.leapMonth = leapMonth; }
    public Gender getGender() { return gender; }
    public void setGender(Gender gender) { this.gender = gender; }
    public int getLiunianYears() { return liunianYears; }
    public void setLiunianYears(int liunianYears) { this.liunianYears = liunianYears; }
}