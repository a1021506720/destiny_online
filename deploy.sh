#!/bin/bash

# 腾讯云轻量服务器部署脚本
# 使用方法: ./deploy.sh [服务器IP]

SERVER_IP=${1:-"你的服务器IP"}
APP_NAME="destiny-online"
JAR_FILE="target/destiny-online-0.0.1-SNAPSHOT.jar"

echo "🚀 开始部署生辰八字算命应用到腾讯云..."

# 1. 检查jar包是否存在
if [ ! -f "$JAR_FILE" ]; then
    echo "📦 正在构建应用..."
    ./mvnw clean package -DskipTests
    
    if [ $? -ne 0 ]; then
        echo "❌ 构建失败，请检查项目配置"
        exit 1
    fi
fi

echo "✅ 应用构建完成"

# 2. 上传jar包到服务器
echo "📤 正在上传应用到服务器..."
scp $JAR_FILE lighthouse@$SERVER_IP:/home/lighthouse/

if [ $? -ne 0 ]; then
    echo "❌ 上传失败，请检查服务器连接"
    echo "💡 确保已配置SSH密钥或使用密码认证"
    exit 1
fi

echo "✅ 应用上传完成"

# 3. 在服务器上部署
echo "🔧 正在服务器上配置应用..."
ssh lighthouse@$SERVER_IP << 'EOF'
# 创建日志目录
mkdir -p /home/lighthouse/logs

# 停止旧的应用进程
pkill -f "destiny-online"

# 等待进程完全停止
sleep 3

# 启动新的应用
nohup java -jar -Dspring.profiles.active=prod destiny-online-0.0.1-SNAPSHOT.jar > /home/lighthouse/logs/app.log 2>&1 &

# 等待应用启动
sleep 10

# 检查应用是否启动成功
if curl -f http://localhost:8080/actuator/health > /dev/null 2>&1; then
    echo "✅ 应用启动成功！"
    echo "🌐 访问地址: http://$(curl -s ifconfig.me):8080"
else
    echo "❌ 应用启动失败，请检查日志"
    tail -n 20 /home/lighthouse/logs/app.log
fi
EOF

echo "🎉 部署完成！"
echo ""
echo "📋 后续步骤："
echo "1. 访问 http://$SERVER_IP:8080/actuator/health 检查健康状态"
echo "2. 访问 http://$SERVER_IP:8080/api/bazi/info 测试API"
echo "3. 查看日志: ssh lighthouse@$SERVER_IP 'tail -f /home/lighthouse/logs/app.log'"