# 🚀 Railway 部署完整指南

## 📋 准备工作清单

### ✅ 已完成的配置
- [x] Railway配置文件 (`railway.json`, `nixpacks.toml`)
- [x] 生产环境配置 (`application-prod.yml`)
- [x] Git忽略文件 (`.gitignore`)
- [x] 项目文档 (`README.md`)
- [x] 前端生产环境配置更新

### 🔜 需要你完成的步骤

## 第一步：创建GitHub仓库并推送后端代码

### 1.1 在GitHub创建仓库
1. 登录 https://github.com
2. 点击右上角 "+" → "New repository"
3. 仓库名：`destiny-online`
4. 设为Public（Railway免费版需要）
5. 不要初始化README（我们已有）

### 1.2 推送代码到GitHub
```bash
# 在后端项目目录执行
cd /Users/zenghao/Documents/project/destiny_online

# 初始化Git仓库
git init

# 添加所有文件
git add .

# 提交代码
git commit -m "feat: 初始化生辰八字算命后端服务

- 支持八字计算、大运分析、子平术分析
- 配置Railway部署环境
- 添加CORS支持和健康检查"

# 设置主分支
git branch -M main

# 添加远程仓库（替换YOUR_USERNAME为你的GitHub用户名）
git remote add origin https://github.com/YOUR_USERNAME/destiny-online.git

# 推送到GitHub
git push -u origin main
```

## 第二步：在Railway部署后端

### 2.1 注册Railway账号
1. 访问 https://railway.app
2. 使用GitHub账号登录
3. 授权Railway访问你的仓库

### 2.2 创建新项目
1. 点击 "Start a New Project"
2. 选择 "Deploy from GitHub repo"
3. 找到并选择 `destiny-online` 仓库
4. Railway会自动开始部署

### 2.3 查看部署状态
1. 部署过程大约3-5分钟
2. 可以在 "Deployments" 标签查看实时日志
3. 成功后会显示绿色的 "Success" 状态

### 2.4 获取部署URL
1. 在项目页面点击 "Settings"
2. 找到 "Environment" 部分
3. 点击 "Generate Domain"
4. 会得到类似 `https://destiny-online-production.up.railway.app` 的URL

### 2.5 测试后端API
```bash
# 测试健康检查
curl https://your-railway-url.up.railway.app/actuator/health

# 测试八字API
curl -X POST https://your-railway-url.up.railway.app/api/bazi/calculate \
  -H "Content-Type: application/json" \
  -d '{"date": "1990-01-01", "time": "12:00", "dateType": "SOLAR"}'
```

## 第三步：更新前端配置

### 3.1 更新生产API地址
编辑 `/Users/zenghao/Documents/project/destiny/config.js`：
```javascript
production: {
    apiBaseURL: 'https://你的railway域名.up.railway.app',
    corsEnabled: false
},
```

## 第四步：部署前端到Vercel

### 4.1 为前端创建GitHub仓库
```bash
# 在前端项目目录执行
cd /Users/zenghao/Documents/project/destiny

# 初始化Git仓库
git init
git add .
git commit -m "feat: 生辰八字算命前端应用

- 支持三种报告类型（八字、大运、子平术）
- 响应式设计和现代化UI
- 自动环境检测和API切换
- 完整的用户交互流程"

git branch -M main
git remote add origin https://github.com/YOUR_USERNAME/destiny-frontend.git
git push -u origin main
```

### 4.2 部署到Vercel
1. 访问 https://vercel.com
2. 使用GitHub登录
3. 点击 "New Project"
4. 导入 `destiny-frontend` 仓库
5. 保持默认设置，点击 "Deploy"
6. 获得 `.vercel.app` 域名

## 第五步：最终测试

### 5.1 功能测试清单
- [ ] 前端页面正常加载
- [ ] 介绍模态框显示
- [ ] 表单提交正常
- [ ] API调用成功
- [ ] 八字数据显示正确
- [ ] 大运数据显示正确  
- [ ] 子平术数据显示正确
- [ ] 命理解读生成正常
- [ ] 分享功能正常

### 5.2 监控和维护
- Railway提供免费的使用额度
- 可以在Railway面板查看日志和指标
- Vercel自动处理前端的缓存和CDN

## 🆘 常见问题

### Q: Railway部署失败怎么办？
A: 查看Deployments页面的日志，常见问题：
- 检查 `pom.xml` 语法是否正确
- 确保Java版本兼容（项目使用JDK 8）
- 检查依赖是否都能正常下载

### Q: API调用出现CORS错误？
A: 确保后端的CorsConfig正确配置，Railway部署后CORS策略可能需要调整

### Q: 前端访问后端API超时？
A: Railway的免费版应用可能会休眠，第一次访问需要等待几秒唤醒

## 💰 成本说明

### Railway
- 免费额度：每月500小时运行时间
- 休眠机制：无访问时自动休眠节省额度
- 付费升级：$5/月去除限制

### Vercel  
- 免费额度：每月100GB带宽
- 对于静态网站通常足够使用
- 自动HTTPS和全球CDN

## 🎉 部署完成

完成所有步骤后，你将拥有：
1. 完全托管的后端API服务
2. 全球CDN加速的前端应用
3. 自动HTTPS证书
4. 持续集成/持续部署

记得在两个项目的README中更新实际的域名地址！