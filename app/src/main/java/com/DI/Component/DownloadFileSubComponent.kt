package com.DI.Component

import com.DI.Modules.DownloadFileModule
import com.DI.Scopes.DownloadFileScope
import com.View.Dialogs.DialogDownloadFile
import dagger.Subcomponent

@DownloadFileScope
@Subcomponent(modules = [DownloadFileModule::class])
interface DownloadFileSubComponent {

    fun inject(dialogDownloadFile: DialogDownloadFile)

}