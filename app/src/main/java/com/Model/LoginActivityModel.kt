package com.Model

import android.content.Context
import android.util.Log
import android.widget.Spinner
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.Constants.ConstantsPreferences
import com.Controller.BD.DAO.UserDAO
import com.Controller.BD.Entity.UserEntity
import com.DataModel.User
import com.Interfaces.LoginAcitivityMVP
import com.Utilities.Curl
import com.Utilities.FilesK
import com.Utilities.PreferenceHelper
import com.Utilities.Utilities
import com.Utilities.Utils
import com.innovacion.eks.beerws.databinding.SeleccionDeImpresoraBinding
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonEncodingException
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.webservicestasks.ReadJsonFeadTaskK
import com.webservicestasks.ToksWebServiceExceptionRate
import com.webservicestasks.ToksWebServicesConnection
import com.webservicestasks.ToksWebServicesConnection.KEY
import com.webservicestasks.ToksWebServicesConnection.LOG_USUARIO
import com.webservicestasks.ToksWebServicesConnection.NAMESPACE
import com.webservicestasks.ToksWebServicesConnection.STRING
import com.webservicestasks.workers.WorkerEnviarLogs
import com.webservicestasks.workers.WorkerEnviarTicketsNoInsertados
import dagger.Lazy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.joda.time.DateTime
import org.json.JSONArray
import org.ksoap2.serialization.SoapObject
import java.lang.reflect.Type
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class LoginActivityModel @Inject constructor(
    private var presenter: Lazy<LoginAcitivityMVP.Presenter>,
    private var preferenceHelper: PreferenceHelper,
    private var files: FilesK,
    private var moshi: Moshi,
    private var context: Context,
    private var userDAO: UserDAO
) : ReadJsonFeadTaskK(), ToksWebServicesConnection, LoginAcitivityMVP.Model, CoroutineScope {

    private val TAG = "LoginActivityModel"
    private val job = Job()
    private var binding: SeleccionDeImpresoraBinding? = null
    private var spinnerPrinter : Spinner? = null;

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
        val logsWorkRequest =
            PeriodicWorkRequestBuilder<WorkerEnviarLogs>(30, TimeUnit.MINUTES).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork("logsWorkRequest", ExistingPeriodicWorkPolicy.KEEP, logsWorkRequest
        )

    }

    override fun getVersion():Boolean {
        Log.i("holaaaaaaa", "")
        var params= HashMap<String,String>()
        params["Llave"] = "e936c17f7b54f02d32@@5463e0c3f749080b9"
        params["Cadena"] = STRING
        params["id_local"] = preferenceHelper.getString(ConstantsPreferences.PREF_UNIDAD, null)

        var reponse = Curl.sendPostRequest(params,"Revisa_version", preferenceHelper,1)
        val jsonArray = JSONArray(reponse)
        val VERSION = jsonArray.getJSONObject(0).getString("VERSION")
        Log.i("**Version", jsonArray.toString())
        if ( VERSION == Utilities.getVersionName()) {
            return false
        }
        return true
    }

    override fun executeLoginTask(estafeta: String, password: String) {

        launch {

            val user = userDAO
                .getUsuario(Integer.parseInt(estafeta))

            user?.let {
                if (user.PASSWORD == password.toInt()) {
                    preferenceHelper.putString(
                        ConstantsPreferences.PREF_ESTAFETA,
                        user.ESTAFETA.toString()
                    )
                    preferenceHelper.putString(
                        ConstantsPreferences.PREF_NOMBRE_EMPLEADO,
                        user.NOMBRE!!
                    )

                    files.registerLogs("Termina LoginBD", user.toString())

                    withContext(Main) { presenter.get().onResponseReceived("SUCCESS") }
                } else {

                    withContext(Main) {
                        presenter.get().onResponseReceived("Fail,Contraseña incorrecta")
                    }

                }

            } ?: run {

                try {

                    initLogin(estafeta, password)

                } catch (e: JsonEncodingException) {

                    withContext(Main) {
                        presenter.get()
                            .onResponseReceived("Error al generar JSON\nVuelva a intentar")
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    files.createFileException(
                        "login/LoginActivityModel/executeLoginTask " +
                                Log.getStackTraceString(e)
                    )
                    withContext(Main) { postExceptionFromAPI(e) }
                }

            }


        }


    }

  /*  private fun getPrinters(){
        var params= HashMap<String,String>()
        params["Llave"] = "e936c17f7b54f02d32@@5463e0c3f749080b9"
        params["Cadena"] = STRING
        var reponse = Curl.sendPostRequest(params,"Cat_Impresoras", preferenceHelper,7)
        val jsonArray = JSONArray(reponse)
        val ID_PRINTER = jsonArray.getJSONObject(0).getString("ID_PRINTER")
        val PRINTER = jsonArray.getJSONObject(1).getString(("PRINTER"))

        Log.i("--->id DE LA IMPRESORA", ID_PRINTER)
        Log.i("----> printer", PRINTER)

    }*/

    private fun EnviaImpresion(){
        var params= HashMap<String,String>()
        var fecha = Utilities.getJulianDate()
        params["Llave"] = "e936c17f7b54f02d32@@5463e0c3f749080b9"
        params["Cadena"] = STRING
        params["id_printer"] = "8"
        params["fecha"] = Utilities.getJulianDate()
        params["id_term"] = "3"
        params["contenido"] = "sacsdasd"
        params["barcode"] = "asdasd"
        var reponse = Curl.sendPostRequest(params,"Envia_impresion", preferenceHelper,7)
        Log.i("--->Response Envia--->", reponse.toString())
        Log.i("---->Fecha--->", fecha)
        //val VERSION = jsonArray.getJSONObject(0).getString("VERSION")
        Log.i("Revisa_saldo", reponse)
    }

    private fun postExceptionFromAPI(e: Exception) {

        val exceptionRate = ToksWebServiceExceptionRate.rateError(Log.getStackTraceString(e))
        presenter.get().onResponseReceived("Exception,$exceptionRate")

    }

    //================================================================================
    // Methods for Login
    //================================================================================

    private suspend fun initLogin(usuario: String, password: String) {

        val request = createRequestLogin(usuario, password)
        val result = this.doIn(request, LOG_USUARIO, preferenceHelper)
        withContext(Main) { postResultFromAPI(result) }

    }


    private fun createRequestLogin(usuario: String, password: String): SoapObject {
        Log.d("PrinterService", "ppp");
        val request = SoapObject(NAMESPACE, LOG_USUARIO)
        request.addProperty("Llave", KEY)
        request.addProperty("Usuario", usuario)
        request.addProperty("Pass", password)
        files.registerLogs("Inicia Login", request.toString())
        Log.i(TAG, "createRequestLogin: $request")
        return request

    }

    private fun postResultFromAPI(logUsuarioresponse: String) {

        val today = DateTime(Calendar.getInstance().getTime())


        files.registerLogs("Termina Login", logUsuarioresponse)

        if (ToksWebServiceExceptionRate.rateError(logUsuarioresponse).isNotEmpty()) {

            presenter.get().onResponseReceived(
                "Exception,${
                    ToksWebServiceExceptionRate.rateError(logUsuarioresponse)
                }"

            )

        } else {

            val type: Type = Types.newParameterizedType(List::class.java, User::class.java)
            val jsonAdapter: JsonAdapter<List<User>> = moshi.adapter(type)
            val users: List<User>? = jsonAdapter.fromJson(logUsuarioresponse)

            users?.let {

                if (it[0].CODIGO == "OK") {
                    preferenceHelper.putString(
                        ConstantsPreferences.PREF_ESTAFETA,
                        it[0].ESTAFETA.toString()
                    )
                    preferenceHelper.putString(
                        ConstantsPreferences.PREF_NOMBRE_EMPLEADO,
                        it[0].NOMBRE!!
                    )

                    val userEntity = UserEntity(
                        it[0].ESTAFETA,
                        it[0].ESTAFETA,
                        it[0].NOMBRE,
                        it[0].APP,
                        it[0].INTFALLOS,
                        Utils.month
                    )

                    launch {
                        userDAO.insertarUsuario(userEntity)
                    }

                    presenter.get().onResponseReceived("SUCCESS")

                } else {
                    presenter.get().onResponseReceived("Fail,${it[0].CODIGO}")
                }
            } ?: run {
                presenter.get().onResponseReceived("Fail,Error al generar Usuario")
            }

        }


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