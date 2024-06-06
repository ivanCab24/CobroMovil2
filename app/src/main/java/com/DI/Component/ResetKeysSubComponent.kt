package com.DI.Component

import com.DI.Modules.ResetKeysModule
import com.DI.Scopes.ResetKeysScope
import com.View.Dialogs.DialogWaitingResetKeys
import dagger.Subcomponent

@ResetKeysScope
@Subcomponent(modules = [ResetKeysModule::class])
interface ResetKeysSubComponent {

    fun inject(dialogWaitingResetKeys: DialogWaitingResetKeys)

}