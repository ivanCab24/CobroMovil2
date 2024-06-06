package com.Model

import android.util.Log
import com.Constants.ConstantsPreferences
import com.Controller.BD.DAO.CatalogoBinesDAO
import com.Controller.BD.DAO.MFPGODAO
import com.Controller.BD.Entity.MBCO2
import com.Controller.BD.Entity.MFPGO
import com.DataModel.CatalogoBines
import com.DataModel.CatalogoFpgo
import com.Interfaces.SettingsMVP
import com.Utilities.FilesK
import com.Utilities.PreferenceHelper
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.webservicestasks.ReadJsonFeadTaskK
import com.webservicestasks.ToksWebServiceExceptionRate
import com.webservicestasks.ToksWebServicesConnection
import com.webservicestasks.ToksWebServicesConnection.CATALOGO_BINES
import com.webservicestasks.ToksWebServicesConnection.CATALOGO_FPGO
import com.webservicestasks.ToksWebServicesConnection.KEY
import com.webservicestasks.ToksWebServicesConnection.NAMESPACE
import com.webservicestasks.ToksWebServicesConnection.STRING
import dagger.Lazy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.ksoap2.serialization.SoapObject
import java.lang.reflect.Type
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class DialogSettingsModel @Inject constructor(
    private var presenter: Lazy<SettingsMVP.Presenter>,
    private var preferenceHelper: PreferenceHelper,
    private var files: FilesK,
    private var catalogoBinesDAO: CatalogoBinesDAO,
    private var catalogoFpgoDAO: MFPGODAO
) : ReadJsonFeadTaskK(), ToksWebServicesConnection, SettingsMVP.Model, CoroutineScope {

    val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
    private val job = Job()


    override fun executeBinesTask() {

        launch {
            try {
                initBines()
                initFpgo()
            } catch (e: Exception) {
                e.printStackTrace()
                files.createFileException(
                    "settings/DialogSettingsModel/executeBinesTask " +
                            Log.getStackTraceString(e)
                )
                withContext(Main) { posExceptionFromAPI(e) }
            }
        }
    }

    private fun posExceptionFromAPI(e: Exception) {
        val exceptionRate = ToksWebServiceExceptionRate.rateError(Log.getStackTraceString(e))
        presenter.get().onResponseReceived("Exception,$exceptionRate")
    }

    private suspend fun initBines() {

        val request = createRequestCatalogoBines()
        files.registerLogs(
            "Inicia MBCO",
            request.toString()
        )
        val result = this.doIn(request, CATALOGO_BINES, preferenceHelper)
        files.registerLogs("Termina Catalogo_bines", "")
        withContext(Main) { posResultFromAPIMBCO(result) }
        files.registerLogs("Termina insertar MBCO2 BD", "")


    }

    private suspend fun initFpgo() {

        val request = createRequestCatalogoFpgo()
        files.registerLogs(
            "Inicia Catalogo_fpgo",
            request.toString()
        )
        val result = this.doIn(request, CATALOGO_FPGO, preferenceHelper)
        files.registerLogs("Termina Catalogo_fpgo", "")
        withContext(Main) { posResultFromAPIFpgo(result) }
        files.registerLogs("Termina insertar FPGO BD", "")

    }

    //================================================================================
    // Create Request Data
    //================================================================================

    private fun createRequestCatalogoBines(): SoapObject {
        val request = SoapObject(NAMESPACE, CATALOGO_BINES)
        request.addProperty("Llave", KEY)
        request.addProperty("Cadena", STRING)

        return request
    }

    private fun createRequestCatalogoFpgo(): SoapObject {

        val request = SoapObject(NAMESPACE, CATALOGO_FPGO)
        request.addProperty("Llave", KEY)
        request.addProperty("Cadena", STRING)

        return request

    }


    //================================================================================
    // Post Request Result
    //================================================================================

    private fun posResultFromAPIMBCO(catalogoBinesresponse: String) {

        if (ToksWebServiceExceptionRate.rateError(catalogoBinesresponse).isNotEmpty()) {

            presenter.get().onResponseReceived(
                "Exception,${
                    ToksWebServiceExceptionRate.rateError(catalogoBinesresponse)
                }"
            )

        } else {

            val type: Type = Types.newParameterizedType(List::class.java, CatalogoBines::class.java)
            val jsonAdapter: JsonAdapter<List<CatalogoBines>> = moshi.adapter(type)
            val catalogoBines: List<CatalogoBines>? = jsonAdapter.fromJson(catalogoBinesresponse)

            catalogoBines?.let {
                if (it[0].CODIGO == "OK") {
                    launch { insertDataCatalogoBines(it) }
                } else {
                    presenter.get().onResponseReceived("Fail,${it[0].CODIGO}")
                }
            }

        }

    }

    private fun posResultFromAPIFpgo(resultFpgo: String) {

        if (ToksWebServiceExceptionRate.rateError(resultFpgo).isNotEmpty()) {

            presenter.get().onResponseReceived(
                "Exception,${
                    ToksWebServiceExceptionRate.rateError(resultFpgo)
                }"
            )

        } else {

            val type: Type = Types.newParameterizedType(List::class.java, CatalogoFpgo::class.java)
            val jsonAdapter: JsonAdapter<List<CatalogoFpgo>> = moshi.adapter(type)
            val catalogoFpgo: List<CatalogoFpgo>? = jsonAdapter.fromJson(resultFpgo)

            catalogoFpgo?.let {
                if (it[0].CODIGO == "OK") {
                    launch { insertDataCatalogoFpgo(it) }
                } else {
                    presenter.get().onResponseReceived("Fail,${it[0].CODIGO}")
                }
            }

        }

    }

    //================================================================================
    // Database Operations
    //================================================================================
    private suspend fun insertDataCatalogoBines(catalogoBines: List<CatalogoBines>) {

        if (catalogoBinesDAO.countRecords() > 0) {

            catalogoBinesDAO.deleteTableMBCO2()

        }

        for (item in catalogoBines) {
            catalogoBinesDAO
                .insertarBines(
                    MBCO2(
                        item.TC_PREFIJO!!.toInt(),
                        item.TIPO_FPGO!!.toInt(),
                        item.SA_IDFPGO!!.toInt(),
                        item.TR_IDFPGO.toString(),
                        item.TC_DEBITO,
                        item.FACTIVO!!.toInt(),
                        item.TC_TIPOCOM!!.toInt()
                    )
                )
        }

    }

    private suspend fun insertDataCatalogoFpgo(catalogoFpgo: List<CatalogoFpgo>) {

        if (catalogoFpgoDAO.countRecords() > 0) {

            catalogoFpgoDAO.deleteTableMFPGO()

        }

        for (item in catalogoFpgo) {
            catalogoFpgoDAO
                .insertarMFPGO(
                    MFPGO(
                        item.TIPO_FPGO,
                        item.ID_FPGO,
                        item.FORMA_PAGO,
                        item.DESCRIPCION,
                        item.CLAVE,
                        item.FPROPINA,
                        item.FREFERENCIA,
                        item.FBANCO,
                        item.COMISION,
                        item.TIPO_COMISION,
                        item.FORMA_PAGO_SAT,
                        item.FORMA_PAGO_SAT2
                    )
                )
        }

        withContext(Main) { presenter.get().onResponseReceived("SUCCESS") }

    }

    override fun saveConfig(
        serverIP: String,
        ipTerminal: String,
        numeroTerminal: String,
        cajaMP: String,
        unidad: String,
        impresora: String
    ) {
        if(unidad!="null"){
            preferenceHelper.putString(ConstantsPreferences.PREF_SERVER, serverIP)
            preferenceHelper.putString(ConstantsPreferences.PREF_PINPAD, ipTerminal)
            preferenceHelper.putString(ConstantsPreferences.PREF_NUMERO_TERMINAL, numeroTerminal)
            preferenceHelper.putString(ConstantsPreferences.PREF_CAJA_MP, cajaMP)
            preferenceHelper.putString(ConstantsPreferences.PREF_UNIDAD, unidad)
            preferenceHelper.putString(ConstantsPreferences.PREF_IMPRESORA, impresora)
            preferenceHelper.putString(ConstantsPreferences.PREF_SERVER_COPY, if(preferenceHelper.getBoolean(ConstantsPreferences.SWITCH_TRAINING))preferenceHelper.getString(ConstantsPreferences.PREF_SERVER_COPY, null)else serverIP)
            preferenceHelper.putString(ConstantsPreferences.PREF_UNIDAD_COPY, if(preferenceHelper.getBoolean(ConstantsPreferences.SWITCH_TRAINING))preferenceHelper.getString(ConstantsPreferences.PREF_UNIDAD_COPY, null)else unidad)

        }

    }

    override fun getConfig(): Array<String> {

        return arrayOf(
            preferenceHelper.getString(ConstantsPreferences.PREF_SERVER, null),
            preferenceHelper.getString(ConstantsPreferences.PREF_PINPAD, null),
            preferenceHelper.getString(ConstantsPreferences.PREF_NUMERO_TERMINAL, null),
            preferenceHelper.getString(ConstantsPreferences.PREF_CAJA_MP, null),
            preferenceHelper.getString(ConstantsPreferences.PREF_UNIDAD, null),
            preferenceHelper.getString(ConstantsPreferences.PREF_IMPRESORA, null)
        )

    }

    override fun getTrining(): Boolean {
       return  preferenceHelper.getBoolean(ConstantsPreferences.SWITCH_TRAINING)
    }

    override fun getMarca(): String {
        return preferenceHelper.getString(ConstantsPreferences.PREF_MARCA_SELECCIONADA, null)
    }

    override fun getToken(): String {
        TODO("Not yet implemented")
    }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO


}