/*
 * *
 *  * Created by Gerardo Ruiz on 11/19/20 7:40 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 11/19/20 7:40 PM
 *
 */
package com.DI.Modules

import com.DI.Scopes.UploadFilesScope
import com.Interfaces.DialogWaitingFilesMVP
import com.Model.DialogWaitingFilesModel
import com.Presenter.DialogWaitingFilesPresenter
import com.Utilities.FilesK
import com.Utilities.PreferenceHelper
import dagger.Lazy
import dagger.Module
import dagger.Provides

@Module
class UploadFilesModule {

    @UploadFilesScope
    @Provides
    fun providesUploadFilesPresenter(model: DialogWaitingFilesMVP.Model): DialogWaitingFilesMVP.Presenter =
        DialogWaitingFilesPresenter(model)

    @UploadFilesScope
    @Provides
    fun providesUploadFilesModel(
        presenter: Lazy<DialogWaitingFilesMVP.Presenter>,
        preferenceHelper: PreferenceHelper,
        filesK: FilesK
    ): DialogWaitingFilesMVP.Model = DialogWaitingFilesModel(presenter, preferenceHelper, filesK)

}