package com.Presenter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.SharedPreferences
import android.graphics.Paint
import android.graphics.Rect
import android.view.Gravity
import android.widget.TableRow
import android.widget.TextView
import com.Constants.ConstantsMarcas
import com.DataModel.Pagos
import com.Interfaces.DialogVisualizadorMVP
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import com.View.Fragments.ContentFragment
import com.innovacion.eks.beerws.R
import com.innovacion.eks.beerws.databinding.FragmentContentBinding
import com.innovacion.eks.beerws.databinding.VisualizadorDialogBinding
import java.lang.ref.WeakReference
import javax.inject.Inject


class DialogVisulizadorPresenter @Inject constructor(private var model: DialogVisualizadorMVP.Model) :DialogVisualizadorMVP.Presenter{

    private  var binding:VisualizadorDialogBinding? = null
    private val filas // Array de las filas de la tabla
            : ArrayList<TableRow>? = null
    private val actividad: Activity? = null
    private var FILAS = 0
    private  var COLUMNAS:Int = 0
    override fun setView(getView: DialogVisualizadorMVP.View) {
        view=getView
    }

    override fun getMarca():String {
        return model.getMarca()
    }

    override fun onResponseReceived(onResponse: String) {
        TODO("Not yet implemented")
    }

    private fun obtenerAnchoPixelesTexto(texto: String): Int {
        val p = Paint()
        val bounds = Rect()
        p.textSize = 50F
        p.getTextBounds(texto, 0, texto.length*4/6, bounds)
        return bounds.width()+8
    }

    @SuppressLint("ResourceType")
    override fun agregarCabecera() {
        var layoutCelda: TableRow.LayoutParams?
        val fila = TableRow(ContentFragment.activity)
        val layoutFila: TableRow.LayoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )
        fila.layoutParams = layoutFila
        fila.gravity = Gravity.CENTER
        val arraycabecera = arrayOfNulls<String>(5)
        arraycabecera[0] = "Mesa"
        //arraycabecera[1] = "#Pers"
        arraycabecera[1] = "#Ticket"

        arraycabecera[2] = "Información"
        arraycabecera[3] = "Estado"
        arraycabecera[4] = "Total"
        //arraycabecera[5] = "Serie"
        //arraycabecera[6] = "Folio"
        //arraycabecera[7] = "#Corte"
        for (i in arraycabecera.indices) {
            val texto = TextView(ContentFragment.activity)
            layoutCelda = TableRow.LayoutParams(
                obtenerAnchoPixelesTexto(arraycabecera[i]!!),
                45
            )
            texto.text = arraycabecera[i]
            texto.gravity = Gravity.CENTER
            texto.setTextAppearance(ContentFragment.activity, R.attr.estilo_edittext_propina);
            var marca = model.getMarca()
            when(marca){
                ConstantsMarcas.MARCA_BEER_FACTORY -> {
                    texto.setBackgroundResource(R.drawable.tabla_celda_cabecera)
                    texto.setTextAppearance(ContentFragment.activity, R.style.estiloTipButtonsBeer)
                }
                ConstantsMarcas.MARCA_TOKS -> {
                    texto.setBackgroundResource(R.drawable.tabla_celda_cabecera_toks)
                    texto.setTextAppearance(ContentFragment.activity, R.style.estiloTipButtonsToks)
                }
                ConstantsMarcas.MARCA_EL_FAROLITO -> {
                    texto.setBackgroundResource(R.drawable.tabla_celda_cabecera_farol)
                    texto.setTextAppearance(ContentFragment.activity, R.style.estiloTipButtonsFarolito)
                }
            }
            texto.layoutParams = layoutCelda
            fila.addView(texto)
        }
        binding!!.tableVisualizador.addView(fila)
    }

    override fun agregarContenido(registros:ArrayList<ArrayList<String>>) {
        for (registro in registros) {
            var layoutCelda: TableRow.LayoutParams?
            val fila = TableRow(ContentFragment.activity)
            fila.gravity = Gravity.CENTER
            val layoutFila: TableRow.LayoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )
            fila.layoutParams = layoutFila
            for (celda in registro) {
                val texto = TextView(ContentFragment.activity)
                layoutCelda = TableRow.LayoutParams(
                    obtenerAnchoPixelesTexto(celda),
                    40
                )
                texto.text = celda
                texto.gravity = Gravity.CENTER
                texto.setBackgroundResource(R.drawable.tabla_celda_cabecera_white)
                texto.setTextAppearance(ContentFragment.activity, R.style.estiloCeldaWhite)
                texto.layoutParams = layoutCelda
                fila.addView(texto)
            }
            binding!!.tableVisualizador.addView(fila)
        }
    }

    override fun setBinding(binding: VisualizadorDialogBinding) {
        this.binding=binding
    }

    override fun executeCatalogoPayRequest() {
        model.executeCatalogoPayRequest()
    }

    companion object {
        var view: DialogVisualizadorMVP.View? = null
        var getSharedPreferences: SharedPreferences? = null
        var preferencesHelper: PreferenceHelper? = null
        var preferenceHelperLogs: PreferenceHelperLogs? = null
        var filesWeakReference: WeakReference<Files?>? = null
        val pagosArrayList = ArrayList<Pagos?>()
        var fragmentContentBindingWeakReference: WeakReference<FragmentContentBinding?>? = null
    }
}