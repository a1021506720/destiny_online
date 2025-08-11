package com.destiny.service;

import com.destiny.dto.DayunRequest;
import com.destiny.dto.DayunResponse;
import com.destiny.dto.BaziResponse;
import com.nlf.calendar.Lunar;
import com.nlf.calendar.Solar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 大运排盘计算服务
 */
@Service
public class DayunService {
    
    @Autowired
    private BaziService baziService;
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    
    // 天干数组（用于判断阴阳）
    private static final String[] YANG_GAN = {"甲", "丙", "戊", "庚", "壬"};
    private static final String[] YIN_GAN = {"乙", "丁", "己", "辛", "癸"};
    
    // 天干地支数组（用于大运排序）
    private static final String[] TIAN_GAN = {"甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸"};
    private static final String[] DI_ZHI = {"子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥"};
    
    // 24节气中的12个"节"
    private static final String[] JIE_QI_NAMES = {
        "立春", "惊蛰", "清明", "立夏", "芒种", "小暑", 
        "立秋", "白露", "寒露", "立冬", "大雪", "小寒"
    };
    
    // 60甲子空亡表（每旬空亡的地支）
    private static final String[][] KONG_WANG_TABLE = {
        {"戌", "亥"}, // 甲子旬
        {"申", "酉"}, // 甲戌旬
        {"午", "未"}, // 甲申旬
        {"辰", "巳"}, // 甲午旬
        {"寅", "卯"}, // 甲辰旬
        {"子", "丑"}  // 甲寅旬
    };
    
    /**
     * 计算大运排盘
     */
    public DayunResponse calculateDayun(DayunRequest request) {
        try {
            // 1. 先计算基本八字信息
            BaziResponse baziInfo = baziService.calculateBazi(request);
            
            // 2. 解析出生日期时间
            LocalDate birthDate = LocalDate.parse(request.getDate(), DATE_FORMATTER);
            LocalTime birthTime = LocalTime.parse(request.getTime(), TIME_FORMATTER);
            
            // 3. 获取Solar和Lunar对象
            Solar solar = getSolarFromRequest(request, birthDate, birthTime);
            Lunar lunar = solar.getLunar();
            
            // 4. 判断年命阴阳
            String yearGan = lunar.getYearGan();
            boolean isYangYear = Arrays.asList(YANG_GAN).contains(yearGan);
            String yearNature = isYangYear ? "阳年" : "阴年";
            
            // 5. 确定大运顺逆排法
            boolean isShunPai = determineDayunDirection(isYangYear, request.getGender());
            String dayunDirection = isShunPai ? "顺排" : "逆排";
            
            // 6. 计算起运年龄
            int daysToJie = calculateDaysToJie(solar, isShunPai);
            int startAge = calculateStartAge(daysToJie);
            int startYear = birthDate.getYear() + startAge - 1; // 虚岁转实岁
            
            // 7. 计算空亡
            List<String> kongWang = calculateKongWang(lunar.getDayInGanZhi());
            
            // 8. 排大运
            List<DayunResponse.DayunStep> dayunSteps = calculateDayunSteps(
                lunar.getMonthInGanZhi(), isShunPai, startAge, startYear, request.getYears()
            );
            
            // 9. 构建响应
            DayunResponse response = new DayunResponse();
            response.setBaziInfo(baziInfo);
            response.setGender(request.getGender() == DayunRequest.Gender.MALE ? "男" : "女");
            response.setYearNature(yearNature);
            response.setDayunDirection(dayunDirection);
            response.setStartAge(startAge);
            response.setStartYear(startYear);
            response.setDaysToLastJie(daysToJie);
            response.setKongWang(kongWang);
            response.setDayunSteps(dayunSteps);
            response.setDescription(buildDescription(request, yearNature, dayunDirection, daysToJie, startAge));
            
            return response;
            
        } catch (Exception e) {
            throw new RuntimeException("计算大运时发生错误: " + e.getMessage(), e);
        }
    }
    
    /**
     * 从请求获取Solar对象
     */
    private Solar getSolarFromRequest(DayunRequest request, LocalDate date, LocalTime time) {
        if (request.getDateType() == DayunRequest.DateType.LUNAR) {
            Lunar lunar = new Lunar(date.getYear(), date.getMonthValue(), 
                                  date.getDayOfMonth(), time.getHour(), 
                                  time.getMinute(), 0);
            return lunar.getSolar();
        } else {
            return new Solar(date.getYear(), date.getMonthValue(), 
                           date.getDayOfMonth(), time.getHour(), 
                           time.getMinute(), 0);
        }
    }
    
    /**
     * 确定大运顺逆排法
     * 阳年男命顺排，阴年男命逆排，阳年女命逆排，阴年女命顺排
     */
    private boolean determineDayunDirection(boolean isYangYear, DayunRequest.Gender gender) {
        if (gender == DayunRequest.Gender.MALE) {
            return isYangYear; // 阳年男命顺排，阴年男命逆排
        } else {
            return !isYangYear; // 阳年女命逆排，阴年女命顺排
        }
    }
    
    /**
     * 计算到节的天数（根据顺排逆排决定方向）
     * 顺排：计算到下一个节的天数
     * 逆排：计算到上一个节的天数
     * 
     * 24节气中的12个"节"：立春、惊蛰、清明、立夏、芒种、小暑、立秋、白露、寒露、立冬、大雪、小寒
     * 每个月只有一个"节"，大约在每月的4-6日左右
     */
    private int calculateDaysToJie(Solar solar, boolean isShunPai) {
        int day = solar.getDay();
        int month = solar.getMonth();
        
        // 每个月的"节"大概日期（简化估算）
        int[] jieRiDates = {
            5,  // 1月 - 立春
            5,  // 2月 - 惊蛰  
            5,  // 3月 - 清明
            5,  // 4月 - 立夏
            6,  // 5月 - 芒种
            7,  // 6月 - 小暑
            7,  // 7月 - 立秋
            8,  // 8月 - 白露
            8,  // 9月 - 寒露
            8,  // 10月 - 立冬
            7,  // 11月 - 大雪
            6   // 12月 - 小寒
        };
        
        int currentMonthJieDate = jieRiDates[month - 1];
        int daysToJie;
        
        if (isShunPai) {
            // 顺排：计算到下一个节的天数
            if (day <= currentMonthJieDate) {
                // 距离当月的节
                daysToJie = currentMonthJieDate - day;
            } else {
                // 距离下个月的节
                int nextMonth = (month % 12) + 1;
                int nextMonthJieDate = jieRiDates[nextMonth - 1];
                // 简化计算：假设每月30天
                daysToJie = (30 - day) + nextMonthJieDate;
            }
        } else {
            // 逆排：计算到上一个节的天数
            if (day >= currentMonthJieDate) {
                // 距离当月的节
                daysToJie = day - currentMonthJieDate;
            } else {
                // 距离上个月的节
                int prevMonth = month == 1 ? 12 : month - 1;
                int prevMonthJieDate = jieRiDates[prevMonth - 1];
                // 简化计算
                daysToJie = day + (30 - prevMonthJieDate);
            }
        }
        
        // 确保在合理范围内
        if (daysToJie <= 0) {
            daysToJie = 3;
        } else if (daysToJie > 30) {
            daysToJie = 15;
        }
        
        return daysToJie;
    }
    
    /**
     * 计算起运年龄
     */
    private int calculateStartAge(int daysToLastJie) {
        int baseAge = daysToLastJie / 3;
        int remainder = daysToLastJie % 3;
        
        // 有余数则进一岁
        if (remainder > 0) {
            baseAge++;
        }
        
        return baseAge;
    }
    
    /**
     * 计算空亡
     */
    private List<String> calculateKongWang(String dayGanZhi) {
        // 根据日柱确定所在旬
        String dayGan = dayGanZhi.substring(0, 1);
        
        int xunIndex = 0;
        switch (dayGan) {
            case "甲": xunIndex = 0; break; // 甲子旬或甲戌旬等
            case "乙": case "丙": xunIndex = 0; break; // 在甲子旬内
            case "丁": case "戊": xunIndex = 0; break;
            case "己": case "庚": xunIndex = 0; break;
            case "辛": case "壬": xunIndex = 0; break;
            case "癸": xunIndex = 0; break;
        }
        
        // 需要根据日干支的具体组合来确定旬
        // 这里简化处理，根据日干在60甲子中的位置计算
        int ganIndex = Arrays.asList(TIAN_GAN).indexOf(dayGan);
        String dayZhi = dayGanZhi.substring(1);
        int zhiIndex = Arrays.asList(DI_ZHI).indexOf(dayZhi);
        
        // 计算在60甲子中的位置
        int jiazi60Index = (ganIndex * 12 + zhiIndex) % 60;
        xunIndex = jiazi60Index / 10;
        
        return Arrays.asList(KONG_WANG_TABLE[xunIndex]);
    }
    
    /**
     * 计算大运步骤
     */
    private List<DayunResponse.DayunStep> calculateDayunSteps(
            String monthGanZhi, boolean isShunPai, int startAge, int startYear, int totalYears) {
        
        List<DayunResponse.DayunStep> steps = new ArrayList<>();
        
        // 获取月柱的天干地支索引
        String monthGan = monthGanZhi.substring(0, 1);
        String monthZhi = monthGanZhi.substring(1);
        int ganIndex = Arrays.asList(TIAN_GAN).indexOf(monthGan);
        int zhiIndex = Arrays.asList(DI_ZHI).indexOf(monthZhi);
        
        int stepCount = totalYears / 10; // 每步大运10年
        
        for (int i = 0; i < stepCount; i++) {
            DayunResponse.DayunStep step = new DayunResponse.DayunStep();
            step.setStep(i + 1);
            
            // 计算大运干支
            int currentGanIndex, currentZhiIndex;
            if (isShunPai) {
                // 顺排：向下一个干支排列
                currentGanIndex = (ganIndex + i + 1) % 10;
                currentZhiIndex = (zhiIndex + i + 1) % 12;
            } else {
                // 逆排：向上一个干支排列
                currentGanIndex = (ganIndex - i - 1 + 10) % 10;
                currentZhiIndex = (zhiIndex - i - 1 + 12) % 12;
            }
            
            String currentGanZhi = TIAN_GAN[currentGanIndex] + DI_ZHI[currentZhiIndex];
            step.setGanZhi(currentGanZhi);
            
            // 计算年龄和年份
            step.setStartAge(startAge + i * 10);
            step.setEndAge(startAge + i * 10 + 9);
            step.setStartYear(startYear + i * 10);
            step.setEndYear(startYear + i * 10 + 9);
            
            steps.add(step);
        }
        
        return steps;
    }
    
    /**
     * 构建描述信息
     */
    private String buildDescription(DayunRequest request, String yearNature, 
                                  String dayunDirection, int daysToJie, int startAge) {
        String jieDirection = dayunDirection.equals("顺排") ? "下一个节" : "上一个节";
        return String.format(
            "%s，%s，故大运%s。距离%s%d天，%d÷3=%d%s，故%d岁起运。",
            yearNature,
            request.getGender() == DayunRequest.Gender.MALE ? "男命" : "女命",
            dayunDirection,
            jieDirection,
            daysToJie,
            daysToJie,
            daysToJie / 3,
            daysToJie % 3 > 0 ? "余" + (daysToJie % 3) + "进一岁" : "",
            startAge
        );
    }
}
