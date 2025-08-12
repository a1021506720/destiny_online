@echo off
REM Windows服务器部署脚本
REM 使用方法: 将此脚本和jar包上传到Windows服务器，然后运行

echo 🚀 开始部署生辰八字算命应用...

REM 1. 创建日志目录
if not exist "logs" mkdir logs

REM 2. 停止旧的应用进程（如果存在）
echo 停止旧的应用进程...
taskkill /F /IM java.exe 2>nul
timeout /t 3 >nul

REM 3. 启动新的应用
echo 启动Spring Boot应用...
start /B java -jar -Dspring.profiles.active=prod destiny-online-0.0.1-SNAPSHOT.jar > logs\app.log 2>&1

REM 4. 等待应用启动
echo 等待应用启动...
timeout /t 15 >nul

REM 5. 检查应用是否启动成功
echo 检查应用健康状态...
curl -f http://localhost:8080/actuator/health > nul 2>&1
if %errorlevel% == 0 (
    echo ✅ 应用启动成功！
    echo 🌐 访问地址: http://119.91.70.176:8080
    echo 📊 健康检查: http://119.91.70.176:8080/actuator/health
    echo 📋 API信息: http://119.91.70.176:8080/api/bazi/info
) else (
    echo ❌ 应用启动失败，请检查日志
    echo 查看日志: type logs\app.log
)

echo 🎉 部署完成！
pause