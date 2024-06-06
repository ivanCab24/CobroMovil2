package com.webservicestasks.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.Controller.BD.DAO.TicketsNoRegistradosDAO
import com.Controller.BD.Entity.TicketNoInsertados
import com.Utilities.Files
import com.Utilities.Notifications.NotificationHelper
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import com.webservicestasks.ReadJsonFeadTaskK
import com.webservicestasks.ToksWebServiceExceptionRate
import com.webservicestasks.ToksWebServicesConnection
import com.webservicestasks.ToksWebServicesConnection.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONException
import org.ksoap2.serialization.SoapObject
import javax.inject.Inject

class WorkerEnviarTicketsNoInsertados(
    private val context: Context,
    workerParams: WorkerParameters,
    private val preferenceHelper: PreferenceHelper,
    private val preferenceHelperLogs: PreferenceHelperLogs,
    private val files: Files,
    private val ticketsNoRegistradosDAO: TicketsNoRegistradosDAO
) : CoroutineWorker(context, workerParams) {

    private val TAG = "WorkerEnviarTicketsNoIn"
    private val notificationHelper = NotificationHelper(context)
    private var numeroDeTickets = 0
    private var ticketsCorrectos = 0

    override suspend fun doWork(): Result {

        withContext(Dispatchers.IO) {

            Log.i(TAG, "doWork: PreferenceHelper Injected $preferenceHelper")
            sendTicketsNoInsertados()

        }

        return Result.success()

    }

    private fun sendTicketsNoInsertados() {

        numeroDeTickets = ticketsNoRegistradosDAO.countRecords()

        if (numeroDeTickets > 0) {

            doStrongWork().initWork()
        }

    }

    class Factory @Inject constructor(
        private var preferenceHelper: PreferenceHelper,
        private var preferenceHelperLogs: PreferenceHelperLogs,
        private var files: Files,
        private var ticketsNoRegistradosDAO: TicketsNoRegistradosDAO
    ) : ChildWorkerFactory<WorkerEnviarTicketsNoInsertados> {
        override fun create(
            appContext: Context,
            workerParameters: WorkerParameters
        ): WorkerEnviarTicketsNoInsertados {
            return WorkerEnviarTicketsNoInsertados(
                appContext,
                workerParameters,
                preferenceHelper, preferenceHelperLogs, files, ticketsNoRegistradosDAO
            )
        }
    }

    inner class doStrongWork : ReadJsonFeadTaskK(), ToksWebServicesConnection {

        private val TAG = "WorkerEnviarTicketsNoIn"

        fun initWork() {

            try {

                val allTicketNoInsertados = getAllTicketsNoRegistrados()

                /*for (item in allTicketNoInsertados) {
                    initRegistrarTickets(item)
                }

                notificationHelper.sendHighPriorityNotification(
                    "A Comer Club",
                    "Tickets Enviados \n$ticketsCorrectos de $numeroDeTickets enviados correctamente"
                )*/

            } catch (e: Exception) {

                e.printStackTrace()
                files.createFileException(
                    "login/LoginActivityModel/executeTicketNoEnviados " +
                            Log.getStackTraceString(e), preferenceHelper
                )

            }

        }

        private fun getAllTicketsNoRegistrados(): List<TicketNoInsertados> {
            return ticketsNoRegistradosDAO.allTicketsNoInsertados
        }

        private fun initRegistrarTickets(ticketNoInsertados: TicketNoInsertados) {

            val request = createRequestTicketsNoInsertados(ticketNoInsertados)
            val result = doIn(request, INSERTA_TICKET, preferenceHelper)
            postResultFromAPITicket(result, ticketNoInsertados)

        }

        private fun postResultFromAPITicket(
            result: String,
            ticketNoInsertados: TicketNoInsertados
        ) {

            if (ToksWebServiceExceptionRate.rateError(result).isEmpty()) {

                files.registerLogs(
                    "Termina InsertaTicket No Registrados",
                    result, preferenceHelperLogs, preferenceHelper
                )

                try {

                    val jsonArray = JSONArray(result)
                    val code = jsonArray.getJSONObject(0).getString("code")

                    if (code == "200" || code == "400") {
                        ticketsCorrectos++
                        ticketsNoRegistradosDAO.deleteTicket(ticketNoInsertados)
                    }

                } catch (json: JSONException) {
                    json.printStackTrace()
                }


            }

        }

        private fun createRequestTicketsNoInsertados(ticket: TicketNoInsertados): SoapObject {

            val request = SoapObject(
                NAMESPACE,
                INSERTA_TICKET
            )
            request.addProperty("Llave", KEY)
            request.addProperty("ticket", ticket.ticket)
            request.addProperty("monto", ticket.monto)
            request.addProperty("membresia", ticket.membresia)
            request.addProperty("marca", ticket.marca)
            request.addProperty("fechaConsumo", ticket.fechaConsumo)

            files.registerLogs(
                "Inicia InsertaTicket No Registrados",
                request.toString(), preferenceHelperLogs, preferenceHelper
            )
            Log.i(TAG, "createRequestTicketsNoInsertados: $request")

            return request

        }

    }

}