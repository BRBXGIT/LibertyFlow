package com.example.local.watched_eps_db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Represents a specific anime series within the local database.
 * @property animeId Unique identifier for the anime (Primary Key).
 */
@Entity
data class AnimeEntity(
    @PrimaryKey
    val animeId: Int
)

/**
 * Represents an individual episode of an anime that has been marked as watched.
 * * This entity maintains a many-to-one relationship with [AnimeEntity] via a
 * foreign key constraint. If the parent anime is deleted, all associated
 * watched episodes are automatically removed.
 *
 * @property animeId The ID of the parent anime (Part of Composite Primary Key).
 * @property episodeIndex The specific episode number/index (Part of Composite Primary Key).
 * @property lastPosition Position when the user stop watching.
 */
@Entity(
    primaryKeys = [
        "animeId",
        "episodeIndex"
    ],
    foreignKeys = [
        ForeignKey(
            entity = AnimeEntity::class,
            parentColumns = ["animeId"],
            childColumns = ["animeId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["animeId"])
    ]
)

data class EpisodeEntity(
    val animeId: Int,
    val episodeIndex: Int,
    val lastPosition: Long
)