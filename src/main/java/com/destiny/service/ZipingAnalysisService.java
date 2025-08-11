package com.destiny.service;

import com.destiny.dto.BaziResponse;
import com.destiny.constants.ZipingConstants;
import com.destiny.dto.BaziDetail;
import com.destiny.dto.ZipingAnalysisResult;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 子平术分析核心服务
 * 实现完整的子平术分析功能
 */
@Service
public class ZipingAnalysisService {
    
    /**
     * 执行完整的子平术分析
     */
    public ZipingAnalysisResult analyzeZiping(BaziResponse baziInfo) {
        ZipingAnalysisResult result = new ZipingAnalysisResult();
        
        // 1. 提取和解析八字详细信息
        BaziDetail baziDetail = extractBaziDetail(baziInfo);
        result.setBaziDetail(baziDetail);
        
        // 2. 五行统计分析
        ZipingAnalysisResult.WuxingStats wuxingStats = analyzeWuxing(baziDetail);
        result.setWuxingStats(wuxingStats);
        
        // 3. 十神分析
        ZipingAnalysisResult.ShishenStats shishenStats = analyzeShishen(baziDetail);
        result.setShishenStats(shishenStats);
        
        // 4. 十二宫分析
        ZipingAnalysisResult.ShierGongStats shierGongStats = analyzeShierGong(baziDetail);
        result.setShierGongStats(shierGongStats);
        
        // 5. 旺衰分析
        ZipingAnalysisResult.WangShuaiAnalysis wangShuaiAnalysis = analyzeWangShuai(baziDetail, shierGongStats);
        result.setWangShuaiAnalysis(wangShuaiAnalysis);
        
        return result;
    }
    
    /**
     * 提取八字详细信息
     */
    private BaziDetail extractBaziDetail(BaziResponse baziInfo) {
        BaziDetail detail = new BaziDetail();
        
        // 解析四柱干支
        String[] pillars = baziInfo.getBazi().split(" ");
        
        // 年柱
        detail.setYearGan(pillars[0].substring(0, 1));
        detail.setYearZhi(pillars[0].substring(1));
        
        // 月柱
        detail.setMonthGan(pillars[1].substring(0, 1));
        detail.setMonthZhi(pillars[1].substring(1));
        
        // 日柱
        detail.setDayGan(pillars[2].substring(0, 1));
        detail.setDayZhi(pillars[2].substring(1));
        
        // 时柱
        detail.setHourGan(pillars[3].substring(0, 1));
        detail.setHourZhi(pillars[3].substring(1));
        
        // 分析地支藏干
        Map<String, List<String>> zhiZangGan = analyzeZhiZangGan(detail);
        detail.setZhiZangGan(zhiZangGan);
        
        // 扁平化所有干支（包含权重）
        List<BaziDetail.GanZhiItem> allGanZhi = flattenAllGanZhi(detail);
        detail.setAllGanZhi(allGanZhi);
        
        return detail;
    }
    
    /**
     * 分析地支藏干
     */
    private Map<String, List<String>> analyzeZhiZangGan(BaziDetail detail) {
        Map<String, List<String>> zhiZangGan = new HashMap<>();
        
        // 四个地支的藏干
        String[] zhis = {detail.getYearZhi(), detail.getMonthZhi(), detail.getDayZhi(), detail.getHourZhi()};
        
        for (String zhi : zhis) {
            zhiZangGan.put(zhi, ZipingConstants.ZHI_ZANGGAN.get(zhi));
        }
        
        return zhiZangGan;
    }
    
    /**
     * 扁平化所有干支（包含权重信息）
     */
    private List<BaziDetail.GanZhiItem> flattenAllGanZhi(BaziDetail detail) {
        List<BaziDetail.GanZhiItem> allGanZhi = new ArrayList<>();
        
        // 添加天干（权重1.0）
        addGanZhiItem(allGanZhi, detail.getYearGan(), "年干", ZipingConstants.TIANGAN_WEIGHT, "年");
        addGanZhiItem(allGanZhi, detail.getMonthGan(), "月干", ZipingConstants.TIANGAN_WEIGHT, "月");
        addGanZhiItem(allGanZhi, detail.getDayGan(), "日干", ZipingConstants.TIANGAN_WEIGHT, "日");
        addGanZhiItem(allGanZhi, detail.getHourGan(), "时干", ZipingConstants.TIANGAN_WEIGHT, "时");
        
        // 添加地支藏干（根据权重）
        addZhiZangGanItems(allGanZhi, detail.getYearZhi(), "年", detail.getMonthZhi());
        addZhiZangGanItems(allGanZhi, detail.getMonthZhi(), "月", detail.getMonthZhi());
        addZhiZangGanItems(allGanZhi, detail.getDayZhi(), "日", detail.getMonthZhi());
        addZhiZangGanItems(allGanZhi, detail.getHourZhi(), "时", detail.getMonthZhi());
        
        return allGanZhi;
    }
    
    /**
     * 添加干支项目
     */
    private void addGanZhiItem(List<BaziDetail.GanZhiItem> list, String gan, String source, double weight, String position) {
        String wuxing = ZipingConstants.GAN_WUXING.get(gan);
        String yinyang = ZipingConstants.GAN_YINYANG.get(gan);
        list.add(new BaziDetail.GanZhiItem(gan, wuxing, yinyang, source, weight, position));
    }
    
    /**
     * 添加地支藏干项目
     */
    private void addZhiZangGanItems(List<BaziDetail.GanZhiItem> list, String zhi, String position, String monthZhi) {
        List<String> zangGans = ZipingConstants.ZHI_ZANGGAN.get(zhi);
        Double[] weights = ZipingConstants.ZANGGAN_WEIGHTS.get(zhi);
        
        for (int i = 0; i < zangGans.size(); i++) {
            String gan = zangGans.get(i);
            double weight = weights[i];
            
            // 如果是月支，权重加倍
            if (zhi.equals(monthZhi)) {
                weight *= ZipingConstants.YUELING_WEIGHT;
            }
            
            String wuxing = ZipingConstants.GAN_WUXING.get(gan);
            String yinyang = ZipingConstants.GAN_YINYANG.get(gan);
            String source = position + "支藏干";
            
            list.add(new BaziDetail.GanZhiItem(gan, wuxing, yinyang, source, weight, position));
        }
    }
    
    /**
     * 五行统计分析
     */
    private ZipingAnalysisResult.WuxingStats analyzeWuxing(BaziDetail detail) {
        ZipingAnalysisResult.WuxingStats stats = new ZipingAnalysisResult.WuxingStats();
        
        // 统计五行（考虑权重）
        Map<String, Double> wuxingCounts = new HashMap<>();
        Map<String, Integer> rawCounts = new HashMap<>();
        
        // 初始化
        Arrays.asList("木", "火", "土", "金", "水").forEach(wx -> {
            wuxingCounts.put(wx, 0.0);
            rawCounts.put(wx, 0);
        });
        
        // 统计
        for (BaziDetail.GanZhiItem item : detail.getAllGanZhi()) {
            String wuxing = item.getWuxing();
            wuxingCounts.put(wuxing, wuxingCounts.get(wuxing) + item.getWeight());
            rawCounts.put(wuxing, rawCounts.get(wuxing) + 1);
        }
        
        stats.setWuxingCounts(wuxingCounts);
        stats.setRawCounts(rawCounts);
        
        // 找出最强和最弱五行
        String strongestWuxing = Collections.max(wuxingCounts.entrySet(), Map.Entry.comparingByValue()).getKey();
        String weakestWuxing = Collections.min(wuxingCounts.entrySet(), Map.Entry.comparingByValue()).getKey();
        
        stats.setStrongestWuxing(strongestWuxing);
        stats.setWeakestWuxing(weakestWuxing);
        stats.setDayMasterWuxing(ZipingConstants.GAN_WUXING.get(detail.getDayGan()));
        
        return stats;
    }
    
    /**
     * 十神分析
     */
    private ZipingAnalysisResult.ShishenStats analyzeShishen(BaziDetail detail) {
        ZipingAnalysisResult.ShishenStats stats = new ZipingAnalysisResult.ShishenStats();
        
        String dayGan = detail.getDayGan();
        Map<String, Integer> shishenCounts = new HashMap<>();
        List<ZipingAnalysisResult.ShishenStats.ShishenItem> shishenDetails = new ArrayList<>();
        
        // 统计所有干支的十神
        for (BaziDetail.GanZhiItem item : detail.getAllGanZhi()) {
            if (!item.getGan().equals(dayGan)) { // 排除日干自身
                String shishen = calculateShishen(dayGan, item.getGan());
                shishenCounts.put(shishen, shishenCounts.getOrDefault(shishen, 0) + 1);
                shishenDetails.add(new ZipingAnalysisResult.ShishenStats.ShishenItem(
                    item.getGan(), shishen, item.getPosition(), item.getSource()
                ));
            }
        }
        
        stats.setShishenCounts(shishenCounts);
        stats.setShishenDetails(shishenDetails);
        
        // 找出主导十神
        if (!shishenCounts.isEmpty()) {
            String dominantShishen = Collections.max(shishenCounts.entrySet(), Map.Entry.comparingByValue()).getKey();
            stats.setDominantShishen(dominantShishen);
        }
        
        return stats;
    }
    
    /**
     * 计算十神关系
     */
    private String calculateShishen(String dayGan, String otherGan) {
        String dayWuxing = ZipingConstants.GAN_WUXING.get(dayGan);
        String dayYinyang = ZipingConstants.GAN_YINYANG.get(dayGan);
        String otherWuxing = ZipingConstants.GAN_WUXING.get(otherGan);
        String otherYinyang = ZipingConstants.GAN_YINYANG.get(otherGan);
        
        boolean sameYinyang = dayYinyang.equals(otherYinyang);
        
        if (dayWuxing.equals(otherWuxing)) {
            // 同类
            return sameYinyang ? "比肩" : "劫财";
        } else if (isShengWo(otherWuxing, dayWuxing)) {
            // 生我者
            return sameYinyang ? "正印" : "偏印";
        } else if (isShengWo(dayWuxing, otherWuxing)) {
            // 我生者
            return sameYinyang ? "伤官" : "食神";
        } else if (isKeWo(otherWuxing, dayWuxing)) {
            // 克我者
            return sameYinyang ? "七杀" : "正官";
        } else if (isKeWo(dayWuxing, otherWuxing)) {
            // 我克者
            return sameYinyang ? "偏财" : "正财";
        }
        
        return "未知";
    }
    
    /**
     * 判断五行相生关系
     */
    private boolean isShengWo(String from, String to) {
        Map<String, String> shengMap = new HashMap<String, String>() {{
            put("水", "木"); put("木", "火"); put("火", "土"); put("土", "金"); put("金", "水");
        }};
        return to.equals(shengMap.get(from));
    }
    
    /**
     * 判断五行相克关系
     */
    private boolean isKeWo(String from, String to) {
        Map<String, String> keMap = new HashMap<String, String>() {{
            put("水", "火"); put("火", "金"); put("金", "木"); put("木", "土"); put("土", "水");
        }};
        return to.equals(keMap.get(from));
    }
    
    /**
     * 十二宫分析
     */
    private ZipingAnalysisResult.ShierGongStats analyzeShierGong(BaziDetail detail) {
        ZipingAnalysisResult.ShierGongStats stats = new ZipingAnalysisResult.ShierGongStats();
        
        Map<String, String> ganZhiGong = new HashMap<>();
        Map<String, Integer> gongCounts = new HashMap<>();
        
        // 分析日主在各地支的十二宫状态
        String dayGan = detail.getDayGan();
        String[] zhis = {detail.getYearZhi(), detail.getMonthZhi(), detail.getDayZhi(), detail.getHourZhi()};
        String[] positions = {"年", "月", "日", "时"};
        
        for (int i = 0; i < zhis.length; i++) {
            String gong = getShierGong(dayGan, zhis[i]);
            ganZhiGong.put(positions[i] + "支", gong);
            gongCounts.put(gong, gongCounts.getOrDefault(gong, 0) + 1);
        }
        
        stats.setGanZhiGong(ganZhiGong);
        stats.setGongCounts(gongCounts);
        
        // 日主在月令的十二宫状态（最重要）
        String dayMasterYuelingGong = getShierGong(dayGan, detail.getMonthZhi());
        stats.setDayMasterYuelingGong(dayMasterYuelingGong);
        
        return stats;
    }
    
    /**
     * 获取天干在地支的十二宫状态
     */
    private String getShierGong(String gan, String zhi) {
        String yinyang = ZipingConstants.GAN_YINYANG.get(gan);
        
        if ("阳".equals(yinyang)) {
            List<String> gongOrder = ZipingConstants.YANG_GAN_SHIERGONG.get(gan);
            if (gongOrder != null) {
                int index = gongOrder.indexOf(zhi);
                if (index != -1) {
                    return ZipingConstants.SHIERGONG_NAMES[index];
                }
            }
        } else {
            List<String> gongOrder = ZipingConstants.YIN_GAN_SHIERGONG.get(gan);
            if (gongOrder != null) {
                int index = gongOrder.indexOf(zhi);
                if (index != -1) {
                    return ZipingConstants.SHIERGONG_NAMES[index];
                }
            }
        }
        
        return "未知";
    }
    
    /**
     * 旺衰分析
     */
    private ZipingAnalysisResult.WangShuaiAnalysis analyzeWangShuai(BaziDetail detail, ZipingAnalysisResult.ShierGongStats shierGongStats) {
        ZipingAnalysisResult.WangShuaiAnalysis analysis = new ZipingAnalysisResult.WangShuaiAnalysis();
        
        String dayGan = detail.getDayGan();
        String dayWuxing = ZipingConstants.GAN_WUXING.get(dayGan);
        String yuelingGong = shierGongStats.getDayMasterYuelingGong();
        
        // 基础分数
        double strengthScore = 0.0;
        List<String> supportFactors = new ArrayList<>();
        List<String> weakenFactors = new ArrayList<>();
        
        // 月令影响（最重要）
        double yuelingScore = getGongStrengthScore(yuelingGong) * 3.0; // 月令权重最大
        strengthScore += yuelingScore;
        
        if (yuelingScore > 0) {
            supportFactors.add("月令" + yuelingGong + "助力日主");
        } else {
            weakenFactors.add("月令" + yuelingGong + "削弱日主");
        }
        
        // 统计同类五行（生助日主的）
        for (BaziDetail.GanZhiItem item : detail.getAllGanZhi()) {
            if (!item.getGan().equals(dayGan)) {
                String itemWuxing = item.getWuxing();
                if (itemWuxing.equals(dayWuxing) || isShengWo(itemWuxing, dayWuxing)) {
                    strengthScore += item.getWeight();
                    supportFactors.add(item.getPosition() + item.getSource() + item.getGan() + "(" + itemWuxing + ")助力");
                } else if (isKeWo(itemWuxing, dayWuxing) || isShengWo(dayWuxing, itemWuxing)) {
                    strengthScore -= item.getWeight();
                    weakenFactors.add(item.getPosition() + item.getSource() + item.getGan() + "(" + itemWuxing + ")削弱");
                }
            }
        }
        
        analysis.setStrengthScore(strengthScore);
        analysis.setSupportFactors(supportFactors);
        analysis.setWeakenFactors(weakenFactors);
        analysis.setYuelingEffect("日主在月令" + yuelingGong + "，" + getGongDescription(yuelingGong));
        
        // 判断强弱
        String strength;
        if (strengthScore >= 2.0) {
            strength = "偏旺";
        } else if (strengthScore >= 0.5) {
            strength = "中和偏旺";
        } else if (strengthScore >= -0.5) {
            strength = "中和";
        } else if (strengthScore >= -2.0) {
            strength = "中和偏弱";
        } else {
            strength = "偏弱";
        }
        
        analysis.setDayMasterStrength(strength);
        analysis.setAnalysis(String.format("日主%s在四柱中%s，综合评分%.2f", 
            dayGan + "(" + dayWuxing + ")", strength, strengthScore));
        
        return analysis;
    }
    
    /**
     * 获取十二宫强度分数
     */
    private double getGongStrengthScore(String gong) {
        switch (gong) {
            case "长生": return 0.5;
            case "沐浴": return -0.3;
            case "冠带": return 0.2;
            case "临官": return 0.8;
            case "帝旺": return 1.0;
            case "衰": return -0.5;
            case "病": return -0.8;
            case "死": return -1.0;
            case "墓": return -0.3;
            case "绝": return -1.2;
            case "胎": return 0.1;
            case "养": return 0.3;
            default: return 0.0;
        }
    }
    
    /**
     * 获取十二宫描述
     */
    private String getGongDescription(String gong) {
        switch (gong) {
            case "长生": return "得生助力";
            case "沐浴": return "不稳定";
            case "冠带": return "渐强";
            case "临官": return "较强";
            case "帝旺": return "最强";
            case "衰": return "渐弱";
            case "病": return "较弱";
            case "死": return "很弱";
            case "墓": return "收藏";
            case "绝": return "最弱";
            case "胎": return "初生";
            case "养": return "养育";
            default: return "未知";
        }
    }
}
