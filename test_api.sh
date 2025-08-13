#!/bin/bash

# 完整命理分析API测试脚本

echo "========================================"
echo "🔮 命理分析系统 API 测试"
echo "========================================"

# 服务地址
BASE_URL="http://localhost:8082"

echo ""
echo "1️⃣ 测试健康检查接口..."
curl -s -X GET "$BASE_URL/api/analysis/health"
echo -e "\n"

echo ""
echo "2️⃣ 测试完整命理分析接口..."
echo "📋 请求参数："
echo "  👤 姓名: 李小姐"
echo "  📅 出生日期: 1985年8月25日"
echo "  🕙 出生时间: 上午10:30"
echo "  📍 日期类型: 阳历"
echo "  👩 性别: 女"
echo "  📊 流年分析: 8年"

echo ""
echo "🚀 发送请求中..."

curl -s -X POST "$BASE_URL/api/analysis/complete" \
  -H "Content-Type: application/json" \
  -d '{
    "date": "1985-08-25",
    "time": "10:30",
    "dateType": "SOLAR",
    "leapMonth": false,
    "gender": "FEMALE",
    "liunianYears": 8
  }' | python3 -m json.tool

echo ""
echo "========================================"
echo "✅ 测试完成！"
echo "========================================"