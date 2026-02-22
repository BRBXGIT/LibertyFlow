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

    private fun getTimerIntent(): PendingIntent? {
        val intent = Intent(context, SleepTimerReceiver::class.java)
        return PendingIntent.getBroadcast(
            /* context = */ context,
            /* requestCode = */ 0,
            /* intent = */ intent,
            /* flags = */ PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    fun setTimer(minutes: Int) {
        val totalDelayMillis = (minutes * 60 * 1000L)
        val triggerTime = SystemClock.elapsedRealtime() + totalDelayMillis

        val pendingIntent = getTimerIntent()
        if (pendingIntent != null) {
            alarmManager.setExactAndAllowWhileIdle(
                /* type = */ AlarmManager.ELAPSED_REALTIME_WAKEUP,
                /* triggerAtMillis = */ triggerTime,
                /* operation = */ pendingIntent
            )
        }
    }

    fun cancelTimer() {
        val pendingIntent = getTimerIntent()
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
        }
    }
}