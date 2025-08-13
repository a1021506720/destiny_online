package com.destiny.controller;

import com.destiny.dto.CompleteAnalysisRequest;
import com.destiny.dto.CompleteAnalysisResponse;
import com.destiny.service.CompleteAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 完整命理分析控制器
 */
@RestController
@RequestMapping("/api/analysis")
@CrossOrigin(origins = "*")
public class CompleteAnalysisController {
    
    @Autowired
    private CompleteAnalysisService completeAnalysisService;
    
    /**
     * 执行完整命理分析
     */
    @PostMapping("/complete")
    public CompleteAnalysisResponse performCompleteAnalysis(@RequestBody CompleteAnalysisRequest request) {
        // 设置默认值
        if (request.getLiunianYears() <= 0) {
            request.setLiunianYears(10);
        }
        
        return completeAnalysisService.performCompleteAnalysis(request);
    }
    
    /**
     * 健康检查接口
     */
    @GetMapping("/health")
    public String health() {
        return "Complete Analysis Service is running!";
    }
}