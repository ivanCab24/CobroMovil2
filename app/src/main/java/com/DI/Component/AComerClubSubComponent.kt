package com.DI.Component

import com.DI.Modules.AComerClubModule
import com.DI.Scopes.AComerClubScope
import com.View.Dialogs.*
import dagger.Subcomponent

@AComerClubScope
@Subcomponent(modules = [AComerClubModule::class])
interface AComerClubSubComponent {

    fun inject(dialogBuscarAfiliado: DialogBuscarAfiliado)
    fun inject(dialogRedencionDePuntos: DialogRedencionDePuntos)
    fun inject(dialogAcumularPuntos: DialogAcumularPuntos)
    fun inject(dialogRedencionCupon: DialogRedencionCupon)
    fun inject(dialogInputNipAComer: DialogInputNipAComer)
    fun inject(dialogCuponesDescuentos: DialogCuponesDescuentos)
}