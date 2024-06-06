package com.Model

import android.os.AsyncTask
import android.util.Log
import com.DataModel.VtolCancelResponse
import com.Interfaces.C54CancelacionTaskMVP
import com.Presenter.C54CancelacionTaskPresenter
import com.Verifone.CommandUtils
import com.Verifone.EMVResponse
import com.Verifone.VerifonePinpadInterface
import com.View.Fragments.ContentFragment
import org.apache.commons.lang3.ArrayUtils

class C54CanncelacionModel:C54CancelacionTaskMVP.Model {
    private class sendC54Cancelacion
    /**
     * Type: Method.
     * Parent: sendC54Cancelacion.
     * Name: sendC54Cancelacion.
     *
     * @param vtolCancelResponse @PsiType:VtolCancelResponse.
     * @param response           @PsiType:EMVResponse.
     * @Description.
     * @EndDescription.
     */(
        /**
         * The Vtol cancel response.
         */
        var vtolCancelResponse: VtolCancelResponse?,
        /**
         * The Response.
         */
        var response: EMVResponse?
    ) : AsyncTask<Void?, Void?, String>() {
        override fun doInBackground(vararg p0: Void?): String {
            C54CancelacionTaskPresenter.filesWeakReference!!.get()!!.registerLogs(
                "Inicia C54CancelacionTask",
                "",
                C54CancelacionTaskPresenter.preferenceHelperLogs!!,
                C54CancelacionTaskPresenter.preferenceHelper!!
            )
            var regreso: ArrayList<String>
            var tag9f27: String
            val tag9f27Aux: String
            var tag9f27Aux1 = ""
            val OutPutError: String
            val outPutByte: String
            val chDataIn = ByteArray(1024)
            val chAck = byteArrayOf(0x06)
            val msg: ByteArray
            if (vtolCancelResponse!!.codigo.toUpperCase() == "ERROR") {
                val auxAnio = vtolCancelResponse!!.campo25.substring(2, 4)
                val auxMes = vtolCancelResponse!!.campo25.substring(4, 6)
                val auxDia = vtolCancelResponse!!.campo25.substring(6, 8)
                val auxHora = vtolCancelResponse!!.campo25.substring(8, 10)
                val auxMin = vtolCancelResponse!!.campo25.substring(10, 12)
                val auxSeg = vtolCancelResponse!!.campo25.substring(12, 14)
                val auxCam27 = vtolCancelResponse!!.campo27.substring(0, 1)
                val auxCam271 = vtolCancelResponse!!.campo27.substring(1, 2)
                VerifonePinpadInterface.MSG_C54_DES_APROVED[13] = auxCam27[0].toByte()
                VerifonePinpadInterface.MSG_C54_DES_APROVED[14] = auxCam271[0].toByte()
                VerifonePinpadInterface.MSG_C54_DES_APROVED[29] =
                    java.lang.Byte.valueOf(auxAnio, 16)
                VerifonePinpadInterface.MSG_C54_DES_APROVED[30] = java.lang.Byte.valueOf(auxMes, 16)
                VerifonePinpadInterface.MSG_C54_DES_APROVED[31] = java.lang.Byte.valueOf(auxDia, 16)
                VerifonePinpadInterface.MSG_C54_DES_APROVED[34] =
                    java.lang.Byte.valueOf(auxHora, 16)
                VerifonePinpadInterface.MSG_C54_DES_APROVED[35] = java.lang.Byte.valueOf(auxMin, 16)
                VerifonePinpadInterface.MSG_C54_DES_APROVED[36] = java.lang.Byte.valueOf(auxSeg, 16)
                C54CancelacionTaskPresenter.mensajeTexto = vtolCancelResponse!!.campo28
                val newCadena =
                    CommandUtils.byteArrayToHexString(VerifonePinpadInterface.MSG_C54_DES_APROVED)
                val auxCad = newCadena.replace(" ", "")
                val enviaLrc = CommandUtils.hexToAscii(auxCad)
                val lrc = CommandUtils.calculateLRC(enviaLrc)
                msg = ArrayUtils.addAll(VerifonePinpadInterface.MSG_C54_DES_APROVED, lrc)
            } else {
                val auxCam22 = vtolCancelResponse!!.campo22.substring(0, 1)
                val auxCam222 = vtolCancelResponse!!.campo22.substring(1, 2)
                val auxCam223 = vtolCancelResponse!!.campo22.substring(2, 3)
                val auxCam224 = vtolCancelResponse!!.campo22.substring(3, 4)
                val auxCam225 = vtolCancelResponse!!.campo22.substring(4, 5)
                val auxCam226 = vtolCancelResponse!!.campo22.substring(5, 6)

                //18 11 09 10 27 02
                val auxAnio = vtolCancelResponse!!.campo25.substring(2, 4)
                val auxMes = vtolCancelResponse!!.campo25.substring(4, 6)
                val auxDia = vtolCancelResponse!!.campo25.substring(6, 8)
                val auxHora = vtolCancelResponse!!.campo25.substring(8, 10)
                val auxMin = vtolCancelResponse!!.campo25.substring(10, 12)
                val auxSeg = vtolCancelResponse!!.campo25.substring(12, 14)
                val auxCam27 = vtolCancelResponse!!.campo27.substring(0, 1)
                val auxCam271 = vtolCancelResponse!!.campo27.substring(1, 2)
                VerifonePinpadInterface.MSG_C54_APROVED[11] = auxCam22[0].toByte()
                VerifonePinpadInterface.MSG_C54_APROVED[12] = auxCam222[0].toByte()
                VerifonePinpadInterface.MSG_C54_APROVED[13] = auxCam223[0].toByte()
                VerifonePinpadInterface.MSG_C54_APROVED[14] = auxCam224[0].toByte()
                VerifonePinpadInterface.MSG_C54_APROVED[15] = auxCam225[0].toByte()
                VerifonePinpadInterface.MSG_C54_APROVED[16] = auxCam226[0].toByte()
                VerifonePinpadInterface.MSG_C54_APROVED[19] = auxCam27[0].toByte()
                VerifonePinpadInterface.MSG_C54_APROVED[20] = auxCam271[0].toByte()

                //DATE
                VerifonePinpadInterface.MSG_C54_APROVED[25] = java.lang.Byte.valueOf(auxAnio, 16)
                VerifonePinpadInterface.MSG_C54_APROVED[26] = java.lang.Byte.valueOf(auxMes, 16)
                VerifonePinpadInterface.MSG_C54_APROVED[27] = java.lang.Byte.valueOf(auxDia, 16)
                VerifonePinpadInterface.MSG_C54_APROVED[30] = java.lang.Byte.valueOf(auxHora, 16)
                VerifonePinpadInterface.MSG_C54_APROVED[31] = java.lang.Byte.valueOf(auxMin, 16)
                VerifonePinpadInterface.MSG_C54_APROVED[32] = java.lang.Byte.valueOf(auxSeg, 16)
                val newCadena =
                    CommandUtils.byteArrayToHexString(VerifonePinpadInterface.MSG_C54_APROVED)
                val auxCad = newCadena.replace(" ", "")
                val enviaLrc = CommandUtils.hexToAscii(auxCad)
                val lrc = CommandUtils.calculateLRC(enviaLrc)
                msg = ArrayUtils.addAll(VerifonePinpadInterface.MSG_C54_APROVED, lrc)
            }
            ContentFragment.contentFragment!!.btManager!!.BtSendData(msg)
            regreso =
                ContentFragment.contentFragment!!.btManager!!.BtRecvData(chDataIn, 10000) //10000
            regreso =
                ContentFragment.contentFragment!!.btManager!!.BtRecvData(chDataIn, 30000) //30000
            ContentFragment.contentFragment!!.btManager!!.BtSendData(chAck)
            try {
                Thread.sleep(700)
            } catch (e: InterruptedException) {
                e.printStackTrace()
                C54CancelacionTaskPresenter.filesWeakReference!!.get()!!.createFileException(
                    "Controller/C54CancelacionTask/sendC54Cancelacion " + Log.getStackTraceString(e),
                    C54CancelacionTaskPresenter.preferenceHelper!!
                )
            }
            val output = StringBuilder()
            for (i in 0 until regreso.size - 1) {
                val str2 = regreso[i]
                val str = str2.replace("ffffff", "")
                output.append(str.toInt(16).toChar())
            }
            if (output.length > 0) {
                outPutByte = CommandUtils.byteArrayToHexString(output.toString().toByteArray())
                    .replace(" ", "")
                val OutputError2 = outPutByte.substring(9, 10)
                val OutputError3 = outPutByte.substring(11, 12)
                OutPutError = OutputError2 + OutputError3
                if (response!!.transactionType == "CHIP") {
                    if (OutPutError != "23") {
                        tag9f27 = CommandUtils.byteArrayToHexString(output.toString().toByteArray())
                            .replace(" ", "")
                        tag9f27Aux = tag9f27.substring(tag9f27.indexOf("9F27"))
                        tag9f27Aux1 = tag9f27Aux.substring(6, 8)
                    }
                }
                tag9f27 = tag9f27Aux1
                C54CancelacionTaskPresenter.errorOutput = OutPutError
                C54CancelacionTaskPresenter.filesWeakReference!!.get()!!.registerLogs(
                    "9f27 " + tag9f27 + " OutputError " + C54CancelacionTaskPresenter.errorOutput,
                    "",
                    C54CancelacionTaskPresenter.preferenceHelperLogs!!,
                    C54CancelacionTaskPresenter.preferenceHelper!!
                )
                when (OutPutError) {
                    "29" -> return CommandUtils.catalogStatus()["29"].also { C54CancelacionTaskPresenter.mensajeTexto = it!! }!!
                    "06" -> return CommandUtils.catalogStatus()["06"].also { C54CancelacionTaskPresenter.mensajeTexto = it!! }!!
                    "23" -> return CommandUtils.catalogStatus()["23"].also { C54CancelacionTaskPresenter.mensajeTexto = it!! }!!
                    "99" -> return CommandUtils.catalogStatus()["99"].also { C54CancelacionTaskPresenter.mensajeTexto = it!! }!!
                    "62" -> return CommandUtils.catalogStatus()["62"].also { C54CancelacionTaskPresenter.mensajeTexto = it!! }!!
                    "63" -> return CommandUtils.catalogStatus()["63"].also { C54CancelacionTaskPresenter.mensajeTexto = it!! }!!
                    else -> if (vtolCancelResponse!!.codigo.toUpperCase() == "OK") {
                        C54CancelacionTaskPresenter.mensajeTexto = "OK"
                    } else {
                        C54CancelacionTaskPresenter.mensajeTexto = vtolCancelResponse!!.codigo
                    }
                }
            } else {
                if (vtolCancelResponse!!.codigo.toUpperCase() == "OK") {
                    C54CancelacionTaskPresenter.mensajeTexto = "OK"
                } else if (vtolCancelResponse!!.codigo.toUpperCase() == "ERROR") {
                    C54CancelacionTaskPresenter.mensajeTexto =
                        "Error " + vtolCancelResponse!!.campo28 + " (" + vtolCancelResponse!!.campo27 + ")"
                }
            }
            return C54CancelacionTaskPresenter.mensajeTexto
        }

        override fun onPostExecute(s: String) {
            super.onPostExecute(s)
            C54CancelacionTaskPresenter.filesWeakReference!!.get()!!.registerLogs(
                "Termina C54Cancelacion",
                "",
                C54CancelacionTaskPresenter.preferenceHelperLogs!!,
                C54CancelacionTaskPresenter.preferenceHelper!!
            )
            if (s == "OK") {
                C54CancelacionTaskPresenter.view!!.onC54CancelacionSuccessResult("Cancelando Pago")
            } else {
                C54CancelacionTaskPresenter.view!!.onC54CancelacionFailResult(s)
            }
        }
    }

    override fun executeSendC54Cancelacion(
        vtolCancelResponse: VtolCancelResponse?,
        response: EMVResponse?
    ) {
        sendC54Cancelacion(vtolCancelResponse,response).execute()
    }
}