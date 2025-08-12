# 🏝️ 腾讯云轻量应用服务器部署详细指南

## 📋 当前状态检查

### ✅ 已完成的准备工作
- [x] Spring Boot应用已构建完成
- [x] 生产环境配置已优化
- [x] 自动部署脚本已创建
- [x] 前端配置已更新

### 📦 本地构建结果
```bash
构建成功！生成的jar包：
/Users/zenghao/Documents/project/destiny_online/target/destiny-online-0.0.1-SNAPSHOT.jar
大小：约 15MB
```

---

## 🛒 第一步：购买腾讯云服务器（如果还没有）

### 1.1 访问购买页面
- 链接：https://cloud.tencent.com/product/lighthouse
- 点击 **"立即选购"**

### 1.2 配置选择（重要）
```
地域：广州 或 上海
镜像：应用镜像 → Java环境
套餐：1核2G 40GB SSD 3Mbps
价格：30元/年（新用户）
时长：1年
```

### 1.3 完成购买
- 确认订单 → 支付 → 等待开通（通常2分钟内）

---

## ⚙️ 第二步：获取服务器信息

### 2.1 进入控制台
1. 登录腾讯云控制台
2. 进入 **"轻量应用服务器"**
3. 找到你刚购买的服务器

### 2.2 记录重要信息
```bash
公网IP：xxx.xxx.xxx.xxx  # 记下这个IP
用户名：lighthouse
密码：（点击重置密码设置）
```

### 2.3 配置SSH连接（推荐）
```bash
# 在你的Mac上生成SSH密钥（如果还没有）
ssh-keygen -t rsa -b 2048

# 查看公钥
cat ~/.ssh/id_rsa.pub

# 在腾讯云控制台：
# 服务器详情 → 密钥 → 绑定密钥 → 粘贴公钥内容
```

---

## 🔒 第三步：配置安全组

### 3.1 开放端口
在腾讯云控制台：
1. 点击你的服务器
2. 选择 **"防火墙"** 标签
3. 点击 **"添加规则"**

### 3.2 添加以下规则
```
应用类型：自定义
协议：TCP
端口：8080
策略：允许
来源：0.0.0.0/0
备注：Spring Boot API端口
```

---

## 🚀 第四步：一键部署应用

### 方法A：使用自动化脚本（推荐）

```bash
# 在你的Mac上执行
cd /Users/zenghao/Documents/project/destiny_online

# 替换为你的实际服务器IP
./deploy.sh 你的服务器IP地址

# 例如：
# ./deploy.sh 123.456.789.123
```

脚本会自动完成：
- ✅ 上传jar包到服务器
- ✅ 创建日志目录
- ✅ 停止旧版本应用
- ✅ 启动新版本应用
- ✅ 检查应用健康状态

### 方法B：手动部署（如果脚本失败）

#### 4.1 上传文件
```bash
# 上传jar包
scp target/destiny-online-0.0.1-SNAPSHOT.jar lighthouse@你的IP:/home/lighthouse/
```

#### 4.2 连接服务器
```bash
ssh lighthouse@你的IP
```

#### 4.3 在服务器上执行
```bash
# 创建日志目录
mkdir -p logs

# 启动应用
nohup java -jar -Dspring.profiles.active=prod destiny-online-0.0.1-SNAPSHOT.jar > logs/app.log 2>&1 &

# 查看日志
tail -f logs/app.log
```

---

## 🧪 第五步：测试应用

### 5.1 健康检查
```bash
# 在浏览器或命令行测试
curl http://你的IP:8080/actuator/health

# 期望结果：
{"status":"UP"}
```

### 5.2 API功能测试
```bash
# 测试八字计算API
curl -X POST http://你的IP:8080/api/bazi/calculate \
  -H "Content-Type: application/json" \
  -d '{"date": "1990-01-01", "time": "12:00", "dateType": "SOLAR"}'

# 应该返回详细的八字数据
```

### 5.3 获取API信息
```bash
curl http://你的IP:8080/api/bazi/info
```

---

## 🌐 第六步：更新前端配置

### 6.1 修改前端API地址
编辑 `/Users/zenghao/Documents/project/destiny/config.js`：

```javascript
production: {
    apiBaseURL: 'http://你的实际IP:8080',
    corsEnabled: false
},
```

### 6.2 测试前端连接
1. 用浏览器打开前端页面
2. 打开开发者工具查看Console
3. 尝试生成一个测试报告
4. 确认API调用成功

---

## 📈 第七步：应用管理

### 7.1 查看应用状态
```bash
# SSH连接服务器后
ps aux | grep java                    # 查看Java进程
tail -f logs/app.log                  # 查看实时日志
tail -f logs/destiny-online.log       # 查看应用日志
```

### 7.2 重启应用
```bash
# 停止应用
pkill -f "destiny-online"

# 启动应用
nohup java -jar -Dspring.profiles.active=prod destiny-online-0.0.1-SNAPSHOT.jar > logs/app.log 2>&1 &
```

### 7.3 设置开机自启（可选）
```bash
# 创建systemd服务文件
sudo nano /etc/systemd/system/destiny-online.service
```

内容：
```ini
[Unit]
Description=Destiny Online Service
After=network.target

[Service]
Type=simple
User=lighthouse
WorkingDirectory=/home/lighthouse
ExecStart=/usr/bin/java -jar -Dspring.profiles.active=prod destiny-online-0.0.1-SNAPSHOT.jar
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
```

启用服务：
```bash
sudo systemctl enable destiny-online
sudo systemctl start destiny-online
sudo systemctl status destiny-online
```

---

## 🎯 成功标志

完成部署后，你应该能够：

### ✅ 后端服务正常
- [ ] `http://你的IP:8080/actuator/health` 返回 `{"status":"UP"}`
- [ ] `http://你的IP:8080/api/bazi/info` 返回API信息
- [ ] 所有API端点正常响应

### ✅ 前端功能正常
- [ ] 页面正常加载
- [ ] 表单提交成功
- [ ] 八字、大运、子平术数据正确显示
- [ ] 命理解读正常生成

---

## 🆘 故障排除

### 问题1：无法访问8080端口
**解决方案**：
```bash
# 检查防火墙规则
sudo ufw status
sudo ufw allow 8080

# 检查腾讯云安全组设置
```

### 问题2：应用启动失败
**解决方案**：
```bash
# 查看详细错误日志
tail -n 50 logs/app.log

# 检查Java版本
java -version

# 检查内存使用
free -h
```

### 问题3：前端无法调用后端API
**解决方案**：
1. 检查前端config.js中的IP地址是否正确
2. 确认浏览器Console没有CORS错误
3. 测试API是否可以直接访问

---

## 💰 费用说明

### 首年费用：30元
- 包含：1核2G服务器 + 40G存储 + 3M带宽
- 续费：108元/年

### 流量费用：免费
- 轻量服务器包含充足的流量包
- 对于你的应用完全够用

---

## 🎉 下一步计划

1. **域名绑定**（可选）
   - 购买域名：腾讯云域名注册约29元/年
   - 配置解析：免费

2. **SSL证书**（可选）
   - 腾讯云免费SSL证书
   - 升级为HTTPS访问

3. **前端部署**
   - 推荐使用Vercel或Netlify
   - 免费静态网站托管

4. **监控告警**（可选）
   - 配置服务器监控
   - 应用异常自动通知

---

需要我帮你执行哪个步骤？或者遇到任何问题都可以随时询问！