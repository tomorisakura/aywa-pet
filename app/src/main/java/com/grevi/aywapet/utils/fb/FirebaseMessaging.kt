package com.grevi.aywapet.utils.fb

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.grevi.aywapet.utils.makeStatusNotification

class FirebaseMessaging : FirebaseMessagingService() {
    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        if (p0.data.isNotEmpty()) {
            makeStatusNotification(p0.notification?.title!!, p0.notification?.body!!, this)
        }
    }
}