package com.tvapp.minimax

import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.MediaController
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity

class VideoPlayerActivity : AppCompatActivity() {

    private lateinit var videoView: VideoView
    private var videoUrl: String = ""
    private var mediaController: MediaController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)

        // 隐藏状态栏和导航栏
        window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_FULLSCREEN or
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        )

        videoUrl = intent.getStringExtra("VIDEO_URL") ?: ""

        videoView = findViewById(R.id.video_view)

        setupVideoPlayer()
        playVideo()
    }

    private fun setupVideoPlayer() {
        // 创建TV友好的媒体控制器
        mediaController = object : MediaController(this) {
            override fun show(timeout: Int) {
                super.show(timeout)
                // 5秒后自动隐藏控制器
                postDelayed({
                    hide()
                }, 5000)
            }
        }

        videoView.setMediaController(mediaController)

        // 设置视频完成监听
        videoView.setOnCompletionListener {
            // 视频播放完成，可以显示重播按钮或返回
        }

        // 设置错误监听
        videoView.setOnErrorListener { _, what, extra ->
            // 处理播放错误
            true
        }
    }

    private fun playVideo() {
        try {
            if (videoUrl.isNotEmpty()) {
                videoView.setVideoURI(Uri.parse(videoUrl))
                videoView.start()

                // 显示加载进度
                mediaController?.show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_ENTER -> {
                // 播放/暂停
                if (videoView.isPlaying) {
                    videoView.pause()
                } else {
                    videoView.start()
                }
                return true
            }
            KeyEvent.KEYCODE_DPAD_LEFT -> {
                // 快退10秒
                val currentPosition = videoView.currentPosition
                videoView.seekTo(currentPosition - 10000)
                return true
            }
            KeyEvent.KEYCODE_DPAD_RIGHT -> {
                // 快进10秒
                val currentPosition = videoView.currentPosition
                videoView.seekTo(currentPosition + 10000)
                return true
            }
            KeyEvent.KEYCODE_BACK -> {
                // 返回
                finish()
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            )
        }
    }

    override fun onResume() {
        super.onResume()
        videoView.resume()
    }

    override fun onPause() {
        super.onPause()
        videoView.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        videoView.stopPlayback()
    }
}