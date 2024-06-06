package com.DI.Component

import com.DI.Modules.DescuentosMultiplesModule
import com.DI.Scopes.DescuentosMultiplesScope
import com.View.Dialogs.DialogMultipleDiscounts
import dagger.Subcomponent

@DescuentosMultiplesScope
@Subcomponent(modules = [DescuentosMultiplesModule::class])
interface DescuentosMultiplesSubComponent {

    fun inject(dialogMultipleDiscounts: DialogMultipleDiscounts)

}