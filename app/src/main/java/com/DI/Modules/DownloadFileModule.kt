package com.DI.Modules

import com.DI.Scopes.DownloadFileScope
import com.Interfaces.DownloadMVP
import com.Model.DownloadUpdateModel
import com.Presenter.DownloadUpdatePresenter
import com.Utilities.FilesK
import com.Utilities.PreferenceHelper
import dagger.Lazy
import dagger.Module
import dagger.Provides

@Module
class DownloadFileModule {

    @DownloadFileScope
    @Provides
    fun providesDownloadFileMVP_Presenter(model: DownloadMVP.Model): DownloadMVP.Presenter =
        DownloadUpdatePresenter(model)

    @DownloadFileScope
    @Provides
    fun providesDownloadMVP_Model(
        presenter: Lazy<DownloadMVP.Presenter>,
        preferenceHelper: PreferenceHelper,
        files: FilesK
    ): DownloadMVP.Model = DownloadUpdateModel(presenter, preferenceHelper, files)

}