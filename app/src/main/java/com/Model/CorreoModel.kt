package com.Model

import com.Constants.ConstantsPreferences
import com.Interfaces.CorreoMVP
import com.Utilities.Curl
import com.Utilities.PreferenceHelper
import com.webservicestasks.ToksWebServicesConnection
import dagger.Lazy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class CorreoModel @Inject constructor(private var presenter: Lazy<CorreoMVP.Presenter>, private var preferenceHelper: PreferenceHelper) :
    CorreoMVP.Model {
    private val job = Job()
    val coroutineContext: CoroutineContext //  override
        get() = job + Dispatchers.IO

    override fun enviaTicketRequest(badera: Int, folio: String, correo: String) {
        try {
            var params = HashMap<String, String>()
            params["Llave"] = ToksWebServicesConnection.KEY
            params["Cadena"] = ToksWebServicesConnection.STRING
            params["Folio"] = folio
            params["Unidad"] = preferenceHelper.getString(ConstantsPreferences.PREF_UNIDAD, null)
            params["datocliente"] = correo.toString()
            params["Canalenvio"] = "1"
            var reponse = Curl.sendPostRequest(params, "Envia_Ticket", preferenceHelper, badera)
            presenter!!.get().getV().onSuccessSend(reponse)
        } catch (e: Exception) {
            presenter.get().getV().onFailSend(e.message.toString())
        }

    }
}