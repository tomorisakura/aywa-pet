package com.grevi.aywapet.service

import android.app.Service
import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import com.grevi.aywapet.utils.Constant.TIMER_BR

class TimerService : Service() {

    private lateinit var countDownTimer: CountDownTimer
    private val initialCountDown : Long = 86400000 //24hours
    private val countDownInterval : Long = 1000

    private val TAG = TimerService::class.java.simpleName

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        setTimer()
    }

    private fun setTimer() {
        countDownTimer = object : CountDownTimer(initialCountDown, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                val hours = ((millisUntilFinished / (1000 * 60 * 60)) % 60)
                val minutes = (millisUntilFinished / (1000 * 60) % 60)
                val seconds = (millisUntilFinished / 1000) % 60
                Log.i(TAG, "running $hours : $minutes : $seconds")
                Intent(TIMER_BR).also {
                    it.putExtra("times", "$hours : $minutes : $seconds")
                    sendBroadcast(it)
                }
            }

            override fun onFinish() {
                //cancel
            }

        }.start()
    }


}