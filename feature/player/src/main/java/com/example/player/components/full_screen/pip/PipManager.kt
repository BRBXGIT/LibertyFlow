package com.example.player.components.full_screen.pip

import android.app.Activity
import android.app.PictureInPictureParams
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Rect
import android.util.Rational
import com.example.common.ui_helpers.utils.findActivity

/**
 * A helper class that encapsulates the logic for entering and configuring
 * Picture-in-Picture (PiP) mode.
 *
 * This manager bridges the gap between the Compose layout (which provides visual bounds)
 * and the Android Activity (which handles the system-level transition).
 */
internal class PipManager {
    /**
     * Stores the global coordinates of the video player view.
     * * This is used as a "Source Rect Hint" to provide a smooth, continuous
     * scaling animation when the video transitions from the app into the
     * floating PiP window.
     */
    var videoViewBounce = Rect()

    fun isPipSupported(context: Context): Boolean {
        val activity = context as Activity
        return activity.packageManager.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE)
    }

    fun updatedPipParams(): PictureInPictureParams? {
        return PictureInPictureParams.Builder()
            .setSourceRectHint(videoViewBounce)
            .setAspectRatio(Rational(16, 9))
            .build()
    }

    fun tryEnterPip(context: Context) {
        if (isPipSupported(context)) {
            val params = updatedPipParams() ?: return
            (context.findActivity())?.enterPictureInPictureMode(params)
        }
    }
}