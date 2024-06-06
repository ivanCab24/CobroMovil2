package com.DI.Component

import com.DI.Modules.HIstoricoPagosModule
import com.DI.Scopes.HistoricoPagosScope
import com.View.Fragments.HistoricosPagosFragment
import dagger.Subcomponent

@HistoricoPagosScope
@Subcomponent(modules = [HIstoricoPagosModule::class])
interface HistoricoPagosSubcomponent {

    fun inject(historicosPagosFragment: HistoricosPagosFragment)

}