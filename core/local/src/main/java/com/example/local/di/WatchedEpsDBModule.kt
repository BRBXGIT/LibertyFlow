package com.example.local.di

import android.content.Context
import androidx.room.Room
import com.example.local.watched_eps_db.WatchedEpsDao
import com.example.local.watched_eps_db.WatchedEpsDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger Module for providing Room Database with watched episodes.
 */
@Module
@InstallIn(SingletonComponent::class)
object WatchedEpsDBModule {

    private const val WATCHED_EPS_DB_NAME = "WatchedEpsDb"

    /**
     * Provides the [WatchedEpsDao] for watched episodes.
     * Hilt will handle the database creation internally when providing the [WatchedEpsDao].
     */
    @Provides
    @Singleton
    fun provideWatchedEpsDao(
        @ApplicationContext context: Context
    ): WatchedEpsDao {
        return Room.databaseBuilder(
            context = context,
            klass = WatchedEpsDb::class.java,
            name = WATCHED_EPS_DB_NAME,
        )
            .build()
            .watchedEpsDao()
    }
}