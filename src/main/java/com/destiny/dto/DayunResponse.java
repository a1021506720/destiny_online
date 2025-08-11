package com.destiny.dto;

import java.util.List;

/**
 * 大运排盘响应 DTO
 */
public class DayunResponse {
    
    /**
     * 基本八字信息
     */
    private BaziResponse baziInfo;
    
    /**
     * 性别
     */
    private String gender;
    
    /**
     * 年命阴阳性质（阳年/阴年）
     */
    private String yearNature;
    
    /**
     * 排运方式（顺排/逆排）
     */
    private String dayunDirection;
    
    /**
     * 起运年龄（虚岁）
     */
    private int startAge;
    
    /**
     * 起运年份
     */
    private int startYear;
    
    /**
     * 距离上一个节的天数
     */
    private int daysToLastJie;
    
    /**
     * 空亡地支
     */
    private List<String> kongWang;
    
    /**
     * 大运列表
     */
    private List<DayunStep> dayunSteps;
    
    /**
     * 排盘说明
     */
    private String description;
    
    // Getter 和 Setter 方法
    public BaziResponse getBaziInfo() {
        return baziInfo;
    }
    
    public void setBaziInfo(BaziResponse baziInfo) {
        this.baziInfo = baziInfo;
    }
    
    public String getGender() {
        return gender;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public String getYearNature() {
        return yearNature;
    }
    
    public void setYearNature(String yearNature) {
        this.yearNature = yearNature;
    }
    
    public String getDayunDirection() {
        return dayunDirection;
    }
    
    public void setDayunDirection(String dayunDirection) {
        this.dayunDirection = dayunDirection;
    }
    
    public int getStartAge() {
        return startAge;
    }
    
    public void setStartAge(int startAge) {
        this.startAge = startAge;
    }
    
    public int getStartYear() {
        return startYear;
    }
    
    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }
    
    public int getDaysToLastJie() {
        return daysToLastJie;
    }
    
    public void setDaysToLastJie(int daysToLastJie) {
        this.daysToLastJie = daysToLastJie;
    }
    
    public List<String> getKongWang() {
        return kongWang;
    }
    
    public void setKongWang(List<String> kongWang) {
        this.kongWang = kongWang;
    }
    
    public List<DayunStep> getDayunSteps() {
        return dayunSteps;
    }
    
    public void setDayunSteps(List<DayunStep> dayunSteps) {
        this.dayunSteps = dayunSteps;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * 大运步骤信息
     */
    public static class DayunStep {
        
        /**
         * 第几步大运
         */
        private int step;
        
        /**
         * 大运干支
         */
        private String ganZhi;
        
        /**
         * 开始年龄（虚岁）
         */
        private int startAge;
        
        /**
         * 结束年龄（虚岁）
         */
        private int endAge;
        
        /**
         * 开始年份
         */
        private int startYear;
        
        /**
         * 结束年份
         */
        private int endYear;
        
        // Getter 和 Setter 方法
        public int getStep() {
            return step;
        }
        
        public void setStep(int step) {
            this.step = step;
        }
        
        public String getGanZhi() {
            return ganZhi;
        }
        
        public void setGanZhi(String ganZhi) {
            this.ganZhi = ganZhi;
        }
        
        public int getStartAge() {
            return startAge;
        }
        
        public void setStartAge(int startAge) {
            this.startAge = startAge;
        }
        
        public int getEndAge() {
            return endAge;
        }
        
        public void setEndAge(int endAge) {
            this.endAge = endAge;
        }
        
        public int getStartYear() {
            return startYear;
        }
        
        public void setStartYear(int startYear) {
            this.startYear = startYear;
        }
        
        public int getEndYear() {
            return endYear;
        }
        
        public void setEndYear(int endYear) {
            this.endYear = endYear;
        }
    }
}
