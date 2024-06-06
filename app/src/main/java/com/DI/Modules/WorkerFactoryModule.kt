package com.DI.Modules

import androidx.work.CoroutineWorker
import com.DI.Scopes.WorkerKey
import com.webservicestasks.workers.ChildWorkerFactory
import com.webservicestasks.workers.WorkerEnviarLogs
import com.webservicestasks.workers.WorkerEnviarTicketsNoInsertados
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface WorkerFactoryModule {

    @Binds
    @IntoMap
    @WorkerKey(WorkerEnviarTicketsNoInsertados::class)
    fun bindWorkerFactory(provideFactory: WorkerEnviarTicketsNoInsertados.Factory): ChildWorkerFactory<out CoroutineWorker>

    @Binds
        @IntoMap
        @WorkerKey(WorkerEnviarLogs::class)
        fun bindWorkerFactoryLogs(provideFactory: WorkerEnviarLogs.Factory): ChildWorkerFactory<out CoroutineWorker>
}