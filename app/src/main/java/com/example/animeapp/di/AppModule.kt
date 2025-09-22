package com.example.jikananime.di

import android.content.Context
import androidx.room.Room
import com.example.animeapp.data.api.JikanApi
import com.example.animeapp.data.db.AnimeDao
import com.example.animeapp.data.db.AnimeDatabase
import com.example.jikananime.data.SyncPrefs
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

  @Provides
  @Singleton
  fun provideMoshi(): Moshi = Moshi.Builder().build()

  @Provides
  @Singleton
  fun provideOkHttp(): OkHttpClient {
    val logger = HttpLoggingInterceptor().apply {
      level = HttpLoggingInterceptor.Level.BASIC
    }
    return OkHttpClient.Builder().addInterceptor(logger).build()
  }

  @Provides
  @Singleton
  fun provideRetrofit(moshi: Moshi, client: OkHttpClient): Retrofit =
    Retrofit.Builder()
      .baseUrl("https://api.jikan.moe/v4/") // include /v4 for endpoints
      .addConverterFactory(MoshiConverterFactory.create(moshi))
      .client(client)
      .build()

  @Provides
  @Singleton
  fun provideJikanApi(retrofit: Retrofit): JikanApi =
    retrofit.create(JikanApi::class.java)

  @Provides
  @Singleton
  fun provideDatabase(@ApplicationContext ctx: Context): AnimeDatabase =
    Room.databaseBuilder(ctx, AnimeDatabase::class.java, "jikan.db").build()

  @Provides
  fun provideAnimeDao(db: AnimeDatabase): AnimeDao = db.animeDao()

  @Provides
  @Singleton
  fun provideSyncPrefs(@ApplicationContext ctx: Context): SyncPrefs = SyncPrefs(ctx)
}
