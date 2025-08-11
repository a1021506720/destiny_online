package com.destiny.dto;

import java.util.List;
import java.util.Map;

/**
 * 子平术分析结果
 */
public class ZipingAnalysisResult {
    
    /**
     * 基础八字信息
     */
    private BaziDetail baziDetail;
    
    /**
     * 五行统计分析
     */
    private WuxingStats wuxingStats;
    
    /**
     * 十神分析
     */
    private ShishenStats shishenStats;
    
    /**
     * 十二宫分析
     */
    private ShierGongStats shierGongStats;
    
    /**
     * 旺衰分析
     */
    private WangShuaiAnalysis wangShuaiAnalysis;
    
    // Getter 和 Setter 方法
    public BaziDetail getBaziDetail() {
        return baziDetail;
    }
    
    public void setBaziDetail(BaziDetail baziDetail) {
        this.baziDetail = baziDetail;
    }
    
    public WuxingStats getWuxingStats() {
        return wuxingStats;
    }
    
    public void setWuxingStats(WuxingStats wuxingStats) {
        this.wuxingStats = wuxingStats;
    }
    
    public ShishenStats getShishenStats() {
        return shishenStats;
    }
    
    public void setShishenStats(ShishenStats shishenStats) {
        this.shishenStats = shishenStats;
    }
    
    public ShierGongStats getShierGongStats() {
        return shierGongStats;
    }
    
    public void setShierGongStats(ShierGongStats shierGongStats) {
        this.shierGongStats = shierGongStats;
    }
    
    public WangShuaiAnalysis getWangShuaiAnalysis() {
        return wangShuaiAnalysis;
    }
    
    public void setWangShuaiAnalysis(WangShuaiAnalysis wangShuaiAnalysis) {
        this.wangShuaiAnalysis = wangShuaiAnalysis;
    }
    
    /**
     * 五行统计结果
     */
    public static class WuxingStats {
        private Map<String, Double> wuxingCounts;   // 五行统计（考虑权重）
        private Map<String, Integer> rawCounts;     // 原始个数统计
        private String strongestWuxing;             // 最强五行
        private String weakestWuxing;               // 最弱五行
        private String dayMasterWuxing;             // 日主五行
        
        // Getter 和 Setter 方法
        public Map<String, Double> getWuxingCounts() {
            return wuxingCounts;
        }
        
        public void setWuxingCounts(Map<String, Double> wuxingCounts) {
            this.wuxingCounts = wuxingCounts;
        }
        
        public Map<String, Integer> getRawCounts() {
            return rawCounts;
        }
        
        public void setRawCounts(Map<String, Integer> rawCounts) {
            this.rawCounts = rawCounts;
        }
        
        public String getStrongestWuxing() {
            return strongestWuxing;
        }
        
        public void setStrongestWuxing(String strongestWuxing) {
            this.strongestWuxing = strongestWuxing;
        }
        
        public String getWeakestWuxing() {
            return weakestWuxing;
        }
        
        public void setWeakestWuxing(String weakestWuxing) {
            this.weakestWuxing = weakestWuxing;
        }
        
        public String getDayMasterWuxing() {
            return dayMasterWuxing;
        }
        
        public void setDayMasterWuxing(String dayMasterWuxing) {
            this.dayMasterWuxing = dayMasterWuxing;
        }
    }
    
    /**
     * 十神统计结果
     */
    public static class ShishenStats {
        private Map<String, Integer> shishenCounts;     // 十神统计
        private List<ShishenItem> shishenDetails;       // 十神详细信息
        private String dominantShishen;                 // 主导十神
        
        // Getter 和 Setter 方法
        public Map<String, Integer> getShishenCounts() {
            return shishenCounts;
        }
        
        public void setShishenCounts(Map<String, Integer> shishenCounts) {
            this.shishenCounts = shishenCounts;
        }
        
        public List<ShishenItem> getShishenDetails() {
            return shishenDetails;
        }
        
        public void setShishenDetails(List<ShishenItem> shishenDetails) {
            this.shishenDetails = shishenDetails;
        }
        
        public String getDominantShishen() {
            return dominantShishen;
        }
        
        public void setDominantShishen(String dominantShishen) {
            this.dominantShishen = dominantShishen;
        }
        
        /**
         * 十神项目详情
         */
        public static class ShishenItem {
            private String gan;         // 天干
            private String shishen;     // 十神
            private String position;    // 位置
            private String source;      // 来源
            
            public ShishenItem(String gan, String shishen, String position, String source) {
                this.gan = gan;
                this.shishen = shishen;
                this.position = position;
                this.source = source;
            }
            
            // Getter 和 Setter 方法
            public String getGan() {
                return gan;
            }
            
            public void setGan(String gan) {
                this.gan = gan;
            }
            
            public String getShishen() {
                return shishen;
            }
            
            public void setShishen(String shishen) {
                this.shishen = shishen;
            }
            
            public String getPosition() {
                return position;
            }
            
            public void setPosition(String position) {
                this.position = position;
            }
            
            public String getSource() {
                return source;
            }
            
            public void setSource(String source) {
                this.source = source;
            }
        }
    }
    
    /**
     * 十二宫统计结果
     */
    public static class ShierGongStats {
        private Map<String, String> ganZhiGong;         // 每个天干在各地支的十二宫状态
        private String dayMasterYuelingGong;            // 日主在月令的十二宫状态
        private Map<String, Integer> gongCounts;        // 各十二宫状态统计
        
        // Getter 和 Setter 方法
        public Map<String, String> getGanZhiGong() {
            return ganZhiGong;
        }
        
        public void setGanZhiGong(Map<String, String> ganZhiGong) {
            this.ganZhiGong = ganZhiGong;
        }
        
        public String getDayMasterYuelingGong() {
            return dayMasterYuelingGong;
        }
        
        public void setDayMasterYuelingGong(String dayMasterYuelingGong) {
            this.dayMasterYuelingGong = dayMasterYuelingGong;
        }
        
        public Map<String, Integer> getGongCounts() {
            return gongCounts;
        }
        
        public void setGongCounts(Map<String, Integer> gongCounts) {
            this.gongCounts = gongCounts;
        }
    }
    
    /**
     * 旺衰分析结果
     */
    public static class WangShuaiAnalysis {
        private String dayMasterStrength;       // 日主强弱（旺、中和、弱）
        private double strengthScore;           // 强度分数
        private String yuelingEffect;           // 月令影响
        private String analysis;                // 分析说明
        private List<String> supportFactors;    // 助力因素
        private List<String> weakenFactors;     // 削弱因素
        
        // Getter 和 Setter 方法
        public String getDayMasterStrength() {
            return dayMasterStrength;
        }
        
        public void setDayMasterStrength(String dayMasterStrength) {
            this.dayMasterStrength = dayMasterStrength;
        }
        
        public double getStrengthScore() {
            return strengthScore;
        }
        
        public void setStrengthScore(double strengthScore) {
            this.strengthScore = strengthScore;
        }
        
        public String getYuelingEffect() {
            return yuelingEffect;
        }
        
        public void setYuelingEffect(String yuelingEffect) {
            this.yuelingEffect = yuelingEffect;
        }
        
        public String getAnalysis() {
            return analysis;
        }
        
        public void setAnalysis(String analysis) {
            this.analysis = analysis;
        }
        
        public List<String> getSupportFactors() {
            return supportFactors;
        }
        
        public void setSupportFactors(List<String> supportFactors) {
            this.supportFactors = supportFactors;
        }
        
        public List<String> getWeakenFactors() {
            return weakenFactors;
        }
        
        public void setWeakenFactors(List<String> weakenFactors) {
            this.weakenFactors = weakenFactors;
        }
    }
}
