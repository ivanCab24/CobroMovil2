/*
 * *
 *  * Created by Gerardo Ruiz on 11/19/20 7:39 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 11/19/20 7:39 PM
 *
 */
package com.DI.Component

import com.DI.Modules.UploadFilesModule
import com.DI.Scopes.UploadFilesScope
import com.View.Dialogs.DialogWaitingFiles
import dagger.Subcomponent

@UploadFilesScope
@Subcomponent(modules = [UploadFilesModule::class])
interface UploadFilesSubComponent {

    fun inject(dialogWaitingFiles: DialogWaitingFiles)

}