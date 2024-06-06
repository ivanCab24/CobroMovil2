package com.DI.Component

import com.DI.Modules.DescuentosAplicadosModule
import com.DI.Scopes.DialogDescuentosAplicadosScope
import com.View.Dialogs.DialogListaDescuentos
import dagger.Subcomponent

@DialogDescuentosAplicadosScope
@Subcomponent(modules = [DescuentosAplicadosModule::class])
interface DescuentosAplicadosSubComponent {

    fun inject(dialogListaDescuentos: DialogListaDescuentos)

}