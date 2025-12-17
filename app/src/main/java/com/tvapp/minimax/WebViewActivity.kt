package com.tvapp.minimax

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.webkit.*
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class WebViewActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar
    private var url: String = ""
    private var title: String = ""

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        // 隐藏状态栏
        window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_FULLSCREEN or
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        )

        url = intent.getStringExtra("URL") ?: "https://www.5m9m1o8e7e9.shop/"
        title = intent.getStringExtra("TITLE") ?: "视频内容"

        webView = findViewById(R.id.webview)
        progressBar = findViewById(R.id.progress_bar)

        setupWebView()
        webView.loadUrl(url)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true
        webView.settings.builtInZoomControls = false
        webView.settings.displayZoomControls = false
        webView.settings.setSupportZoom(true)
        webView.settings.cacheMode = WebSettings.LOAD_DEFAULT
        webView.settings.mediaPlaybackRequiresUserGesture = false

        // 设置WebViewClient
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val url = request?.url.toString()
                return handleUrl(url)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progressBar.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progressBar.visibility = View.GONE
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                progressBar.visibility = View.GONE
            }
        }

        // 设置WebChromeClient以支持视频播放
        webView.webChromeClient = object : WebChromeClient() {
            override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
                super.onShowCustomView(view, callback)
                if (view is FrameLayout) {
                    setContentView(R.layout.activity_fullscreen_video)
                    val videoView = findViewById<FrameLayout>(R.id.video_container)
                    videoView.addView(view)

                    // 全屏视频的关闭按钮
                    val closeButton = findViewById<View>(R.id.close_button)
                    closeButton.setOnClickListener {
                        callback?.onCustomViewHidden()
                        setContentView(R.layout.activity_webview)
                        videoView.removeAllViews()
                    }
                }
            }

            override fun onHideCustomView() {
                super.onHideCustomView()
                setContentView(R.layout.activity_webview)
            }
        }
    }

    private fun handleUrl(url: String): Boolean {
        // 检查是否为视频URL
        if (isVideoUrl(url)) {
            val intent = android.content.Intent(this, VideoPlayerActivity::class.java)
            intent.putExtra("VIDEO_URL", url)
            startActivity(intent)
            return true
        }

        // 检查是否为应用内的其他页面
        if (url.contains("5m9m1o8e7e9.shop")) {
            webView.loadUrl(url)
            return true
        }

        // 其他URL直接在WebView中打开
        return false
    }

    private fun isVideoUrl(url: String): Boolean {
        val videoExtensions = listOf(".mp4", ".avi", ".mov", ".wmv", ".flv", ".webm", ".m3u8")
        return videoExtensions.any { url.contains(it) } || url.contains("video") || url.contains("play")
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }


    override fun onResume() {
        super.onResume()
        webView.onResume()
    }

    override fun onPause() {
        super.onPause()
        webView.onPause()
    }

    override fun onDestroy() {
        webView.destroy()
        super.onDestroy()
    }
}