package com.example.animeapp.data.db

import androidx.room.TypeConverter
import com.example.animeapp.data.model.Images
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class Converters {
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val adapter = moshi.adapter(Images::class.java)

    @TypeConverter
    fun fromImages(images: Images?): String? {
        return images?.let { adapter.toJson(it) }
    }

    @TypeConverter
    fun toImages(json: String?): Images? {
        return json?.let { adapter.fromJson(it) }
    }
}
