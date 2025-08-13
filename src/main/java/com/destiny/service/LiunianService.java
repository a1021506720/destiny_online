package com.destiny.service;

import com.destiny.dto.BaziDetail;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

/**
 * 流年计算服务
 * 实现流年运势分析和预测
 */
@Service
public class LiunianService {
    
    // 天干地支数组
    private static final String[] TIAN_GAN = {"甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸"};
    private static final String[] DI_ZHI = {"子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥"};
    
    // 地支对应的生肖
    private static final String[] SHENGXIAO = {"鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪"};
    
    /**
     * 计算流年运势
     */
    public LiunianResult calculateLiunian(BaziDetail baziDetail, int startYear, int years) {
        LiunianResult result = new LiunianResult();
        
        List<LiunianItem> liunianList = new ArrayList<>();
        
        for (int i = 0; i < years; i++) {
            int year = startYear + i;
            String liunianGanZhi = getLiunianGanZhi(year);
            String liunianGan = liunianGanZhi.substring(0, 1);
            String liunianZhi = liunianGanZhi.substring(1);
            String shengxiao = getShengxiao(liunianZhi);
            
            LiunianItem item = new LiunianItem();
            item.setYear(year);
            item.setGanZhi(liunianGanZhi);
            item.setShengxiao(shengxiao);
            
            // 分析与命主的关系
            LiunianAnalysis analysis = analyzeLiunianRelation(baziDetail, liunianGan, liunianZhi);
            item.setAnalysis(analysis);
            
            liunianList.add(item);
        }
        
        result.setLiunianList(liunianList);
        result.setStartYear(startYear);
        result.setYears(years);
        result.setSummary(generateLiunianSummary(liunianList, baziDetail));
        
        return result;
    }
    
    /**
     * 根据年份计算干支
     */
    private String getLiunianGanZhi(int year) {
        // 以1984年甲子年为基准
        int baseYear = 1984;
        int offset = year - baseYear;
        
        int ganIndex = (offset % 10 + 10) % 10;
        int zhiIndex = (offset % 12 + 12) % 12;
        
        return TIAN_GAN[ganIndex] + DI_ZHI[zhiIndex];
    }
    
    /**
     * 根据地支获取生肖
     */
    private String getShengxiao(String zhi) {
        for (int i = 0; i < DI_ZHI.length; i++) {
            if (DI_ZHI[i].equals(zhi)) {
                return SHENGXIAO[i];
            }
        }
        return "未知";
    }
    
    /**
     * 分析流年与命主的关系
     */
    private LiunianAnalysis analyzeLiunianRelation(BaziDetail baziDetail, String liunianGan, String liunianZhi) {
        LiunianAnalysis analysis = new LiunianAnalysis();
        
        String dayGan = baziDetail.getDayGan();
        String dayZhi = baziDetail.getDayZhi();
        
        // 1. 分析流年天干与日干的关系
        String ganRelation = analyzeGanRelation(dayGan, liunianGan);
        analysis.setGanRelation(ganRelation);
        
        // 2. 分析流年地支与日支的关系
        String zhiRelation = analyzeZhiRelation(dayZhi, liunianZhi);
        analysis.setZhiRelation(zhiRelation);
        
        // 3. 分析与命局整体的关系
        String overallRelation = analyzeOverallRelation(baziDetail, liunianGan, liunianZhi);
        analysis.setOverallRelation(overallRelation);
        
        // 4. 流年运势评级
        int score = calculateLiunianScore(baziDetail, liunianGan, liunianZhi);
        analysis.setLuckScore(score);
        analysis.setLuckLevel(getLuckLevel(score));
        
        // 5. 重点关注事项
        List<String> keyPoints = getKeyPoints(baziDetail, liunianGan, liunianZhi);
        analysis.setKeyPoints(keyPoints);
        
        return analysis;
    }
    
    /**
     * 分析天干关系
     */
    private String analyzeGanRelation(String dayGan, String liunianGan) {
        if (dayGan.equals(liunianGan)) {
            return "比肩年，同类相助，自主性强";
        }
        
        // 天干五合
        Map<String, String> wuhe = new HashMap<String, String>() {{
            put("甲", "己"); put("己", "甲");
            put("乙", "庚"); put("庚", "乙");
            put("丙", "辛"); put("辛", "丙");
            put("丁", "壬"); put("壬", "丁");
            put("戊", "癸"); put("癸", "戊");
        }};
        
        if (wuhe.containsKey(dayGan) && wuhe.get(dayGan).equals(liunianGan)) {
            return "天干合化年，主和谐融洽，感情丰富";
        }
        
        // 分析十神关系
        String shishen = calculateShishen(dayGan, liunianGan);
        return shishen + "年，" + getShishenDescription(shishen);
    }
    
    /**
     * 分析地支关系
     */
    private String analyzeZhiRelation(String dayZhi, String liunianZhi) {
        if (dayZhi.equals(liunianZhi)) {
            return "值年太岁，运势起伏较大";
        }
        
        // 地支冲合
        Map<String, String> liuchong = new HashMap<String, String>() {{
            put("子", "午"); put("午", "子");
            put("丑", "未"); put("未", "丑");
            put("寅", "申"); put("申", "寅");
            put("卯", "酉"); put("酉", "卯");
            put("辰", "戌"); put("戌", "辰");
            put("巳", "亥"); put("亥", "巳");
        }};
        
        if (liuchong.containsKey(dayZhi) && liuchong.get(dayZhi).equals(liunianZhi)) {
            return "太岁相冲，变动较大，需谨慎行事";
        }
        
        Map<String, String> liuhe = new HashMap<String, String>() {{
            put("子", "丑"); put("丑", "子");
            put("寅", "亥"); put("亥", "寅");
            put("卯", "戌"); put("戌", "卯");
            put("辰", "酉"); put("酉", "辰");
            put("巳", "申"); put("申", "巳");
            put("午", "未"); put("未", "午");
        }};
        
        if (liuhe.containsKey(dayZhi) && liuhe.get(dayZhi).equals(liunianZhi)) {
            return "太岁相合，运势和顺，利于合作";
        }
        
        return "与太岁关系平和";
    }
    
    /**
     * 分析与命局整体的关系
     */
    private String analyzeOverallRelation(BaziDetail baziDetail, String liunianGan, String liunianZhi) {
        // 简化分析：主要看流年干支对命局五行平衡的影响
        Map<String, String> ganWuxing = new HashMap<String, String>() {{
            put("甲", "木"); put("乙", "木");
            put("丙", "火"); put("丁", "火");
            put("戊", "土"); put("己", "土");
            put("庚", "金"); put("辛", "金");
            put("壬", "水"); put("癸", "水");
        }};
        
        String dayGanWuxing = ganWuxing.get(baziDetail.getDayGan());
        String liunianGanWuxing = ganWuxing.get(liunianGan);
        
        if (liunianGanWuxing.equals(dayGanWuxing)) {
            return "流年助身，精神饱满，行动力强";
        } else if (isShengRelation(liunianGanWuxing, dayGanWuxing)) {
            return "流年生身，得贵人助，运势向上";
        } else if (isKeRelation(liunianGanWuxing, dayGanWuxing)) {
            return "流年克身，压力较大，需低调行事";
        } else if (isShengRelation(dayGanWuxing, liunianGanWuxing)) {
            return "流年泄身，付出较多，注意保养";
        } else {
            return "流年耗身，消耗精神，量力而行";
        }
    }
    
    /**
     * 计算流年运势评分
     */
    private int calculateLiunianScore(BaziDetail baziDetail, String liunianGan, String liunianZhi) {
        int score = 50; // 基础分
        
        String dayGan = baziDetail.getDayGan();
        String dayZhi = baziDetail.getDayZhi();
        
        // 天干关系加分
        if (dayGan.equals(liunianGan)) {
            score += 10;
        } else {
            String shishen = calculateShishen(dayGan, liunianGan);
            score += getShishenScore(shishen);
        }
        
        // 地支关系加分
        if (dayZhi.equals(liunianZhi)) {
            score += 5; // 值年太岁，运势起伏
        } else if (isZhiHe(dayZhi, liunianZhi)) {
            score += 15;
        } else if (isZhiChong(dayZhi, liunianZhi)) {
            score -= 10;
        }
        
        // 确保分数在合理范围内
        return Math.max(0, Math.min(100, score));
    }
    
    /**
     * 根据分数获取运势等级
     */
    private String getLuckLevel(int score) {
        if (score >= 80) return "大吉";
        if (score >= 70) return "吉";
        if (score >= 60) return "中上";
        if (score >= 50) return "中平";
        if (score >= 40) return "中下";
        if (score >= 30) return "凶";
        return "大凶";
    }
    
    /**
     * 获取关键注意事项
     */
    private List<String> getKeyPoints(BaziDetail baziDetail, String liunianGan, String liunianZhi) {
        List<String> points = new ArrayList<>();
        
        String dayGan = baziDetail.getDayGan();
        String dayZhi = baziDetail.getDayZhi();
        
        // 根据天干关系给出建议
        String shishen = calculateShishen(dayGan, liunianGan);
        switch (shishen) {
            case "正财":
            case "偏财":
                points.add("财运年，适宜投资理财，但需谨慎");
                break;
            case "正官":
            case "七杀":
                points.add("官杀年，事业压力较大，需提升能力");
                break;
            case "正印":
            case "偏印":
                points.add("印星年，利于学习进修，贵人相助");
                break;
            case "食神":
            case "伤官":
                points.add("食伤年，创意丰富，表达能力强");
                break;
            case "比肩":
            case "劫财":
                points.add("比劫年，朋友助力，但需防破财");
                break;
        }
        
        // 根据地支关系给出建议
        if (isZhiChong(dayZhi, liunianZhi)) {
            points.add("太岁相冲，避免重大决策，宜守不宜攻");
        } else if (isZhiHe(dayZhi, liunianZhi)) {
            points.add("太岁相合，人际和谐，利于合作发展");
        }
        
        // 根据五行关系给出建议
        Map<String, String> ganWuxing = new HashMap<String, String>() {{
            put("甲", "木"); put("乙", "木");
            put("丙", "火"); put("丁", "火");
            put("戊", "土"); put("己", "土");
            put("庚", "金"); put("辛", "金");
            put("壬", "水"); put("癸", "水");
        }};
        
        String liunianWuxing = ganWuxing.get(liunianGan);
        switch (liunianWuxing) {
            case "木":
                points.add("木旺之年，利于发展创业，东方有利");
                break;
            case "火":
                points.add("火旺之年，热情高涨，南方有利");
                break;
            case "土":
                points.add("土旺之年，踏实稳重，中央有利");
                break;
            case "金":
                points.add("金旺之年，决断果敢，西方有利");
                break;
            case "水":
                points.add("水旺之年，智慧灵活，北方有利");
                break;
        }
        
        return points;
    }
    
    /**
     * 生成流年总结
     */
    private String generateLiunianSummary(List<LiunianItem> liunianList, BaziDetail baziDetail) {
        int totalYears = liunianList.size();
        int goodYears = 0;
        int averageYears = 0;
        int badYears = 0;
        
        for (LiunianItem item : liunianList) {
            int score = item.getAnalysis().getLuckScore();
            if (score >= 60) {
                goodYears++;
            } else if (score >= 40) {
                averageYears++;
            } else {
                badYears++;
            }
        }
        
        return String.format("未来%d年中，运势较好的年份有%d年，平稳年份有%d年，需要谨慎的年份有%d年。" +
                "建议在好运年份积极进取，平稳年份稳中求进，低迷年份以守为主。",
                totalYears, goodYears, averageYears, badYears);
    }
    
    // 辅助方法
    private String calculateShishen(String dayGan, String otherGan) {
        Map<String, String> ganWuxing = new HashMap<String, String>() {{
            put("甲", "木"); put("乙", "木");
            put("丙", "火"); put("丁", "火");
            put("戊", "土"); put("己", "土");
            put("庚", "金"); put("辛", "金");
            put("壬", "水"); put("癸", "水");
        }};
        
        Map<String, String> ganYinyang = new HashMap<String, String>() {{
            put("甲", "阳"); put("乙", "阴");
            put("丙", "阳"); put("丁", "阴");
            put("戊", "阳"); put("己", "阴");
            put("庚", "阳"); put("辛", "阴");
            put("壬", "阳"); put("癸", "阴");
        }};
        
        String dayWuxing = ganWuxing.get(dayGan);
        String dayYinyang = ganYinyang.get(dayGan);
        String otherWuxing = ganWuxing.get(otherGan);
        String otherYinyang = ganYinyang.get(otherGan);
        
        boolean sameYinyang = dayYinyang.equals(otherYinyang);
        
        if (dayWuxing.equals(otherWuxing)) {
            return sameYinyang ? "比肩" : "劫财";
        } else if (isShengRelation(otherWuxing, dayWuxing)) {
            return sameYinyang ? "正印" : "偏印";
        } else if (isShengRelation(dayWuxing, otherWuxing)) {
            return sameYinyang ? "伤官" : "食神";
        } else if (isKeRelation(otherWuxing, dayWuxing)) {
            return sameYinyang ? "七杀" : "正官";
        } else if (isKeRelation(dayWuxing, otherWuxing)) {
            return sameYinyang ? "偏财" : "正财";
        }
        
        return "未知";
    }
    
    private boolean isShengRelation(String from, String to) {
        Map<String, String> shengMap = new HashMap<String, String>() {{
            put("水", "木"); put("木", "火"); put("火", "土"); put("土", "金"); put("金", "水");
        }};
        return to.equals(shengMap.get(from));
    }
    
    private boolean isKeRelation(String from, String to) {
        Map<String, String> keMap = new HashMap<String, String>() {{
            put("水", "火"); put("火", "金"); put("金", "木"); put("木", "土"); put("土", "水");
        }};
        return to.equals(keMap.get(from));
    }
    
    private boolean isZhiHe(String zhi1, String zhi2) {
        Map<String, String> liuhe = new HashMap<String, String>() {{
            put("子", "丑"); put("丑", "子");
            put("寅", "亥"); put("亥", "寅");
            put("卯", "戌"); put("戌", "卯");
            put("辰", "酉"); put("酉", "辰");
            put("巳", "申"); put("申", "巳");
            put("午", "未"); put("未", "午");
        }};
        return liuhe.containsKey(zhi1) && liuhe.get(zhi1).equals(zhi2);
    }
    
    private boolean isZhiChong(String zhi1, String zhi2) {
        Map<String, String> liuchong = new HashMap<String, String>() {{
            put("子", "午"); put("午", "子");
            put("丑", "未"); put("未", "丑");
            put("寅", "申"); put("申", "寅");
            put("卯", "酉"); put("酉", "卯");
            put("辰", "戌"); put("戌", "辰");
            put("巳", "亥"); put("亥", "巳");
        }};
        return liuchong.containsKey(zhi1) && liuchong.get(zhi1).equals(zhi2);
    }
    
    private String getShishenDescription(String shishen) {
        switch (shishen) {
            case "比肩": return "自立性强，朋友助力";
            case "劫财": return "竞争激烈，需防破财";
            case "食神": return "才华展现，创意丰富";
            case "伤官": return "表达力强，变化较多";
            case "偏财": return "偏财机会，投机之年";
            case "正财": return "正财稳固，工作收入";
            case "七杀": return "压力较大，需要坚强";
            case "正官": return "责任加重，升迁之机";
            case "偏印": return "学习之年，偏门技艺";
            case "正印": return "贵人相助，文书利益";
            default: return "运势平稳";
        }
    }
    
    private int getShishenScore(String shishen) {
        switch (shishen) {
            case "正印": case "食神": return 15;
            case "正财": case "正官": return 10;
            case "比肩": case "偏印": return 5;
            case "偏财": case "伤官": return 0;
            case "劫财": case "七杀": return -5;
            default: return 0;
        }
    }
    
    /**
     * 流年结果类
     */
    public static class LiunianResult {
        private List<LiunianItem> liunianList;
        private int startYear;
        private int years;
        private String summary;
        
        // getter and setter
        public List<LiunianItem> getLiunianList() { return liunianList; }
        public void setLiunianList(List<LiunianItem> liunianList) { this.liunianList = liunianList; }
        public int getStartYear() { return startYear; }
        public void setStartYear(int startYear) { this.startYear = startYear; }
        public int getYears() { return years; }
        public void setYears(int years) { this.years = years; }
        public String getSummary() { return summary; }
        public void setSummary(String summary) { this.summary = summary; }
    }
    
    /**
     * 流年项目类
     */
    public static class LiunianItem {
        private int year;
        private String ganZhi;
        private String shengxiao;
        private LiunianAnalysis analysis;
        
        // getter and setter
        public int getYear() { return year; }
        public void setYear(int year) { this.year = year; }
        public String getGanZhi() { return ganZhi; }
        public void setGanZhi(String ganZhi) { this.ganZhi = ganZhi; }
        public String getShengxiao() { return shengxiao; }
        public void setShengxiao(String shengxiao) { this.shengxiao = shengxiao; }
        public LiunianAnalysis getAnalysis() { return analysis; }
        public void setAnalysis(LiunianAnalysis analysis) { this.analysis = analysis; }
    }
    
    /**
     * 流年分析类
     */
    public static class LiunianAnalysis {
        private String ganRelation;      // 天干关系
        private String zhiRelation;      // 地支关系
        private String overallRelation;  // 整体关系
        private int luckScore;           // 运势评分
        private String luckLevel;        // 运势等级
        private List<String> keyPoints;  // 关键点
        
        // getter and setter
        public String getGanRelation() { return ganRelation; }
        public void setGanRelation(String ganRelation) { this.ganRelation = ganRelation; }
        public String getZhiRelation() { return zhiRelation; }
        public void setZhiRelation(String zhiRelation) { this.zhiRelation = zhiRelation; }
        public String getOverallRelation() { return overallRelation; }
        public void setOverallRelation(String overallRelation) { this.overallRelation = overallRelation; }
        public int getLuckScore() { return luckScore; }
        public void setLuckScore(int luckScore) { this.luckScore = luckScore; }
        public String getLuckLevel() { return luckLevel; }
        public void setLuckLevel(String luckLevel) { this.luckLevel = luckLevel; }
        public List<String> getKeyPoints() { return keyPoints; }
        public void setKeyPoints(List<String> keyPoints) { this.keyPoints = keyPoints; }
    }
}