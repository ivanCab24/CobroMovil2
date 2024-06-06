package com.Model

import android.content.Context
import com.Constants.ConstantsPreferences
import com.Controller.BD.DAO.UserDAO
import com.Interfaces.ContentFragmentMVP
import com.Utilities.FilesK
import com.Utilities.PreferenceHelper
import com.squareup.moshi.Moshi
import com.webservicestasks.ReadJsonFeadTaskK
import com.webservicestasks.ToksWebServicesConnection
import dagger.Lazy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ContentFragmentModel @Inject constructor(
    private var presenter: Lazy<ContentFragmentMVP.Presenter>,
    private var preferenceHelper: PreferenceHelper,
    private var files: FilesK,
    private var moshi: Moshi,
    private var context: Context,
    private var userDAO: UserDAO
) : ReadJsonFeadTaskK(), ToksWebServicesConnection, ContentFragmentMVP.Model, CoroutineScope {
    private val job = Job()
    override fun getMarca(): String {
        return preferenceHelper.getString(ConstantsPreferences.PREF_MARCA_SELECCIONADA, null)
    }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO
}