package com.brbx.local_dbs.watch_history.db.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    primaryKeys = ["animeId", "episodeIndex"],
    foreignKeys = [
        ForeignKey(
            entity = AnimeEntity::class,
            parentColumns = ["animeId"],
            childColumns = ["animeId"],
            onDelete = ForeignKey.CASCADE,
        )
    ],
    indices = [
        Index(value = ["animeId"])
    ]
)
data class EpisodeEntity(
    val animeId: Int,
    val episodeIndex: Int,
    val lastPosition: Long,
)