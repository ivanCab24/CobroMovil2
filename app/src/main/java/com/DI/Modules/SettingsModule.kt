package com.DI.Modules

import com.Controller.BD.DAO.CatalogoBinesDAO
import com.Controller.BD.DAO.MFPGODAO
import com.DI.Scopes.AppComponentScope
import com.Interfaces.SettingsMVP
import com.Model.DialogSettingsModel
import com.Presenter.DialogSettingsPresenter
import com.Utilities.FilesK
import com.Utilities.PreferenceHelper
import dagger.Lazy
import dagger.Module
import dagger.Provides

@Module
class SettingsModule {

    @AppComponentScope
    @Provides
    fun providesSettingsMVP_Presenter(settingsMVPModel: SettingsMVP.Model): SettingsMVP.Presenter =
        DialogSettingsPresenter(settingsMVPModel)

    @AppComponentScope
    @Provides
    fun providesSettingsMVP_Model(
        presenter: Lazy<SettingsMVP.Presenter>,
        preferenceHelper: PreferenceHelper,
        files: FilesK,
        catalogoBinesDAO: CatalogoBinesDAO,
        mfpgodao: MFPGODAO
    ): SettingsMVP.Model = DialogSettingsModel(
        presenter,
        preferenceHelper,
        files,
        catalogoBinesDAO,
        mfpgodao
    )
}