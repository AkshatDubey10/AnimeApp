package com.example.animeapp.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.animeapp.data.model.Images

@Entity(tableName = "anime")
data class AnimeEntity(
    @PrimaryKey val malId: Int,
    val title: String,
    val episodes: Int?,
    val score: Double?,
    val images: Images? = null,
    val isStale: Boolean = false,
    val lastUpdated: Long = System.currentTimeMillis()
)
