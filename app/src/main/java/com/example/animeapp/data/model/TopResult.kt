package com.example.animeapp.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TopResult(
    val data: List<AnimeBrief>,
    val isStale: Boolean = false
)
