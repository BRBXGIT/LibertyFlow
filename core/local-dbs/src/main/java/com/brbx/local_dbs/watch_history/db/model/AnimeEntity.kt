package com.brbx.local_dbs.watch_history.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AnimeEntity(
    @PrimaryKey val animeId: Int,
    val title: String,
)