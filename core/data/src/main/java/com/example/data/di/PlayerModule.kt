package com.example.data.di

import android.content.ComponentName
import android.content.Context
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
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

/**
 * Hilt module responsible for providing media playback dependencies.
 *
 * This module is installed in the [SingletonComponent], meaning all provided
 * instances will exist for the duration of the application's lifecycle.
 */
@Module
@InstallIn(SingletonComponent::class)
object PlayerModule {

    /**
     * Provides [AudioAttributes] configured for movie/video content.
     * Sets the content type to [C.AUDIO_CONTENT_TYPE_MOVIE] and usage to [C.USAGE_MEDIA].
     */
    @Provides
    @Singleton
    fun provideAudioAttributes(): AudioAttributes = AudioAttributes.Builder()
        .setContentType(C.AUDIO_CONTENT_TYPE_MOVIE)
        .setUsage(C.USAGE_MEDIA)
        .build()

    /**
     * Provides a singleton instance of [ExoPlayer].
     *
     * Features enabled:
     * - **Audio Focus Management**: Automatically handles pausing/ducking when other apps play sound.
     * - **Noisy Intent Handling**: Automatically pauses playback when headphones are disconnected.
     *
     * @param context The application context.
     * @param audioAttributes The attributes defined in [provideAudioAttributes].
     */
    @Provides
    @Singleton
    fun provideExoPlayer(
        @ApplicationContext context: Context,
        audioAttributes: AudioAttributes
    ): ExoPlayer = ExoPlayer.Builder(context)
        .setAudioAttributes(audioAttributes, true) // true = handleAudioFocus
        .setHandleAudioBecomingNoisy(true)
        .build()

    /**
     * Provides a [ListenableFuture] for a [MediaController].
     *
     * This allows the UI components to asynchronously connect to the [PlaybackService]
     * using a [SessionToken].
     *
     * @param context The application context.
     */
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