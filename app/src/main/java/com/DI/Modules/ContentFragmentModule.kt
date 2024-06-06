package com.DI.Modules

import android.content.Context
import com.Controller.BD.DAO.UserDAO
import com.DI.Scopes.ContentFragmentScope
import com.Interfaces.AplicaDescuentoMVP
import com.Interfaces.AplicaPagoMVP
import com.Interfaces.CajaMVP
import com.Interfaces.CancelaDescuentoMVP
import com.Interfaces.CancelarPagoMVP
import com.Interfaces.CierraCuentaMVP
import com.Interfaces.ContentFragmentMVP
import com.Interfaces.DescuentoFavoritoMVP
import com.Interfaces.DescuentosMVP
import com.Interfaces.DialogVisualizadorMVP
import com.Interfaces.FormasDePagoMPV
import com.Interfaces.InformacionLocalMVP
import com.Interfaces.InsertaCuentaCerradaMVP
import com.Interfaces.InsertaTicketMVP
import com.Interfaces.PagosMVP
import com.Interfaces.PinpadTaskMVP
import com.Model.AplicaDescuentoModel
import com.Model.AplicaPagoModel
import com.Model.CajaModel
import com.Model.CancelaDescuentoModel
import com.Model.CancelarPagoModel
import com.Model.CierraCuentaModel
import com.Model.ContentFragmentModel
import com.Model.DescuentoFavoritoModel
import com.Model.DescuentosModel
import com.Model.DialogVisualozadorModel
import com.Model.FormasDePagoModel
import com.Model.InformacionLocalModel
import com.Model.InsertCuentaCerradaModel
import com.Model.InsertaTicketModel
import com.Model.PagosModel
import com.Model.PinPadTaskModel
import com.Presenter.AplicaDescuentoPresenter
import com.Presenter.AplicaPagoPresenter
import com.Presenter.CajaPresenter
import com.Presenter.CancelaDescuentoPresenter
import com.Presenter.CancelarPagoPresenter
import com.Presenter.CierraCuentaPresenter
import com.Presenter.ContentFragmentPresenter
import com.Presenter.DescuentoFavoritoPresenter
import com.Presenter.DescuentosPresenter
import com.Presenter.DialogVisulizadorPresenter
import com.Presenter.FormasDePagoPresenter
import com.Presenter.InformacionLocalPresenter
import com.Presenter.InsertCuentaCerradaPresenter
import com.Presenter.InsertaTicketPresenter
import com.Presenter.PagosPresenter
import com.Presenter.PinpadTaskPresenter
import com.Utilities.FilesK
import com.Utilities.PreferenceHelper
import com.View.Fragments.ContentFragment
import com.squareup.moshi.Moshi
import dagger.Lazy
import dagger.Module
import dagger.Provides
import javax.inject.Inject

@Module
class ContentFragmentModule @Inject constructor(var contentFragment: ContentFragment) {

    @Provides
    @ContentFragmentScope
    fun providesCuentaPresenter(model: ContentFragmentMVP.Model): ContentFragmentMVP.Presenter =
        ContentFragmentPresenter(model)

    @Provides
    fun provideContentFragmentModel(
        presenter: Lazy<ContentFragmentMVP.Presenter>,
        preferenceHelper: PreferenceHelper,
        files: FilesK,
        moshi: Moshi,
        context: Context,
        userDAO: UserDAO
    ): ContentFragmentMVP.Model = ContentFragmentModel(
        presenter,
        preferenceHelper,
        files,
        moshi,
        context,
        userDAO
    )

    @Provides
    @ContentFragmentScope
    fun providesPagosPresenter(model: PagosMVP.Model): PagosMVP.Presenter = PagosPresenter(model)

    @Provides
    @ContentFragmentScope
    fun providesPagosModel(): PagosMVP.Model = PagosModel()


    @Provides
    @ContentFragmentScope
    fun providesCajaPresenter(model: CajaMVP.Model): CajaMVP.Presenter = CajaPresenter(model)

    @Provides
    @ContentFragmentScope
    fun providesCajaModel(): CajaMVP.Model = CajaModel()

    @Provides
    @ContentFragmentScope
    fun providesDescuentosPresenter(model: DescuentosMVP.Model): DescuentosMVP.Presenter =
        DescuentosPresenter(model)

    @Provides
    @ContentFragmentScope
    fun providesDescuentosModel(): DescuentosMVP.Model = DescuentosModel()


    @Provides
    @ContentFragmentScope
    fun providesDescuentoFavoritoPresenter(model:DescuentoFavoritoMVP.Model): DescuentoFavoritoMVP.Presenter =
        DescuentoFavoritoPresenter(model)

    @Provides
    @ContentFragmentScope
    fun providesDescuentoFavoritoModel(): DescuentoFavoritoMVP.Model = DescuentoFavoritoModel()

    @Provides
    @ContentFragmentScope
    fun providesAplicaDescuentoPresenter(model:AplicaDescuentoMVP.Model): AplicaDescuentoMVP.Presenter =
        AplicaDescuentoPresenter(model)

    @Provides
    @ContentFragmentScope
    fun providesAplicaDescuentoModel(): AplicaDescuentoMVP.Model = AplicaDescuentoModel()

    @Provides
    @ContentFragmentScope
    fun providesFormasDePagoPresenter(model: FormasDePagoMPV.Model): FormasDePagoMPV.Presenter =
        FormasDePagoPresenter(model)


    @Provides
    @ContentFragmentScope
    fun providesFormasDePagoModel(): FormasDePagoMPV.Model = FormasDePagoModel()

    @Provides
    @ContentFragmentScope
    fun providesAplicaPagoPresenter(model:AplicaPagoMVP.Model): AplicaPagoMVP.Presenter =
        AplicaPagoPresenter(model)

    @Provides
    @ContentFragmentScope
    fun providesAplicaPagoModel(): AplicaPagoMVP.Model = AplicaPagoModel()

    @Provides
    @ContentFragmentScope
    fun providesCancelarPagoPresenter(model:CancelarPagoMVP.Model): CancelarPagoMVP.Presenter = CancelarPagoPresenter(model)

    @Provides
    @ContentFragmentScope
    fun providesCancelarPagoModel(): CancelarPagoMVP.Model = CancelarPagoModel()

    @Provides
    @ContentFragmentScope
    fun providesCierraCuentaPresenter(model: CierraCuentaMVP.Model): CierraCuentaMVP.Presenter =
        CierraCuentaPresenter(model)

    @Provides
    @ContentFragmentScope
    fun cierraCuentaModel(): CierraCuentaMVP.Model = CierraCuentaModel()

    @Provides
    @ContentFragmentScope
    fun providesCancelaDescuentoPresenter(model:CancelaDescuentoMVP.Model): CancelaDescuentoMVP.Presenter =
        CancelaDescuentoPresenter(model)

    @Provides
    @ContentFragmentScope
    fun providesCancelaDescuentoModel(): CancelaDescuentoMVP.Model = CancelaDescuentoModel()

    @Provides
    @ContentFragmentScope
    fun providesPinpadTaskPresenter(model:PinpadTaskMVP.Model): PinpadTaskMVP.Presenter = PinpadTaskPresenter(model)

    @Provides
    @ContentFragmentScope
    fun providesPinpadTaskModel(): PinpadTaskMVP.Model = PinPadTaskModel()

    @Provides
    @ContentFragmentScope
    fun providesInsertaTicketPresenter(model:InsertaTicketMVP.Model): InsertaTicketMVP.Presenter = InsertaTicketPresenter(model)

    @Provides
    @ContentFragmentScope
    fun providesInsertaTicketModel(): InsertaTicketMVP.Model = InsertaTicketModel()

    @Provides
    @ContentFragmentScope
    fun providesCuentaCerradaPresenter(model:InsertaCuentaCerradaMVP.Model): InsertaCuentaCerradaMVP.Presenter =
        InsertCuentaCerradaPresenter(model)

    @Provides
    @ContentFragmentScope
    fun providesCuentaCerradaModel(): InsertaCuentaCerradaMVP.Model = InsertCuentaCerradaModel()

    @Provides
    @ContentFragmentScope
    fun providesInformacionLocalPresenter(model:InformacionLocalMVP.Model): InformacionLocalMVP.Presenter =
        InformacionLocalPresenter(model)
    @Provides
    @ContentFragmentScope
    fun providesInformacionLocalModel(): InformacionLocalMVP.Model = InformacionLocalModel()

    @Provides
    @ContentFragmentScope
    fun providesVisualizadorPresenter(model:DialogVisualizadorMVP.Model): DialogVisualizadorMVP.Presenter =
        DialogVisulizadorPresenter(model)
    @Provides
    @ContentFragmentScope
    fun providesVisualizadorModel(): DialogVisualizadorMVP.Model = DialogVisualozadorModel()

}