package com.Interfaces

import android.content.SharedPreferences
import com.DataModel.Miembro
import com.DataModel.Producto
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs

/**
 * Type: Interface.
 * Parent: ConsultaAfiliadoPresenter.java.
 * Name: ConsultaAfiliadoPresenter.
 */
interface ConsultaAfiliadoMVP {
    /**
     * Type: Interface.
     * Parent: ConsultaAfiliadoPresenter.
     * Name: View.
     */
    interface View {
        /**
         * Type: Method.
         * Parent: View.
         * Name: onExceptionConsultaAfiliado.
         *
         * @param onExceptionResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onExceptionConsultaAfiliado(onExceptionResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onFailConsultaAfiliado.
         *
         * @param onFailResult @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun onFailConsultaAfiliado(onFailResult: String?)

        /**
         * Type: Method.
         * Parent: View.
         * Name: onSuccessConsultaAfiliado.
         *
         * @param miembro @PsiType:Miembro.
         * @Description.
         * @EndDescription.
         */
        fun onSuccessConsultaAfiliado(miembro: Miembro?)

        fun getCurrentMarca(value: String)
    }

    /**
     * Type: Interface.
     * Parent: ConsultaAfiliadoPresenter.
     * Name: Presenter.
     */
    interface Presenter {
        /**
         * Type: Method.
         * Parent: Presenter.
         * Name: setView.
         *
         * @Description.
         * @EndDescription.
         * @param: getView @PsiType:View.
         */
        fun executeRedimeCupon(numeroCupon: String?, producto: Producto)

        fun executeRedimeCupon2(numeroCupon: String?)

        fun executeVerificaCuponGRG(cupon:String)

        fun executeVerificaCuponEngachement(cupon:String)

        fun setView(getView: View?)
        fun generaCupones(cupones:List<Miembro.Response.Cupones>, marca:String):ArrayList<Miembro.Response.Cupones>
        fun generaDescuentos(cupones:List<Miembro.Response.Cupones>,marca:String):ArrayList<Miembro.Response.Cupones>

        /**
         * Type: Method.
         * Parent: Presenter.
         * Name: setPreferences.
         *
         * @Description.
         * @EndDescription.
         * @param: sharedPreferences @PsiType:SharedPreferences.
         * @param: preferenceHelper @PsiType:PreferenceHelper.
         */
        fun setPreferences(
            sharedPreferences: SharedPreferences?,
            preferenceHelper: PreferenceHelper?
        )

        /**
         * Type: Method.
         * Parent: Presenter.
         * Name: setLogsInfo.
         *
         * @Description.
         * @EndDescription.
         * @param: preferenceHelperLogs @PsiType:PreferenceHelperLogs.
         * @param: files @PsiType:Files.
         */
        fun setLogsInfo(preferenceHelperLogs: PreferenceHelperLogs?, files: Files?)

        /**
         * Type: Method.
         * Parent: Presenter.
         * Name: executeConsultaAfiliado.
         *
         * @param numeroMiembro @PsiType:String.
         * @Description.
         * @EndDescription.
         */
        fun executeConsultaAfiliado(numeroMiembro: String?)

        fun getMarca(): String
    }

    interface Model{
        fun executeConsultaAfiliadoRequest(numeroMiembro: String?)
        fun executeRedimeCuponRequest(numeroCupon: String?, producto: Producto)
        fun executeRedimeCuponRequest20(numeroCupon: String?)
        fun executeVerificaCuponGRGRequest(cupon:String)
        fun executeVerificaCuponEngachement(cupon:String)
    }
}