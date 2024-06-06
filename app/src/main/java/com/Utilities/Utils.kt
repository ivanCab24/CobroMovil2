package com.Utilities

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.Constants.ConstantsAComer
import com.Constants.ConstantsDescuentos
import com.Constants.ConstantsMarcas
import com.View.UserInteraction
import com.github.ybq.android.spinkit.style.DoubleBounce
import com.innovacion.eks.beerws.BuildConfig
import com.innovacion.eks.beerws.R
import com.webservicestasks.ToksWebServiceExceptionRate
import mx.com.adquira.blueadquira.pockdata.StateDefine
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.ksoap2.serialization.PropertyInfo
import org.ksoap2.serialization.SoapObject
import java.text.SimpleDateFormat
import java.util.*

/**
 * Type: Class.
 * Access: Public.
 * Name: Utils.
 *
 * @Description.
 * @EndDescription.
 */
object Utils {
    private const val TAG = "Utils"

    /**
     * Type: Method.
     * Parent: Utils.
     * Name: hideSoftKeyboard.
     *
     * @param view    @PsiType:View.
     * @param context @PsiType:Context.
     * @Description.
     * @EndDescription.
     */
    @JvmStatic
    fun hideSoftKeyboard(view: View?, context: Context?) {
        if (view != null) {
            val inputMethodManager =
                context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    /**
     * Type: Method.
     * Parent: Utils.
     * Name: checkIfBluetoothIsOn.
     *
     * @param activity @PsiType:Activity.
     * @Description.
     * @EndDescription.
     */
    @SuppressLint("MissingPermission")
    fun checkIfBluetoothIsOn(activity: Activity) {
        val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (mBluetoothAdapter == null) {
            UserInteraction.snackyException(activity, null, "Bluetooth no soportado")
        } else {
            if (mBluetoothAdapter.isEnabled) {
                //UserInteraction.snackySuccess(activity, null, "Bluetooth activado");
            } else {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                activity.startActivityForResult(enableBtIntent, StateDefine.REQUEST_ENABLE_BT)
            }
        }
    }

    /**
     * Type: Method.
     * Parent: Utils.
     * Name: checkIfWifiIsOn.
     *
     * @param activity @PsiType:Activity.
     * @Description.
     * @EndDescription.
     */
    fun checkIfWifiIsOn(activity: Activity) {
        val wifiManager =
            activity.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        if (!wifiManager.isWifiEnabled) {
            wifiManager.isWifiEnabled = true
        }
    }

    /**
     * Type: Method.
     * Parent: Utils.
     * Name: printProperty.
     *
     * @param request @PsiType:SoapObject.
     * @Description.
     * @EndDescription.
     */
    fun printProperty(request: SoapObject) {
        for (i in 0 until request.propertyCount) {
            val piData = PropertyInfo()
            request.getPropertyInfo(i, piData)
            Log.i(TAG, "printProperty: " + piData.name + " Valor: " + piData.value)
        }
    }

    /**
     * Type: Method.
     * Parent: Utils.
     * Name: updateProperty.
     *
     * @param request @PsiType:SoapObject.
     * @param name    @PsiType:String.
     * @param value   @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    fun updateProperty(request: SoapObject, name: String, value: String?) {
        try {
            for (i in 0 until request.propertyCount) {
                val piData = PropertyInfo()
                request.getPropertyInfo(i, piData)
                if (name == piData.name) {
                    piData.value = value
                    request.setProperty(i, piData.value)
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    /**
     * Type: Method.
     * Parent: Utils.
     * Name: round.
     *
     * @param value  @PsiType:double.
     * @param places @PsiType:int.
     * @return double
     * @Description.
     * @EndDescription. double.
     */
    @JvmStatic
    fun round(value: Double, places: Int): Double {
        var value = value
        require(places >= 0)
        val factor = Math.pow(10.0, places.toDouble()).toLong()
        value *= factor
        val tmp = Math.round(value)
        return tmp.toDouble() / factor
    }

    /**
     * Type: Method.
     * Parent: Utils.
     * Name: hideKeyboard.
     *
     * @param activity @PsiType:Activity.
     * @Description.
     * @EndDescription.
     */
    @JvmStatic
    fun hideKeyboard(activity: Activity) {
        val view = activity.findViewById<View>(android.R.id.content)
        if (view != null) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    /**
     * Type: Method.
     * Parent: Utils.
     * Name: ocultarTecladoAlToque.
     *
     * @param view    @PsiType:View.
     * @param context @PsiType:Context.
     * @param window  @PsiType:Window.
     * @Description.
     * @EndDescription.
     */
    @SuppressLint("ClickableViewAccessibility")
    fun ocultarTecladoAlToque(view: View, context: Context) {
        view.setOnTouchListener { _, _ ->
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
            true
        }
    }

    /**
     * Type: Method.
     * Parent: Utils.
     * Name: imprimeTicketError.
     *
     * @return array list
     * @Description.
     * @EndDescription. array list.
     */
    @JvmStatic
    fun imprimeTicketError(): ArrayList<String> {
        val arrayOperacionCancelada = ArrayList<String>()
        arrayOperacionCancelada.add("Pago no procesado")
        arrayOperacionCancelada.add("")
        arrayOperacionCancelada.add("OPERACION CANCELADA")
        arrayOperacionCancelada.add("")
        return arrayOperacionCancelada
    }

    /**
     * Type: Method.
     * Parent: Utils.
     * Name: generaTicketAcomerClub.
     *
     * @param monto @PsiType:double.
     * @return array list
     * @Description.
     * @EndDescription. array list.
     */
    @JvmStatic
    fun generaTicketAcomerClub(monto: Double): ArrayList<String> {
        val puntos = monto * .10
        val arrayAcomer = ArrayList<String>()
        arrayAcomer.add("")
        arrayAcomer.add("A COMER CLUB")
        arrayAcomer.add("En este consumo puedes acumular hasta")
        arrayAcomer.add("( " + puntos.toInt() + " ) punto(s).")
        arrayAcomer.add("Conoce mas y registrate en: ")
        arrayAcomer.add("acomerclub.com.mx")
        arrayAcomer.add("")
        return arrayAcomer
    }

    /**
     * Type: Method.
     * Parent: Utils.
     * Name: generaComprobanteDescuentoGRGAComer.
     *
     * @param estafetaAutorizadora @PsiType:String.
     * @param nombreEmpleado       @PsiType:String.
     * @param estafeta             @PsiType:String.
     * @return array list
     * @Description.
     * @EndDescription. array list.
     */
    @JvmStatic
    fun generaComprobanteDescuentoGRGAComer(
        estafetaAutorizadora: String,
        nombreEmpleado: String,
        estafeta: String
    ): ArrayList<String> {
        val arrayListDescuento = ArrayList<String>()
        arrayListDescuento.add("")
        arrayListDescuento.add("AUTORIZA: $estafetaAutorizadora")
        arrayListDescuento.add("EMPLEADO: $nombreEmpleado")
        arrayListDescuento.add("ESTAFETA: $estafeta")
        arrayListDescuento.add("")
        arrayListDescuento.add("________________________")
        arrayListDescuento.add("")
        return arrayListDescuento
    }

    @JvmStatic
    fun generaOfflineTic(
        nombreMiembro: String,
        puntosActuales: String
    ): ArrayList<String> {
        val arrayAcomer = ArrayList<String>()
        arrayAcomer.add("")
        arrayAcomer.add("A Comer Club en Offline")
        arrayAcomer.add(nombreMiembro)
        arrayAcomer.add("Actualmente tienes: $puntosActuales punto(s).")
        arrayAcomer.add("La actualización de saldo en tu cuenta se verá reflejado en un periodo de hasta\n 24 hrs.")
        arrayAcomer.add("Contáctatenos al 800 713 9500")
        arrayAcomer.add("o escríbenos al clientes@acomerclub.com.mx")
        arrayAcomer.add("")
        return arrayAcomer
    }

    /**
     * Type: Method.
     * Parent: Utils.
     * Name: generaComprobanteAcumulacionAcomer.
     *
     * @param nombreMiembro     @PsiType:String.
     * @param puntosAcumulados  @PsiType:String.
     * @param puntosActuales    @PsiType:String.
     * @param puntosCalificados @PsiType:String.
     * @param nivelActual       @PsiType:String.
     * @return array list
     * @Description.
     * @EndDescription. array list.
     */
    @JvmStatic
    fun generaComprobanteAcumulacionAcomer(
        nombreMiembro: String, puntosAcumulados: String,
        puntosActuales: String, puntosCalificados: String,
        nivelActual: String
    ): ArrayList<String> {
        val arrayAcomer = ArrayList<String>()
        arrayAcomer.add("")
        arrayAcomer.add("A COMER CLUB")
        arrayAcomer.add(nombreMiembro)
        arrayAcomer.add("acumulaste $puntosAcumulados punto(s).")
        arrayAcomer.add("Actualmente tienes: $puntosActuales punto(s).")
        arrayAcomer.add("Puntos para alcanzar el siguiente")
        arrayAcomer.add("nivel: $puntosCalificados")
        arrayAcomer.add("Nivel actual: $nivelActual")
        arrayAcomer.add("")
        return arrayAcomer
    }

    /**
     * Type: Method.
     * Parent: Utils.
     * Name: isUIThread.
     *
     * @return boolean
     * @Description.
     * @EndDescription. boolean.
     */
    val isUIThread: Boolean
        get() = Looper.getMainLooper().thread === Thread.currentThread()

    /**
     * Type: Method.
     * Parent: Utils.
     * Name: jsonLogcat.
     *
     * @param debug @PsiType:boolean.
     * @param level @PsiType:int.
     * @param tag   @PsiType:String.
     * @param msg   @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    @JvmStatic
    fun jsonLogcat(debug: Boolean, level: Int, tag: String, msg: String) {
        if (debug) {
            if (msg.isEmpty()) {
                Log.i(TAG, "jsonLogcat: $level$tag$msg")
            } else {
                val message: String
                message = try {
                    if (msg.startsWith("{")) {
                        val jsonObject = JSONObject(msg)
                        jsonObject.toString(4)
                    } else if (msg.startsWith("[")) {
                        val jsonArray = JSONArray(msg)
                        jsonArray.toString(4)
                    } else {
                        Log.i(TAG, "jsonLogcat: $msg")
                        msg
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    msg
                }
                Log.i(
                    TAG,
                    "jsonLogcat: Level $level Tag: $tag╔═══════════════════════════════════════════════════════════════════════════════════════"
                )
                val lines = ArrayList<String>()
                val myarray = message.split("\n").toTypedArray()
                Collections.addAll(lines, *myarray)
                for (i in lines.indices) {
                    Log.i(TAG, "jsonLogcat: Level " + level + " Tag: " + tag + " " + lines[i])
                }
                Log.i(
                    TAG,
                    "jsonLogcat: Level $level Tag: $tag╚═══════════════════════════════════════════════════════════════════════════════════════"
                )
            }
        }
    }

    /**
     * Type: Method.
     * Parent: Utils.
     * Name: dateAndTimeAComer.
     *
     * @return string
     * @Description.
     * @EndDescription. string.
     */
    @JvmStatic
    fun dateAndTimeAComer(): String {
        val date =
            SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
        val hora =
            SimpleDateFormat("HH:mm:ss", Locale.US).format(Date())
        return "$date $hora"
    }

    /**
     * Type: Method.
     * Parent: Utils.
     * Name: evaluarMarcaSeleccionada.
     *
     * @param marca @PsiType:String.
     * @return string
     * @Description.
     * @EndDescription. string.
     */
    @JvmStatic
    fun evaluarMarcaSeleccionada(marca: String): String {
        return if (marca == ConstantsMarcas.MARCA_BEER_FACTORY) {
            ConstantsAComer.MARCA_BEER
        } else if (marca == ConstantsMarcas.MARCA_TOKS) {
            ConstantsAComer.MARCA_TOKS
        } else {
            ConstantsAComer.MARCA_FAROLITO
        }
    }

    /**
     * Type: Method.
     * Parent: Utils.
     * Name: getDoubleBounce.
     *
     * @param context @PsiType:Context.
     * @param marca   @PsiType:String.
     * @return the double bounce
     * @Description.
     * @EndDescription.
     */
    @JvmStatic
    fun getDoubleBounce(context: Context, marca: String?): DoubleBounce {
        val doubleBounce = DoubleBounce()
        when (marca) {
            ConstantsMarcas.MARCA_BEER_FACTORY -> doubleBounce.color =
                context.resources.getColor(R.color.progressbar_color_beer)
            ConstantsMarcas.MARCA_EL_FAROLITO -> doubleBounce.color =
                context.resources.getColor(R.color.progressbar_color_farolito)
            ConstantsMarcas.MARCA_TOKS -> doubleBounce.color =
                context.resources.getColor(R.color.progressbar_color_toks)
            else -> doubleBounce.color = context.resources.getColor(R.color.progressbar_color_toks)
        }
        return doubleBounce
    }

    /**
     * Type: Method.
     * Parent: Utils.
     * Name: catalogDescuentoCuponFrecuencia.
     *
     * @return hash map
     * @Description.
     * @EndDescription. hash map.
     */
    fun catalogDescuentoCuponFrecuencia(): HashMap<String, String> {
        val cuponFrecuenciaCatalog = HashMap<String, String>()
        cuponFrecuenciaCatalog[ConstantsDescuentos.DESCUENTO_FRECUENTE] = "30"
        cuponFrecuenciaCatalog[ConstantsDescuentos.DESCUENTO_REGULAR] = "30"
        cuponFrecuenciaCatalog[ConstantsDescuentos.DESCUENTO_OCASIONAL] = "40"
        cuponFrecuenciaCatalog[ConstantsDescuentos.DESCUENTO_NUEVO_REGRESA] = "40"
        cuponFrecuenciaCatalog[ConstantsDescuentos.DESCUENTO_LAPSED] = "50"
        return cuponFrecuenciaCatalog
    }

    /**
     * Type: Method.
     * Parent: Utils.
     * Name: catalogDescuentosCorporativos.
     *
     * @return hash map
     * @Description.
     * @EndDescription. hash map.
     */
    @JvmStatic
    fun catalogDescuentosCorporativos(): HashMap<Int, String> {
        val tipoMembresia = HashMap<Int, String>()
        tipoMembresia[50] = "GRG"
        tipoMembresia[12] = "PETCO"
        tipoMembresia[10] = "GI"
        tipoMembresia[2] = "GG"
        tipoMembresia[9] = "HS"
        tipoMembresia[19] = "SET"
        tipoMembresia[25] = "OD"
        tipoMembresia[26] = "PM"
        tipoMembresia[27] = "FE"
        tipoMembresia[6] = "GP"
        tipoMembresia[28] = "CI"
        tipoMembresia[29] = "GS"
        //tipoMembresia[6] = "FORMAS EFICIENTES"
        return tipoMembresia
    }

    fun catalogDescuentosCorporativosINV(): HashMap<String, Int> {
        val tipoMembresia = HashMap<String, Int>()
        tipoMembresia["GRG"] = 50

        tipoMembresia["PETCO"] = 12
        tipoMembresia["GI"] = 10
        tipoMembresia["GG"] = 2
        tipoMembresia["HS"] = 9
        tipoMembresia["SET"] = 19
        tipoMembresia["OD"] = 25
        tipoMembresia["PM"] = 26
        tipoMembresia["FE"] = 27
        tipoMembresia["GP"] = 6
        tipoMembresia["CI"] = 28
        tipoMembresia["GS"] = 29
        //tipoMembresia[6] = "FORMAS EFICIENTES"
        return tipoMembresia
    }

    /**
     * Type: Method.
     * Parent: Utils.
     * Name: getMonth.
     *
     * @return the month
     * @Description.
     * @EndDescription.
     */
    val month: Int
        get() = SimpleDateFormat("MM").format(Date()).toInt()

    /**
     * Type: Method.
     * Parent: Utils.
     * Name: getExceptionRated.
     *
     * @param e @PsiType:Exception.
     * @return string
     * @Description.
     * @EndDescription. string.
     */
    fun getExceptionRated(e: Exception?): String {
        return ToksWebServiceExceptionRate.rateError(Log.getStackTraceString(e))
    }

    /**
     * Type: Method.
     * Parent: Utils.
     * Name: getVersionName.
     *
     * @return the version name
     * @Description.
     * @EndDescription.
     */
    val versionName: String
        get() = BuildConfig.VERSION_NAME
}