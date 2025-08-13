package com.destiny.service;

import com.destiny.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 完整命理分析服务
 * 整合所有命理分析功能
 */
@Service
public class CompleteAnalysisService {
    
    @Autowired
    private BaziService baziService;
    
    @Autowired
    private ZipingAnalysisService zipingAnalysisService;
    
    @Autowired
    private ShenshaService shenshaService;
    
    @Autowired
    private ChongheService chongheService;
    
    @Autowired
    private DayunService dayunService;
    
    @Autowired
    private LiunianService liunianService;
    
    /**
     * 执行完整的命理分析
     */
    public CompleteAnalysisResponse performCompleteAnalysis(CompleteAnalysisRequest request) {
        CompleteAnalysisResponse response = new CompleteAnalysisResponse();
        
        try {
            // 1. 计算基础八字信息
            BaziRequest baziRequest = convertToBaziRequest(request);
            BaziResponse baziInfo = baziService.calculateBazi(baziRequest);
            response.setBaziInfo(baziInfo);
            
            // 2. 执行子平术分析（五行、十神、十二宫、旺衰）
            ZipingAnalysisResult zipingAnalysis = zipingAnalysisService.analyzeZiping(baziInfo);
            response.setZipingAnalysis(zipingAnalysis);
            
            // 3. 分析神煞
            BaziDetail baziDetail = zipingAnalysis.getBaziDetail();
            ShenshaService.ShenshaResult shenshaResult = shenshaService.calculateShensha(baziDetail);
            response.setShenshaResult(shenshaResult);
            
            // 4. 分析冲合关系
            ChongheService.ChongheResult chongheResult = chongheService.calculateChonghe(baziDetail);
            response.setChongheResult(chongheResult);
            
            // 5. 计算大运
            DayunRequest dayunRequest = convertToDayunRequest(request);
            DayunResponse dayunInfo = dayunService.calculateDayun(dayunRequest);
            response.setDayunInfo(dayunInfo);
            
            // 6. 提取空亡信息
            response.setKongWang(dayunInfo.getKongWang());
            
            // 7. 计算流年
            int currentYear = LocalDate.now().getYear();
            int liunianYears = request.getLiunianYears() > 0 ? request.getLiunianYears() : 10;
            LiunianService.LiunianResult liunianResult = liunianService.calculateLiunian(
                baziDetail, currentYear, liunianYears);
            response.setLiunianResult(liunianResult);
            
            // 8. 生成综合分析
            CompleteAnalysisResponse.ComprehensiveAnalysis comprehensiveAnalysis = 
                generateComprehensiveAnalysis(response);
            response.setComprehensiveAnalysis(comprehensiveAnalysis);
            
            return response;
            
        } catch (Exception e) {
            throw new RuntimeException("执行完整命理分析时发生错误: " + e.getMessage(), e);
        }
    }
    
    /**
     * 转换为八字请求
     */
    private BaziRequest convertToBaziRequest(CompleteAnalysisRequest request) {
        BaziRequest baziRequest = new BaziRequest();
        baziRequest.setDate(request.getDate());
        baziRequest.setTime(request.getTime());
        baziRequest.setDateType(BaziRequest.DateType.valueOf(request.getDateType().name()));
        baziRequest.setLeapMonth(request.isLeapMonth());
        return baziRequest;
    }
    
    /**
     * 转换为大运请求
     */
    private DayunRequest convertToDayunRequest(CompleteAnalysisRequest request) {
        DayunRequest dayunRequest = new DayunRequest();
        dayunRequest.setDate(request.getDate());
        dayunRequest.setTime(request.getTime());
        dayunRequest.setDateType(DayunRequest.DateType.valueOf(request.getDateType().name()));
        dayunRequest.setLeapMonth(request.isLeapMonth());
        dayunRequest.setGender(DayunRequest.Gender.valueOf(request.getGender().name()));
        dayunRequest.setYears(80); // 默认分析80年大运
        return dayunRequest;
    }
    
    /**
     * 生成综合分析
     */
    private CompleteAnalysisResponse.ComprehensiveAnalysis generateComprehensiveAnalysis(
            CompleteAnalysisResponse response) {
        
        CompleteAnalysisResponse.ComprehensiveAnalysis analysis = 
            new CompleteAnalysisResponse.ComprehensiveAnalysis();
        
        BaziDetail baziDetail = response.getZipingAnalysis().getBaziDetail();
        ZipingAnalysisResult.WuxingStats wuxingStats = response.getZipingAnalysis().getWuxingStats();
        ZipingAnalysisResult.ShishenStats shishenStats = response.getZipingAnalysis().getShishenStats();
        ZipingAnalysisResult.WangShuaiAnalysis wangShuaiAnalysis = response.getZipingAnalysis().getWangShuaiAnalysis();
        
        // 性格分析
        analysis.setPersonalityAnalysis(analyzePersonality(baziDetail, wuxingStats, shishenStats, wangShuaiAnalysis));
        
        // 事业分析
        analysis.setCareerAnalysis(analyzeCareer(baziDetail, shishenStats, wangShuaiAnalysis));
        
        // 财运分析
        analysis.setWealthAnalysis(analyzeWealth(baziDetail, shishenStats, wangShuaiAnalysis));
        
        // 感情分析
        analysis.setRelationshipAnalysis(analyzeRelationship(baziDetail, shishenStats, response.getShenshaResult()));
        
        // 健康分析
        analysis.setHealthAnalysis(analyzeHealth(baziDetail, wuxingStats, response.getShenshaResult()));
        
        // 总体评价
        analysis.setOverallSummary(generateOverallSummary(response));
        
        // 生活建议
        analysis.setSuggestions(generateSuggestions(response));
        
        return analysis;
    }
    
    /**
     * 性格分析
     */
    private String analyzePersonality(BaziDetail baziDetail, ZipingAnalysisResult.WuxingStats wuxingStats,
                                    ZipingAnalysisResult.ShishenStats shishenStats,
                                    ZipingAnalysisResult.WangShuaiAnalysis wangShuaiAnalysis) {
        StringBuilder analysis = new StringBuilder();
        
        String dayGan = baziDetail.getDayGan();
        String dayMasterWuxing = wuxingStats.getDayMasterWuxing();
        String strength = wangShuaiAnalysis.getDayMasterStrength();
        String dominantShishen = shishenStats.getDominantShishen();
        
        // 根据日干分析基本性格
        analysis.append("日主").append(dayGan).append("(").append(dayMasterWuxing).append(")，");
        switch (dayMasterWuxing) {
            case "木":
                analysis.append("性格正直仁慈，富有同情心，具有向上发展的特性，");
                break;
            case "火":
                analysis.append("性格热情开朗，积极主动，具有礼貌文明的特性，");
                break;
            case "土":
                analysis.append("性格稳重踏实，诚实守信，具有包容宽厚的特性，");
                break;
            case "金":
                analysis.append("性格刚毅果断，重义守信，具有自律严格的特性，");
                break;
            case "水":
                analysis.append("性格灵活机智，善于应变，具有流动变化的特性，");
                break;
        }
        
        // 根据旺衰分析性格特点
        analysis.append("命局").append(strength).append("，");
        if (strength.contains("旺")) {
            analysis.append("个性较强，自信心足，行动力强，但有时可能过于自我；");
        } else if (strength.contains("弱")) {
            analysis.append("性格温和，善于合作，注重他人感受，但有时缺乏主见；");
        } else {
            analysis.append("性格平和，进退有度，既有主见又能听取建议；");
        }
        
        // 根据主导十神分析
        if (dominantShishen != null) {
            analysis.append("命中").append(dominantShishen).append("较多，");
            switch (dominantShishen) {
                case "比肩":
                case "劫财":
                    analysis.append("重视友情，善于合作，但有时过于依赖他人。");
                    break;
                case "食神":
                case "伤官":
                    analysis.append("才华横溢，表达能力强，追求自由和创新。");
                    break;
                case "正财":
                case "偏财":
                    analysis.append("重视物质生活，理财能力强，善于抓住机会。");
                    break;
                case "正官":
                case "七杀":
                    analysis.append("责任心强，有领导才能，但压力也较大。");
                    break;
                case "正印":
                case "偏印":
                    analysis.append("求知欲强，注重精神修养，有学者气质。");
                    break;
                default:
                    analysis.append("性格较为均衡。");
                    break;
            }
        }
        
        return analysis.toString();
    }
    
    /**
     * 事业分析
     */
    private String analyzeCareer(BaziDetail baziDetail, ZipingAnalysisResult.ShishenStats shishenStats,
                               ZipingAnalysisResult.WangShuaiAnalysis wangShuaiAnalysis) {
        StringBuilder analysis = new StringBuilder();
        
        String dominantShishen = shishenStats.getDominantShishen();
        String strength = wangShuaiAnalysis.getDayMasterStrength();
        
        analysis.append("事业运势方面，");
        
        if (strength.contains("旺")) {
            analysis.append("自身能力强，适合自主创业或担任领导职位，");
        } else if (strength.contains("弱")) {
            analysis.append("适合团队合作，在大机构中发展较为有利，");
        } else {
            analysis.append("适应能力强，可以胜任多种工作环境，");
        }
        
        if (dominantShishen != null) {
            switch (dominantShishen) {
                case "正官":
                    analysis.append("有管理才能，适合公务员、管理岗位等稳定职业。");
                    break;
                case "七杀":
                    analysis.append("有开拓精神，适合军警、体育、竞争性行业。");
                    break;
                case "正印":
                    analysis.append("有学习天赋，适合教育、文化、学术研究工作。");
                    break;
                case "偏印":
                    analysis.append("有创意才能，适合艺术、设计、技术开发工作。");
                    break;
                case "正财":
                    analysis.append("有理财能力，适合金融、贸易、实业经营。");
                    break;
                case "偏财":
                    analysis.append("有投资眼光，适合投资、投机、流动性行业。");
                    break;
                case "食神":
                    analysis.append("有服务精神，适合餐饮、娱乐、服务行业。");
                    break;
                case "伤官":
                    analysis.append("有表达能力，适合传媒、演艺、创意行业。");
                    break;
                case "比肩":
                case "劫财":
                    analysis.append("有团队精神，适合合伙经营、销售推广工作。");
                    break;
                default:
                    analysis.append("职业选择面广，可根据兴趣发展。");
                    break;
            }
        } else {
            analysis.append("需要综合考虑个人兴趣和能力来选择职业方向。");
        }
        
        return analysis.toString();
    }
    
    /**
     * 财运分析
     */
    private String analyzeWealth(BaziDetail baziDetail, ZipingAnalysisResult.ShishenStats shishenStats,
                               ZipingAnalysisResult.WangShuaiAnalysis wangShuaiAnalysis) {
        StringBuilder analysis = new StringBuilder();
        
        // 统计财星数量
        int zhengcaiCount = shishenStats.getShishenCounts().getOrDefault("正财", 0);
        int piancaiCount = shishenStats.getShishenCounts().getOrDefault("偏财", 0);
        int totalCaiCount = zhengcaiCount + piancaiCount;
        
        analysis.append("财运方面，");
        
        if (totalCaiCount == 0) {
            analysis.append("命中财星不现，需要通过努力工作获得收入，不宜投机取巧。");
        } else if (totalCaiCount == 1) {
            analysis.append("财星适中，财运稳定，通过正当途径可以获得不错的收入。");
        } else if (totalCaiCount >= 2) {
            String strength = wangShuaiAnalysis.getDayMasterStrength();
            if (strength.contains("旺")) {
                analysis.append("财星较多且身强，财运很好，有较强的求财能力和机会。");
            } else {
                analysis.append("财星较多但身弱，财多身弱，需要提升自身能力才能抓住财机。");
            }
        }
        
        if (zhengcaiCount > 0 && piancaiCount > 0) {
            analysis.append("正偏财并见，既有稳定收入，也有额外财源。");
        } else if (zhengcaiCount > 0) {
            analysis.append("以正财为主，收入稳定，但增长较慢。");
        } else if (piancaiCount > 0) {
            analysis.append("以偏财为主，收入波动较大，但有暴富机会。");
        }
        
        return analysis.toString();
    }
    
    /**
     * 感情分析
     */
    private String analyzeRelationship(BaziDetail baziDetail, ZipingAnalysisResult.ShishenStats shishenStats,
                                     ShenshaService.ShenshaResult shenshaResult) {
        StringBuilder analysis = new StringBuilder();
        
        analysis.append("感情运势方面，");
        
        // 检查桃花
        boolean hasTaohua = shenshaResult.getShenshaList().stream()
            .anyMatch(item -> item.getName().equals("桃花"));
        
        if (hasTaohua) {
            analysis.append("命带桃花，异性缘分较好，感情生活丰富，");
        }
        
        // 分析夫妻宫
        String dayZhi = baziDetail.getDayZhi();
        analysis.append("夫妻宫坐").append(dayZhi).append("，");
        
        // 检查日支与其他支的关系
        boolean hasChong = false;
        boolean hasHe = false;
        
        String[] zhis = {baziDetail.getYearZhi(), baziDetail.getMonthZhi(), baziDetail.getHourZhi()};
        for (String zhi : zhis) {
            if (isZhiChong(dayZhi, zhi)) {
                hasChong = true;
                break;
            }
            if (isZhiHe(dayZhi, zhi)) {
                hasHe = true;
                break;
            }
        }
        
        if (hasChong) {
            analysis.append("夫妻宫被冲，感情容易有波折，需要多沟通理解。");
        } else if (hasHe) {
            analysis.append("夫妻宫有合，感情和谐，夫妻关系融洽。");
        } else {
            analysis.append("夫妻宫平和，感情较为稳定。");
        }
        
        // 检查孤辰寡宿
        boolean hasGuchenGuasu = shenshaResult.getShenshaList().stream()
            .anyMatch(item -> item.getName().equals("孤辰") || item.getName().equals("寡宿"));
        
        if (hasGuchenGuasu) {
            analysis.append("命带孤辰寡宿，个人独立性强，但需要在感情中多付出关怀。");
        }
        
        return analysis.toString();
    }
    
    /**
     * 健康分析
     */
    private String analyzeHealth(BaziDetail baziDetail, ZipingAnalysisResult.WuxingStats wuxingStats,
                               ShenshaService.ShenshaResult shenshaResult) {
        StringBuilder analysis = new StringBuilder();
        
        analysis.append("健康方面，");
        
        // 检查五行平衡
        String strongestWuxing = wuxingStats.getStrongestWuxing();
        String weakestWuxing = wuxingStats.getWeakestWuxing();
        
        analysis.append("五行以").append(strongestWuxing).append("最旺，").append(weakestWuxing).append("最弱，");
        
        // 根据最弱五行给出健康建议
        switch (weakestWuxing) {
            case "木":
                analysis.append("木气不足，需注意肝胆、神经系统，宜多接触绿色环境。");
                break;
            case "火":
                analysis.append("火气不足，需注意心脏、血液循环，宜适当运动增强体质。");
                break;
            case "土":
                analysis.append("土气不足，需注意脾胃消化系统，宜规律饮食。");
                break;
            case "金":
                analysis.append("金气不足，需注意肺部、呼吸系统，宜保持空气清新。");
                break;
            case "水":
                analysis.append("水气不足，需注意肾脏、泌尿系统，宜多饮水适当休息。");
                break;
        }
        
        // 检查羊刃等凶神
        boolean hasYangre = shenshaResult.getShenshaList().stream()
            .anyMatch(item -> item.getName().equals("羊刃"));
        
        if (hasYangre) {
            analysis.append(" 命带羊刃，需注意外伤刀剑之害，行事宜谨慎。");
        }
        
        return analysis.toString();
    }
    
    /**
     * 生成总体评价
     */
    private String generateOverallSummary(CompleteAnalysisResponse response) {
        StringBuilder summary = new StringBuilder();
        
        ZipingAnalysisResult.WangShuaiAnalysis wangShuaiAnalysis = response.getZipingAnalysis().getWangShuaiAnalysis();
        String strength = wangShuaiAnalysis.getDayMasterStrength();
        
        summary.append("综合来看，此命").append(strength).append("，");
        
        int shenshaCount = response.getShenshaResult().getShenshaCount();
        int chongheCount = response.getChongheResult().getChongheCount();
        
        if (shenshaCount > 5) {
            summary.append("神煞较多，人生经历丰富多彩，");
        }
        
        if (chongheCount > 3) {
            summary.append("冲合关系复杂，人际关系变化较大，");
        }
        
        summary.append("需要根据大运流年的变化，适时调整人生策略，");
        summary.append("发挥优势，规避风险，方能获得更好的人生发展。");
        
        return summary.toString();
    }
    
    /**
     * 生成建议
     */
    private List<String> generateSuggestions(CompleteAnalysisResponse response) {
        List<String> suggestions = new ArrayList<>();
        
        ZipingAnalysisResult.WuxingStats wuxingStats = response.getZipingAnalysis().getWuxingStats();
        String weakestWuxing = wuxingStats.getWeakestWuxing();
        
        // 五行补救建议
        switch (weakestWuxing) {
            case "木":
                suggestions.add("五行缺木，宜多接触绿色，从事木相关行业，东方有利");
                break;
            case "火":
                suggestions.add("五行缺火，宜多用红色，从事火相关行业，南方有利");
                break;
            case "土":
                suggestions.add("五行缺土，宜多用黄色，从事土相关行业，中央有利");
                break;
            case "金":
                suggestions.add("五行缺金，宜多用白色，从事金相关行业，西方有利");
                break;
            case "水":
                suggestions.add("五行缺水，宜多用黑色，从事水相关行业，北方有利");
                break;
        }
        
        // 神煞相关建议
        ShenshaService.ShenshaResult shenshaResult = response.getShenshaResult();
        boolean hasGuiren = shenshaResult.getShenshaList().stream()
            .anyMatch(item -> item.getName().equals("天乙贵人"));
        
        if (hasGuiren) {
            suggestions.add("命带天乙贵人，遇事多求助于长辈贵人，可逢凶化吉");
        }
        
        // 冲合相关建议  
        ChongheService.ChongheResult chongheResult = response.getChongheResult();
        boolean hasChong = chongheResult.getChongheList().stream()
            .anyMatch(item -> item.getType().contains("冲"));
        
        if (hasChong) {
            suggestions.add("命中有冲，宜稳重行事，避免冲动决策，重大事项多商量");
        }
        
        // 流年建议
        suggestions.add("关注流年变化，在运势好的年份积极进取，运势低迷时以守为主");
        
        // 通用建议
        suggestions.add("保持积极心态，行善积德，自然福报增加");
        suggestions.add("加强学习修养，提升个人能力，以德配命");
        
        return suggestions;
    }
    
    // 辅助方法
    private boolean isZhiChong(String zhi1, String zhi2) {
        String[][] chongPairs = {
            {"子", "午"}, {"丑", "未"}, {"寅", "申"}, 
            {"卯", "酉"}, {"辰", "戌"}, {"巳", "亥"}
        };
        
        for (String[] pair : chongPairs) {
            if ((pair[0].equals(zhi1) && pair[1].equals(zhi2)) ||
                (pair[1].equals(zhi1) && pair[0].equals(zhi2))) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isZhiHe(String zhi1, String zhi2) {
        String[][] hePairs = {
            {"子", "丑"}, {"寅", "亥"}, {"卯", "戌"}, 
            {"辰", "酉"}, {"巳", "申"}, {"午", "未"}
        };
        
        for (String[] pair : hePairs) {
            if ((pair[0].equals(zhi1) && pair[1].equals(zhi2)) ||
                (pair[1].equals(zhi1) && pair[0].equals(zhi2))) {
                return true;
            }
        }
        return false;
    }
}