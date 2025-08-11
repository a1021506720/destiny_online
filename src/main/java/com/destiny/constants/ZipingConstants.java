package com.destiny.constants;

import java.util.*;

/**
 * 子平术基础常量定义
 * 包含天干地支、五行、十神、十二宫等基础数据
 */
public class ZipingConstants {
    
    // 十天干
    public static final String[] TIAN_GAN = {"甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸"};
    
    // 十二地支
    public static final String[] DI_ZHI = {"子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥"};
    
    // 五行属性
    public static final Map<String, String> GAN_WUXING = new HashMap<String, String>() {{
        put("甲", "木"); put("乙", "木");
        put("丙", "火"); put("丁", "火");
        put("戊", "土"); put("己", "土");
        put("庚", "金"); put("辛", "金");
        put("壬", "水"); put("癸", "水");
    }};
    
    // 天干阴阳属性
    public static final Map<String, String> GAN_YINYANG = new HashMap<String, String>() {{
        put("甲", "阳"); put("乙", "阴");
        put("丙", "阳"); put("丁", "阴");
        put("戊", "阳"); put("己", "阴");
        put("庚", "阳"); put("辛", "阴");
        put("壬", "阳"); put("癸", "阴");
    }};
    
    // 地支藏干对照表（主气、中气、余气）
    public static final Map<String, List<String>> ZHI_ZANGGAN = new HashMap<String, List<String>>() {{
        put("子", Arrays.asList("癸"));
        put("丑", Arrays.asList("己", "癸", "辛"));
        put("寅", Arrays.asList("甲", "丙", "戊"));
        put("卯", Arrays.asList("乙"));
        put("辰", Arrays.asList("戊", "乙", "癸"));
        put("巳", Arrays.asList("丙", "戊", "庚"));
        put("午", Arrays.asList("丁", "己"));
        put("未", Arrays.asList("己", "乙", "丁"));
        put("申", Arrays.asList("庚", "壬", "戊"));
        put("酉", Arrays.asList("辛"));
        put("戌", Arrays.asList("戊", "辛", "丁"));
        put("亥", Arrays.asList("壬", "甲"));
    }};
    
    // 十神名称
    public static final String[] SHISHEN_NAMES = {
        "比肩", "劫财", "食神", "伤官", "偏财", "正财", "七杀", "正官", "偏印", "正印"
    };
    
    // 十二宫名称
    public static final String[] SHIERGONG_NAMES = {
        "长生", "沐浴", "冠带", "临官", "帝旺", "衰", "病", "死", "墓", "绝", "胎", "养"
    };
    
    // 阳干寄生十二宫对照表（顺行）
    public static final Map<String, List<String>> YANG_GAN_SHIERGONG = new HashMap<String, List<String>>() {{
        put("甲", Arrays.asList("亥", "子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌"));
        put("丙", Arrays.asList("寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥", "子", "丑"));
        put("戊", Arrays.asList("寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥", "子", "丑"));
        put("庚", Arrays.asList("巳", "午", "未", "申", "酉", "戌", "亥", "子", "丑", "寅", "卯", "辰"));
        put("壬", Arrays.asList("申", "酉", "戌", "亥", "子", "丑", "寅", "卯", "辰", "巳", "午", "未"));
    }};
    
    // 阴干寄生十二宫对照表（逆行）
    public static final Map<String, List<String>> YIN_GAN_SHIERGONG = new HashMap<String, List<String>>() {{
        put("乙", Arrays.asList("午", "巳", "辰", "卯", "寅", "丑", "子", "亥", "戌", "酉", "申", "未"));
        put("丁", Arrays.asList("酉", "申", "未", "午", "巳", "辰", "卯", "寅", "丑", "子", "亥", "戌"));
        put("己", Arrays.asList("酉", "申", "未", "午", "巳", "辰", "卯", "寅", "丑", "子", "亥", "戌"));
        put("辛", Arrays.asList("子", "亥", "戌", "酉", "申", "未", "午", "巳", "辰", "卯", "寅", "丑"));
        put("癸", Arrays.asList("卯", "寅", "丑", "子", "亥", "戌", "酉", "申", "未", "午", "巳", "辰"));
    }};
    
    // 地支藏干权重（主气、中气、余气的权重分配）
    public static final Map<String, Double[]> ZANGGAN_WEIGHTS = new HashMap<String, Double[]>() {{
        put("子", new Double[]{1.0}); // 独气
        put("丑", new Double[]{0.7, 0.2, 0.1}); // 己土主气、癸水中气、辛金余气
        put("寅", new Double[]{0.7, 0.2, 0.1}); // 甲木主气、丙火中气、戊土余气
        put("卯", new Double[]{1.0}); // 独气
        put("辰", new Double[]{0.7, 0.2, 0.1}); // 戊土主气、乙木中气、癸水余气
        put("巳", new Double[]{0.7, 0.2, 0.1}); // 丙火主气、戊土中气、庚金余气
        put("午", new Double[]{0.8, 0.2}); // 丁火主气、己土余气
        put("未", new Double[]{0.7, 0.2, 0.1}); // 己土主气、乙木中气、丁火余气
        put("申", new Double[]{0.7, 0.2, 0.1}); // 庚金主气、壬水中气、戊土余气
        put("酉", new Double[]{1.0}); // 独气
        put("戌", new Double[]{0.7, 0.2, 0.1}); // 戊土主气、辛金中气、丁火余气
        put("亥", new Double[]{0.8, 0.2}); // 壬水主气、甲木中气
    }};
    
    // 月令权重（月支在旺衰判断中的重要性）
    public static final double YUELING_WEIGHT = 1.5;
    
    // 天干权重
    public static final double TIANGAN_WEIGHT = 1.0;
}
