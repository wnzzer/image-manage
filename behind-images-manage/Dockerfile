# 使用官方Java运行环境作为基础镜像
FROM openjdk:11-jre-slim

# 将本地的jar文件添加到容器中
COPY behind-images-manage-0.0.1-SNAPSHOT.jar /usr/src/myapp/myapp.jar

# 设置工作目录
WORKDIR /usr/src/myapp

# 设置容器在启动时运行Java应用
CMD ["java", "-jar", "myapp.jar"]

