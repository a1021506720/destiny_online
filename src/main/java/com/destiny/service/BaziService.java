package com.destiny.service;

import com.destiny.dto.BaziRequest;
import com.destiny.dto.BaziResponse;
import com.destiny.dto.DayunRequest;
import com.nlf.calendar.Lunar;
import com.nlf.calendar.Solar;
// import com.nlf.calendar.eightchar.EightChar;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * 生辰八字计算服务
 * 在 Go 中相当于一个包含业务逻辑的 package
 */
@Service  // Spring 的服务注解，相当于将这个类注册为单例服务
public class BaziService {
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    
    /**
     * 计算生辰八字
     * @param request 请求参数
     * @return 八字信息
     */
    public BaziResponse calculateBazi(BaziRequest request) {
        try {
            // 解析日期和时间
            LocalDate date = LocalDate.parse(request.getDate(), DATE_FORMATTER);
            LocalTime time = LocalTime.parse(request.getTime(), TIME_FORMATTER);
            
            // 获取 Solar（阳历）对象
            Solar solar;
            if (request.getDateType() == BaziRequest.DateType.LUNAR) {
                // 如果输入的是农历，先创建农历对象再转换为阳历
                Lunar lunar = new Lunar(date.getYear(), date.getMonthValue(), 
                                      date.getDayOfMonth(), time.getHour(), 
                                      time.getMinute(), 0);
                solar = lunar.getSolar();
            } else {
                // 直接使用阳历
                solar = new Solar(date.getYear(), date.getMonthValue(), 
                                date.getDayOfMonth(), time.getHour(), 
                                time.getMinute(), 0);
            }
            
            // 获取农历对象
            Lunar lunar = solar.getLunar();
            
            // 特别处理子时的情况
            int hour = time.getHour();
            int minute = time.getMinute();
            boolean isLateZiShi = (hour == 23); // 晚子时：23:00-23:59
            boolean isEarlyZiShi = (hour == 0);  // 早子时：00:00-00:59
            
            // 如果是晚子时（23:00-23:59），需要按第二天计算
            if (isLateZiShi) {
                // 获取第二天的日期来计算时柱
                Solar nextDaySolar = solar.next(1);
                Lunar nextDayLunar = nextDaySolar.getLunar();
                
                // 构建响应
                BaziResponse response = new BaziResponse();
                response.setSolarDate(solar.toYmdHms());
                response.setLunarDate(lunar.toString());
                
                // 年月日使用当天，时辰使用第二天的子时
                response.setYearPillar(lunar.getYearInGanZhi());
                response.setMonthPillar(lunar.getMonthInGanZhi());
                response.setDayPillar(lunar.getDayInGanZhi());
                
                // 晚子时的时柱使用第二天的天干配子
                String nextDayGan = nextDayLunar.getDayGan(); // 第二天的日天干
                String timeGan = getTimeGan(nextDayGan, 0);   // 根据第二天日干推算子时天干
                response.setHourPillar(timeGan + "子");
                
                response.setBazi(String.format("%s %s %s %s", 
                    response.getYearPillar(), 
                    response.getMonthPillar(), 
                    response.getDayPillar(), 
                    response.getHourPillar()));
                
                response.setHourType("晚子时（23:00-23:59，按次日子时计算）");
                response.setDescription(String.format(
                    "出生于 %s（农历：%s）晚上 %d 点 %d 分，属于晚子时，时柱按第二天（%s）的子时计算",
                    solar.toYmd(), lunar.toString(), hour, minute, nextDayLunar.getDayInGanZhi()
                ));
                
                return response;
            }
            
            // 其他时间（包括早子时）正常计算
            // 直接使用 Lunar 对象获取八字信息
            
            // 构建响应
            BaziResponse response = new BaziResponse();
            response.setSolarDate(solar.toYmdHms());
            response.setLunarDate(lunar.toString());
            response.setYearPillar(lunar.getYearInGanZhi());
            response.setMonthPillar(lunar.getMonthInGanZhi());
            response.setDayPillar(lunar.getDayInGanZhi());
            response.setHourPillar(lunar.getTimeInGanZhi());
            response.setBazi(String.format("%s %s %s %s", 
                response.getYearPillar(), 
                response.getMonthPillar(), 
                response.getDayPillar(), 
                response.getHourPillar()));
            
            if (isEarlyZiShi) {
                response.setHourType("早子时（00:00-00:59，按当日子时计算）");
                response.setDescription(String.format(
                    "出生于 %s（农历：%s）凌晨 %d 点 %d 分，属于早子时，按当日计算",
                    solar.toYmd(), lunar.toString(), hour, minute
                ));
            } else {
                String shiChen = getShiChen(hour);
                response.setHourType(shiChen + "时（" + getTimeRange(hour) + "）");
                response.setDescription(String.format(
                    "出生于 %s（农历：%s）%d 点 %d 分",
                    solar.toYmd(), lunar.toString(), hour, minute
                ));
            }
            
            return response;
            
        } catch (Exception e) {
            throw new RuntimeException("计算八字时发生错误: " + e.getMessage(), e);
        }
    }
    
    /**
     * 根据日天干和时辰地支索引计算时天干
     * 使用五鼠遁元法（日上起时法）
     */
    private String getTimeGan(String dayGan, int timeIndex) {
        String[] tianGan = {"甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸"};
        
        // 日天干对应的起始时干索引
        int startIndex;
        switch (dayGan) {
            case "甲":
            case "己":
                startIndex = 0; // 甲子
                break;
            case "乙":
            case "庚":
                startIndex = 2; // 丙子
                break;
            case "丙":
            case "辛":
                startIndex = 4; // 戊子
                break;
            case "丁":
            case "壬":
                startIndex = 6; // 庚子
                break;
            case "戊":
            case "癸":
                startIndex = 8; // 壬子
                break;
            default:
                startIndex = 0;
        }
        
        return tianGan[(startIndex + timeIndex * 2) % 10];
    }
    
    /**
     * 获取时辰名称
     */
    private String getShiChen(int hour) {
        String[] shiChen = {"子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥"};
        return shiChen[(hour + 1) / 2 % 12];
    }
    
    /**
     * 获取时辰对应的时间范围
     */
    private String getTimeRange(int hour) {
        int startHour = (hour / 2) * 2 - 1;
        if (startHour < 0) startHour = 23;
        int endHour = startHour + 2;
        if (endHour >= 24) endHour = 1;
        return String.format("%02d:00-%02d:59", startHour, endHour);
    }
    
    /**
     * 计算生辰八字（DayunRequest重载版本）
     * @param request 大运请求参数
     * @return 八字信息
     */
    public BaziResponse calculateBazi(DayunRequest request) {
        // 将DayunRequest转换为BaziRequest
        BaziRequest baziRequest = new BaziRequest();
        baziRequest.setDate(request.getDate());
        baziRequest.setTime(request.getTime());
        baziRequest.setDateType(BaziRequest.DateType.valueOf(request.getDateType().name()));
        baziRequest.setLeapMonth(request.isLeapMonth());
        
        return calculateBazi(baziRequest);
    }
}