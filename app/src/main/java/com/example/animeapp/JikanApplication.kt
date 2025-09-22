package com.example.jikananime

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.animeapp.sync.SyncWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class JikanApplication : Application() {

    @Inject lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        // schedule periodic sync every 1 hour
        // val work = PeriodicWorkRequestBuilder<SyncWorker>(1, TimeUnit.HOURS).build()
        // WorkManager.getInstance(this).enqueueUniquePeriodicWork("jikan_sync", ExistingPeriodicWorkPolicy.KEEP, work)
    }
}
