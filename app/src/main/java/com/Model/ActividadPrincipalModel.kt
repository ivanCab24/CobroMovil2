package com.Model

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.Constants.ConstantsPreferences
import com.Controller.BD.DAO.UserDAO
import com.Interfaces.ActividadPrincipalMVP
import com.Utilities.FilesK
import com.Utilities.PreferenceHelper
import com.squareup.moshi.Moshi
import com.webservicestasks.ReadJsonFeadTaskK
import com.webservicestasks.ToksWebServicesConnection
import com.webservicestasks.workers.WorkerEnviarTicketsNoInsertados
import dagger.Lazy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ActividadPrincipalModel @Inject constructor(
    private var presenter: Lazy<ActividadPrincipalMVP.Presenter>,
    private var preferenceHelper: PreferenceHelper,
    private var files: FilesK,
    private var moshi: Moshi,
    private var context: Context,
    private var userDAO: UserDAO
) : ReadJsonFeadTaskK(), ToksWebServicesConnection, ActividadPrincipalMVP.Model, CoroutineScope {

    private val TAG = "LoginActivityModel"
    private val job = Job()

    override fun getMarca(): String {
        return preferenceHelper.getString(ConstantsPreferences.PREF_MARCA_SELECCIONADA, null)
    }

    override fun executeTicketNoEnviados() {
        val ticketsNoInsertadosWorkRequest =
            PeriodicWorkRequestBuilder<WorkerEnviarTicketsNoInsertados>(1, TimeUnit.HOURS)
                .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "ticketNoInsertadosWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            ticketsNoInsertadosWorkRequest
        )

    }

    override fun executeLogs() {
        //val logsWorkRequest =
        //            PeriodicWorkRequestBuilder<WorkerEnviarLogs>(30, TimeUnit.MINUTES).build()
        //        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        //            "logsWorkRequest",
        //            ExistingPeriodicWorkPolicy.KEEP,
        //            logsWorkRequest
        //        )
    }


    //================================================================================
    // Set Default Shared Preferences
    //================================================================================

    override fun executeDefaultSharedPreferences() {
        if (preferenceHelper.getString(ConstantsPreferences.PREF_SERVER, null).isEmpty()
            && preferenceHelper.getString(ConstantsPreferences.PREF_NUMERO_TERMINAL, null).isEmpty()
            && preferenceHelper.getString(ConstantsPreferences.PREF_CAJA_MP, null).isEmpty()
            && preferenceHelper.getString(ConstantsPreferences.PREF_UNIDAD, null).isEmpty()
        ) {
            preferenceHelper.putString(ConstantsPreferences.PREF_SERVER, "172.20.239.15")
            preferenceHelper.putString(ConstantsPreferences.PREF_NUMERO_TERMINAL, "3")
            preferenceHelper.putString(ConstantsPreferences.PREF_CAJA_MP, "C001")
            preferenceHelper.putString(ConstantsPreferences.PREF_UNIDAD, "11")
        }
    }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO


}