package com.example.local.watched_eps_db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

// Entity representing a anime
@Entity
data class AnimeEntity(
    // Primary key for the anime
    @PrimaryKey
    val animeId: Int
)

// Entity representing a watched episode of a anime
@Entity(
    // Composite primary key: a anime + episode number must be unique
    primaryKeys = ["animeId", "episodeIndex"],

    // Foreign key linking this table to TitleEntity
    foreignKeys = [
        ForeignKey(
            entity = AnimeEntity::class,      // Parent table
            parentColumns = ["animeId"],      // Column in parent table
            childColumns = ["animeId"],       // Column in this table
            onDelete = ForeignKey.CASCADE     // Delete episodes when the anime is deleted
        )
    ],

    // Index to speed up queries filtering by animeId
    indices = [Index(value = ["animeId"])]
)

data class EpisodeEntity(

    // ID of the related anime
    val animeId: Int,

    // Episode index that has been watched
    val episodeIndex: Int
)