package com.webservicestasks.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import javax.inject.Inject
import javax.inject.Provider

class WorkerProviderFactory @Inject constructor(
    private val factories: MutableMap<Class<out CoroutineWorker>, @JvmSuppressWildcards Provider<ChildWorkerFactory<out CoroutineWorker>>>
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {

        val foundEntry = factories.entries.find { Class.forName(workerClassName).isAssignableFrom(it.key) }
        val factoryProvider = foundEntry?.value?: throw IllegalArgumentException("Unknown worker class name: $workerClassName")
        return factoryProvider.get().create(appContext, workerParameters)
    }
}