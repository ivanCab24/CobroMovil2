package com.Model

import android.os.AsyncTask
import android.util.Log
import com.Constants.ConstantsPreferences
import com.Interfaces.PinpadTaskMVP
import com.Presenter.PinpadTaskPresenter
import com.Verifone.BtManager
import com.Verifone.VerifoneTaskManager.sendACK2
import com.View.Fragments.ContentFragment

class PinPadTaskModel:PinpadTaskMVP.Model {
    /**
     * Type: Class.
     * Access: Public.
     * Name: TaskPinpadConection.
     *
     * @Description.
     * @EndDescription.
     */
    class TaskPinpadConection
    /**
     * Type: Method.
     * Parent: TaskPinpadConection.
     * Name: TaskPinpadConection.
     *
     * @param btManager @PsiType:BtManager.
     * @Description.
     * @EndDescription.
     */
        (private var btManager: BtManager?) : AsyncTask<Void?, String?, String>() {
        override fun doInBackground(vararg p0: Void?): String{
            Log.i("ssssssss","sdsdjsjdoods")
            PinpadTaskPresenter.filesWeakReference!!.get()!!.registerLogs(
                "Inicia TaskPinpadConnection",
                "",
                PinpadTaskPresenter.preferenceHelperLogs!!,
                PinpadTaskPresenter.preferenceHelper!!
            )
            var respuesta:String

            if (btManager == null) {
                btManager = BtManager(
                    ContentFragment.activity,
                    PinpadTaskPresenter.preferenceHelper!!.getString(ConstantsPreferences.PREF_PINPAD, null)
                )
            }

            respuesta = if (!btManager!!.isBtConected) {
                if (btManager!!.BtSppWaitClient() != BtManager.BT_RETULT.SUCCESS) "Fail"
                else "SUCCESS"
            } else {
                sendACK2(btManager)
                "SUCCESS"
            }
            return respuesta
        }

        override fun onPostExecute(s: String) {
            PinpadTaskPresenter.filesWeakReference!!.get()!!.registerLogs(
                "Termina PinpadTaskConnection",
                s,
                PinpadTaskPresenter.preferenceHelperLogs!!,
                PinpadTaskPresenter.preferenceHelper!!
            )
            if (s == "SUCCESS") PinpadTaskPresenter.view!!.onSuccessPinpadTaskConnection("Conexión correcta", btManager)
            else PinpadTaskPresenter.view!!.onFailPinpadTaskConnection("No se pudo conectar a la pinpad\nSi el problema persiste reinicie el dispositivo")
        }
    }

    override fun executeTaskPinpadConection(btManager: BtManager?) {
        TaskPinpadConection(btManager).execute()
    }
}