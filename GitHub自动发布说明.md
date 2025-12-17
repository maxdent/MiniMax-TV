# 🚀 GitHub 自动发布说明

## 📋 快速开始

### 1️⃣ 初始化项目

```bash
# 1. 克隆或创建项目
git init
git add .
git commit -m "Initial commit"

# 2. 连接GitHub仓库
git remote add origin https://github.com/YOUR_USERNAME/MiniMax-TV.git

# 3. 推送代码
git push -u origin main
```

### 2️⃣ 配置签名密钥

**生成密钥库**:
```bash
keytool -genkey -v -keystore upload-keystore.jks -keyalg RSA -keysize 2048 -validity 10000 -alias upload
```

**配置keystore.properties**:
```properties
storeFile=../upload-keystore.jks
storePassword=你的密码
keyAlias=upload
keyPassword=你的密钥密码
```

### 3️⃣ 设置GitHub Secrets

在GitHub仓库设置中添加以下Secrets：

| 名称 | 值 |
|------|-----|
| `KEYSTORE_FILE` | 密钥库文件的Base64编码 |
| `KEYSTORE_PASSWORD` | 密钥库密码 |
| `KEY_ALIAS` | upload |
| `KEY_PASSWORD` | 密钥密码 |

### 4️⃣ 发布新版本

#### 方法一: 使用发布脚本

**Linux/macOS**:
```bash
chmod +x release.sh
./release.sh 1.0.0
```

**Windows**:
```cmd
release.bat 1.0.0
```

#### 方法二: 手动发布

```bash
# 1. 更新版本号
echo "VERSION_NAME=1.0.0" > version.properties
echo "VERSION_CODE=$(date +%s)" >> version.properties

# 2. 提交并创建标签
git add .
git commit -m "Release version 1.0.0"
git tag v1.0.0
git push origin main v1.0.0
```

#### 方法三: GitHub界面触发

1. 进入 Actions 页面
2. 选择 "Build and Release APK"
3. 点击 "Run workflow"
4. 选择分支并运行

---

## 📦 工作流程说明

### GitHub Actions 工作流

#### 1. build-and-release.yml
- **触发**: 推送版本标签或手动触发
- **功能**: 构建APK并发布到GitHub Releases
- **输出**:
  - app-release.apk (已签名发布版)
  - app-debug.apk (调试版)

#### 2. restore-keystore.yml
- **触发**: 手动触发
- **功能**: 恢复密钥库文件并测试构建
- **用途**: 验证密钥库配置

#### 3. auto-tag-release.yml
- **触发**: 推送到main分支且提交信息为 "Release version"
- **功能**: 自动创建版本标签

---

## 🔐 安全配置

### 密钥库管理

1. **本地生成**:
```bash
keytool -genkey -v -keystore upload-keystore.jks \
  -keyalg RSA -keysize 2048 -validity 10000 -alias upload
```

2. **Base64编码**:
```bash
# Linux/macOS
base64 -w 0 upload-keystore.jks

# Windows PowerShell
[Convert]::ToBase64String([IO.File]::ReadAllBytes("upload-keystore.jks"))
```

3. **GitHub Secrets**:
   - 将Base64编码字符串保存为 `KEYSTORE_FILE` Secret
   - 保存密码到相应的Secrets中

---

## 📊 发布状态监控

### 查看构建状态

1. **GitHub Actions**:
   - 访问仓库 → Actions 标签
   - 查看工作流运行状态
   - 点击工作流查看详细日志

2. **GitHub Releases**:
   - 访问仓库 → Releases 标签
   - 查看所有发布版本
   - 下载APK文件

### 构建成功标志

✅ **成功的标志**:
- Actions页面显示绿色勾号
- Releases页面显示新版本
- APK文件可以正常下载和安装

❌ **失败的标志**:
- Actions页面显示红色叉号
- 需要查看日志排查问题

---

## 🛠️ 故障排除

### 常见问题

#### 1. 密钥库验证失败
```
Error: Input required and not supplied: KEYSTORE_FILE
```
**解决**: 检查GitHub Secrets是否正确设置

#### 2. 构建失败
```
Execution failed for task ':app:packageRelease'.
```
**解决**:
- 验证密钥库密码
- 检查keystore.properties配置
- 运行restore-keystore.yml测试

#### 3. 版本号错误
```
Could not get unknown property 'VERSION_CODE'
```
**解决**:
- 确认version.properties文件存在
- 检查文件格式（等号分隔）

### 调试步骤

1. **检查工作流日志**:
   - Actions → 点击失败的工作流
   - 查看详细错误信息

2. **本地测试构建**:
   ```bash
   ./gradlew assembleRelease
   ```

3. **验证密钥库**:
   ```bash
   keytool -list -keystore upload-keystore.jks -alias upload
   ```

---

## 📚 最佳实践

### 版本管理

1. **语义化版本**:
   - 主版本.次版本.修订版本
   - 例如: 1.0.0, 1.1.0, 1.1.1

2. **版本代码递增**:
   - 每次发布必须递增
   - 使用时间戳确保唯一性

3. **提交信息规范**:
   ```
   Release version 1.0.0
   ```

### 发布流程

1. **开发完成**:
   - 所有功能测试通过
   - 代码审查完成

2. **更新版本号**:
   - 修改version.properties
   - 提交更改

3. **创建发布**:
   - 运行发布脚本
   - 或手动创建标签

4. **监控构建**:
   - 确认Actions执行成功
   - 验证APK下载可用

5. **通知用户**:
   - 更新发布说明
   - 提供下载链接

---

## 📞 支持

### 资源链接

- [GitHub Actions文档](https://docs.github.com/en/actions)
- [Android应用签名](https://developer.android.com/studio/publish/app-signing)
- [工作流配置参考](.github/workflows/)

### 获取帮助

1. 查看工作流日志
2. 检查Secrets配置
3. 验证密钥库设置
4. 联系技术支持

---

## 📝 示例发布

### 示例1: 首次发布

```bash
# 1. 生成密钥库
keytool -genkey -v -keystore upload-keystore.jks \
  -keyalg RSA -keysize 2048 -validity 10000 -alias upload

# 2. 配置keystore.properties
cat > app/keystore.properties << EOF
storeFile=../upload-keystore.jks
storePassword=yourpassword
keyAlias=upload
keyPassword=yourkeypassword
EOF

# 3. 首次发布
./release.sh 1.0.0
```

### 示例2: 后续更新

```bash
# 更新版本到1.0.1
./release.sh 1.0.1
```

### 示例3: 紧急修复

```bash
# 修复版本
./release.sh 1.0.1-hotfix
```

---

**最后更新**: 2025-12-17
**版本**: 1.0.0