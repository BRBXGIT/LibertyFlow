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
class PlaybackService : MediaSessionService() {

    @Inject
    lateinit var player: ExoPlayer

    private var mediaSession: MediaSession? = null
    private lateinit var notificationManager: PlayerNotificationManager

    override fun onCreate() {
        super.onCreate()

        val channel = NotificationChannel(
            "playback_channel",
            "Playback",
            NotificationManager.IMPORTANCE_LOW
        )
        getSystemService(NotificationManager::class.java)
            .createNotificationChannel(channel)

        mediaSession = MediaSession.Builder(this, player).build()

        notificationManager =
            PlayerNotificationManager.Builder(
                this,
                1,
                "playback_channel"
            )
                .setSmallIconResourceId(LibertyFlowIcons.LibertyFlow)
                .setMediaDescriptionAdapter(object :
                    PlayerNotificationManager.MediaDescriptionAdapter {

                    override fun getCurrentContentTitle(player: Player) =
                        "LibertyFlow"

                    override fun createCurrentContentIntent(player: Player) = null

                    override fun getCurrentContentText(player: Player) =
                        player.mediaMetadata.title?.toString()

                    override fun getCurrentLargeIcon(
                        player: Player,
                        callback: PlayerNotificationManager.BitmapCallback
                    ) = null
                })
                .setNotificationListener(object :
                    PlayerNotificationManager.NotificationListener {

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
                })
                .build()

        notificationManager.setPlayer(player)
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo) =
        mediaSession

    override fun onDestroy() {
        notificationManager.setPlayer(null)
        mediaSession?.release()
        player.release()
        super.onDestroy()
    }
}
