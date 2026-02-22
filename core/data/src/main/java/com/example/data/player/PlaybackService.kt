package com.example.data.player

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Intent
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * A background service that manages the lifecycle of an [ExoPlayer] instance
 * and its associated [MediaSession].
 *
 * This service allows playback to continue when the app is in the background
 * or the screen is off. It integrates with the system media controls via
 * Media3's [MediaSession] API.
 *
 * @property player The [ExoPlayer] instance injected by Hilt, used as the
 * underlying media engine.
 * @property mediaSession The session that communicates with the system
 * and external controllers.
 */
@AndroidEntryPoint
class PlaybackService: MediaSessionService() {

    companion object {
        const val ACTION_STOP_BY_SLEEP_TIMER = "STOP_BY_SLEEP_TIMER"
        private const val FADE_OUT_DURATION = 5000L
    }

    @Inject
    lateinit var player: ExoPlayer

    private var mediaSession: MediaSession? = null
    private var fadeAnimator: ValueAnimator? = null

    override fun onCreate() {
        super.onCreate()
        mediaSession = MediaSession.Builder(this, player).build()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo) = mediaSession

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_STOP_BY_SLEEP_TIMER) {
            fadeOutAndPause()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        player.release()
        mediaSession?.release()
        mediaSession = null
        super.onDestroy()
    }

    private fun fadeOutAndPause() {
        fadeAnimator?.cancel()

        val startVolume = player.volume

        fadeAnimator = ValueAnimator.ofFloat(startVolume, 0f).apply {
            duration = FADE_OUT_DURATION
            addUpdateListener { animator ->
                player.volume = animator.animatedValue as Float
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    player.pause()
                    player.volume = startVolume
                }
            })
            start()
        }
    }
}