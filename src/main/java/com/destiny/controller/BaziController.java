package com.destiny.controller;

import com.destiny.dto.BaziRequest;
import com.destiny.dto.BaziResponse;
import com.destiny.dto.DayunRequest;
import com.destiny.dto.DayunResponse;
import com.destiny.service.BaziService;
import com.destiny.service.DayunService;
import com.destiny.service.ZipingAnalysisService;
import com.destiny.dto.ZipingAnalysisResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 八字计算 REST API 控制器
 * 在 Go 中相当于 HTTP Handler
 */
@RestController
@RequestMapping("/api/bazi")
@CrossOrigin(originPatterns = "*", maxAge = 3600)
public class BaziController {
    
    @Autowired  // 依赖注入，在 Go 中通常通过函数参数或包级变量实现
    private BaziService baziService;
    
    @Autowired
    private DayunService dayunService;
    
    @Autowired
    private ZipingAnalysisService zipingAnalysisService;
    
    /**
     * 计算生辰八字
     * POST /api/bazi/calculate
     * 
     * 请求示例：
     * {
     *   "date": "1990-05-15",
     *   "time": "23:30",
     *   "dateType": "SOLAR"
     * }
     */
    @PostMapping("/calculate")
    public ResponseEntity<?> calculateBazi(@RequestBody BaziRequest request) {
        try {
            // 参数校验
            if (request.getDate() == null || request.getTime() == null) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "日期和时间不能为空");
                return ResponseEntity.badRequest().body(error);
            }
            
            // 计算八字
            BaziResponse response = baziService.calculateBazi(request);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * 获取API使用说明
     * GET /api/bazi/info
     */
    @GetMapping("/info")
    public Map<String, Object> getApiInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("description", "生辰八字计算API");
        info.put("endpoints", new String[]{
            "POST /api/bazi/calculate - 计算生辰八字",
            "POST /api/bazi/dayun - 计算大运排盘",
            "POST /api/bazi/ziping - 子平术分析"
        });
        
        Map<String, Object> example = new HashMap<>();
        example.put("date", "1990-05-15");
        example.put("time", "23:30");
        example.put("dateType", "SOLAR");
        example.put("leapMonth", false);
        info.put("exampleRequest", example);
        
        Map<String, String> notes = new HashMap<>();
        notes.put("子时说明", "23:00-23:59为晚子时，按次日子时计算；00:00-00:59为早子时，按当日计算");
        notes.put("日期格式", "yyyy-MM-dd");
        notes.put("时间格式", "HH:mm");
        notes.put("日期类型", "SOLAR（阳历）或 LUNAR（农历）");
        info.put("notes", notes);
        
        return info;
    }
    
    /**
     * 计算大运排盘
     * POST /api/bazi/dayun
     * 
     * 请求示例：
     * {
     *   "date": "1990-05-15",
     *   "time": "14:30",
     *   "dateType": "SOLAR",
     *   "gender": "MALE",
     *   "years": 80
     * }
     */
    @PostMapping("/dayun")
    public ResponseEntity<?> calculateDayun(@RequestBody DayunRequest request) {
        try {
            // 参数校验
            if (request.getDate() == null || request.getTime() == null || request.getGender() == null) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "日期、时间和性别不能为空");
                return ResponseEntity.badRequest().body(error);
            }
            
            // 计算大运
            DayunResponse response = dayunService.calculateDayun(request);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * 子平术分析
     * POST /api/bazi/ziping
     * 
     * 请求示例：
     * {
     *   "date": "1997-07-18",
     *   "time": "16:30",
     *   "dateType": "SOLAR"
     * }
     */
    @PostMapping("/ziping")
    public ResponseEntity<?> analyzeZiping(@RequestBody BaziRequest request) {
        try {
            // 参数校验
            if (request.getDate() == null || request.getTime() == null) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "日期和时间不能为空");
                return ResponseEntity.badRequest().body(error);
            }
            
            // 先计算基本八字
            BaziResponse baziInfo = baziService.calculateBazi(request);
            
            // 进行子平术分析
            ZipingAnalysisResult zipingResult = zipingAnalysisService.analyzeZiping(baziInfo);
            
            return ResponseEntity.ok(zipingResult);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
