package com.Model

import android.os.AsyncTask
import android.util.Log
import com.DataModel.SaleVtol
import com.Interfaces.C54TaskMPV
import com.Presenter.C54TaskPresenter
import com.Verifone.BtManager
import com.Verifone.CommandUtils
import com.Verifone.VerifonePinpadInterface
import com.View.Fragments.ContentFragment
import org.apache.commons.lang3.ArrayUtils

class C54Model:C54TaskMPV.Model {
    private class c54Task
    /**
     * Type: Method.
     * Parent: c54Task.
     * Name: c54Task.
     *
     * @param saleVtol        @PsiType:SaleVtol.
     * @param btManager       @PsiType:BtManager.
     * @param transactionType @PsiType:String.
     * @Description.
     * @EndDescription.
     */(
        /**
         * The Sale vtol.
         */
        var saleVtol: SaleVtol?,
        /**
         * The Bt manager.
         */
        var btManager: BtManager?,
        /**
         * The Transaction type.
         */
        var transactionType: String?
    ) : AsyncTask<Void?, Void?, String>() {
        override fun doInBackground(vararg p0: Void?): String {
            C54TaskPresenter.filesWeakReference!!.get()!!
                .registerLogs("Inicia C54Task", "", C54TaskPresenter.preferenceHelperLogs!!, C54TaskPresenter.preferenceHelper!!)
            if (saleVtol!!.codigo.toUpperCase() == "ERROR") {
                C54TaskPresenter.transaccionExitosa = false
                val auxAnio = saleVtol!!.campo25.substring(2, 4)
                val auxMes = saleVtol!!.campo25.substring(4, 6)
                val auxDia = saleVtol!!.campo25.substring(6, 8)
                val auxHora = saleVtol!!.campo25.substring(8, 10)
                val auxMin = saleVtol!!.campo25.substring(10, 12)
                val auxSeg = saleVtol!!.campo25.substring(12, 14)
                val auxCam27 = saleVtol!!.campo27.substring(0, 1)
                val auxCam271 = saleVtol!!.campo27.substring(1, 2)
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
                val newCadena =
                    CommandUtils.byteArrayToHexString(VerifonePinpadInterface.MSG_C54_DES_APROVED)
                val auxCad = newCadena.replace(" ", "")
                val enviaLrc = CommandUtils.hexToAscii(auxCad)
                val lrc = CommandUtils.calculateLRC(enviaLrc)
                C54TaskPresenter.msg = ArrayUtils.addAll(VerifonePinpadInterface.MSG_C54_DES_APROVED, lrc)
                C54TaskPresenter.mensaje = "Exception" + saleVtol!!.campo28
            } else {
                C54TaskPresenter.transaccionExitosa = true
                val auxCam22 = saleVtol!!.campo22.substring(0, 1)
                val auxCam222 = saleVtol!!.campo22.substring(1, 2)
                val auxCam223 = saleVtol!!.campo22.substring(2, 3)
                val auxCam224 = saleVtol!!.campo22.substring(3, 4)
                val auxCam225 = saleVtol!!.campo22.substring(4, 5)
                val auxCam226 = saleVtol!!.campo22.substring(5, 6)
                val auxAnio = saleVtol!!.campo25.substring(2, 4)
                val auxMes = saleVtol!!.campo25.substring(4, 6)
                val auxDia = saleVtol!!.campo25.substring(6, 8)
                val auxHora = saleVtol!!.campo25.substring(8, 10)
                val auxMin = saleVtol!!.campo25.substring(10, 12)
                val auxSeg = saleVtol!!.campo25.substring(12, 14)
                val auxCam27 = saleVtol!!.campo27.substring(0, 1)
                val auxCam271 = saleVtol!!.campo27.substring(1, 2)
                VerifonePinpadInterface.MSG_C54_APROVED[11] = auxCam22[0].toByte()
                VerifonePinpadInterface.MSG_C54_APROVED[12] = auxCam222[0].toByte()
                VerifonePinpadInterface.MSG_C54_APROVED[13] = auxCam223[0].toByte()
                VerifonePinpadInterface.MSG_C54_APROVED[14] = auxCam224[0].toByte()
                VerifonePinpadInterface.MSG_C54_APROVED[15] = auxCam225[0].toByte()
                VerifonePinpadInterface.MSG_C54_APROVED[16] = auxCam226[0].toByte()
                VerifonePinpadInterface.MSG_C54_APROVED[19] = auxCam27[0].toByte()
                VerifonePinpadInterface.MSG_C54_APROVED[20] = auxCam271[0].toByte()
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
                C54TaskPresenter.msg = ArrayUtils.addAll(VerifonePinpadInterface.MSG_C54_APROVED, lrc)
            }
            val btManager = ContentFragment.contentFragment!!.btManager
            if (btManager != null) {
                btManager.BtSendData(C54TaskPresenter.msg)
                C54TaskPresenter.regreso = btManager.BtRecvData(
                    C54TaskPresenter.chDataIn,
                    10000
                ) // TODO: 15/12/20 Cambiar a valore originales 10000
                C54TaskPresenter.regreso = btManager.BtRecvData(
                    C54TaskPresenter.chDataIn,
                    30000
                ) // TODO: 15/12/20 Cambiar a valores originales 30000
                btManager.BtSendData(C54TaskPresenter.chAck)
                try {
                    Thread.sleep(700)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                    C54TaskPresenter.filesWeakReference!!.get()!!.createFileException(
                        "Controller/C54Task/c54Task " + Log.getStackTraceString(e), C54TaskPresenter.preferenceHelper!!
                    )
                }
                val output = StringBuilder()
                for (i in 0 until C54TaskPresenter.regreso.size - 1) {
                    val str2 = C54TaskPresenter.regreso[i]
                    val str = str2.replace("ffffff", "")
                    val str3 = str.replace("�", "0")
                    output.append(str3.toInt(16).toChar())
                }
                if (output.length > 0) {
                    C54TaskPresenter.outPutByte = CommandUtils.byteArrayToHexString(output.toString().toByteArray())
                        .replace(" ", "")
                    val OutputError2 = C54TaskPresenter.outPutByte.substring(9, 10)
                    val OutputError3 = C54TaskPresenter.outPutByte.substring(11, 12)
                    C54TaskPresenter.OutPutError = OutputError2 + OutputError3
                    if (transactionType == "CHIP") {
                        if (C54TaskPresenter.OutPutError != "23") {
                            C54TaskPresenter.tag9f27 =
                                CommandUtils.byteArrayToHexString(output.toString().toByteArray())
                                    .replace(" ", "")
                            C54TaskPresenter.tag9f27Aux = C54TaskPresenter.tag9f27.substring(
                                C54TaskPresenter.tag9f27.indexOf("9F27"))
                            C54TaskPresenter.tag9f27Aux1 = C54TaskPresenter.tag9f27Aux.substring(6, 8)
                        }
                    }
                    C54TaskPresenter.outpuError = C54TaskPresenter.OutPutError
                    C54TaskPresenter.filesWeakReference!!.get()!!.registerLogs(
                        "9f27 " + C54TaskPresenter.tag9f27Aux1 + "OutpuError " + C54TaskPresenter.outpuError,
                        "",
                        C54TaskPresenter.preferenceHelperLogs!!,
                        C54TaskPresenter.preferenceHelper!!
                    )
                    when (C54TaskPresenter.OutPutError) {
                        "29" -> C54TaskPresenter.mensaje = "Error" + CommandUtils.catalogStatus()["29"]
                        "06" -> C54TaskPresenter.mensaje = "Error" + CommandUtils.catalogStatus()["06"]
                        "23" -> C54TaskPresenter.mensaje = "Error" + CommandUtils.catalogStatus()["23"]
                        "99" -> C54TaskPresenter.mensaje = "Error" + CommandUtils.catalogStatus()["99"]
                        "62" -> C54TaskPresenter.mensaje = "Error" + CommandUtils.catalogStatus()["62"]
                        "63" -> C54TaskPresenter.mensaje = "Error" + CommandUtils.catalogStatus()["63"]
                        "27" -> {
                            C54TaskPresenter.mensaje = "Error" + CommandUtils.catalogStatus()["27"]
                            if (saleVtol!!.codigo.toUpperCase() == "OK") {
                                C54TaskPresenter.mensaje = "OK"
                            } else if (saleVtol!!.codigo.toUpperCase() == "ERROR") {
                                C54TaskPresenter.mensaje =
                                    "Error " + saleVtol!!.campo28 + " (" + saleVtol!!.campo27 + ")"
                            } else {
                                C54TaskPresenter.mensaje = "Error" + CommandUtils.catalogStatus()[C54TaskPresenter.OutPutError]
                            }
                        }
                        else -> if (saleVtol!!.codigo.toUpperCase() == "OK") {
                            C54TaskPresenter.mensaje = "OK"
                        } else if (saleVtol!!.codigo.toUpperCase() == "ERROR") {
                            C54TaskPresenter.mensaje =
                                "Error " + saleVtol!!.campo28 + " (" + saleVtol!!.campo27 + ")"
                        } else {
                            C54TaskPresenter.mensaje = "Error" + CommandUtils.catalogStatus()[C54TaskPresenter.OutPutError]
                        }
                    }
                } else {
                    if (saleVtol!!.codigo.toUpperCase() == "OK") C54TaskPresenter.mensaje =
                        "OK" else if (saleVtol!!.codigo.toUpperCase() == "ERROR") C54TaskPresenter.mensaje =
                        "Error " + saleVtol!!.campo28 + " (" + saleVtol!!.campo27 + ")"
                }
            } else {
                C54TaskPresenter.mensaje = "Conexion perdida con pinpad\nComunicarse con sistemas"
            }
            return C54TaskPresenter.mensaje
        }

        override fun onPostExecute(s: String) {
            super.onPostExecute(s)
            Log.i(
                C54TaskPresenter.TAG,
                "onPostExecute: " + C54TaskPresenter.tag9f27Aux1 + " transaccionExitosa " + C54TaskPresenter.transaccionExitosa
            )
            C54TaskPresenter.filesWeakReference!!.get()!!
                .registerLogs("Termina C54Task", s, C54TaskPresenter.preferenceHelperLogs!!, C54TaskPresenter.preferenceHelper!!)
            if (s.contains("Error")) C54TaskPresenter.view!!.onFailC54Result(
                s.replace("Error", ""),
                C54TaskPresenter.tag9f27Aux1,
                C54TaskPresenter.transaccionExitosa
            ) else if (s.contains("Exception")) C54TaskPresenter.view!!.onExceptionC54Result(
                s.replace(
                    "Exception",
                    ""
                )
            ) else C54TaskPresenter.view!!.onSuccessC54Result(
                C54TaskPresenter.transaccionExitosa, C54TaskPresenter.outpuError, C54TaskPresenter.tag9f27Aux1
            )
        }
    }

    override fun executeC54(saleVtol: SaleVtol?, btManager: BtManager?, transactionType: String?) {
        c54Task(saleVtol,btManager,transactionType).execute()
    }
}