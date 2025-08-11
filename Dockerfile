# 使用OpenJDK 8作为基础镜像
FROM openjdk:8-jre-slim

# 设置工作目录
WORKDIR /app

# 复制Maven wrapper和pom.xml
COPY mvnw .
COPY mvnw.cmd .
COPY pom.xml .
COPY .mvn .mvn

# 复制源代码
COPY src ./src

# 构建应用
RUN ./mvnw clean package -DskipTests

# 暴露端口
EXPOSE 8080

# 启动命令
CMD ["java", "-Dserver.port=${PORT:-8080}", "-jar", "target/destiny-online-0.0.1-SNAPSHOT.jar"]