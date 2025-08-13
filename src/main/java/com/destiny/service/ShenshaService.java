package com.destiny.service;

import com.destiny.dto.BaziDetail;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 神煞计算服务
 * 实现常用神煞的计算和分析
 */
@Service
public class ShenshaService {
    
    /**
     * 计算所有神煞
     */
    public ShenshaResult calculateShensha(BaziDetail baziDetail) {
        ShenshaResult result = new ShenshaResult();
        
        String yearZhi = baziDetail.getYearZhi();
        String monthZhi = baziDetail.getMonthZhi();
        String dayGan = baziDetail.getDayGan();
        String dayZhi = baziDetail.getDayZhi();
        String hourZhi = baziDetail.getHourZhi();
        
        List<ShenshaItem> shenshaList = new ArrayList<>();
        
        // 1. 天乙贵人
        List<String> tianyiGuiren = calculateTianyiGuiren(dayGan);
        addShenshaItems(shenshaList, "天乙贵人", tianyiGuiren, baziDetail, "主贵人相助，逢凶化吉");
        
        // 2. 桃花（咸池）
        String taohua = calculateTaohua(yearZhi, monthZhi, dayZhi, hourZhi);
        if (taohua != null) {
            addShenshaItem(shenshaList, "桃花", taohua, baziDetail, "主异性缘分，感情丰富");
        }
        
        // 3. 驿马
        String yima = calculateYima(yearZhi, monthZhi, dayZhi, hourZhi);
        if (yima != null) {
            addShenshaItem(shenshaList, "驿马", yima, baziDetail, "主奔波走动，变化多端");
        }
        
        // 4. 华盖
        String huagai = calculateHuagai(yearZhi, monthZhi, dayZhi, hourZhi);
        if (huagai != null) {
            addShenshaItem(shenshaList, "华盖", huagai, baziDetail, "主孤高清雅，多才艺");
        }
        
        // 5. 文昌
        String wenchang = calculateWenchang(dayGan, yearZhi, monthZhi, dayZhi, hourZhi);
        if (wenchang != null) {
            addShenshaItem(shenshaList, "文昌", wenchang, baziDetail, "主文才智慧，利学业");
        }
        
        // 6. 将星
        String jiangxing = calculateJiangxing(yearZhi, monthZhi, dayZhi, hourZhi);
        if (jiangxing != null) {
            addShenshaItem(shenshaList, "将星", jiangxing, baziDetail, "主权威领导，有统御力");
        }
        
        // 7. 羊刃
        String yangre = calculateYangre(dayGan, yearZhi, monthZhi, dayZhi, hourZhi);
        if (yangre != null) {
            addShenshaItem(shenshaList, "羊刃", yangre, baziDetail, "主刚烈果断，易有刑伤");
        }
        
        // 8. 劫煞
        String jiesha = calculateJiesha(yearZhi, monthZhi, dayZhi, hourZhi);
        if (jiesha != null) {
            addShenshaItem(shenshaList, "劫煞", jiesha, baziDetail, "主破财损失，小人是非");
        }
        
        // 9. 灾煞
        String zaisha = calculateZaisha(yearZhi, monthZhi, dayZhi, hourZhi);
        if (zaisha != null) {
            addShenshaItem(shenshaList, "灾煞", zaisha, baziDetail, "主意外灾祸，需防范");
        }
        
        // 10. 孤辰寡宿
        String[] guchenGuasu = calculateGuchenGuasu(yearZhi, monthZhi, dayZhi, hourZhi);
        if (guchenGuasu[0] != null) {
            addShenshaItem(shenshaList, "孤辰", guchenGuasu[0], baziDetail, "主孤独，六亲缘薄");
        }
        if (guchenGuasu[1] != null) {
            addShenshaItem(shenshaList, "寡宿", guchenGuasu[1], baziDetail, "主孤独，婚姻不利");
        }
        
        result.setShenshaList(shenshaList);
        result.setShenshaCount(shenshaList.size());
        
        // 统计神煞类型
        Map<String, Integer> typeCount = new HashMap<>();
        for (ShenshaItem item : shenshaList) {
            String type = getShenshaType(item.getName());
            typeCount.put(type, typeCount.getOrDefault(type, 0) + 1);
        }
        result.setTypeCount(typeCount);
        
        return result;
    }
    
    /**
     * 计算天乙贵人
     */
    private List<String> calculateTianyiGuiren(String dayGan) {
        Map<String, List<String>> tianyiMap = new HashMap<String, List<String>>() {{
            put("甲", Arrays.asList("丑", "未"));
            put("乙", Arrays.asList("子", "申"));
            put("丙", Arrays.asList("亥", "酉"));
            put("丁", Arrays.asList("亥", "酉"));
            put("戊", Arrays.asList("丑", "未"));
            put("己", Arrays.asList("子", "申"));
            put("庚", Arrays.asList("丑", "未"));
            put("辛", Arrays.asList("午", "寅"));
            put("壬", Arrays.asList("卯", "巳"));
            put("癸", Arrays.asList("卯", "巳"));
        }};
        
        return tianyiMap.getOrDefault(dayGan, new ArrayList<>());
    }
    
    /**
     * 计算桃花（咸池）
     * 申子辰见酉，亥卯未见子，寅午戌见卯，巳酉丑见午
     */
    private String calculateTaohua(String yearZhi, String monthZhi, String dayZhi, String hourZhi) {
        String[] zhis = {yearZhi, monthZhi, dayZhi, hourZhi};
        
        // 检查申子辰
        if (containsAny(zhis, "申", "子", "辰")) {
            if (contains(zhis, "酉")) return "酉";
        }
        
        // 检查亥卯未
        if (containsAny(zhis, "亥", "卯", "未")) {
            if (contains(zhis, "子")) return "子";
        }
        
        // 检查寅午戌
        if (containsAny(zhis, "寅", "午", "戌")) {
            if (contains(zhis, "卯")) return "卯";
        }
        
        // 检查巳酉丑
        if (containsAny(zhis, "巳", "酉", "丑")) {
            if (contains(zhis, "午")) return "午";
        }
        
        return null;
    }
    
    /**
     * 计算驿马
     * 申子辰见寅，亥卯未见巳，寅午戌见申，巳酉丑见亥
     */
    private String calculateYima(String yearZhi, String monthZhi, String dayZhi, String hourZhi) {
        String[] zhis = {yearZhi, monthZhi, dayZhi, hourZhi};
        
        // 检查申子辰
        if (containsAny(zhis, "申", "子", "辰")) {
            if (contains(zhis, "寅")) return "寅";
        }
        
        // 检查亥卯未
        if (containsAny(zhis, "亥", "卯", "未")) {
            if (contains(zhis, "巳")) return "巳";
        }
        
        // 检查寅午戌
        if (containsAny(zhis, "寅", "午", "戌")) {
            if (contains(zhis, "申")) return "申";
        }
        
        // 检查巳酉丑
        if (containsAny(zhis, "巳", "酉", "丑")) {
            if (contains(zhis, "亥")) return "亥";
        }
        
        return null;
    }
    
    /**
     * 计算华盖
     * 申子辰见辰，亥卯未见未，寅午戌见戌，巳酉丑见丑
     */
    private String calculateHuagai(String yearZhi, String monthZhi, String dayZhi, String hourZhi) {
        String[] zhis = {yearZhi, monthZhi, dayZhi, hourZhi};
        
        // 检查申子辰
        if (containsAny(zhis, "申", "子", "辰")) {
            if (contains(zhis, "辰")) return "辰";
        }
        
        // 检查亥卯未
        if (containsAny(zhis, "亥", "卯", "未")) {
            if (contains(zhis, "未")) return "未";
        }
        
        // 检查寅午戌
        if (containsAny(zhis, "寅", "午", "戌")) {
            if (contains(zhis, "戌")) return "戌";
        }
        
        // 检查巳酉丑
        if (containsAny(zhis, "巳", "酉", "丑")) {
            if (contains(zhis, "丑")) return "丑";
        }
        
        return null;
    }
    
    /**
     * 计算文昌
     */
    private String calculateWenchang(String dayGan, String yearZhi, String monthZhi, String dayZhi, String hourZhi) {
        Map<String, String> wenchangMap = new HashMap<String, String>() {{
            put("甲", "巳");
            put("乙", "午");
            put("丙", "申");
            put("丁", "酉");
            put("戊", "申");
            put("己", "酉");
            put("庚", "亥");
            put("辛", "子");
            put("壬", "寅");
            put("癸", "卯");
        }};
        
        String wenchang = wenchangMap.get(dayGan);
        String[] zhis = {yearZhi, monthZhi, dayZhi, hourZhi};
        
        if (wenchang != null && contains(zhis, wenchang)) {
            return wenchang;
        }
        
        return null;
    }
    
    /**
     * 计算将星
     * 申子辰见子，亥卯未见卯，寅午戌见午，巳酉丑见酉
     */
    private String calculateJiangxing(String yearZhi, String monthZhi, String dayZhi, String hourZhi) {
        String[] zhis = {yearZhi, monthZhi, dayZhi, hourZhi};
        
        // 检查申子辰
        if (containsAny(zhis, "申", "子", "辰")) {
            if (contains(zhis, "子")) return "子";
        }
        
        // 检查亥卯未
        if (containsAny(zhis, "亥", "卯", "未")) {
            if (contains(zhis, "卯")) return "卯";
        }
        
        // 检查寅午戌
        if (containsAny(zhis, "寅", "午", "戌")) {
            if (contains(zhis, "午")) return "午";
        }
        
        // 检查巳酉丑
        if (containsAny(zhis, "巳", "酉", "丑")) {
            if (contains(zhis, "酉")) return "酉";
        }
        
        return null;
    }
    
    /**
     * 计算羊刃
     */
    private String calculateYangre(String dayGan, String yearZhi, String monthZhi, String dayZhi, String hourZhi) {
        Map<String, String> yangreMap = new HashMap<String, String>() {{
            put("甲", "卯");
            put("乙", "寅");
            put("丙", "午");
            put("丁", "巳");
            put("戊", "午");
            put("己", "巳");
            put("庚", "酉");
            put("辛", "申");
            put("壬", "子");
            put("癸", "亥");
        }};
        
        String yangre = yangreMap.get(dayGan);
        String[] zhis = {yearZhi, monthZhi, dayZhi, hourZhi};
        
        if (yangre != null && contains(zhis, yangre)) {
            return yangre;
        }
        
        return null;
    }
    
    /**
     * 计算劫煞
     * 申子辰见巳，亥卯未见申，寅午戌见亥，巳酉丑见寅
     */
    private String calculateJiesha(String yearZhi, String monthZhi, String dayZhi, String hourZhi) {
        String[] zhis = {yearZhi, monthZhi, dayZhi, hourZhi};
        
        // 检查申子辰
        if (containsAny(zhis, "申", "子", "辰")) {
            if (contains(zhis, "巳")) return "巳";
        }
        
        // 检查亥卯未
        if (containsAny(zhis, "亥", "卯", "未")) {
            if (contains(zhis, "申")) return "申";
        }
        
        // 检查寅午戌
        if (containsAny(zhis, "寅", "午", "戌")) {
            if (contains(zhis, "亥")) return "亥";
        }
        
        // 检查巳酉丑
        if (containsAny(zhis, "巳", "酉", "丑")) {
            if (contains(zhis, "寅")) return "寅";
        }
        
        return null;
    }
    
    /**
     * 计算灾煞
     * 申子辰见未，亥卯未见戌，寅午戌见丑，巳酉丑见辰
     */
    private String calculateZaisha(String yearZhi, String monthZhi, String dayZhi, String hourZhi) {
        String[] zhis = {yearZhi, monthZhi, dayZhi, hourZhi};
        
        // 检查申子辰
        if (containsAny(zhis, "申", "子", "辰")) {
            if (contains(zhis, "未")) return "未";
        }
        
        // 检查亥卯未
        if (containsAny(zhis, "亥", "卯", "未")) {
            if (contains(zhis, "戌")) return "戌";
        }
        
        // 检查寅午戌
        if (containsAny(zhis, "寅", "午", "戌")) {
            if (contains(zhis, "丑")) return "丑";
        }
        
        // 检查巳酉丑
        if (containsAny(zhis, "巳", "酉", "丑")) {
            if (contains(zhis, "辰")) return "辰";
        }
        
        return null;
    }
    
    /**
     * 计算孤辰寡宿
     */
    private String[] calculateGuchenGuasu(String yearZhi, String monthZhi, String dayZhi, String hourZhi) {
        String[] zhis = {yearZhi, monthZhi, dayZhi, hourZhi};
        String guchen = null;
        String guasu = null;
        
        // 申子辰年见寅为孤辰，见戌为寡宿
        if (containsAny(zhis, "申", "子", "辰")) {
            if (contains(zhis, "寅")) guchen = "寅";
            if (contains(zhis, "戌")) guasu = "戌";
        }
        
        // 亥卯未年见巳为孤辰，见丑为寡宿
        if (containsAny(zhis, "亥", "卯", "未")) {
            if (contains(zhis, "巳")) guchen = "巳";
            if (contains(zhis, "丑")) guasu = "丑";
        }
        
        // 寅午戌年见申为孤辰，见辰为寡宿
        if (containsAny(zhis, "寅", "午", "戌")) {
            if (contains(zhis, "申")) guchen = "申";
            if (contains(zhis, "辰")) guasu = "辰";
        }
        
        // 巳酉丑年见亥为孤辰，见未为寡宿
        if (containsAny(zhis, "巳", "酉", "丑")) {
            if (contains(zhis, "亥")) guchen = "亥";
            if (contains(zhis, "未")) guasu = "未";
        }
        
        return new String[]{guchen, guasu};
    }
    
    // 辅助方法
    private boolean contains(String[] array, String value) {
        return Arrays.asList(array).contains(value);
    }
    
    private boolean containsAny(String[] array, String... values) {
        for (String value : values) {
            if (contains(array, value)) {
                return true;
            }
        }
        return false;
    }
    
    private void addShenshaItems(List<ShenshaItem> list, String name, List<String> zhis, BaziDetail baziDetail, String description) {
        for (String zhi : zhis) {
            addShenshaItem(list, name, zhi, baziDetail, description);
        }
    }
    
    private void addShenshaItem(List<ShenshaItem> list, String name, String zhi, BaziDetail baziDetail, String description) {
        String position = findPositionOfZhi(zhi, baziDetail);
        if (position != null) {
            list.add(new ShenshaItem(name, zhi, position, description));
        }
    }
    
    private String findPositionOfZhi(String zhi, BaziDetail baziDetail) {
        if (zhi.equals(baziDetail.getYearZhi())) return "年支";
        if (zhi.equals(baziDetail.getMonthZhi())) return "月支";
        if (zhi.equals(baziDetail.getDayZhi())) return "日支";
        if (zhi.equals(baziDetail.getHourZhi())) return "时支";
        return null;
    }
    
    private String getShenshaType(String name) {
        switch (name) {
            case "天乙贵人":
            case "文昌":
            case "将星":
                return "吉神";
            case "桃花":
            case "驿马":
            case "华盖":
                return "中性神煞";
            case "羊刃":
            case "劫煞":
            case "灾煞":
            case "孤辰":
            case "寡宿":
                return "凶神";
            default:
                return "其他";
        }
    }
    
    /**
     * 神煞结果类
     */
    public static class ShenshaResult {
        private List<ShenshaItem> shenshaList;
        private int shenshaCount;
        private Map<String, Integer> typeCount;
        
        // getter and setter
        public List<ShenshaItem> getShenshaList() { return shenshaList; }
        public void setShenshaList(List<ShenshaItem> shenshaList) { this.shenshaList = shenshaList; }
        public int getShenshaCount() { return shenshaCount; }
        public void setShenshaCount(int shenshaCount) { this.shenshaCount = shenshaCount; }
        public Map<String, Integer> getTypeCount() { return typeCount; }
        public void setTypeCount(Map<String, Integer> typeCount) { this.typeCount = typeCount; }
    }
    
    /**
     * 神煞项目类
     */
    public static class ShenshaItem {
        private String name;        // 神煞名称
        private String zhi;         // 所在地支
        private String position;    // 所在位置
        private String description; // 描述
        
        public ShenshaItem(String name, String zhi, String position, String description) {
            this.name = name;
            this.zhi = zhi;
            this.position = position;
            this.description = description;
        }
        
        // getter and setter
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getZhi() { return zhi; }
        public void setZhi(String zhi) { this.zhi = zhi; }
        public String getPosition() { return position; }
        public void setPosition(String position) { this.position = position; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }
}