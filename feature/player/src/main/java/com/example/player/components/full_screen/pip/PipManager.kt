package com.example.player.components.full_screen.pip

import android.app.Activity
import android.app.PictureInPictureParams
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Rect
import android.util.Rational

internal class PipManager {
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
}