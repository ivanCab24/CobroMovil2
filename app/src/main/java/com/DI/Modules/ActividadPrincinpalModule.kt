package com.DI.Modules

import android.content.Context
import com.Controller.BD.DAO.UserDAO
import com.DI.Scopes.AppComponentScope
import com.Interfaces.ActividadPrincipalMVP
import com.Model.ActividadPrincipalModel
import com.Presenter.ActividadPrincipalPresenter
import com.Utilities.FilesK
import com.Utilities.PreferenceHelper
import com.squareup.moshi.Moshi
import dagger.Lazy
import dagger.Module
import dagger.Provides

@Module
class ActividadPrincinpalModule {
    @AppComponentScope
    @Provides
    fun providesactividadPrincipalMVPPresenter(model: ActividadPrincipalMVP.Model): ActividadPrincipalMVP.Presenter =
        ActividadPrincipalPresenter(model)

    @Provides
    fun provideActividadPrinciaplModel(
        presenter: Lazy<ActividadPrincipalMVP.Presenter>,
        preferenceHelper: PreferenceHelper,
        files: FilesK,
        moshi: Moshi,
        context: Context,
        userDAO: UserDAO
    ): ActividadPrincipalMVP.Model = ActividadPrincipalModel(
        presenter,
        preferenceHelper,
        files,
        moshi,
        context,
        userDAO
    )

}