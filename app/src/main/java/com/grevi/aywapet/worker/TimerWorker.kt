package com.grevi.aywapet.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.grevi.aywapet.R
import com.grevi.aywapet.utils.Constant.CHANEL_ID
import com.grevi.aywapet.utils.Constant.HOUR_KEY
import com.grevi.aywapet.utils.Constant.MINUTES_KEY
import com.grevi.aywapet.utils.Constant.NOTIFICATION_ID
import com.grevi.aywapet.utils.Constant.SECONDS_KEY
import com.grevi.aywapet.utils.Constant.TIMER_KEY
import com.grevi.aywapet.utils.SharedUtils
import java.util.*
import javax.inject.Inject

class TimerWorker @WorkerInject constructor(@Assisted appContext : Context, @Assisted params : WorkerParameters) : CoroutineWorker(appContext, params) {

    val TAG = TimerWorker::class.simpleName

    private val notificationManager = appContext.getSystemService(NotificationManager::class.java)
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

            val seconds = inputData.getLong(SECONDS_KEY, 0L)
            val minutes = inputData.getLong(MINUTES_KEY, 0L)
            val hour = inputData.getLong(HOUR_KEY, 0)

            val stringFormat = String.format(Locale.getDefault(), "%02d:%02d:%02d", hour, minutes, seconds)
            setProgressAsync(workDataOf(TIMER_KEY to stringFormat))
            println("Running on do while ... $stringFormat")
            sharedUtils.setCtShared(stringFormat)

//            for (i in 0 until data) {
//                setProgressAsync(workDataOf(TIMER_KEY to i))
//                println(i)
//                delay(2000L)
//            }
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
}