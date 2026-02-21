package com.example.data.player

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages the scheduling and cancellation of sleep timers using the Android [AlarmManager].
 *
 * This manager allows the application to schedule a broadcast event to occur after a
 * specified duration, even if the device is in a low-power idle mode (Doze).
 *
 * @property context The application context used to access system services and create intents.
 * @constructor Injects the application context required for [AlarmManager] access.
 */
@Singleton
class SleepTimerManager @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun setTimer(minutes: Int) {
        val triggerTime = SystemClock.elapsedRealtime() + (minutes * 60 * 1000L)
        val intent = Intent(context, SleepTimerReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            triggerTime,
            pendingIntent
        )
    }

    fun cancelTimer() {
        val intent = Intent(context, SleepTimerReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context, 0, intent, PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
        }
    }
}