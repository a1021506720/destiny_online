# 🔮 Destiny Online - 生辰八字算命后端服务

基于Spring Boot的中国传统命理分析服务，支持八字计算、大运分析、子平术等功能。

## 🚀 快速开始

### 本地开发
```bash
# 运行应用
./mvnw spring-boot:run

# 或者打包后运行
./mvnw clean package
java -jar target/destiny-online-0.0.1-SNAPSHOT.jar
```

### API端点
- 健康检查: `GET /actuator/health`
- 八字计算: `POST /api/bazi/calculate`
- 大运分析: `POST /api/bazi/dayun`
- 子平术分析: `POST /api/bazi/ziping`
- API信息: `GET /api/bazi/info`

## 🌐 Railway部署

### 前置条件
1. GitHub账号
2. Railway账号 (https://railway.app)

### 部署步骤

#### 1. 创建GitHub仓库
```bash
cd /Users/zenghao/Documents/project/destiny_online
git init
git add .
git commit -m "Initial commit: Spring Boot destiny online service"
git branch -M main
git remote add origin https://github.com/YOUR_USERNAME/destiny-online.git
git push -u origin main
```

#### 2. 连接Railway
1. 访问 https://railway.app
2. 点击 "Start a New Project"
3. 选择 "Deploy from GitHub repo"
4. 选择你的 `destiny-online` 仓库
5. Railway会自动检测到Spring Boot项目并开始部署

#### 3. 配置环境变量（可选）
- `SPRING_PROFILES_ACTIVE=prod`
- `MAVEN_OPTS=-Xmx512m`

#### 4. 获取部署URL
部署完成后，Railway会提供一个类似这样的URL：
`https://destiny-online-production.up.railway.app`

## 📝 环境配置

### 开发环境
- 端口: 8080
- 配置文件: `application.yml`

### 生产环境（Railway）
- 动态端口: `$PORT`
- 配置文件: `application-prod.yml`
- 自动HTTPS支持

## 🔧 技术栈
- **框架**: Spring Boot 2.7.18
- **Java版本**: JDK 8
- **构建工具**: Maven
- **依赖**: 
  - Spring Web
  - Spring Actuator
  - Lunar Calendar (6tail)

## 📊 监控
- 健康检查: `/actuator/health`
- 应用信息: `/actuator/info`
- 指标监控: `/actuator/metrics`

## 🛡️ CORS配置
生产环境已配置CORS支持，允许前端应用跨域访问。

## 📞 联系
如有问题，请查看Railway部署日志或联系开发者。