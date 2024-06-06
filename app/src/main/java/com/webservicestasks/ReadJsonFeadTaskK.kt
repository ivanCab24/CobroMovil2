package com.webservicestasks

import android.app.Activity
import android.util.Log
import com.Constants.ConstantsPreferences
import com.DEBUG
import com.Utilities.PreferenceHelper
import com.Utilities.Utils
import com.View.UserInteraction
import com.e_url
import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.SoapObject
import org.ksoap2.serialization.SoapPrimitive
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpTransportSE

abstract class ReadJsonFeadTaskK {

    private val TAG = "ReadJsonFeadTasKK"
    private var method: String? = null
    private var preferenceHelper: PreferenceHelper? = null
    private var envelope: SoapSerializationEnvelope? = null
    private var resultRequestSOAP: SoapPrimitive? = null
    private var resultString = ""
    private val TIMEOUT = 100000

    fun doIn(request: SoapObject, method: String, preferenceHelper: PreferenceHelper): String {

        try {
            if(preferenceHelper.getBoolean(ConstantsPreferences.SWITCH_TRAINING))
                UserInteraction.snackyWarning(Activity(),null,"Modo de pruebas activado")
            this.method = method
            this.preferenceHelper = preferenceHelper
            envelope = SoapSerializationEnvelope(SoapEnvelope.VER11)
            envelope!!.dotNet = true
            envelope!!.setOutputSoapObject(request)

            val URL: String =
                "http://" + preferenceHelper.getString(ConstantsPreferences.PREF_SERVER, null) + "/ws_pagomovil" + e_url + "/ws_pagomovil.asmx"
            Log.i(TAG, URL)
            val transportSE = HttpTransportSE(URL, TIMEOUT)
            transportSE.debug = true

            transportSE.call(ToksWebServicesConnection.NAMESPACE + method, envelope)
            resultRequestSOAP = envelope!!.response as SoapPrimitive

            resultString = resultRequestSOAP.toString()

            Log.i(TAG, "doIn $method: $resultString")

            Utils.jsonLogcat(false, 2, method, resultString)


        } catch (e: Exception) {
            e.printStackTrace()
            resultString = e.message.toString()
        }

        if (DEBUG) {
            if (method == ToksWebServicesConnection.VTOL_RESETKYS) {
                resultString =
                    "[{\"CODIGO\":\"OK\",\"CAMPO24\":\"NA\",\"CAMPO27\":\"00\",\"CAMPO28\":\"APROBADA\",\"CAMPO102\":\"! EX00068 EA0E36981B11EAEC039DF72805D929A203302000013480000000E60E9F0031C382DF\"}]\n"
                //resultString = "[{\"CODIGO\":\"OK\",\"CAMPO24\":\"NA\",\"CAMPO27\":\"PEDIR AUTORIZACION\",\"CAMPO28\":\"01\",\"CAMPO102\":\"NA\"}]\n"
            }
        }

        /*if (method == ToksWebServicesConnection.LOG_USUARIO) {
            //response = "[{\"CODIGO\":\"OK\",\"ESTAFETA\":110719,\"NOMBRE\":\"GOMEZ/CONTRERAS/MARIA DE LA L\",\"APP\":1,\"INTFALLOS\":null}]"
            //resultString = "[{\"CODIGO\":\"OK\",\"ESTAFETA\":null,\"NOMBRE\":\"MORENO/CASTULO/ARTURO\",\"APP\":null,\"INTFALLOS\":null}]"
            resultString = "Attempt to invoke interface method 'int org.ksoap2.serialization.KvmSerializable.getPropertyCount()' on a null object reference"
            //resultString = "[{\"CODIGO\":\"OK\",\"ESTAFETA\":108910,\"NOMBRE\":\"MEDINA/GOMEZ/GABRIEL\",\"APP\":1,\"INTFALLOS\":0}]"
        }else if(method == ToksWebServicesConnection.GENERAR_NIP){
            resultString = "{\"code\":0,\"userError\":\"\",\"exceptionMessage\":\"\",\"success\":true,\"response\":{\"nip\":\"4668\",\"fechaInicial\":\"2021-08-25T19:13:30.5005489-05:00\",\"fechaExpiracion\":\"2021-08-25T19:23:30.5005489-05:00\"}}"
        } else*/



        return resultString
    }


}