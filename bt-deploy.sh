#!/bin/bash
# 宝塔面板服务器部署脚本

echo "🎯 宝塔面板 - Spring Boot应用部署脚本"
echo "服务器IP: 119.91.70.176"

# 1. 创建应用目录
APP_DIR="/www/wwwroot/destiny-online"
sudo mkdir -p $APP_DIR
cd $APP_DIR

echo "✅ 创建应用目录: $APP_DIR"

# 2. 下载应用文件（如果本地上传失败的话）
echo "📦 请手动上传jar包到: $APP_DIR"
echo "   文件: destiny-online-0.0.1-SNAPSHOT.jar"

# 3. 创建启动脚本
cat > start.sh << 'EOF'
#!/bin/bash
# Spring Boot应用启动脚本

APP_NAME="destiny-online"
JAR_FILE="destiny-online-0.0.1-SNAPSHOT.jar"
PID_FILE="$APP_NAME.pid"

# 检查是否已运行
if [ -f "$PID_FILE" ]; then
    PID=$(cat $PID_FILE)
    if ps -p $PID > /dev/null 2>&1; then
        echo "应用已在运行 (PID: $PID)"
        exit 1
    else
        rm -f $PID_FILE
    fi
fi

# 启动应用
echo "🚀 启动 $APP_NAME..."
nohup java -jar -Dspring.profiles.active=prod \
    -Dserver.port=8080 \
    -Xmx512m -Xms256m \
    $JAR_FILE > app.log 2>&1 &

# 保存PID
echo $! > $PID_FILE
echo "✅ 应用启动成功 (PID: $!)"
echo "📋 访问地址: http://119.91.70.176:8080"
echo "📊 健康检查: http://119.91.70.176:8080/actuator/health"
EOF

# 4. 创建停止脚本
cat > stop.sh << 'EOF'
#!/bin/bash
# Spring Boot应用停止脚本

APP_NAME="destiny-online"
PID_FILE="$APP_NAME.pid"

if [ -f "$PID_FILE" ]; then
    PID=$(cat $PID_FILE)
    if ps -p $PID > /dev/null 2>&1; then
        echo "🛑 停止应用 (PID: $PID)..."
        kill $PID
        sleep 3
        
        # 强制停止
        if ps -p $PID > /dev/null 2>&1; then
            kill -9 $PID
            echo "⚠️  强制停止应用"
        else
            echo "✅ 应用已停止"
        fi
        rm -f $PID_FILE
    else
        echo "❌ 应用未运行"
        rm -f $PID_FILE
    fi
else
    echo "❌ 找不到PID文件，应用可能未运行"
fi
EOF

# 5. 创建重启脚本
cat > restart.sh << 'EOF'
#!/bin/bash
echo "🔄 重启应用..."
./stop.sh
sleep 2
./start.sh
EOF

# 6. 设置执行权限
chmod +x start.sh stop.sh restart.sh

echo ""
echo "🎉 部署脚本创建完成！"
echo ""
echo "📋 使用方法："
echo "1. 上传jar包到: $APP_DIR"
echo "2. 启动应用: ./start.sh"
echo "3. 停止应用: ./stop.sh"  
echo "4. 重启应用: ./restart.sh"
echo "5. 查看日志: tail -f app.log"
echo ""
echo "🌐 测试地址："
echo "- 健康检查: http://119.91.70.176:8080/actuator/health"
echo "- API信息: http://119.91.70.176:8080/api/bazi/info"