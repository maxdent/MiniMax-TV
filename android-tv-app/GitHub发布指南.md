# 🚀 GitHub 发布指南

## 📋 目录
1. [环境准备](#环境准备)
2. [配置签名密钥](#配置签名密钥)
3. [设置GitHub Actions](#设置github-actions)
4. [发布新版本](#发布新版本)
5. [自动发布流程](#自动发布流程)
6. [故障排除](#故障排除)

---

## 🔧 环境准备

### 1. 创建GitHub仓库

```bash
# 初始化Git仓库
git init

# 添加所有文件
git add .

# 提交代码
git commit -m "Initial commit"

# 连接GitHub仓库（替换为你的仓库地址）
git remote add origin https://github.com/YOUR_USERNAME/MiniMax-TV.git

# 推送代码
git push -u origin main
```

### 2. 创建签名密钥库

**在本地生成签名密钥**：

```bash
# 进入项目目录
cd android-tv-app

# 生成密钥库（运行一次即可）
keytool -genkey -v -keystore upload-keystore.jks -keyalg RSA -keysize 2048 -validity 10000 -alias upload
```

**密钥库信息填写示例**：
```
Enter keystore password: 你的密码
Re-enter new password: 你的密码
What is your first and last name?: YourName
What is your name of your Organizational Unit?: Dev
What is your name of your Organization?: YourOrg
What is your City or Locality?: Beijing
What is your State or Province?: Beijing
What is your Two-Letter Country Code?: CN
Is CN=YourName, OU=Dev, O=YourOrg, L=Beijing, ST=Beijing, C=CN correct?: yes

Enter key password for <upload>: 你的密钥密码
```

**将密钥库文件保存**：
- 将 `upload-keystore.jks` 文件放置在 `android-tv-app/` 目录下
- **重要**: 不要将密钥库文件提交到GitHub！

### 3. 配置密钥库信息

编辑 `app/keystore.properties` 文件：

```properties
storeFile=../upload-keystore.jks
storePassword=YOUR_STORE_PASSWORD
keyAlias=upload
keyPassword=YOUR_KEY_PASSWORD
```

**更新版本号**：

编辑 `version.properties` 文件：
```properties
VERSION_NAME=1.0.0
VERSION_CODE=1
```

---

## ⚙️ 设置GitHub Actions

### 1. 配置GitHub Secrets

在GitHub仓库中设置以下Secrets：

1. 进入仓库 → Settings → Secrets and variables → Actions
2. 点击 "New repository secret"
3. 添加以下Secrets：

#### **必须添加的Secrets**：

| Secret Name | Value | 说明 |
|-------------|-------|------|
| `KEYSTORE_FILE` | 密钥库文件的Base64编码 | 使用以下命令生成：<br>`base64 -w 0 upload-keystore.jks` |
| `KEYSTORE_PASSWORD` | 密钥库密码 | 生成密钥库时设置的密码 |
| `KEY_ALIAS` | `upload` | 密钥别名 |
| `KEY_PASSWORD` | 密钥密码 | 密钥的密码 |

#### **获取Base64编码的密钥库**：

**Linux/macOS**:
```bash
base64 -w 0 upload-keystore.jks
```

**Windows PowerShell**:
```powershell
[Convert]::ToBase64String([IO.File]::ReadAllBytes("upload-keystore.jks"))
```

### 2. 创建密钥库恢复工作流

创建文件 `.github/workflows/restore-keystore.yml`：

```yaml
name: Restore Keystore

on:
  workflow_dispatch:

jobs:
  restore:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v4

    - name: Create keystore file
      run: |
        echo "${{ secrets.KEYSTORE_FILE }}" | base64 -d > upload-keystore.jks

    - name: Verify keystore
      run: |
        keytool -list -keystore upload-keystore.jks -alias upload
      env:
        KEYSTORE_PASSWORD: ${{ secrets.KEYSTORE_PASSWORD }}
```

---

## 📦 发布新版本

### 方法1: 手动创建标签发布

**步骤1**: 更新版本号

编辑 `version.properties`：
```properties
VERSION_NAME=1.0.0
VERSION_CODE=1
```

**步骤2**: 提交并推送代码

```bash
git add .
git commit -m "Release version 1.0.0"

# 创建标签
git tag v1.0.0

# 推送代码和标签
git push origin main
git push origin v1.0.0
```

**步骤3**: 验证发布

1. 打开GitHub仓库页面
2. 进入 "Actions" 标签
3. 等待构建完成
4. 进入 "Releases" 标签
5. 查看新创建的Release

### 方法2: 使用GitHub界面手动触发

**步骤1**: 推送代码到main分支

**步骤2**: 进入Actions页面

**步骤3**: 选择 "Build and Release APK" 工作流

**步骤4**: 点击 "Run workflow"

**步骤5**: 选择分支并运行

---

## 🔄 自动发布流程

### GitHub Actions工作流说明

#### 1. 构建工作流 (build-and-release.yml)

**触发条件**：
- 推送版本标签（如 v1.0.0）
- 手动触发

**工作流程**：
```
1. 检出代码
2. 设置JDK 11
3. 设置Android SDK
4. 构建Release APK
5. 构建Debug APK
6. 创建GitHub Release
7. 上传APK文件
```

**生成的APK**：
- `app-release.apk` - 已签名的发布版本
- `app-debug.apk` - 调试版本

#### 2. 自动标签工作流 (auto-tag-release.yml)

**触发条件**：
- 推送到main分支且提交信息为 "Release version"

**功能**：
- 自动从build.gradle提取版本号
- 创建版本标签
- 触发构建流程

---

## 📱 获取APK文件

### 从GitHub Releases下载

1. 打开GitHub仓库页面
2. 进入 "Releases" 标签
3. 点击最新版本
4. 下载 `app-release.apk` 文件

### 从GitHub Actions下载

1. 进入 "Actions" 标签
2. 选择工作流运行记录
3. 点击 "release-apk" 或 "debug-apk" artifact
4. 下载ZIP文件并解压

---

## 🔐 安全配置

### 密钥库管理

1. **本地保存**：
   - 密钥库文件仅保存在本地
   - 不提交到Git仓库

2. **GitHub Secrets**：
   - 密钥库密码存储在GitHub Secrets中
   - 永远不会暴露在代码或日志中

3. **访问权限**：
   - GitHub Actions有权限访问Secrets
   - 工作流执行完成后Secrets不会泄露

### 代码保护

1. **ProGuard混淆**：
   ```gradle
   buildTypes {
       release {
           minifyEnabled true
           proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
       }
   }
   ```

2. **敏感信息过滤**：
   - 创建 `.gitignore` 文件
   - 过滤密钥库和敏感文件

---

## ❗ 故障排除

### 常见错误

#### 1. 签名错误
**错误信息**：
```
Execution failed for task ':app:packageRelease'.
> A failure occurred while executing com.android.build.gradle.internal.tasks.PackageAndroidArtifactImpl
```

**解决方案**：
- 检查密钥库文件路径
- 验证密码是否正确
- 确认密钥别名正确

#### 2. 版本号错误
**错误信息**：
```
Could not get unknown property 'VERSION_CODE' for extension 'app'
```

**解决方案**：
- 确认 `version.properties` 文件存在
- 检查文件格式（使用等号，不是冒号）
- 确保文件编码为UTF-8

#### 3. GitHub Secrets未设置
**错误信息**：
```
Error: Input required and not supplied: KEYSTORE_FILE
```

**解决方案**：
- 在GitHub仓库设置中添加所有必需的Secrets
- 确认Secrets名称拼写正确
- 重新运行工作流

#### 4. 权限错误
**错误信息**：
```
Permission denied (publickey)
```

**解决方案**：
- 使用HTTPS而不是SSH
- 确认GitHub token有足够权限
- 检查工作流文件中的token使用

### 调试技巧

1. **查看工作流日志**：
   - Actions页面 → 点击工作流 → 查看步骤日志

2. **本地测试构建**：
   ```bash
   ./gradlew assembleRelease
   ```

3. **验证密钥库**：
   ```bash
   keytool -list -keystore upload-keystore.jks
   ```

---

## 📊 最佳实践

### 版本管理

1. **语义化版本**：
   - 主版本.次版本.修订版本
   - 例如：1.0.0, 1.1.0, 1.1.1

2. **版本代码**：
   - 每次发布递增
   - 用于Google Play识别更新

3. **提交信息规范**：
   ```
   Release version 1.0.0
   ```

### 安全最佳实践

1. **密钥库安全**：
   - 使用强密码
   - 设置较长的有效期
   - 备份到安全位置

2. **Secrets管理**：
   - 定期更新密码
   - 最小权限原则
   - 监控异常访问

3. **代码审查**：
   - 所有更改需要审查
   - 禁止直接提交到main分支
   - 使用Pull Request流程

---

## 📞 支持与帮助

### 资源链接

- [GitHub Actions文档](https://docs.github.com/en/actions)
- [Android签名指南](https://developer.android.com/studio/publish/app-signing)
- [Android发布检查清单](https://developer.android.com/distribute/marketing-tools/alternative-distribution)

### 获取帮助

1. 查看GitHub Actions日志
2. 检查工作流配置文件
3. 验证所有Secrets设置
4. 联系技术支持

---

## 🎯 快速参考

### 发布新版本流程

```bash
# 1. 更新版本号
echo "VERSION_NAME=1.0.0" > version.properties
echo "VERSION_CODE=1" >> version.properties

# 2. 提交代码
git add .
git commit -m "Release version 1.0.0"

# 3. 创建标签
git tag v1.0.0

# 4. 推送
git push origin main
git push origin v1.0.0
```

### 手动触发构建

1. GitHub仓库 → Actions
2. 选择 "Build and Release APK"
3. 点击 "Run workflow"
4. 等待完成

---

**最后更新**: 2025-12-17
**文档版本**: 1.0.0