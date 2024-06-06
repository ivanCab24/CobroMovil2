package com.webservicestasks.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

interface ChildWorkerFactory<T : CoroutineWorker> {
    fun create(appContext: Context, workerParameters: WorkerParameters): T
}