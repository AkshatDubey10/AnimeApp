package com.example.jikananime.data

import android.content.Context
import androidx.core.content.edit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyncPrefs @Inject constructor(private val ctx: Context) {
    private val prefs = ctx.getSharedPreferences("jikan_prefs", Context.MODE_PRIVATE)
    companion object { private const val KEY_LAST_SYNC = "last_sync" }
    fun setLastSync(ts: Long) = prefs.edit { putLong(KEY_LAST_SYNC, ts) }
    fun getLastSync(): Long = prefs.getLong(KEY_LAST_SYNC, 0L)
}
