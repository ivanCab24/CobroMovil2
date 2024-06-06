package com.Model

import android.os.AsyncTask
import com.Constants.ConstantsPreferences
import com.Controller.BD.Entity.CuentaCerrada
import com.Interfaces.InsertaCuentaCerradaMVP
import com.Presenter.InsertCuentaCerradaPresenter
import com.View.Fragments.ContentFragment
import java.text.SimpleDateFormat
import java.util.*

class InsertCuentaCerradaModel:InsertaCuentaCerradaMVP.Model {
    private class insertaCuentaCerrada : AsyncTask<Void?, Void?, Void?>() {
        override fun doInBackground(vararg p0: Void?): Void? {
            val day = SimpleDateFormat("dd").format(Date()).toInt()
            val month = SimpleDateFormat("MM").format(Date()).toInt()
            val year = SimpleDateFormat("yyyy").format(Date()).toInt()
            val hour = SimpleDateFormat("HH").format(Date()).toInt()
            val minutes = SimpleDateFormat("mm").format(Date()).toInt()
            val seconds = SimpleDateFormat("ss").format(Date()).toInt()
            if (ContentFragment.saleVtol != null) {
                InsertCuentaCerradaPresenter.campo32 = ContentFragment.saleVtol!!.campo32
            }
            val cuentaCerrada = CuentaCerrada(
                day,
                month,
                year,
                hour,
                minutes,
                seconds,
                InsertCuentaCerradaPresenter.preferenceHelper!!.getString(ConstantsPreferences.PREF_ESTAFETA, null),
                InsertCuentaCerradaPresenter.preferenceHelper!!.getString(ConstantsPreferences.PREF_NOMBRE_EMPLEADO, null),
                ContentFragment.cuentaCerrada!!.fecha.toInt(),
                ContentFragment.cuentaCerrada!!.idLocal
                    .toInt(),
                ContentFragment.cuentaCerrada!!.idComa.toInt(),
                ContentFragment.cuentaCerrada!!.idPos
                    .toInt(),
                ContentFragment.cuentaCerrada!!.saldo,
                ContentFragment.cuentaCerrada!!.total,
                ContentFragment.cuentaCerrada!!.importe,
                ContentFragment.cuentaCerrada!!.desc,
                ContentFragment.cuentaCerrada!!.descTipo
                    .toInt(),
                ContentFragment.cuentaCerrada!!.descID.toInt(),
                ContentFragment.cuentaCerrada!!.referencia
                    .toInt(),
                ContentFragment.cuentaCerrada!!.cerrada,
                ContentFragment.cuentaCerrada!!.puedecerrar.toInt(),
                ContentFragment.cuentaCerrada!!.facturado,
                ContentFragment.cuentaCerrada!!.imprime,
                ContentFragment.cuentaCerrada!!.folio,
                ContentFragment.cuentaCerrada!!.numcomensales.toInt(),
                InsertCuentaCerradaPresenter.campo32
            )
            InsertCuentaCerradaPresenter.cuentaCerradaDAOWeakReference!!.get()!!.insertaCuentaCerrada(cuentaCerrada)
            return null
        }
    }

    override fun executeInsertaCuentaCerrada() {
        insertaCuentaCerrada().execute()
    }
}