package com.destiny.dto;

import java.util.List;
import java.util.Map;

/**
 * 八字详细信息
 * 包含原始八字和解析后的详细数据
 */
public class BaziDetail {
    
    /**
     * 原始八字信息
     */
    private String yearGan;     // 年干
    private String yearZhi;     // 年支
    private String monthGan;    // 月干
    private String monthZhi;    // 月支
    private String dayGan;      // 日干
    private String dayZhi;      // 日支
    private String hourGan;     // 时干
    private String hourZhi;     // 时支
    
    /**
     * 地支藏干信息
     */
    private Map<String, List<String>> zhiZangGan; // 每个地支的藏干列表
    
    /**
     * 扁平化的所有干支（包含藏干）
     */
    private List<GanZhiItem> allGanZhi; // 所有天干地支项，包含权重信息
    
    // Getter 和 Setter 方法
    public String getYearGan() {
        return yearGan;
    }
    
    public void setYearGan(String yearGan) {
        this.yearGan = yearGan;
    }
    
    public String getYearZhi() {
        return yearZhi;
    }
    
    public void setYearZhi(String yearZhi) {
        this.yearZhi = yearZhi;
    }
    
    public String getMonthGan() {
        return monthGan;
    }
    
    public void setMonthGan(String monthGan) {
        this.monthGan = monthGan;
    }
    
    public String getMonthZhi() {
        return monthZhi;
    }
    
    public void setMonthZhi(String monthZhi) {
        this.monthZhi = monthZhi;
    }
    
    public String getDayGan() {
        return dayGan;
    }
    
    public void setDayGan(String dayGan) {
        this.dayGan = dayGan;
    }
    
    public String getDayZhi() {
        return dayZhi;
    }
    
    public void setDayZhi(String dayZhi) {
        this.dayZhi = dayZhi;
    }
    
    public String getHourGan() {
        return hourGan;
    }
    
    public void setHourGan(String hourGan) {
        this.hourGan = hourGan;
    }
    
    public String getHourZhi() {
        return hourZhi;
    }
    
    public void setHourZhi(String hourZhi) {
        this.hourZhi = hourZhi;
    }
    
    public Map<String, List<String>> getZhiZangGan() {
        return zhiZangGan;
    }
    
    public void setZhiZangGan(Map<String, List<String>> zhiZangGan) {
        this.zhiZangGan = zhiZangGan;
    }
    
    public List<GanZhiItem> getAllGanZhi() {
        return allGanZhi;
    }
    
    public void setAllGanZhi(List<GanZhiItem> allGanZhi) {
        this.allGanZhi = allGanZhi;
    }
    
    /**
     * 干支项目类（包含权重信息）
     */
    public static class GanZhiItem {
        private String gan;         // 天干
        private String wuxing;      // 五行
        private String yinyang;     // 阴阳
        private String source;      // 来源（年干、月支藏干等）
        private double weight;      // 权重
        private String position;    // 位置（年、月、日、时）
        
        public GanZhiItem(String gan, String wuxing, String yinyang, String source, double weight, String position) {
            this.gan = gan;
            this.wuxing = wuxing;
            this.yinyang = yinyang;
            this.source = source;
            this.weight = weight;
            this.position = position;
        }
        
        // Getter 和 Setter 方法
        public String getGan() {
            return gan;
        }
        
        public void setGan(String gan) {
            this.gan = gan;
        }
        
        public String getWuxing() {
            return wuxing;
        }
        
        public void setWuxing(String wuxing) {
            this.wuxing = wuxing;
        }
        
        public String getYinyang() {
            return yinyang;
        }
        
        public void setYinyang(String yinyang) {
            this.yinyang = yinyang;
        }
        
        public String getSource() {
            return source;
        }
        
        public void setSource(String source) {
            this.source = source;
        }
        
        public double getWeight() {
            return weight;
        }
        
        public void setWeight(double weight) {
            this.weight = weight;
        }
        
        public String getPosition() {
            return position;
        }
        
        public void setPosition(String position) {
            this.position = position;
        }
    }
}
