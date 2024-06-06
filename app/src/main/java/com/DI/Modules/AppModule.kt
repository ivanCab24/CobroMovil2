package com.DI.Modules

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.Constants.ConstantsPreferences
import com.Constants.ConstantsPreferencesLogs
import com.DI.Scopes.AppComponentScope
import com.Utilities.Files
import com.Utilities.FilesK
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class AppModule(private val application: Application) {

    @Provides
    @AppComponentScope
    fun providesApplication(): Application = application

    @Provides
    @AppComponentScope
    fun provideApplicationContext(): Context = application.applicationContext

    /*SharedPreferences*/
    @Provides
    @AppComponentScope
    @Named("Preferencias")
    fun provideSharedPreferences(): SharedPreferences = application.getSharedPreferences(
        ConstantsPreferences.PREF_NAME,
        Context.MODE_PRIVATE
    )

    @Provides
    @AppComponentScope
    @Named("Logs")
    fun providesSharedPreferencesLogs(): SharedPreferences = application.getSharedPreferences(
        ConstantsPreferencesLogs.PREF_NAME_LOGS,
        Context.MODE_PRIVATE
    )

    @Provides
    @AppComponentScope
    fun providesFiles(): Files = Files(provideApplicationContext())

    @Provides
    @AppComponentScope
    fun providesFilesK(
        context: Context,
        preferenceHelper: PreferenceHelper, preferenceHelperLogs: PreferenceHelperLogs
    ): FilesK = FilesK(
        context.contentResolver, preferenceHelper, preferenceHelperLogs
    )

}