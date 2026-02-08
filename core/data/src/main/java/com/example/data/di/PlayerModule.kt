package com.example.data.di

import android.content.ComponentName
import android.content.Context
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.C.AUDIO_CONTENT_TYPE_MOVIE
import androidx.media3.common.C.USAGE_MEDIA
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.data.player.PlaybackService
import com.google.common.util.concurrent.ListenableFuture
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PlayerModule {

    private const val SEEK_INCREMENT_MS = 5000L

    @Provides
    @Singleton
    fun provideAudioAttributes(): AudioAttributes = AudioAttributes.Builder()
        .setContentType(AUDIO_CONTENT_TYPE_MOVIE)
        .setUsage(USAGE_MEDIA)
        .build()

    @Provides
    @Singleton
    fun provideExoPlayer(
        @ApplicationContext context: Context,
        audioAttributes: AudioAttributes
    ): ExoPlayer = ExoPlayer.Builder(context)
        .setSeekForwardIncrementMs(SEEK_INCREMENT_MS)
        .setSeekBackIncrementMs(SEEK_INCREMENT_MS)
        .setAudioAttributes(audioAttributes, true) // true = handleAudioFocus
        .setHandleAudioBecomingNoisy(true)
        .build()

    @Provides
    @Singleton
    fun provideMediaControllerFuture(
        @ApplicationContext context: Context
    ): ListenableFuture<MediaController> {
        val sessionToken = SessionToken(
            context,
            ComponentName(context, PlaybackService::class.java)
        )
        return MediaController.Builder(context, sessionToken).buildAsync()
    }
}