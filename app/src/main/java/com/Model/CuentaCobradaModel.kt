package com.Model

import android.os.AsyncTask
import com.Controller.BD.Entity.CuentaCobrada
import com.Interfaces.getCuentaCobradaMVP
import com.Presenter.GetCuentaCobradaPresenter
import java.text.SimpleDateFormat
import java.util.*

class CuentaCobradaModel:getCuentaCobradaMVP.Model {
    private class GetCuentasCobradasTask : AsyncTask<String?, Void?, List<CuentaCobrada?>>() {
        override fun onPostExecute(cuentaCobradaList: List<CuentaCobrada?>) {
            super.onPostExecute(cuentaCobradaList)
            GetCuentaCobradaPresenter.view!!.onReceiveCuentasCobradas(cuentaCobradaList)
        }

        override fun doInBackground(vararg p0: String?): List<CuentaCobrada?> {
            val day = SimpleDateFormat("dd").format(Date()).toInt()
            return GetCuentaCobradaPresenter.cuentaCobradaDAOWeakReference!!.get()!!.getAllCuentaCobrada(p0[0], day)
        }
    }

    private class GetAllCuentasCobradasTask : AsyncTask<String?, Void?, List<CuentaCobrada?>>() {
        override fun onPostExecute(cuentaCobradaList: List<CuentaCobrada?>) {
            super.onPostExecute(cuentaCobradaList)
            GetCuentaCobradaPresenter.view!!.onReceiveAllCuentasCobradas(cuentaCobradaList)
        }

        override fun doInBackground(vararg p0: String?): List<CuentaCobrada?> {
            return GetCuentaCobradaPresenter.cuentaCobradaDAOWeakReference!!.get()!!.allCuentaCobrada
        }
    }

    private class GetEstafetask : AsyncTask<Void?, Void?, List<String?>>() {
        override fun onPostExecute(stringList: List<String?>) {
            super.onPostExecute(stringList)
            GetCuentaCobradaPresenter.view!!.onReciveEstafetas(stringList)
        }

        override fun doInBackground(vararg p0: Void?): List<String?> {
            GetCuentaCobradaPresenter.view!!.onReciveSumas(
                GetCuentaCobradaPresenter.cuentaCobradaDAOWeakReference!!.get()!!.sumas
            )
            GetCuentaCobradaPresenter.view!!.onRecivePropinas(
                GetCuentaCobradaPresenter.cuentaCobradaDAOWeakReference!!.get()!!.propinas
            )
            GetCuentaCobradaPresenter.view!!.onReciveTotalGlobal(
                GetCuentaCobradaPresenter.cuentaCobradaDAOWeakReference!!.get()!!.totalGlobal
            )
            GetCuentaCobradaPresenter.view!!.onRecivePropinasGlobal(
                GetCuentaCobradaPresenter.cuentaCobradaDAOWeakReference!!.get()!!.totalPropinas
            )
            return GetCuentaCobradaPresenter.cuentaCobradaDAOWeakReference!!.get()!!.estafetas
        }
    }

    private class GetCountRecordsTask : AsyncTask<Void?, Void?, Int>() {
        override fun onPostExecute(countRecords: Int) {
            super.onPostExecute(countRecords)
            GetCuentaCobradaPresenter.view!!.onReciveCountRecord(countRecords)
        }

        override fun doInBackground(vararg p0: Void?): Int {
            return GetCuentaCobradaPresenter.cuentaCobradaDAOWeakReference!!.get()!!.countRecords()
        }
    }

    private class DeleteRecordsTask : AsyncTask<Int?, Void?, Void?>() {
        override fun doInBackground(vararg p0: Int?): Void? {
            p0!![0]!!.let {
                GetCuentaCobradaPresenter.cuentaCobradaDAOWeakReference!!.get()!!.deleteRecords(
                    it
                )
            }
            return null
        }
    }

    override fun getAllCuentasCobradas() {
        GetAllCuentasCobradasTask().execute()
    }

    override fun getCuentasCobradas(estafeta: String?) {
        GetCuentasCobradasTask().execute(estafeta)
    }

    override fun getEstafetas() {
        GetEstafetask().execute()
    }

    override fun countRecords() {
        GetCountRecordsTask().execute()
    }

    override fun deleteRecords(month: Int) {
        DeleteRecordsTask().execute(month)
    }
}