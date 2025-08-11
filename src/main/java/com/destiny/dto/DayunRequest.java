package com.destiny.dto;

/**
 * 大运排盘请求 DTO
 */
public class DayunRequest {
    
    /**
     * 日期字符串，格式：yyyy-MM-dd
     */
    private String date;
    
    /**
     * 时间字符串，格式：HH:mm
     */
    private String time;
    
    /**
     * 日期类型：SOLAR（阳历）或 LUNAR（农历）
     */
    private DateType dateType = DateType.SOLAR;
    
    /**
     * 是否闰月（仅农历有效）
     */
    private boolean leapMonth = false;
    
    /**
     * 性别：MALE（男）或 FEMALE（女）
     */
    private Gender gender;
    
    /**
     * 排盘年数（可选，默认80年）
     */
    private int years = 80;
    
    // Getter 和 Setter 方法
    public String getDate() {
        return date;
    }
    
    public void setDate(String date) {
        this.date = date;
    }
    
    public String getTime() {
        return time;
    }
    
    public void setTime(String time) {
        this.time = time;
    }
    
    public DateType getDateType() {
        return dateType;
    }
    
    public void setDateType(DateType dateType) {
        this.dateType = dateType;
    }
    
    public boolean isLeapMonth() {
        return leapMonth;
    }
    
    public void setLeapMonth(boolean leapMonth) {
        this.leapMonth = leapMonth;
    }
    
    public Gender getGender() {
        return gender;
    }
    
    public void setGender(Gender gender) {
        this.gender = gender;
    }
    
    public int getYears() {
        return years;
    }
    
    public void setYears(int years) {
        this.years = years;
    }
    
    /**
     * 日期类型枚举
     */
    public enum DateType {
        SOLAR,  // 阳历
        LUNAR   // 农历
    }
    
    /**
     * 性别枚举
     */
    public enum Gender {
        MALE,   // 男
        FEMALE  // 女
    }
}
