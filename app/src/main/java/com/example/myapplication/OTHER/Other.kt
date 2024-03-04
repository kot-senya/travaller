package com.example.myapplication.OTHER

import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.TextView

object Other {

    fun rotateAnimation():RotateAnimation{
        var animation: RotateAnimation = RotateAnimation(-7f, 7f, 0f, 50f)
        animation.repeatMode = Animation.REVERSE
        animation.setRepeatCount(Animation.INFINITE)
        animation.interpolator = LinearInterpolator()
        animation.setDuration(450)
        return animation
    }

    fun countDownTimer_load(view:TextView):CountDownTimer{
        var timer:CountDownTimer = object :CountDownTimer(60000,300){
            override fun onTick(p0: Long) {
                when(p0 % 3){
                    0L -> view.text = "Идет загрузка..."
                    1L -> view.text = "Идет загрузка."
                    2L -> view.text = "Идет загрузка.."
                }
            }

            override fun onFinish() {
                this.start()
            }
        }

        return timer
    }
}