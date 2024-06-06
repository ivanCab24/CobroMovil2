package com.Model

import android.os.AsyncTask
import android.util.Log
import com.Interfaces.SolicitaRefoundMVP
import com.Presenter.SolicitaRefoundPresenter
import com.Verifone.CommandUtils
import com.Verifone.VerifonePinpadInterface
import com.View.Fragments.ContentFragment
import org.apache.commons.lang3.ArrayUtils
import java.nio.ByteBuffer
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class SolicitaRefoundModel:SolicitaRefoundMVP.Model {
    private class C51RefoundTask : AsyncTask<Void?, Void?, String>() {
        override fun doInBackground(vararg p0: Void?): String {
            SolicitaRefoundPresenter.filesWeakReference!!.get()!!.registerLogs(
                "Inicia C51RefoundTask",
                "",
                SolicitaRefoundPresenter.preferenceHelperLogs!!,
                SolicitaRefoundPresenter.preferenceHelper!!
            )
            var regreso: ArrayList<String>
            val chDataIn = ByteArray(1024)
            val chAck = byteArrayOf(0x06)
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
                SolicitaRefoundPresenter.filesWeakReference!!.get()!!.createFileException(
                    "Controller/SolicitaRefoundTask/Fechas " + Log.getStackTraceString(e),
                    SolicitaRefoundPresenter.preferenceHelper!!
                )
            }
            VerifonePinpadInterface.MSG_C51_REFOUND[11] =
                java.lang.Byte.valueOf((today.year - 100).toString(), 16)
            VerifonePinpadInterface.MSG_C51_REFOUND[12] =
                java.lang.Byte.valueOf(month.toString(), 16)
            VerifonePinpadInterface.MSG_C51_REFOUND[13] =
                java.lang.Byte.valueOf(today.date.toString(), 16)
            VerifonePinpadInterface.MSG_C51_REFOUND[16] =
                java.lang.Byte.valueOf(today.hours.toString(), 16)
            VerifonePinpadInterface.MSG_C51_REFOUND[17] =
                java.lang.Byte.valueOf(today.minutes.toString(), 16)
            VerifonePinpadInterface.MSG_C51_REFOUND[18] =
                java.lang.Byte.valueOf(today.seconds.toString(), 16)
            val amTotal =
                (ContentFragment.pagos!!.abonoMn + ContentFragment.pagos!!.abonoPropina).toInt()
            val byteAmount = ByteBuffer.allocate(4).putInt((amTotal * 100)).array()
            VerifonePinpadInterface.MSG_C51_REFOUND[24] = byteAmount[0]
            VerifonePinpadInterface.MSG_C51_REFOUND[25] = byteAmount[1]
            VerifonePinpadInterface.MSG_C51_REFOUND[26] = byteAmount[2]
            VerifonePinpadInterface.MSG_C51_REFOUND[27] = byteAmount[3]
            val newCadena =
                CommandUtils.byteArrayToHexString(VerifonePinpadInterface.MSG_C51_REFOUND)
            val auxCad = newCadena.replace(" ", "")
            val enviaLrc = CommandUtils.hexToAscii(auxCad)
            val auxlrc1 = CommandUtils.calculateLRC(enviaLrc)
            val msgc51 = ArrayUtils.addAll(VerifonePinpadInterface.MSG_C51_REFOUND, auxlrc1)
            return if (ContentFragment.contentFragment!!.btManager != null) {
                ContentFragment.contentFragment!!.btManager!!.BtSendData(msgc51)
                regreso = ContentFragment.contentFragment!!.btManager!!.BtRecvData(
                    chDataIn,
                    10000
                ) //10000
                regreso = ContentFragment.contentFragment!!.btManager!!.BtRecvData(
                    chDataIn,
                    30000
                ) //30000

                //ENVIO ACK DE RECIBIDO
                ContentFragment.contentFragment!!.btManager!!.BtSendData(chAck)
                try {
                    Thread.sleep(700)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                    SolicitaRefoundPresenter.filesWeakReference!!.get()!!.createFileException(
                        "Controller/SolicitaRefoundTask/InterrupedException " + Log.getStackTraceString(
                            e
                        ), SolicitaRefoundPresenter.preferenceHelper!!
                    )
                }
                val finalRegreso = regreso
                SolicitaRefoundPresenter.year = yearFormatted
                SolicitaRefoundPresenter.month = monthFormatted
                SolicitaRefoundPresenter.day = today.date.toString()
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
                if (raw.length > 0) {
                    raw = raw.substring(0, raw.length - 2)
                    val output = StringBuilder()
                    for (i in 0 until finalRegreso.size - 1) {
                        val str2 = finalRegreso[i]
                        val str = str2.replace("ffffff", "")
                        val strReplaceFFFD = str.replace("fffd", "0")
                        output.append(strReplaceFFFD.toInt(16).toChar())
                    }
                    val auxRaw = output.toString()
                    SolicitaRefoundPresenter.response = CommandUtils.parceC53(raw, auxRaw)
                    if (SolicitaRefoundPresenter.response != null) {
                        SolicitaRefoundPresenter.response!!.modoLectura = "N"
                        when (SolicitaRefoundPresenter.response!!.message) {
                            "29" -> return "Error " + CommandUtils.catalogStatus()["29"].also {
                                SolicitaRefoundPresenter.mensaje = it!!
                            }
                            "06" -> return "Error " + CommandUtils.catalogStatus()["06"].also {
                                SolicitaRefoundPresenter.mensaje = it!!
                            }
                            "23" -> return "Error " + CommandUtils.catalogStatus()["23"].also {
                                SolicitaRefoundPresenter.mensaje = it!!
                            }
                            "99" -> return "Error " + CommandUtils.catalogStatus()["99"].also {
                                SolicitaRefoundPresenter.mensaje = it!!
                            }
                            "63" -> return "Error " + CommandUtils.catalogStatus()["63"].also {
                                SolicitaRefoundPresenter.mensaje = it!!
                            }
                            else -> if (SolicitaRefoundPresenter.response!!.pan != null) {
                                if (!SolicitaRefoundPresenter.response!!.pan.isEmpty() || SolicitaRefoundPresenter.response!!.pan != null) {
                                    val pan = SolicitaRefoundPresenter.response!!.pan
                                    Log.i(SolicitaRefoundPresenter.TAG, "doInBackground: pan: $pan")
                                    val track2 = SolicitaRefoundPresenter.response!!.trackII
                                    Log.i(SolicitaRefoundPresenter.TAG, "doInBackground: trackII: $track2")
                                    Log.i(SolicitaRefoundPresenter.TAG, "doInBackground: trackII: " + track2.substring(8, 9))
                                    if (pan.length < 15) {
                                        return "ERROR EN LA LECTURA 15".also { SolicitaRefoundPresenter.mensaje = it }
                                    } else {
                                        SolicitaRefoundPresenter.auxPan = pan.substring(0, 8)
                                        var tamanoPrefijo = SolicitaRefoundPresenter.auxPan.length
                                        var puntos = 0
                                        var contador = 0
                                        while (contador < SolicitaRefoundPresenter.auxPan.length) {
                                            val auxPanpan = SolicitaRefoundPresenter.auxPan.substring(0, tamanoPrefijo)
                                            if (SolicitaRefoundPresenter.catalogoBinesDAOWeakReference!!.get()!!
                                                    .getTcPrefijo(
                                                        SolicitaRefoundPresenter.auxPan.substring(0, tamanoPrefijo).toInt()
                                                    ) != null
                                            ) {
                                                puntos = SolicitaRefoundPresenter.catalogoBinesDAOWeakReference!!.get()!!
                                                    .getTcPrefijo(
                                                        SolicitaRefoundPresenter.auxPan.substring(0, tamanoPrefijo).toInt()
                                                    ).TC_TIPOCOM
                                                SolicitaRefoundPresenter.auxPan = SolicitaRefoundPresenter.auxPan.substring(0, tamanoPrefijo)
                                                contador = 8
                                            } else {
                                                puntos = 3
                                                contador++
                                                tamanoPrefijo--
                                            }
                                        }
                                        if (puntos == 3) {
                                            return "No hay forma de pago asociada al prefijo.".also {
                                                SolicitaRefoundPresenter.mensaje = it
                                            }
                                        } else {
                                            if (SolicitaRefoundPresenter.auxPan == "603778" && track2.substring(
                                                    8,
                                                    9
                                                ) != "7"
                                            ) {
                                                return "Prefijo de GECapital no autorizado".also {
                                                    SolicitaRefoundPresenter.mensaje = it
                                                }
                                            } else {
                                                if (track2.substring(0, 9) == "588772023") {
                                                    return "Prefijo de Vale Electrónico no Autorizado".also {
                                                        SolicitaRefoundPresenter.mensaje = it
                                                    }
                                                } else {
                                                    if (SolicitaRefoundPresenter.auxPan == "588772") {
                                                        val prefijoTrack = track2.substring(0, 8)
                                                        if (prefijoTrack.isEmpty()) {
                                                            val auxTrack2 = track2.substring(0, 9)
                                                            return if (auxTrack2.isEmpty()) {
                                                                "Prefijo de Vale Electrónico no autorizado".also {
                                                                    SolicitaRefoundPresenter.mensaje = it
                                                                }
                                                            } else {
                                                                "Prefijo de Vale Electrónico caducado, no autorizado".also {
                                                                    SolicitaRefoundPresenter.mensaje = it
                                                                }
                                                            }
                                                        }
                                                    } else {
                                                        if (SolicitaRefoundPresenter.auxPan == "627636") {
                                                            val auxTrack2 = track2.substring(0, 8)
                                                            if (auxTrack2.isEmpty()) {
                                                                return "Prefijo de Vale Electrónico no autorizado".also {
                                                                    SolicitaRefoundPresenter.mensaje = it
                                                                }
                                                            }
                                                        } else {
                                                            if (SolicitaRefoundPresenter.auxPan == "627318") {
                                                                val auxTrack2 =
                                                                    track2.substring(0, 8)
                                                                if (auxTrack2.isEmpty()) {
                                                                    return "Prefijo de Efectivale Electrónico no autorizado".also {
                                                                        SolicitaRefoundPresenter.mensaje = it
                                                                    }
                                                                }
                                                            } else {
                                                                if (puntos == 0) {
                                                                    Log.i(
                                                                        SolicitaRefoundPresenter.TAG,
                                                                        "doInBackground: Continua el proceso*******************"
                                                                    )
                                                                    SolicitaRefoundPresenter.response!!.modoLectura = "N"
                                                                    return "OK".also {
                                                                        SolicitaRefoundPresenter.mensaje = it
                                                                    }
                                                                } else {
                                                                    if (puntos == 1) {
                                                                        Log.i(
                                                                            SolicitaRefoundPresenter.TAG,
                                                                            "doInBackground: Continua el proceso*******************"
                                                                        )
                                                                        SolicitaRefoundPresenter.response!!.modoLectura = "N"
                                                                        return "OK".also {
                                                                            SolicitaRefoundPresenter.mensaje = it
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
                                        SolicitaRefoundPresenter.mensaje = it
                                    }
                                }
                            } else {
                                return "Error al generar EMV".also { SolicitaRefoundPresenter.mensaje = it }
                            }
                        }
                    } else {
                        return "Error " + CommandUtils.catalogStatus()["99"].also { SolicitaRefoundPresenter.mensaje = it!! }
                    }
                    SolicitaRefoundPresenter.mensaje
                } else {
                    "Error de lectura 2".also { SolicitaRefoundPresenter.mensaje = it }
                }
            } else {
                "Error conexión no establecida \n con la pinpad".also { SolicitaRefoundPresenter.mensaje = it }
            }
        }

        override fun onPostExecute(s: String) {
            super.onPostExecute(s)
            SolicitaRefoundPresenter.filesWeakReference!!.get()!!.registerLogs(
                "Termina C51RefoundTask",
                s,
                SolicitaRefoundPresenter.preferenceHelperLogs!!,
                SolicitaRefoundPresenter.preferenceHelper!!
            )
            if (s == "OK") {
                SolicitaRefoundPresenter.view!!.onSuccessSolicitaRefoundResult(
                    SolicitaRefoundPresenter.response,
                    SolicitaRefoundPresenter.year,
                    SolicitaRefoundPresenter.month,
                    SolicitaRefoundPresenter.day,
                    SolicitaRefoundPresenter.ticket
                )
            } else {
                SolicitaRefoundPresenter.view!!.onFailSolicitaRefoundResult(s)
            }
        }
    }
    override fun executeC51RefoundTask() {
        C51RefoundTask().execute()
    }
}