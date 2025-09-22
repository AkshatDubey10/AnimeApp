package com.example.animeapp.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class AnimeDetailResponse(
    val data: AnimeDetail
)

@JsonClass(generateAdapter = true)
data class AnimeDetail(
    @Json(name = "mal_id") val malId: Int,
    val title: String,
    val synopsis: String?,
    val episodes: Int?,
    val score: Double?,
    val genres: List<Genre>,
    val trailer: Trailer?,
    val images: Images
)

@JsonClass(generateAdapter = true)
data class Genre(val name: String)

@JsonClass(generateAdapter = true)
data class Trailer(
    @Json(name = "youtube_id") val youtubeId: String?,
    @Json(name = "url") val url: String?
)
