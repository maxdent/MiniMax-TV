package com.tvapp.minimax

import android.view.KeyEvent
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

object RemoteControllerHelper {

    // 遥控器按键映射
    const val DPAD_CENTER = KeyEvent.KEYCODE_DPAD_CENTER
    const val DPAD_UP = KeyEvent.KEYCODE_DPAD_UP
    const val DPAD_DOWN = KeyEvent.KEYCODE_DPAD_DOWN
    const val DPAD_LEFT = KeyEvent.KEYCODE_DPAD_LEFT
    const val DPAD_RIGHT = KeyEvent.KEYCODE_DPAD_RIGHT
    const val BACK = KeyEvent.KEYCODE_BACK
    const val MENU = KeyEvent.KEYCODE_MENU
    const val HOME = KeyEvent.KEYCODE_HOME

    // 焦点管理
    fun requestFocus(view: View) {
        view.isFocusable = true
        view.isFocusableInTouchMode = true
        view.requestFocus()
    }

    fun setFocusColor(view: View, focused: Boolean) {
        if (focused) {
            view.setBackgroundColor(android.graphics.Color.parseColor("#FF2E2E2E"))
            view.scaleX = 1.05f
            view.scaleY = 1.05f
        } else {
            view.setBackgroundColor(android.graphics.Color.parseColor("#FF1E1E1E"))
            view.scaleX = 1.0f
            view.scaleY = 1.0f
        }
    }

    // 导航动画
    fun animateFocusChange(view: View, hasFocus: Boolean) {
        if (hasFocus) {
            view.animate()
                .scaleX(1.05f)
                .scaleY(1.05f)
                .setDuration(150)
                .start()
        } else {
            view.animate()
                .scaleX(1.0f)
                .scaleY(1.0f)
                .setDuration(150)
                .start()
        }
    }

    // 全屏模式管理
    fun enterFullscreen(activity: androidx.fragment.app.FragmentActivity) {
        activity.window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_FULLSCREEN or
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        )
    }

    fun exitFullscreen(activity: androidx.fragment.app.FragmentActivity) {
        activity.window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_VISIBLE
        )
    }

    // 触摸反馈
    fun performRippleEffect(view: View) {
        view.setPressed(true)
        view.postDelayed({ view.setPressed(false) }, 100)
    }
}