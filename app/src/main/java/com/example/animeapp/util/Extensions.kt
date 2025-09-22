package com.example.animeapp.util

import com.example.animeapp.data.db.AnimeEntity
import com.example.animeapp.data.model.AnimeDetail

fun AnimeDetail.toEntity(): AnimeEntity {
    return AnimeEntity(
        malId = this.malId,
        title = this.title,
        episodes = this.episodes ?: 0,
        score = this.score ?: 0.0,
        images = this.images
    )
}
