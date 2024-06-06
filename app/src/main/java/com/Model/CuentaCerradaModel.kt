package com.Model

import android.os.AsyncTask
import android.util.Log
import com.Controller.BD.Entity.CuentaCerrada
import com.Interfaces.getCuentaCerradaMVP
import com.Presenter.GetCuentaCerradaPresenter.Companion.cuentaCerradaDAOWeakReference
import com.Presenter.GetCuentaCerradaPresenter.Companion.view

class CuentaCerradaModel:getCuentaCerradaMVP.Model {
    class getCuentasCerradasTask : AsyncTask<Void?, Void?, List<CuentaCerrada?>>() {
        override fun onPostExecute(cuentaCerradaList: List<CuentaCerrada?>) {
            super.onPostExecute(cuentaCerradaList)
            view!!.onReceiveCuentasCerradas(cuentaCerradaList)
        }

        override fun doInBackground(vararg p0: Void?): List<CuentaCerrada?> {
            val nc = CuentaCerrada(
                1,
                1,
                1,
                1,
                1,
                1,
                "estafeta",
                "nombre",
                44444,
                777,
                3,
                3,
                3.3,
                777.0,
                777.0,
                0.0,
                0,
                0,
                0,
                0,
                1,
                0,
                0,
                "447121020500500",
                2,
                ""
            )
            val a = cuentaCerradaDAOWeakReference!!.get()!!
                .allCuentasCerradas
            for (c in a) {
                Log.i("Cuenta cerrada: ", c.toString() + "")
            }
            a.add(nc)
            return a
        }
    }

    private class getCountRecordsTask : AsyncTask<Void?, Void?, Int>() {
        @Deprecated("Deprecated in Java")
        override fun onPostExecute(integer: Int) {
            super.onPostExecute(integer)
            view!!.onReceiveCountRecords(integer)
        }

        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg p0: Void?): Int {
            return cuentaCerradaDAOWeakReference!!.get()!!.countRecords
        }
    }

    private class DeleteRecordsTask : AsyncTask<Int?, Void?, Void?>() {
        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg p0: Int?): Void? {
            p0.forEach {cuentaCerradaDAOWeakReference!!.get()!!.deleteRecords(it!!)  }
            return null
        }
    }

    override fun executeGetCuentasCerradas() {
        getCuentasCerradasTask().execute()
    }

    override fun executeDeleteRecords(month: Int) {
        getCountRecordsTask().execute()
    }

    override fun executeCountRecords() {
       DeleteRecordsTask().execute()
    }

}