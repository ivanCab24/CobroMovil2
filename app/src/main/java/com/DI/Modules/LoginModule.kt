package com.DI.Modules

import android.content.Context
import com.Controller.BD.DAO.UserDAO
import com.DI.Scopes.AppComponentScope
import com.Interfaces.LoginAcitivityMVP
import com.Model.LoginActivityModel
import com.Presenter.LoginActivityPresenter
import com.Utilities.FilesK
import com.Utilities.PreferenceHelper
import com.squareup.moshi.Moshi
import dagger.Lazy
import dagger.Module
import dagger.Provides

@Module
class LoginModule {
    @AppComponentScope
    @Provides
    fun providesloginactivitymvpPresenter(model: LoginAcitivityMVP.Model): LoginAcitivityMVP.Presenter =
        LoginActivityPresenter(model)

    @Provides
    fun provideLoginActivityModel(
        presenter: Lazy<LoginAcitivityMVP.Presenter>,
        preferenceHelper: PreferenceHelper,
        files: FilesK,
        moshi: Moshi,
        context: Context,
        userDAO: UserDAO
    ): LoginAcitivityMVP.Model = LoginActivityModel(
        presenter,
        preferenceHelper,
        files,
        moshi,
        context,
        userDAO
    )

}