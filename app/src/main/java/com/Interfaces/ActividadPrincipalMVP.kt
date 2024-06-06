package com.Interfaces

import androidx.fragment.app.FragmentManager
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import com.View.Activities.ActividadPrincipal

interface ActividadPrincipalMVP {

    interface View {
        fun getCurrentMarca(value: String)
    }

    interface Presenter {
        //================================================================================
        // View
        //================================================================================
        fun setView(view: View)
        fun setFiles(files: Files)
        fun setPreferenceHelperLogs(preferenceHelperLogs: PreferenceHelperLogs)
        fun setPreferenceHelper(preferenceHelper: PreferenceHelper)
        fun setDefaultSharedPreferences()
        fun disableBtns()

        fun setActivity(activity: ActividadPrincipal)

        //================================================================================
        // Model
        //================================================================================
        fun sendTickets()
        fun sendLogs()
        fun getMarca()

        fun btnIdentificarMiembro()
        fun setFM(fragmentManager: FragmentManager)
        fun btnResetKey()
        fun btnHistorico()
        fun btnSettings()
        fun btnVisualizador()

    }

    interface Model {
        fun getMarca(): String
        fun executeTicketNoEnviados()
        fun executeLogs()
        fun executeDefaultSharedPreferences()
    }
}