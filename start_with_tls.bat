@echo off
echo ========================================
echo SQL Server 2008 JDK 11 TLS 1.0 启动脚本
echo ========================================

REM 设置JVM参数以启用TLS 1.0和1.1
set JAVA_OPTS=-Djdk.tls.client.protocols="TLSv1,TLSv1.1,TLSv1.2" -Dhttps.protocols="TLSv1,TLSv1.1,TLSv1.2" -Djdk.tls.server.protocols="TLSv1,TLSv1.1,TLSv1.2"

REM 移除TLS禁用算法（允许弱密码套件）
REM set JAVA_OPTS=%JAVA_OPTS% -Djdk.tls.disabledAlgorithms=""

REM 增加SSL调试信息（可选）
REM set JAVA_OPTS=%JAVA_OPTS% -Djavax.net.debug=ssl:handshake:verbose

REM 设置Java路径
if "%JAVA_HOME%"=="" (
    echo 警告: JAVA_HOME 环境变量未设置
    echo 尝试使用系统默认Java...
    set JAVA_CMD=java
) else (
    set JAVA_CMD="%JAVA_HOME%\bin\java"
)

REM 检查jar文件
if not exist "target\biddingdataserver.jar" (
    echo 错误: target\biddingdataserver.jar 不存在
    echo 请先执行: mvn clean package -DskipTests
    pause
    exit /b 1
)

echo.
echo 启动参数: %JAVA_OPTS%
echo.
echo 启动应用...
echo ========================================
echo.

%JAVA_CMD% %JAVA_OPTS% -jar target\biddingdataserver.jar

pause