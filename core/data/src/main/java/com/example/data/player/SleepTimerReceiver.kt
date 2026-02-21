package com.example.data.player

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * A [BroadcastReceiver] that listens for sleep timer events from the [SleepTimerManager].
 *
 * When triggered, this receiver sends a command to the [PlaybackService] to pause
 * audio playback. It acts as the execution point for the scheduled [SleepTimerManager].
 */
class SleepTimerReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val stopIntent = Intent(context, PlaybackService::class.java).apply {
            action = PlaybackService.ACTION_STOP_BY_SLEEP_TIMER
        }
        context?.startService(stopIntent)
    }
}