package com.destiny.dto;

/**
 * 八字计算响应 DTO
 * 在 Go 中相当于定义返回的 JSON 结构体
 */
public class BaziResponse {
    
    /**
     * 阳历日期
     */
    private String solarDate;
    
    /**
     * 农历日期
     */
    private String lunarDate;
    
    /**
     * 年柱（天干地支）
     */
    private String yearPillar;
    
    /**
     * 月柱（天干地支）
     */
    private String monthPillar;
    
    /**
     * 日柱（天干地支）
     */
    private String dayPillar;
    
    /**
     * 时柱（天干地支）
     */
    private String hourPillar;
    
    /**
     * 完整八字
     */
    private String bazi;
    
    /**
     * 时辰类型（早子时/晚子时/其他时辰）
     */
    private String hourType;
    
    /**
     * 详细说明
     */
    private String description;
    
    // Getter 和 Setter 方法
    public String getSolarDate() {
        return solarDate;
    }
    
    public void setSolarDate(String solarDate) {
        this.solarDate = solarDate;
    }
    
    public String getLunarDate() {
        return lunarDate;
    }
    
    public void setLunarDate(String lunarDate) {
        this.lunarDate = lunarDate;
    }
    
    public String getYearPillar() {
        return yearPillar;
    }
    
    public void setYearPillar(String yearPillar) {
        this.yearPillar = yearPillar;
    }
    
    public String getMonthPillar() {
        return monthPillar;
    }
    
    public void setMonthPillar(String monthPillar) {
        this.monthPillar = monthPillar;
    }
    
    public String getDayPillar() {
        return dayPillar;
    }
    
    public void setDayPillar(String dayPillar) {
        this.dayPillar = dayPillar;
    }
    
    public String getHourPillar() {
        return hourPillar;
    }
    
    public void setHourPillar(String hourPillar) {
        this.hourPillar = hourPillar;
    }
    
    public String getBazi() {
        return bazi;
    }
    
    public void setBazi(String bazi) {
        this.bazi = bazi;
    }
    
    public String getHourType() {
        return hourType;
    }
    
    public void setHourType(String hourType) {
        this.hourType = hourType;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
}
