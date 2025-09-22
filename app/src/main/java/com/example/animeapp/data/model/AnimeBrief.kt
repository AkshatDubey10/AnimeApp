package com.example.animeapp.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AnimeBrief(
  @Json(name = "mal_id") val malId: Int,
  val title: String,
  @Json(name = "episodes") val episodes: Int?,
  @Json(name = "score") val score: Double?,
  val images: Images? = null
)

@JsonClass(generateAdapter = true)
data class Images(
  val jpg: ImageFormat? = null,
  val webp: ImageFormat? = null
)

@JsonClass(generateAdapter = true)
data class ImageFormat(
  @Json(name = "image_url") val imageUrl: String? = null,
  @Json(name = "small_image_url") val smallImageUrl: String? = null,
  @Json(name = "large_image_url") val largeImageUrl: String? = null
)

@JsonClass(generateAdapter = true)
data class TopAnimeResponse(
  val data: List<AnimeBrief>
)
