@echo off
chcp 65001 >nul
setlocal enabledelayedexpansion

:: MiniMax TV 快速发布脚本 (Windows版本)
:: 使用方法: release.bat [版本号]

if "%~1"=="" (
    echo 错误: 请提供版本号
    echo 使用方法: %~nx0 版本号
    echo 示例: %~nx0 1.0.0
    exit /b 1
)

set VERSION=%~1

:: 验证版本号格式
echo %VERSION% | findstr /r "^[0-9]\+\.[0-9]\+\.[0-9]\+$" >nul
if errorlevel 1 (
    echo 错误: 版本号格式不正确
    echo 请使用语义化版本号: x.y.z (例如: 1.0.0)
    exit /b 1
)

for /f "tokens=2 delims==" %%i in ('wmic os get localdatetime /value') do set "dt=%%i"
set /a VERSION_CODE=%dt:~0,14%

echo ========================================
echo     MiniMax TV 发布脚本 v1.0.0
echo ========================================
echo.
echo 版本信息:
echo   版本号: %VERSION%
echo   版本代码: %VERSION_CODE%
echo.

:: 检查密钥库文件
if not exist "upload-keystore.jks" (
    echo 错误: 找不到密钥库文件 upload-keystore.jks
    echo 请先运行: keytool -genkey -v -keystore upload-keystore.jks -keyalg RSA -keysize 2048 -validity 10000 -alias upload
    exit /b 1
)

:: 检查keystore.properties文件
if not exist "app\keystore.properties" (
    echo 警告: 找不到 keystore.properties 文件
    echo 请确保密钥库配置正确
    exit /b 1
)

:: 读取密钥库密码
set /p STORE_PASSWORD=请输入密钥库密码:

:: 读取密钥密码
set /p KEY_PASSWORD=请输入密钥密码:

:: 验证密码
echo.
echo 验证密钥库...
echo %STORE_PASSWORD% | keytool -list -keystore upload-keystore.jks -alias upload -storepass:stdin >nul 2>&1
if errorlevel 1 (
    echo 错误: 密钥库密码不正确
    exit /b 1
)

echo ✓ 密钥库验证成功
echo.

:: 更新版本号
echo 更新版本号...
echo VERSION_NAME=%VERSION%> version.properties
echo VERSION_CODE=%VERSION_CODE%>> version.properties

echo ✓ 版本号已更新
echo.

:: Git操作
echo Git操作...

:: 检查是否有未提交的更改
git status --porcelain >nul 2>&1
if not errorlevel 1 (
    echo 发现未提交的更改，正在提交...
    git add .
    git commit -m "Release version %VERSION%"
    echo ✓ 代码已提交
) else (
    echo 没有发现更改，跳过提交
)

:: 创建标签
set TAG=v%VERSION%
git tag -l | findstr /c:"%TAG%" >nul
if not errorlevel 1 (
    echo 警告: 标签 %TAG% 已存在，正在删除...
    git tag -d %TAG%
)

git tag %TAG%
echo ✓ 标签 %TAG% 已创建
echo.

:: 推送到远程
echo 推送到远程仓库...
git push origin main
git push origin %TAG%
if errorlevel 1 (
    echo ✗ 推送失败
    exit /b 1
)

echo ✓ 推送成功
echo.

:: 显示结果
echo ========================================
echo       发布流程已完成！
echo ========================================
echo.
echo 接下来的步骤:
echo 1. 访问 GitHub 仓库
echo 2. 进入 'Actions' 标签
echo 3. 查看 'Build and Release APK' 工作流状态
echo 4. 等待构建完成（约5-10分钟）
echo 5. 进入 'Releases' 标签下载 APK
echo.
echo 版本信息:
echo   标签: %TAG%
for /f "tokens=*" %%i in ('git rev-parse --short HEAD') do set COMMIT=%%i
echo   提交: %COMMIT%
echo   版本代码: %VERSION_CODE%
echo.

:: 获取GitHub仓库URL
for /f "tokens=*" %%i in ('git remote get-url origin') do set REMOTE_URL=%%i
echo GitHub Actions URL:
echo   https://github.com/%REMOTE_URL:https://github.com/%|powershell -Command "$input | ForEach-Object { $_ -replace '.*github\.com[:/]([^/]*)/([^.]*)\.git', '$1/$2' }"
echo.

echo 发布脚本执行完成！
pause