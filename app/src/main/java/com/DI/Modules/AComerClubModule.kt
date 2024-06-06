package com.DI.Modules

import com.DI.Scopes.AComerClubScope
import com.Interfaces.AcumularPuntosMVP
import com.Interfaces.AplicaDescuentoPuntosMVP
import com.Interfaces.ConsultaAfiliadoMVP
import com.Interfaces.CuponesDescuentosMVP
import com.Interfaces.DialogNipMVP
import com.Interfaces.RedencionCuponMVP
import com.Interfaces.RedencionDePuntosMVP
import com.Model.AcumularPuntosModel
import com.Model.AplicaDescuentoPuntosModel
import com.Model.ConsultaAfiliadoModel
import com.Model.CuponesDescuentosModel
import com.Model.DialogNipModel
import com.Model.RedencionCuponModel
import com.Model.RedencionPuntosModel
import com.Presenter.AcumularPuntosPresenter
import com.Presenter.AplicaDescuentoPuntosPresenter
import com.Presenter.ConsultaAfiliadoPresenter
import com.Presenter.CuponesDescuentosPresenter
import com.Presenter.DialogNipPresenter
import com.Presenter.RedencionCuponPresenter
import com.Presenter.RedencionDePuntosPresenter
import com.Utilities.FilesK
import com.Utilities.PreferenceHelper
import dagger.Lazy
import dagger.Module
import dagger.Provides

@Module
class AComerClubModule {

    @Provides
    @AComerClubScope
    fun providesConsultaAfiliadoPresenter(
        model:ConsultaAfiliadoMVP.Model): ConsultaAfiliadoMVP.Presenter =
        ConsultaAfiliadoPresenter(model)

    @Provides
    @AComerClubScope
    fun providesConsultaAfiliadoModel(): ConsultaAfiliadoMVP.Model =
        ConsultaAfiliadoModel()

    @Provides
    @AComerClubScope
    fun providesRedencionDePuntosPresenter(
        model: RedencionDePuntosMVP.Model): RedencionDePuntosMVP.Presenter =
        RedencionDePuntosPresenter(model)

    @Provides
    @AComerClubScope
    fun providesAplicaDescuentoPuntosPresenter(model: AplicaDescuentoPuntosMVP.Model): AplicaDescuentoPuntosMVP.Presenter =
        AplicaDescuentoPuntosPresenter(model)

    @Provides
    @AComerClubScope
    fun aplicaDescuentoPuntosRequestModel(): AplicaDescuentoPuntosMVP.Model =
        AplicaDescuentoPuntosModel()


    @Provides
    @AComerClubScope
    fun providesAcumularPuntosPresenter(model:AcumularPuntosMVP.Model): AcumularPuntosMVP.Presenter =
        AcumularPuntosPresenter(model)

    @Provides
    @AComerClubScope
    fun providesAcumularPuntosModel(): AcumularPuntosMVP.Model =
        AcumularPuntosModel()

    @Provides
    @AComerClubScope
    fun providesRedencionCuponPresenter(model:RedencionCuponMVP.Model): RedencionCuponMVP.Presenter =
        RedencionCuponPresenter(model)

    @Provides
    @AComerClubScope
    fun providesRedencionCuponModel(): RedencionCuponMVP.Model = RedencionCuponModel()

    @Provides
    @AComerClubScope
    fun providesRedencionPuntosModel(): RedencionDePuntosMVP.Model = RedencionPuntosModel()

    @Provides
    @AComerClubScope
    fun providesDialogNipPresenter(model: DialogNipMVP.Model): DialogNipMVP.Presenter =
        DialogNipPresenter(model)

    @Provides
    @AComerClubScope
    fun providesDialogNipModel(
        presenter: Lazy<DialogNipMVP.Presenter>,
        preferenceHelper: PreferenceHelper,
        files: FilesK
    ): DialogNipMVP.Model = DialogNipModel(presenter, preferenceHelper, files)

    @Provides
    @AComerClubScope
    fun providesCuponesDescuentosPresenter(model:CuponesDescuentosMVP.Model): CuponesDescuentosMVP.Presenter =
        CuponesDescuentosPresenter(model)

    @Provides
    @AComerClubScope
    fun providesRCuponesDescuentosModel(): CuponesDescuentosMVP.Model = CuponesDescuentosModel()

}