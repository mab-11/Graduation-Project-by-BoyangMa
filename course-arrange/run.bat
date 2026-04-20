@echo off
chcp 65001 >nul
set "JAVA_HOME=C:\Program Files\Java\jdk1.8.0_192"

echo [1/4] 检查Maven...
if exist "C:\maven\bin\mvn.cmd" goto :run_maven

echo [2/4] 下载Maven (阿里云镜像)...
powershell -Command "[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri 'https://mirrors.aliyun.com/apache/maven/maven-3/3.8.8/binaries/apache-maven-3.8.8-bin.zip' -OutFile '%TEMP%\maven.zip'"

echo [3/4] 解压Maven...
powershell -Command "Expand-Archive -Path '%TEMP%\maven.zip' -DestinationPath 'C:\' -Force"

:run_maven
set "MAVEN_HOME=C:\apache-maven-3.8.8"
set "PATH=%MAVEN_HOME%\bin;%JAVA_HOME%\bin;%PATH%"

echo [4/4] 编译并启动项目...
cd /d e:\jz\3500\代码\course-arrange

echo.
echo ========================================
echo 编译项目...
echo ========================================
call mvn clean compile -q

if errorlevel 1 (
    echo 编译失败!
    pause
    exit /b 1
)

echo.
echo ========================================
echo 启动Spring Boot...
echo ========================================
call mvn spring-boot:run

pause
