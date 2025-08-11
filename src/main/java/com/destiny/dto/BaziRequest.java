package com.destiny.dto;

/**
 * 八字计算请求 DTO
 * 在 Go 中相当于定义一个 struct 来接收 JSON 请求
 */
public class BaziRequest {
    
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
    
    // Getter 和 Setter 方法（在 Go 中，结构体字段通常是公开的，Java 中使用 getter/setter）
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
    
    /**
     * 日期类型枚举
     */
    public enum DateType {
        SOLAR,  // 阳历
        LUNAR   // 农历
    }
}
