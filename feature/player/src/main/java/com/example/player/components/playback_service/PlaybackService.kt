package com.example.player.components.playback_service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.ui.PlayerNotificationManager
import com.example.design_system.theme.icons.LibertyFlowIcons
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@UnstableApi
@AndroidEntryPoint
class PlaybackService: MediaSessionService() {
    @Inject
    lateinit var player: ExoPlayer

    private var mediaSession: MediaSession? = null
    private lateinit var notificationManager: PlayerNotificationManager

    private companion object {
        const val CHANNEL_ID = "playback_channel"
        const val CHANNEL_NAME = "Playback"
        const val NOTIFICATION_ID = 1
    }

    override fun onCreate() {
        super.onCreate()

        createNotificationChannel()
        initMediaSession()
        initNotificationManager()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }

    override fun onDestroy() {
        notificationManager.setPlayer(null)
        mediaSession?.release()
        player.release()
        super.onDestroy()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )

        getSystemService(NotificationManager::class.java)
            .createNotificationChannel(channel)
    }

    private fun initMediaSession() {
        mediaSession = MediaSession.Builder(this, player).build()
    }

    private fun initNotificationManager() {
        notificationManager =
            PlayerNotificationManager.Builder(
                this,
                NOTIFICATION_ID,
                CHANNEL_ID
            )
                .setSmallIconResourceId(LibertyFlowIcons.LibertyFlow)
                .setMediaDescriptionAdapter(mediaDescriptionAdapter)
                .setNotificationListener(notificationListener)
                .build()

        notificationManager.setPlayer(player)
    }

    private val mediaDescriptionAdapter =
        object : PlayerNotificationManager.MediaDescriptionAdapter {

            override fun getCurrentContentTitle(player: Player): CharSequence {
                return player.mediaMetadata.artist.toString()
            }

            override fun createCurrentContentIntent(player: Player) = null

            override fun getCurrentContentText(player: Player): CharSequence? {
                return player.mediaMetadata.title?.toString()
            }

            override fun getCurrentLargeIcon(
                player: Player,
                callback: PlayerNotificationManager.BitmapCallback
            ) = null
        }

    private val notificationListener =
        object : PlayerNotificationManager.NotificationListener {

            override fun onNotificationPosted(
                notificationId: Int,
                notification: Notification,
                ongoing: Boolean
            ) {
                startForeground(notificationId, notification)
            }

            override fun onNotificationCancelled(
                notificationId: Int,
                dismissedByUser: Boolean
            ) {
                stopSelf()
            }
        }
}