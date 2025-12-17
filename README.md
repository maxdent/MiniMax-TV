# MiniMax TV - 安卓TV应用

这是一个专为安卓TV设计的应用，可以将网站 https://www.5m9m1o8e7e9.shop/ 的内容转换为TV友好的界面，支持视频播放和遥控器操作。

## 功能特性

- ✅ TV友好的用户界面
- ✅ WebView加载网站内容
- ✅ 专用视频播放器
- ✅ 遥控器导航支持
- ✅ 搜索功能
- ✅ 分类浏览
- ✅ 全屏视频播放

## 遥控器操作说明

### 主要按键功能
- **OK/ENTER**: 确认选择
- **方向键**: 导航菜单
- **BACK**: 返回上一页
- **快进/快退**: 在视频播放时使用方向键

### 视频播放控制
- **OK/ENTER**: 播放/暂停
- **左方向键**: 快退10秒
- **右方向键**: 快进10秒

## 安装说明

### 开发环境要求
- Android Studio Arctic Fox 或更高版本
- Android SDK 21+ (Android 5.0)
- Kotlin 1.9.10

### 构建步骤

1. 克隆项目到本地
```bash
git clone <项目地址>
cd android-tv-app
```

2. 在Android Studio中打开项目

3. 等待Gradle同步完成

4. 连接安卓TV设备或启动模拟器

5. 运行应用
```bash
./gradlew installDebug
```

### 构建APK
```bash
./gradlew assembleRelease
```

生成的APK文件位于 `app/build/outputs/apk/release/app-release.apk`

## 应用架构

### 主要组件

1. **MainActivity**: 主页面，使用Leanback框架
2. **WebViewActivity**: 网页浏览
3. **VideoPlayerActivity**: 视频播放
4. **SearchFragment**: 搜索功能

### 技术栈

- **语言**: Kotlin
- **UI框架**: Android Leanback (TV专用)
- **Web引擎**: WebView
- **视频播放**: VideoView + MediaController
- **架构**: MVVM模式

## 自定义配置

### 修改网站URL
在 `MainActivity.kt` 中修改URL:
```kotlin
mainAdapter.add(
    Movie("首页", "访问网站首页", R.drawable.ic_home, "YOUR_WEBSITE_URL")
)
```

### 添加更多分类
在 `MainActivity.kt` 的 `loadContent()` 方法中添加新的Movie对象。

### 自定义主题
修改 `res/values/themes.xml` 和 `res/values/colors.xml` 文件。

## 故障排除

### 常见问题

1. **WebView无法加载网站**
   - 检查网络连接
   - 确认网站支持移动端访问
   - 检查SSL证书设置

2. **视频无法播放**
   - 确认视频URL格式正确
   - 检查网络连接
   - 确认设备支持视频格式

3. **遥控器无响应**
   - 检查焦点设置
   - 确认按键映射正确

### 调试模式

启用调试日志:
```kotlin
WebView.setWebContentsDebuggingEnabled(true)
```

## 开发建议

1. **性能优化**
   - 使用WebView缓存
   - 预加载常用页面
   - 优化图片加载

2. **用户体验**
   - 添加加载进度指示
   - 实现错误处理
   - 优化遥控器导航

3. **安全考虑**
   - 验证所有URL
   - 限制网络访问
   - 加密敏感数据

## 许可证

本项目仅供学习和研究使用。使用时请遵守相关法律法规。

## 联系方式

如有问题或建议，请提交Issue或联系开发者。

---

**注意**: 请确保您有权使用目标网站的内容，并遵守相关法律法规。