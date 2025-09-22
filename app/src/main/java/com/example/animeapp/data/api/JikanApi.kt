package com.example.animeapp.data.api

import com.example.animeapp.data.model.AnimeDetail
import com.example.animeapp.data.model.AnimeDetailResponse
import com.example.animeapp.data.model.TopAnimeResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface JikanApi {
    @GET("top/anime")
    suspend fun getTopAnime(): TopAnimeResponse

    @GET("anime/{id}")
    suspend fun getAnimeDetail(@Path("id") animeId: Int): AnimeDetailResponse
}
