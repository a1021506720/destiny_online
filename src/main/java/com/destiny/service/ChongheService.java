package com.destiny.service;

import com.destiny.dto.BaziDetail;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 冲合关系计算服务
 * 实现天干地支的冲合刑害关系分析
 */
@Service
public class ChongheService {
    
    // 天干五合
    private static final Map<String, String> TIANGAN_WUHE = new HashMap<String, String>() {{
        put("甲", "己"); put("己", "甲");
        put("乙", "庚"); put("庚", "乙");
        put("丙", "辛"); put("辛", "丙");
        put("丁", "壬"); put("壬", "丁");
        put("戊", "癸"); put("癸", "戊");
    }};
    
    // 地支六冲
    private static final Map<String, String> DIZHI_LIUCHONG = new HashMap<String, String>() {{
        put("子", "午"); put("午", "子");
        put("丑", "未"); put("未", "丑");
        put("寅", "申"); put("申", "寅");
        put("卯", "酉"); put("酉", "卯");
        put("辰", "戌"); put("戌", "辰");
        put("巳", "亥"); put("亥", "巳");
    }};
    
    // 地支六合
    private static final Map<String, String> DIZHI_LIUHE = new HashMap<String, String>() {{
        put("子", "丑"); put("丑", "子");
        put("寅", "亥"); put("亥", "寅");
        put("卯", "戌"); put("戌", "卯");
        put("辰", "酉"); put("酉", "辰");
        put("巳", "申"); put("申", "巳");
        put("午", "未"); put("未", "午");
    }};
    
    // 地支三合
    private static final String[][] DIZHI_SANHE = {
        {"申", "子", "辰"}, // 申子辰三合水局
        {"亥", "卯", "未"}, // 亥卯未三合木局
        {"寅", "午", "戌"}, // 寅午戌三合火局
        {"巳", "酉", "丑"}  // 巳酉丑三合金局
    };
    
    // 地支三会
    private static final String[][] DIZHI_SANHUI = {
        {"寅", "卯", "辰"}, // 寅卯辰三会东方木
        {"巳", "午", "未"}, // 巳午未三会南方火
        {"申", "酉", "戌"}, // 申酉戌三会西方金
        {"亥", "子", "丑"}  // 亥子丑三会北方水
    };
    
    // 地支相刑
    private static final String[][] DIZHI_XINGFA = {
        {"子", "卯"},           // 子卯相刑（无礼之刑）
        {"寅", "巳", "申"},     // 寅巳申三刑（恩将仇报之刑）
        {"丑", "未", "戌"},     // 丑未戌三刑（持势之刑）
        {"辰", "辰"},           // 辰辰自刑
        {"午", "午"},           // 午午自刑
        {"酉", "酉"},           // 酉酉自刑
        {"亥", "亥"}            // 亥亥自刑
    };
    
    // 地支相害
    private static final Map<String, String> DIZHI_XIANGHAI = new HashMap<String, String>() {{
        put("子", "未"); put("未", "子");
        put("丑", "午"); put("午", "丑");
        put("寅", "巳"); put("巳", "寅");
        put("卯", "辰"); put("辰", "卯");
        put("申", "亥"); put("亥", "申");
        put("酉", "戌"); put("戌", "酉");
    }};
    
    /**
     * 计算所有冲合关系
     */
    public ChongheResult calculateChonghe(BaziDetail baziDetail) {
        ChongheResult result = new ChongheResult();
        
        List<ChongheItem> chongheList = new ArrayList<>();
        
        // 1. 天干五合
        addTianganWuhe(chongheList, baziDetail);
        
        // 2. 地支六冲
        addDizhiLiuchong(chongheList, baziDetail);
        
        // 3. 地支六合
        addDizhiLiuhe(chongheList, baziDetail);
        
        // 4. 地支三合
        addDizhiSanhe(chongheList, baziDetail);
        
        // 5. 地支三会
        addDizhiSanhui(chongheList, baziDetail);
        
        // 6. 地支相刑
        addDizhiXingfa(chongheList, baziDetail);
        
        // 7. 地支相害
        addDizhiXianghai(chongheList, baziDetail);
        
        result.setChongheList(chongheList);
        result.setChongheCount(chongheList.size());
        
        // 统计类型
        Map<String, Integer> typeCount = new HashMap<>();
        for (ChongheItem item : chongheList) {
            typeCount.put(item.getType(), typeCount.getOrDefault(item.getType(), 0) + 1);
        }
        result.setTypeCount(typeCount);
        
        // 分析影响
        result.setAnalysis(analyzeChongheInfluence(chongheList));
        
        return result;
    }
    
    /**
     * 添加天干五合
     */
    private void addTianganWuhe(List<ChongheItem> list, BaziDetail baziDetail) {
        String[] gans = {baziDetail.getYearGan(), baziDetail.getMonthGan(), 
                        baziDetail.getDayGan(), baziDetail.getHourGan()};
        String[] positions = {"年干", "月干", "日干", "时干"};
        
        for (int i = 0; i < gans.length; i++) {
            for (int j = i + 1; j < gans.length; j++) {
                String gan1 = gans[i];
                String gan2 = gans[j];
                
                if (TIANGAN_WUHE.containsKey(gan1) && TIANGAN_WUHE.get(gan1).equals(gan2)) {
                    String wuxing = getTianganHeWuxing(gan1, gan2);
                    list.add(new ChongheItem(
                        "天干五合",
                        gan1 + gan2 + "合" + wuxing,
                        positions[i] + "与" + positions[j],
                        "主和谐融洽，" + wuxing + "气旺盛"
                    ));
                }
            }
        }
    }
    
    /**
     * 添加地支六冲
     */
    private void addDizhiLiuchong(List<ChongheItem> list, BaziDetail baziDetail) {
        String[] zhis = {baziDetail.getYearZhi(), baziDetail.getMonthZhi(), 
                        baziDetail.getDayZhi(), baziDetail.getHourZhi()};
        String[] positions = {"年支", "月支", "日支", "时支"};
        
        for (int i = 0; i < zhis.length; i++) {
            for (int j = i + 1; j < zhis.length; j++) {
                String zhi1 = zhis[i];
                String zhi2 = zhis[j];
                
                if (DIZHI_LIUCHONG.containsKey(zhi1) && DIZHI_LIUCHONG.get(zhi1).equals(zhi2)) {
                    String influence = getDizhiChongInfluence(positions[i], positions[j]);
                    list.add(new ChongheItem(
                        "地支六冲",
                        zhi1 + zhi2 + "冲",
                        positions[i] + "与" + positions[j],
                        influence
                    ));
                }
            }
        }
    }
    
    /**
     * 添加地支六合
     */
    private void addDizhiLiuhe(List<ChongheItem> list, BaziDetail baziDetail) {
        String[] zhis = {baziDetail.getYearZhi(), baziDetail.getMonthZhi(), 
                        baziDetail.getDayZhi(), baziDetail.getHourZhi()};
        String[] positions = {"年支", "月支", "日支", "时支"};
        
        for (int i = 0; i < zhis.length; i++) {
            for (int j = i + 1; j < zhis.length; j++) {
                String zhi1 = zhis[i];
                String zhi2 = zhis[j];
                
                if (DIZHI_LIUHE.containsKey(zhi1) && DIZHI_LIUHE.get(zhi1).equals(zhi2)) {
                    String wuxing = getDizhiHeWuxing(zhi1, zhi2);
                    list.add(new ChongheItem(
                        "地支六合",
                        zhi1 + zhi2 + "合" + wuxing,
                        positions[i] + "与" + positions[j],
                        "主亲密和谐，" + wuxing + "气增强"
                    ));
                }
            }
        }
    }
    
    /**
     * 添加地支三合
     */
    private void addDizhiSanhe(List<ChongheItem> list, BaziDetail baziDetail) {
        String[] zhis = {baziDetail.getYearZhi(), baziDetail.getMonthZhi(), 
                        baziDetail.getDayZhi(), baziDetail.getHourZhi()};
        String[] positions = {"年支", "月支", "日支", "时支"};
        
        for (String[] sanhe : DIZHI_SANHE) {
            List<Integer> foundIndexes = new ArrayList<>();
            List<String> foundPositions = new ArrayList<>();
            
            for (String zhi : sanhe) {
                for (int i = 0; i < zhis.length; i++) {
                    if (zhis[i].equals(zhi)) {
                        foundIndexes.add(i);
                        foundPositions.add(positions[i]);
                        break;
                    }
                }
            }
            
            if (foundIndexes.size() >= 2) {
                String wuxing = getSanheWuxing(sanhe);
                String description = foundIndexes.size() == 3 ? "三合成局" : "半三合";
                list.add(new ChongheItem(
                    "地支三合",
                    Arrays.toString(sanhe) + description + wuxing + "局",
                    String.join("、", foundPositions),
                    description + "，" + wuxing + "气强旺"
                ));
            }
        }
    }
    
    /**
     * 添加地支三会
     */
    private void addDizhiSanhui(List<ChongheItem> list, BaziDetail baziDetail) {
        String[] zhis = {baziDetail.getYearZhi(), baziDetail.getMonthZhi(), 
                        baziDetail.getDayZhi(), baziDetail.getHourZhi()};
        String[] positions = {"年支", "月支", "日支", "时支"};
        
        for (String[] sanhui : DIZHI_SANHUI) {
            List<Integer> foundIndexes = new ArrayList<>();
            List<String> foundPositions = new ArrayList<>();
            
            for (String zhi : sanhui) {
                for (int i = 0; i < zhis.length; i++) {
                    if (zhis[i].equals(zhi)) {
                        foundIndexes.add(i);
                        foundPositions.add(positions[i]);
                        break;
                    }
                }
            }
            
            if (foundIndexes.size() >= 2) {
                String fangwei = getSanhuiFangwei(sanhui);
                String description = foundIndexes.size() == 3 ? "三会成局" : "半三会";
                list.add(new ChongheItem(
                    "地支三会",
                    Arrays.toString(sanhui) + description + fangwei,
                    String.join("、", foundPositions),
                    description + "，" + fangwei + "气势强"
                ));
            }
        }
    }
    
    /**
     * 添加地支相刑
     */
    private void addDizhiXingfa(List<ChongheItem> list, BaziDetail baziDetail) {
        String[] zhis = {baziDetail.getYearZhi(), baziDetail.getMonthZhi(), 
                        baziDetail.getDayZhi(), baziDetail.getHourZhi()};
        String[] positions = {"年支", "月支", "日支", "时支"};
        
        // 处理相刑
        for (String[] xing : DIZHI_XINGFA) {
            if (xing.length == 2) {
                // 两支相刑或自刑
                if (xing[0].equals(xing[1])) {
                    // 自刑
                    int count = 0;
                    List<String> foundPositions = new ArrayList<>();
                    for (int i = 0; i < zhis.length; i++) {
                        if (zhis[i].equals(xing[0])) {
                            count++;
                            foundPositions.add(positions[i]);
                        }
                    }
                    if (count >= 2) {
                        list.add(new ChongheItem(
                            "地支相刑",
                            xing[0] + "自刑",
                            String.join("、", foundPositions),
                            "主自我折磨，内心矛盾"
                        ));
                    }
                } else {
                    // 两支相刑
                    List<String> foundPositions = new ArrayList<>();
                    boolean found1 = false, found2 = false;
                    
                    for (int i = 0; i < zhis.length; i++) {
                        if (zhis[i].equals(xing[0])) {
                            found1 = true;
                            foundPositions.add(positions[i]);
                        }
                        if (zhis[i].equals(xing[1])) {
                            found2 = true;
                            foundPositions.add(positions[i]);
                        }
                    }
                    
                    if (found1 && found2) {
                        list.add(new ChongheItem(
                            "地支相刑",
                            xing[0] + xing[1] + "相刑",
                            String.join("与", foundPositions),
                            "主无礼冲撞，感情不和"
                        ));
                    }
                }
            } else if (xing.length == 3) {
                // 三刑
                List<String> foundPositions = new ArrayList<>();
                int foundCount = 0;
                
                for (String zhiXing : xing) {
                    for (int i = 0; i < zhis.length; i++) {
                        if (zhis[i].equals(zhiXing)) {
                            foundCount++;
                            foundPositions.add(positions[i]);
                            break;
                        }
                    }
                }
                
                if (foundCount >= 2) {
                    String xingType = getXingType(xing);
                    list.add(new ChongheItem(
                        "地支相刑",
                        Arrays.toString(xing) + "三刑",
                        String.join("、", foundPositions),
                        xingType + "，主" + getXingInfluence(xing)
                    ));
                }
            }
        }
    }
    
    /**
     * 添加地支相害
     */
    private void addDizhiXianghai(List<ChongheItem> list, BaziDetail baziDetail) {
        String[] zhis = {baziDetail.getYearZhi(), baziDetail.getMonthZhi(), 
                        baziDetail.getDayZhi(), baziDetail.getHourZhi()};
        String[] positions = {"年支", "月支", "日支", "时支"};
        
        for (int i = 0; i < zhis.length; i++) {
            for (int j = i + 1; j < zhis.length; j++) {
                String zhi1 = zhis[i];
                String zhi2 = zhis[j];
                
                if (DIZHI_XIANGHAI.containsKey(zhi1) && DIZHI_XIANGHAI.get(zhi1).equals(zhi2)) {
                    list.add(new ChongheItem(
                        "地支相害",
                        zhi1 + zhi2 + "相害",
                        positions[i] + "与" + positions[j],
                        "主暗中伤害，小人作祟"
                    ));
                }
            }
        }
    }
    
    // 辅助方法
    private String getTianganHeWuxing(String gan1, String gan2) {
        if ((gan1.equals("甲") && gan2.equals("己")) || (gan1.equals("己") && gan2.equals("甲"))) {
            return "土";
        } else if ((gan1.equals("乙") && gan2.equals("庚")) || (gan1.equals("庚") && gan2.equals("乙"))) {
            return "金";
        } else if ((gan1.equals("丙") && gan2.equals("辛")) || (gan1.equals("辛") && gan2.equals("丙"))) {
            return "水";
        } else if ((gan1.equals("丁") && gan2.equals("壬")) || (gan1.equals("壬") && gan2.equals("丁"))) {
            return "木";
        } else if ((gan1.equals("戊") && gan2.equals("癸")) || (gan1.equals("癸") && gan2.equals("戊"))) {
            return "火";
        }
        return "未知";
    }
    
    private String getDizhiHeWuxing(String zhi1, String zhi2) {
        String combined = zhi1 + zhi2;
        switch (combined) {
            case "子丑": case "丑子": return "土";
            case "寅亥": case "亥寅": return "木";
            case "卯戌": case "戌卯": return "火";
            case "辰酉": case "酉辰": return "金";
            case "巳申": case "申巳": return "水";
            case "午未": case "未午": return "火土";
            default: return "未知";
        }
    }
    
    private String getDizhiChongInfluence(String pos1, String pos2) {
        if ((pos1.equals("年支") && pos2.equals("日支")) || (pos1.equals("日支") && pos2.equals("年支"))) {
            return "主与长辈不和，祖业难守";
        } else if ((pos1.equals("月支") && pos2.equals("时支")) || (pos1.equals("时支") && pos2.equals("月支"))) {
            return "主兄弟不和，子女缘薄";
        } else if ((pos1.equals("日支") && pos2.equals("时支")) || (pos1.equals("时支") && pos2.equals("日支"))) {
            return "主夫妻不和，子女叛逆";
        } else {
            return "主动荡不安，易有变故";
        }
    }
    
    private String getSanheWuxing(String[] sanhe) {
        String first = sanhe[0];
        if (first.equals("申")) return "水";
        if (first.equals("亥")) return "木";
        if (first.equals("寅")) return "火";
        if (first.equals("巳")) return "金";
        return "未知";
    }
    
    private String getSanhuiFangwei(String[] sanhui) {
        String first = sanhui[0];
        if (first.equals("寅")) return "东方木";
        if (first.equals("巳")) return "南方火";
        if (first.equals("申")) return "西方金";
        if (first.equals("亥")) return "北方水";
        return "未知";
    }
    
    private String getXingType(String[] xing) {
        if (Arrays.equals(xing, new String[]{"寅", "巳", "申"})) {
            return "恩将仇报之刑";
        } else if (Arrays.equals(xing, new String[]{"丑", "未", "戌"})) {
            return "持势之刑";
        }
        return "无礼之刑";
    }
    
    private String getXingInfluence(String[] xing) {
        if (Arrays.equals(xing, new String[]{"寅", "巳", "申"})) {
            return "忘恩负义，反复无情";
        } else if (Arrays.equals(xing, new String[]{"丑", "未", "戌"})) {
            return "倚势凌人，固执己见";
        }
        return "粗鲁无礼，冲撞他人";
    }
    
    private String analyzeChongheInfluence(List<ChongheItem> list) {
        int heCount = 0;  // 合的数量
        int chongCount = 0; // 冲的数量
        int xingCount = 0;  // 刑的数量
        int haiCount = 0;   // 害的数量
        
        for (ChongheItem item : list) {
            String type = item.getType();
            if (type.contains("合")) {
                heCount++;
            } else if (type.contains("冲")) {
                chongCount++;
            } else if (type.contains("刑")) {
                xingCount++;
            } else if (type.contains("害")) {
                haiCount++;
            }
        }
        
        StringBuilder analysis = new StringBuilder();
        
        if (heCount > 0) {
            analysis.append("命中有").append(heCount).append("组合化关系，主和谐融洽，人缘较好。");
        }
        
        if (chongCount > 0) {
            analysis.append("命中有").append(chongCount).append("组相冲关系，主动荡不安，易有变故。");
        }
        
        if (xingCount > 0) {
            analysis.append("命中有").append(xingCount).append("组相刑关系，主性格刚烈，易与人冲突。");
        }
        
        if (haiCount > 0) {
            analysis.append("命中有").append(haiCount).append("组相害关系，主暗中受损，需防小人。");
        }
        
        if (analysis.length() == 0) {
            analysis.append("四柱干支关系平和，无明显冲合刑害。");
        }
        
        return analysis.toString();
    }
    
    /**
     * 冲合结果类
     */
    public static class ChongheResult {
        private List<ChongheItem> chongheList;
        private int chongheCount;
        private Map<String, Integer> typeCount;
        private String analysis;
        
        // getter and setter
        public List<ChongheItem> getChongheList() { return chongheList; }
        public void setChongheList(List<ChongheItem> chongheList) { this.chongheList = chongheList; }
        public int getChongheCount() { return chongheCount; }
        public void setChongheCount(int chongheCount) { this.chongheCount = chongheCount; }
        public Map<String, Integer> getTypeCount() { return typeCount; }
        public void setTypeCount(Map<String, Integer> typeCount) { this.typeCount = typeCount; }
        public String getAnalysis() { return analysis; }
        public void setAnalysis(String analysis) { this.analysis = analysis; }
    }
    
    /**
     * 冲合关系项目类
     */
    public static class ChongheItem {
        private String type;        // 关系类型
        private String relation;    // 具体关系
        private String position;    // 涉及位置
        private String description; // 描述
        
        public ChongheItem(String type, String relation, String position, String description) {
            this.type = type;
            this.relation = relation;
            this.position = position;
            this.description = description;
        }
        
        // getter and setter
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getRelation() { return relation; }
        public void setRelation(String relation) { this.relation = relation; }
        public String getPosition() { return position; }
        public void setPosition(String position) { this.position = position; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }
}