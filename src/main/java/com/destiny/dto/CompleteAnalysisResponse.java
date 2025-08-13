package com.destiny.dto;

import com.destiny.service.ShenshaService;
import com.destiny.service.ChongheService;
import com.destiny.service.LiunianService;

import java.util.List;

/**
 * 完整命理分析响应
 */
public class CompleteAnalysisResponse {
    
    // 基础信息
    private BaziResponse baziInfo;              // 八字信息
    private ZipingAnalysisResult zipingAnalysis; // 子平分析（五行、十神、十二宫、旺衰）
    
    // 神煞信息
    private ShenshaService.ShenshaResult shenshaResult; // 神煞分析
    
    // 空亡信息
    private List<String> kongWang;              // 空亡
    
    // 冲合关系
    private ChongheService.ChongheResult chongheResult; // 冲合关系
    
    // 大运信息
    private DayunResponse dayunInfo;            // 大运信息
    
    // 流年信息
    private LiunianService.LiunianResult liunianResult; // 流年分析
    
    // 综合分析
    private ComprehensiveAnalysis comprehensiveAnalysis; // 综合分析
    
    // getter and setter
    public BaziResponse getBaziInfo() { return baziInfo; }
    public void setBaziInfo(BaziResponse baziInfo) { this.baziInfo = baziInfo; }
    public ZipingAnalysisResult getZipingAnalysis() { return zipingAnalysis; }
    public void setZipingAnalysis(ZipingAnalysisResult zipingAnalysis) { this.zipingAnalysis = zipingAnalysis; }
    public ShenshaService.ShenshaResult getShenshaResult() { return shenshaResult; }
    public void setShenshaResult(ShenshaService.ShenshaResult shenshaResult) { this.shenshaResult = shenshaResult; }
    public List<String> getKongWang() { return kongWang; }
    public void setKongWang(List<String> kongWang) { this.kongWang = kongWang; }
    public ChongheService.ChongheResult getChongheResult() { return chongheResult; }
    public void setChongheResult(ChongheService.ChongheResult chongheResult) { this.chongheResult = chongheResult; }
    public DayunResponse getDayunInfo() { return dayunInfo; }
    public void setDayunInfo(DayunResponse dayunInfo) { this.dayunInfo = dayunInfo; }
    public LiunianService.LiunianResult getLiunianResult() { return liunianResult; }
    public void setLiunianResult(LiunianService.LiunianResult liunianResult) { this.liunianResult = liunianResult; }
    public ComprehensiveAnalysis getComprehensiveAnalysis() { return comprehensiveAnalysis; }
    public void setComprehensiveAnalysis(ComprehensiveAnalysis comprehensiveAnalysis) { this.comprehensiveAnalysis = comprehensiveAnalysis; }
    
    /**
     * 综合分析结果
     */
    public static class ComprehensiveAnalysis {
        private String personalityAnalysis;    // 性格分析
        private String careerAnalysis;         // 事业分析
        private String wealthAnalysis;         // 财运分析
        private String relationshipAnalysis;   // 感情分析
        private String healthAnalysis;         // 健康分析
        private String overallSummary;         // 总体评价
        private List<String> suggestions;      // 建议
        
        // getter and setter
        public String getPersonalityAnalysis() { return personalityAnalysis; }
        public void setPersonalityAnalysis(String personalityAnalysis) { this.personalityAnalysis = personalityAnalysis; }
        public String getCareerAnalysis() { return careerAnalysis; }
        public void setCareerAnalysis(String careerAnalysis) { this.careerAnalysis = careerAnalysis; }
        public String getWealthAnalysis() { return wealthAnalysis; }
        public void setWealthAnalysis(String wealthAnalysis) { this.wealthAnalysis = wealthAnalysis; }
        public String getRelationshipAnalysis() { return relationshipAnalysis; }
        public void setRelationshipAnalysis(String relationshipAnalysis) { this.relationshipAnalysis = relationshipAnalysis; }
        public String getHealthAnalysis() { return healthAnalysis; }
        public void setHealthAnalysis(String healthAnalysis) { this.healthAnalysis = healthAnalysis; }
        public String getOverallSummary() { return overallSummary; }
        public void setOverallSummary(String overallSummary) { this.overallSummary = overallSummary; }
        public List<String> getSuggestions() { return suggestions; }
        public void setSuggestions(List<String> suggestions) { this.suggestions = suggestions; }
    }
}