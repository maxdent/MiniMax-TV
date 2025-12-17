#!/bin/bash

# MiniMax TV 快速发布脚本
# 使用方法: ./release.sh [版本号]

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 检查参数
if [ -z "$1" ]; then
    echo -e "${RED}错误: 请提供版本号${NC}"
    echo "使用方法: $0 <版本号>"
    echo "示例: $0 1.0.0"
    exit 1
fi

VERSION=$1

# 验证版本号格式
if ! [[ $VERSION =~ ^[0-9]+\.[0-9]+\.[0-9]+$ ]]; then
    echo -e "${RED}错误: 版本号格式不正确${NC}"
    echo "请使用语义化版本号: x.y.z (例如: 1.0.0)"
    exit 1
fi

VERSION_CODE=$(date +%s)

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}    MiniMax TV 发布脚本 v1.0.0${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""
echo -e "${YELLOW}版本信息:${NC}"
echo "  版本号: $VERSION"
echo "  版本代码: $VERSION_CODE"
echo ""

# 检查密钥库文件
if [ ! -f "upload-keystore.jks" ]; then
    echo -e "${RED}错误: 找不到密钥库文件 upload-keystore.jks${NC}"
    echo "请先运行: keytool -genkey -v -keystore upload-keystore.jks -keyalg RSA -keysize 2048 -validity 10000 -alias upload"
    exit 1
fi

# 检查keystore.properties文件
if [ ! -f "app/keystore.properties" ]; then
    echo -e "${YELLOW}警告: 找不到 keystore.properties 文件${NC}"
    echo "请确保密钥库配置正确"
    exit 1
fi

# 读取密钥库密码
echo -e "${YELLOW}请输入密钥库密码:${NC}"
read -s STORE_PASSWORD

# 读取密钥密码
echo -e "${YELLOW}请输入密钥密码:${NC}"
read -s KEY_PASSWORD

# 验证密码
echo ""
echo -e "${YELLOW}验证密钥库...${NC}"
if ! keytool -list -keystore upload-keystore.jks -alias upload -storepass "$STORE_PASSWORD" > /dev/null 2>&1; then
    echo -e "${RED}错误: 密钥库密码不正确${NC}"
    exit 1
fi

echo -e "${GREEN}✓ 密钥库验证成功${NC}"
echo ""

# 更新版本号
echo -e "${YELLOW}更新版本号...${NC}"
cat > version.properties << EOF
VERSION_NAME=$VERSION
VERSION_CODE=$VERSION_CODE
EOF

echo -e "${GREEN}✓ 版本号已更新${NC}"
echo ""

# 本地构建测试
echo -e "${YELLOW}本地构建测试...${NC}"
chmod +x gradlew
export KEYSTORE_PASSWORD="$STORE_PASSWORD"
export KEY_PASSWORD="$KEY_PASSWORD"

if ./gradlew assembleRelease; then
    echo -e "${GREEN}✓ 本地构建成功${NC}"
else
    echo -e "${RED}✗ 本地构建失败${NC}"
    exit 1
fi
echo ""

# Git操作
echo -e "${YELLOW}Git操作...${NC}"

# 检查是否有未提交的更改
if [ -n "$(git status --porcelain)" ]; then
    echo "发现未提交的更改，正在提交..."
    git add .
    git commit -m "Release version $VERSION"
    echo -e "${GREEN}✓ 代码已提交${NC}"
else
    echo -e "${YELLOW}没有发现更改，跳过提交${NC}"
fi

# 创建标签
TAG="v$VERSION"
if git tag -l | grep -q "^$TAG$"; then
    echo -e "${YELLOW}警告: 标签 $TAG 已存在，正在删除...${NC}"
    git tag -d "$TAG"
fi

git tag "$TAG"
echo -e "${GREEN}✓ 标签 $TAG 已创建${NC}"
echo ""

# 推送到远程
echo -e "${YELLOW}推送到远程仓库...${NC}"
if git push origin main && git push origin "$TAG"; then
    echo -e "${GREEN}✓ 推送成功${NC}"
else
    echo -e "${RED}✗ 推送失败${NC}"
    exit 1
fi
echo ""

# 显示结果
echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}      发布流程已完成！${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""
echo -e "${YELLOW}接下来的步骤:${NC}"
echo "1. 访问 GitHub 仓库"
echo "2. 进入 'Actions' 标签"
echo "3. 查看 'Build and Release APK' 工作流状态"
echo "4. 等待构建完成（约5-10分钟）"
echo "5. 进入 'Releases' 标签下载 APK"
echo ""
echo -e "${YELLOW}版本信息:${NC}"
echo "  标签: $TAG"
echo "  提交: $(git rev-parse --short HEAD)"
echo "  版本代码: $VERSION_CODE"
echo ""
echo -e "${GREEN}GitHub Actions URL:${NC}"
echo "  https://github.com/$(git remote get-url origin | sed 's/.*github.com[:/]\([^/]*\)\/\([^.]*\)\.git/\1\/\2/')/actions"
echo ""

# 清理密码变量
unset KEYSTORE_PASSWORD
unset KEY_PASSWORD

echo -e "${GREEN}发布脚本执行完成！${NC}"