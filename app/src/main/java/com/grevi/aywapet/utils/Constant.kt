package com.grevi.aywapet.utils

object Constant {
    const val BASE_URL = "http://192.168.1.3:3000"
    const val RC_SIGN = 1
    const val MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 10

    const val NOTIFICATION_ID = 12
    const val CHANEL_ID = "foreground service"
    const val TIMER_WORKER_TAG = "timer tag"
    const val ARG_TIMER = "Progress"

    @JvmField val VERBOSE_NOTIFICATION_CHANNEL_NAME: CharSequence = "Verbose Notifications"
    const val VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION = "Shows notifications whenever work starts"
    const val CHANNEL_ID = "VERBOSE_NOTIFICATION"
    const val NOTIFICATION_FB_ID = 13
    const val SUCCESS_KEEP_TOPIC = "success-keep"
    const val CANCEL_KEEP_TOPIC = "cancel-keep"
    const val CHANNEL_FB_ID = "FIREBASE_NOTIFICATION"

    const val TIMER_KEY = "time_key"

    const val SECONDS_KEY = "seconds_key"
    const val MINUTES_KEY = "minutes_key"
    const val HOUR_KEY = "hour_key"
}