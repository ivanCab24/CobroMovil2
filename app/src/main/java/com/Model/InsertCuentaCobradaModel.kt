package com.Model

import android.os.AsyncTask
import com.Constants.ConstantsPreferences
import com.Controller.BD.Entity.CuentaCobrada
import com.DataModel.SaleVtol
import com.Interfaces.InsertaCuentaCobradaMVP
import com.Presenter.InsertCuentaCobradaPresenter
import com.Verifone.EMVResponse
import com.View.Fragments.ContentFragment
import java.text.SimpleDateFormat
import java.util.*

class InsertCuentaCobradaModel:InsertaCuentaCobradaMVP.Model {
    private class cuentaCobradaTask(
        /**
         * The Sale vtol.
         */
        var saleVtol: SaleVtol?,
        /**
         * The Emv response.
         */
        var emvResponse: EMVResponse?
    ) : AsyncTask<Void?, Void?, Void?>() {
        override fun doInBackground(vararg p0: Void?): Void? {
            val day = SimpleDateFormat("dd").format(Date()).toInt()
            val month = SimpleDateFormat("MM").format(Date()).toInt()
            val year = SimpleDateFormat("yyyy").format(Date()).toInt()
            val hour = SimpleDateFormat("HH").format(Date()).toInt()
            val minutes = SimpleDateFormat("mm").format(Date()).toInt()
            val seconds = SimpleDateFormat("ss").format(Date()).toInt()
            val lasDigits = emvResponse!!.pan.substring(12)
            val cuentaCobrada = CuentaCobrada(
                day,
                month,
                year,
                hour,
                minutes,
                seconds,
                InsertCuentaCobradaPresenter.preferenceHelper!!.getString(ConstantsPreferences.PREF_NOMBRE_EMPLEADO, null),
                ContentFragment.montoCobro,
                ContentFragment.getTipAmount(),
                InsertCuentaCobradaPresenter.preferenceHelper!!.getString(ConstantsPreferences.PREF_ESTAFETA, null),
                InsertCuentaCobradaPresenter.preferenceHelper!!.getString(ConstantsPreferences.PREF_FOLIO, null),
                saleVtol!!.campo32,
                saleVtol!!.campo22,
                InsertCuentaCobradaPresenter.preferenceHelper!!.getString(ConstantsPreferences.PREF_MESA, null),
                lasDigits
            )
            InsertCuentaCobradaPresenter.cuentaCobradaDAOWeakReference!!.get()!!.insertaCuentaCobrada(cuentaCobrada)
            return null
        }
    }

    override fun executeCuentaCobradaTask(saleVtol: SaleVtol?, emvResponse: EMVResponse?) {
        cuentaCobradaTask(saleVtol, emvResponse).execute()
    }
}