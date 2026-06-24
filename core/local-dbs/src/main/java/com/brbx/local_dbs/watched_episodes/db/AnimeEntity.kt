package com.brbx.local_dbs.watched_episodes.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AnimeEntity(
    @PrimaryKey val animeId: Int,
)