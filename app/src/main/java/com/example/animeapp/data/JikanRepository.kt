package com.example.animeapp.data

import android.content.Context
import android.util.Log
import com.example.animeapp.data.api.JikanApi
import com.example.animeapp.data.db.AnimeDao
import com.example.animeapp.data.db.AnimeEntity
import com.example.animeapp.data.model.AnimeBrief
import com.example.animeapp.data.model.AnimeDetail
import com.example.animeapp.data.model.TopResult
import com.example.animeapp.util.NetworkUtils
import com.example.animeapp.util.toEntity
import com.example.jikananime.data.SyncPrefs
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class JikanRepository @Inject constructor(
  private val api: JikanApi,
  private val animeDao: AnimeDao,
  @ApplicationContext private val ctx: Context,
  private val prefs: SyncPrefs,
  @Named("IO") private val ioDispatcher: CoroutineDispatcher
) {

  fun topAnimeFlow(): Flow<TopResult> {
    val dbFlow = animeDao.getAllAnime().map { entities ->
      entities.map { entity -> AnimeBrief(entity.malId, entity.title, entity.episodes, entity.score, entity.images) }
    }
    return dbFlow.map { list -> TopResult(list, isStale = !NetworkUtils.isOnline(ctx)) }
  }

  suspend fun refreshTopAnime() = withContext(ioDispatcher) {
    if (!NetworkUtils.isOnline(ctx)) return@withContext
    val resp = api.getTopAnime()
    val list = resp.data
    val entities = list.map { brief ->
      Log.i("JikanRepository", "refreshTopAnime breif: $brief")
      AnimeEntity(
        brief.malId, brief.title, brief.episodes, brief.score, brief.images
      )
    }
    Log.i("JikanRepository", "refreshTopAnime entities: $entities")
    animeDao.insertAll(entities)
    // update last sync
    prefs.setLastSync(System.currentTimeMillis())
  }

  suspend fun getAnimeDetail(malId: Int): AnimeDetail? = withContext(ioDispatcher) {

    // fallback to network if available
    return@withContext try {
      val dto = api.getAnimeDetail(malId).data
      Log.i("JikanRepository", "getAnimeDetail res: $dto")
      animeDao.insert(dto.toEntity())
      dto
    } catch (e: Exception) {
      Log.i("JikanRepository", "getAnimeDetail exception: $e")
      null
    }
  }
}
