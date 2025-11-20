package com.vivek.abacusactivity

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.vivek.abacusactivity.worker.SyncGamesWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class AbacusApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        setupSyncWorkers()
    }

    private fun setupSyncWorkers() {
        val workManager = WorkManager.getInstance(this)

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED) // Only run when online
            .build()

        // 1. Periodic Sync (The "Safety Net")
        // Runs approx every 15 minutes, keeps the app synced in the background.
        val periodicRequest = PeriodicWorkRequestBuilder<SyncGamesWorker>(
            8, TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .build()

        workManager.enqueueUniquePeriodicWork(
            "SyncGamesPeriodic",
            ExistingPeriodicWorkPolicy.KEEP, // Don't restart timer if already scheduled
            periodicRequest
        )

        // 2. Immediate Sync on App Start
        // Forces a check right now when the app opens.
        val startupRequest = OneTimeWorkRequestBuilder<SyncGamesWorker>()
            .setConstraints(constraints)
            .build()

        workManager.enqueueUniqueWork(
            "SyncGamesStartup",
            ExistingWorkPolicy.KEEP, // If a sync is already running, let it finish
            startupRequest
        )
    }
}
