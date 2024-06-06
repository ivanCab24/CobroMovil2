package com.Model

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.util.Log
import com.Interfaces.SolicitaTarjetaMVP
import com.Presenter.SolicitaTarjetaPresenter
import com.Presenter.SolicitaTarjetaPresenter.Companion.auxPan
import com.Presenter.SolicitaTarjetaPresenter.Companion.binesDAOWeakReference
import com.Presenter.SolicitaTarjetaPresenter.Companion.puntos
import com.Verifone.CommandUtils
import com.Verifone.VerifonePinpadInterface
import com.Verifone.VerifoneTaskManager
import com.View.Fragments.ContentFragment
import org.apache.commons.lang3.ArrayUtils
import java.nio.ByteBuffer
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class SolicitaTarjetaModel:SolicitaTarjetaMVP.Model {
    private class C51Task : AsyncTask<Void?, Void?, String>() {
        @SuppressLint("LongLogTag")
        override fun doInBackground(vararg p0: Void?): String {
            SolicitaTarjetaPresenter.filesWeakReference!!.get()!!
                .registerLogs("Inicia C51Task", "", SolicitaTarjetaPresenter.preferenceHelperLogs!!, SolicitaTarjetaPresenter.preferenceHelper!!)
            var regreso: ArrayList<String>
            val chDataIn = ByteArray(1024)
            val chAck = byteArrayOf(0x06)
            if (ContentFragment.montoCobro.toInt() == 0) {
                SolicitaTarjetaPresenter.mensaje = "Error No es posible realizar cobros con monto 0.00"
                return SolicitaTarjetaPresenter.mensaje
            }
            val today = Date()
            val mCalendar = Calendar.getInstance()
            val month = mCalendar[Calendar.MONTH] + 1
            val year = today.year - 100
            var yearFormatted = ""
            var monthFormatted = ""
            try {
                val singleYear: DateFormat = SimpleDateFormat("yy")
                val d = singleYear.parse(year.toString())
                val completeYear: DateFormat = SimpleDateFormat("yyyy")
                yearFormatted = completeYear.format(d)
                val singleMonth: DateFormat = SimpleDateFormat("mm")
                val monthFormat = singleMonth.parse(month.toString())
                monthFormatted = singleMonth.format(monthFormat)
            } catch (e: ParseException) {
                e.printStackTrace()
                SolicitaTarjetaPresenter.filesWeakReference!!.get()!!.createFileException(
                    "Controller/SolicitaRefoundTask/ParseException " + Log.getStackTraceString(e),
                    SolicitaTarjetaPresenter.preferenceHelper!!
                )
            }
            VerifonePinpadInterface.MSG_C51[11] =
                java.lang.Byte.valueOf((today.year - 100).toString(), 16)
            VerifonePinpadInterface.MSG_C51[12] = java.lang.Byte.valueOf(month.toString(), 16)
            VerifonePinpadInterface.MSG_C51[13] = java.lang.Byte.valueOf(today.date.toString(), 16)
            VerifonePinpadInterface.MSG_C51[16] = java.lang.Byte.valueOf(today.hours.toString(), 16)
            VerifonePinpadInterface.MSG_C51[17] =
                java.lang.Byte.valueOf(today.minutes.toString(), 16)
            VerifonePinpadInterface.MSG_C51[18] =
                java.lang.Byte.valueOf(today.seconds.toString(), 16)
            val am = (ContentFragment.montoCobro * 100).toInt() + (ContentFragment.getTipAmount() * 100).toInt()
            val byteAmount = ByteBuffer.allocate(4).putInt(am).array()
            VerifonePinpadInterface.MSG_C51[24] = byteAmount[0]
            VerifonePinpadInterface.MSG_C51[25] = byteAmount[1]
            VerifonePinpadInterface.MSG_C51[26] = byteAmount[2]
            VerifonePinpadInterface.MSG_C51[27] = byteAmount[3]
            val newCadena = CommandUtils.byteArrayToHexString(VerifonePinpadInterface.MSG_C51)
            val auxCad = newCadena.replace(" ", "")
            val enviaLrc = CommandUtils.hexToAscii(auxCad)
            val auxlrc1 = CommandUtils.calculateLRC(enviaLrc)
            val msgc51 = ArrayUtils.addAll(VerifonePinpadInterface.MSG_C51, auxlrc1)
            val btManager = ContentFragment.contentFragment!!.btManager
            return if (btManager != null) {
                btManager.BtSendData(msgc51)
                regreso = btManager.BtRecvData(
                    chDataIn,
                    10000
                ) // TODO: 03/12/20 Cambiar a los valores predeterminados 10000
                regreso = btManager.BtRecvData(
                    chDataIn,
                    30000
                ) // TODO: 03/12/20 Cambiar a los valores predeterminados 30000

                //ENVIO ACK DE RECIBIDO
                btManager.BtSendData(chAck)
                val finalRegreso = regreso
                SolicitaTarjetaPresenter.year = yearFormatted
                SolicitaTarjetaPresenter.month = monthFormatted
                SolicitaTarjetaPresenter.day = today.date.toString()
                val output2 = StringBuilder()
                for (i in 0 until finalRegreso.size - 1) {
                    val str2 = finalRegreso[i]
                    var str = str2.replace("ffffff", "")
                    if (str.contains("a") || str.contains("b") || str.contains("c") || str.contains(
                            "d"
                        ) || str.contains("e") || str.contains("f")
                    ) {
                        if (str.length != 2) {
                            str = "0$str"
                            output2.append(str)
                        } else {
                            output2.append(str)
                        }
                    } else {
                        val `in` = str.toInt()
                        if (`in` <= 9) {
                            val str3 = "0$`in`"
                            output2.append(str3)
                        } else {
                            output2.append(str)
                        }
                    }
                }
                var raw = output2.toString()
                Log.i("AAAA",output2.toString())
                if (raw.length > 0) {
                    raw = raw.substring(0, raw.length - 2)
                    val output = StringBuilder()
                    for (i in 0 until finalRegreso.size - 1) {
                        val str2 = finalRegreso[i]
                        val str = str2.replace("ffffff", "")
                        output.append(str.toInt(16).toChar())
                    }
                    val auxRaw = output.toString()
                    Log.i(SolicitaTarjetaPresenter.TAG, "doInBackground: raw$auxRaw")
                    SolicitaTarjetaPresenter.response = CommandUtils.parceC53(raw, auxRaw)
                    Log.i(SolicitaTarjetaPresenter.TAG, "doInBackground: response" + SolicitaTarjetaPresenter.response)
                    SolicitaTarjetaPresenter.filesWeakReference!!.get()!!
                        .createInfoFile("parseC53", raw, SolicitaTarjetaPresenter.preferenceHelper!!)
                    SolicitaTarjetaPresenter.modoLect = "N"
                    if (SolicitaTarjetaPresenter.response != null) {
                        SolicitaTarjetaPresenter.response!!.modoLectura = SolicitaTarjetaPresenter.modoLect
                        when (SolicitaTarjetaPresenter.response!!.message) {
                            "29" -> return "Error " + CommandUtils.catalogStatus()["29"].also {
                                SolicitaTarjetaPresenter.mensaje = it!!
                            }
                            "06" -> return "Error " + CommandUtils.catalogStatus()["06"].also {
                                SolicitaTarjetaPresenter.mensaje = it!!
                            }
                            "23" -> return "Error " + CommandUtils.catalogStatus()["23"].also {
                                SolicitaTarjetaPresenter.mensaje = it!!
                                VerifoneTaskManager.limpiarTerminal()
                            }
                            "99" -> return "Error " + CommandUtils.catalogStatus()["99"].also {
                                SolicitaTarjetaPresenter.mensaje = it!!
                            }
                            "62" -> return "Error " + CommandUtils.catalogStatus()["62"].also {
                                SolicitaTarjetaPresenter.mensaje = it!!
                            }
                            "63" -> return CommandUtils.catalogStatus()["63"].also {
                                SolicitaTarjetaPresenter.mensaje = it!!
                            }!!
                            "98" -> return "Error " + CommandUtils.catalogStatus()["98"].also {
                                SolicitaTarjetaPresenter.mensaje = it!!
                            }
                            else -> if (SolicitaTarjetaPresenter.response!!.pan != null) {
                                if (!SolicitaTarjetaPresenter.response!!.pan.isEmpty() || SolicitaTarjetaPresenter.response!!.pan != null) {
                                    val pan = SolicitaTarjetaPresenter.response!!.pan
                                    Log.d(SolicitaTarjetaPresenter.TAG, "solicitaTarjetaMonto: pan: $pan")
                                    val track2 = SolicitaTarjetaPresenter.response!!.trackII
                                    Log.d(SolicitaTarjetaPresenter.TAG, "solicitaTarjetaMonto: trackII: $track2")
                                    Log.d(
                                        SolicitaTarjetaPresenter.TAG,
                                        "solicitaTarjetaMonto: trackII: " + track2.substring(8, 9)
                                    )
                                    if (pan.length < 15) {
                                        return "ERROR EN LA LECTURA 15".also { SolicitaTarjetaPresenter.mensaje = it }
                                    } else {
                                        auxPan = pan.substring(0, 8)
                                        var tamanoPrefijo = SolicitaTarjetaPresenter.auxPan.length
                                        var contador = 0
                                        while (contador < SolicitaTarjetaPresenter.auxPan.length) {
                                            if (binesDAOWeakReference!!.get()!!.getTcPrefijo(Integer.parseInt(auxPan.substring(0, tamanoPrefijo))) != null) {

                                                puntos = binesDAOWeakReference!!.get()!!.getTcPrefijo(Integer.parseInt(auxPan.substring(0, tamanoPrefijo))).TC_TIPOCOM
                                                auxPan = auxPan.substring(0, tamanoPrefijo)
                                                contador = 8

                                            } else {

                                                puntos = 3
                                                contador++
                                                tamanoPrefijo--

                                            }

                                        }
                                        if (SolicitaTarjetaPresenter.puntos == 3) {
                                            return "No hay forma de pago asociada al prefijo.".also {
                                                SolicitaTarjetaPresenter.mensaje = it
                                            }
                                        } else {
                                            if (SolicitaTarjetaPresenter.auxPan == "603778" && track2.substring(
                                                    8,
                                                    9
                                                ) != "7"
                                            ) {
                                                return "Prefijo de GECapital no autorizado".also {
                                                    SolicitaTarjetaPresenter.mensaje = it
                                                }
                                            } else {
                                                if (track2.substring(0, 9) == "588772023") {
                                                    return "Prefijo de Vale Electrónico no Autorizado".also {
                                                        SolicitaTarjetaPresenter.mensaje = it
                                                    }
                                                } else {
                                                    if (SolicitaTarjetaPresenter.auxPan == "588772") {
                                                        val prefijoTrack = track2.substring(0, 8)
                                                        if (prefijoTrack.isEmpty()) {
                                                            val auxTrack2 = track2.substring(0, 9)
                                                            return if (auxTrack2.isEmpty()) {
                                                                "Prefijo de Vale Electrónico no autorizado".also {
                                                                    SolicitaTarjetaPresenter.mensaje = it
                                                                }
                                                            } else {
                                                                "Prefijo de Vale Electrónico caducado, no autorizado".also {
                                                                    SolicitaTarjetaPresenter.mensaje = it
                                                                }
                                                            }
                                                        }
                                                    } else {
                                                        if (SolicitaTarjetaPresenter.auxPan == "627636") {
                                                            val auxTrack2 = track2.substring(0, 8)
                                                            if (auxTrack2.isEmpty()) {
                                                                return "Prefijo de Vale Electrónico no autorizado".also {
                                                                    SolicitaTarjetaPresenter.mensaje = it
                                                                }
                                                            }
                                                        } else {
                                                            if (SolicitaTarjetaPresenter.auxPan == "627318") {
                                                                val auxTrack2 =
                                                                    track2.substring(0, 8)
                                                                if (auxTrack2.isEmpty()) {
                                                                    return "Prefijo de Efectivale Electrónico no autorizado".also {
                                                                        SolicitaTarjetaPresenter.mensaje = it
                                                                    }
                                                                }
                                                            } else {
                                                                if (SolicitaTarjetaPresenter.puntos == 0) {
                                                                    Log.d(
                                                                        SolicitaTarjetaPresenter.TAG,
                                                                        "solicitaTarjetaMonto: Continua el proceso*******************"
                                                                    )
                                                                    SolicitaTarjetaPresenter.idFPGO =
                                                                        SolicitaTarjetaPresenter.binesDAOWeakReference!!.get()!!
                                                                            .getTcPrefijo(
                                                                                SolicitaTarjetaPresenter.auxPan.toInt()).SA_IDFPGO
                                                                    SolicitaTarjetaPresenter.tipoFPGO =
                                                                        SolicitaTarjetaPresenter.binesDAOWeakReference!!.get()!!
                                                                            .getTcPrefijo(
                                                                                SolicitaTarjetaPresenter.auxPan.toInt()).TIPO_FPGO
                                                                    return "OK".also {
                                                                        SolicitaTarjetaPresenter.mensaje = it
                                                                    }
                                                                } else {
                                                                    Log.d(
                                                                        SolicitaTarjetaPresenter.TAG,
                                                                        "solicitaTarjetaMonto: puntos valor: " + SolicitaTarjetaPresenter.puntos
                                                                    )
                                                                    if (SolicitaTarjetaPresenter.puntos == 1) {
                                                                        SolicitaTarjetaPresenter.idFPGO =
                                                                            SolicitaTarjetaPresenter.binesDAOWeakReference!!.get()!!
                                                                                .getTcPrefijo(
                                                                                    SolicitaTarjetaPresenter.auxPan.toInt()).SA_IDFPGO
                                                                        SolicitaTarjetaPresenter.tipoFPGO =
                                                                            SolicitaTarjetaPresenter.binesDAOWeakReference!!.get()!!
                                                                                .getTcPrefijo(
                                                                                    SolicitaTarjetaPresenter.auxPan.toInt()).TIPO_FPGO
                                                                        return "OK".also {
                                                                            SolicitaTarjetaPresenter.mensaje = it
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    return "Prefijo desconocido, no se autoriza la tarjeta".also {
                                        SolicitaTarjetaPresenter.mensaje = it
                                    }
                                }
                            } else {
                                return "Error al generar EMV".also { SolicitaTarjetaPresenter.mensaje = it }
                            }
                        }
                    } else {
                        return "Error al generar EMV".also { SolicitaTarjetaPresenter.mensaje = it }
                    }
                    SolicitaTarjetaPresenter.mensaje
                } else {
                    VerifoneTaskManager.restPinpad()
                    "Error de lectura 2".also { SolicitaTarjetaPresenter.mensaje = it }
                }
            } else {
                "Error conexión no establecida \n con la pinpad".also { SolicitaTarjetaPresenter.mensaje = it }
            }
        }

        override fun onPostExecute(s: String) {
            super.onPostExecute(s)
            SolicitaTarjetaPresenter.filesWeakReference!!.get()!!
                .registerLogs("Termina C51Task", s, SolicitaTarjetaPresenter.preferenceHelperLogs!!, SolicitaTarjetaPresenter.preferenceHelper!!)
            if (s == "OK") {
                if (SolicitaTarjetaPresenter.puntos == 0) {
                    SolicitaTarjetaPresenter.view!!.onSuccessSolicitaTarjetaResult(
                        SolicitaTarjetaPresenter.response,
                        SolicitaTarjetaPresenter.year,
                        SolicitaTarjetaPresenter.month,
                        SolicitaTarjetaPresenter.day,
                        SolicitaTarjetaPresenter.idFPGO,
                        SolicitaTarjetaPresenter.tipoFPGO,
                        "0",
                        SolicitaTarjetaPresenter.response!!.transactionType
                    )
                } else {
                    SolicitaTarjetaPresenter.view!!.onSuccessSolicitaTarjetaResult(
                        SolicitaTarjetaPresenter.response,
                        SolicitaTarjetaPresenter.year,
                        SolicitaTarjetaPresenter.month,
                        SolicitaTarjetaPresenter.day,
                        SolicitaTarjetaPresenter.idFPGO,
                        SolicitaTarjetaPresenter.tipoFPGO,
                        "1",
                        SolicitaTarjetaPresenter.response!!.transactionType
                    )
                }
            } else {
                VerifoneTaskManager.limpiarTerminal()
                SolicitaTarjetaPresenter.view!!.onFailSolicitaTarjetaResult(s)
            }
        }
    }

    override fun executeSolicitaTarjetaPresenter() {
        C51Task().execute()
    }
}