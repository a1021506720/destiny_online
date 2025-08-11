package com.destiny.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(originPatterns = "*", maxAge = 3600)
public class HelloController {

    // 处理 GET /api/hello 请求；在 Go 中相当于 http.HandleFunc("/api/hello", handler)
    @GetMapping("/api/hello")
    public Map<String, Object> hello() {
        // 返回一个简单 JSON；在 Go 中通常会定义 struct 并用 json.Marshal 输出
        Map<String, Object> body = new HashMap<>(); // 响应体 Map
        body.put("message", "Destiny Online is up"); // 提示信息
        body.put("status", "ok"); // 状态码文本
        return body;
    }
}


