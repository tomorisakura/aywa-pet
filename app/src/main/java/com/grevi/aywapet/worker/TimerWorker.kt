package com.grevi.aywapet.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.work.*
import com.grevi.aywapet.R
import com.grevi.aywapet.utils.Constant.ARG_TIMER
import com.grevi.aywapet.utils.Constant.CHANEL_ID
import com.grevi.aywapet.utils.Constant.NOTIFICATION_ID
import com.grevi.aywapet.utils.SharedUtils
import java.util.*

class TimerWorker @WorkerInject constructor(@Assisted appContext : Context, @Assisted params : WorkerParameters) : CoroutineWorker(appContext, params) {

    val TAG = TimerWorker::class.simpleName

    private val notificationManager = appContext.getSystemService(NotificationManager::class.java)
    private lateinit var countDownTimer: CountDownTimer
    private val initialCountDown : Long = 86400000 //24hours
    private val countDownInterval : Long = 1000

    private val sharedUtils = SharedUtils(appContext)

    override suspend fun doWork(): Result {
        return try {
            createNotificationChannel()
            val notification = NotificationCompat.Builder(applicationContext, CHANEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Berjalan dilatar belakang")
                    .build()

            val foreground = ForegroundInfo(NOTIFICATION_ID, notification)
            setForeground(foreground)
            val data = Handler(Looper.getMainLooper()).post{ setTimer() }
            val outputKeys =  workDataOf(ARG_TIMER to data)
            setProgress(data = outputKeys)
            Log.d(TAG, "WORKER_CONDITION..$data")
            Result.success()
        } catch (e : InterruptedException) {
            e.printStackTrace()
            Result.failure()
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var notificationChannel = notificationManager?.getNotificationChannel(CHANEL_ID)
            if (notificationChannel == null) {
                notificationChannel = NotificationChannel(
                        CHANEL_ID, TAG, NotificationManager.IMPORTANCE_LOW
                )
                notificationManager?.createNotificationChannel(notificationChannel)
            }
        }
    }

    private fun setTimer() {
        countDownTimer = object : CountDownTimer(initialCountDown, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                val hours = ((millisUntilFinished / (1000 * 60 * 60)) % 60)
                val minutes = (millisUntilFinished / (1000 * 60) % 60)
                val seconds = (millisUntilFinished / 1000) % 60
                val formatTime = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
                Log.d("TIMER_RUN", formatTime)
                sharedUtils.setCtShared(formatTime)
            }

            override fun onFinish() {
                createNotificationChannel()
                NotificationCompat.Builder(applicationContext, CHANEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Proses latar belakang selesai")
                        .build()
                WorkManager.getInstance(applicationContext).cancelAllWork()
            }

        }.start()
    }

}