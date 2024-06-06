package com.DI.Component

import com.DI.Modules.DialogErrorRespuestaModule
import com.DI.Scopes.DialogErrorScope
import com.View.Dialogs.DialogErrorRespuesta
import dagger.Subcomponent

@DialogErrorScope
@Subcomponent(modules = [DialogErrorRespuestaModule::class])
interface DialogErrorRespuestaSubComponent {
    fun inject(dialogErrorRespuesta: DialogErrorRespuesta)

}