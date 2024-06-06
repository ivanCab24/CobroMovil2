package com.DI.Modules

import android.content.Context
import com.DI.Scopes.ResetKeysScope
import com.Interfaces.ResetKeysMVP
import com.Model.DialogResetKeysModel
import com.Presenter.DialogResetKeysPresenter
import com.Utilities.FilesK
import com.Utilities.PreferenceHelper
import com.squareup.moshi.Moshi
import dagger.Lazy
import dagger.Module
import dagger.Provides

@Module
class ResetKeysModule {

    @ResetKeysScope
    @Provides
    fun providesPresenter(model: ResetKeysMVP.Model): ResetKeysMVP.Presenter =
        DialogResetKeysPresenter(model)

    @ResetKeysScope
    @Provides
    fun providesModel(
        presenter: Lazy<ResetKeysMVP.Presenter>,
        preferenceHelper: PreferenceHelper,
        files: FilesK,
        moshi: Moshi,
        context: Context
    ): ResetKeysMVP.Model = DialogResetKeysModel(presenter, preferenceHelper, files, moshi, context)

}