package com.Utilities

import android.content.SharedPreferences
import com.DI.Scopes.AppComponentScope
import javax.inject.Inject
import javax.inject.Named

/**
 * Type: Class.
 * Access: Public.
 * Name: PreferenceHelperLogs.
 *
 * @Description.
 * @EndDescription.
 */
@AppComponentScope
class PreferenceHelperLogs
/**
 * Type: Method.
 * Parent: PreferenceHelperLogs.
 * Name: PreferenceHelperLogs.
 *
 * @param sharedPreferencesLogs @PsiType:SharedPreferences.
 * @Description.
 * @EndDescription.
 */ @Inject internal constructor(@Named("Logs") private val sharedPreferencesLogs: SharedPreferences) {
    /**
     * Type: Method.
     * Parent: PreferenceHelperLogs.
     * Name: putString.
     *
     * @param key   @PsiType:String.
     * @param value @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    fun putString(key: String, value: String) {
        sharedPreferencesLogs.edit().putString(key, value).apply()
    }

    /**
     * Type: Method.
     * Parent: PreferenceHelperLogs.
     * Name: getString.
     *
     * @param key @PsiType:String.
     * @return the string
     * @Description.
     * @EndDescription.
     */
    fun getString(key: String): String {
        return sharedPreferencesLogs.getString(key, "")!!
    }

    /**
     * Type: Method.
     * Parent: PreferenceHelperLogs.
     * Name: putBoolean.
     *
     * @param key   @PsiType:String.
     * @param value @PsiType:boolean.
     * @Description.
     * @EndDescription.
     */
    fun putBoolean(key: String, value: Boolean) {
        sharedPreferencesLogs.edit().putBoolean(key, value).apply()
    }

    /**
     * Type: Method.
     * Parent: PreferenceHelperLogs.
     * Name: getBoolean.
     *
     * @param key @PsiType:String.
     * @return the boolean
     * @Description.
     * @EndDescription.
     */
    fun getBoolean(key: String): Boolean {
        return sharedPreferencesLogs.getBoolean(key, false)
    }

    /**
     * Type: Method.
     * Parent: PreferenceHelperLogs.
     * Name: putInt.
     *
     * @param key   @PsiType:String.
     * @param value @PsiType:boolean.
     * @Description.
     * @EndDescription.
     */
    fun putInt(key: String, value: Boolean) {
        sharedPreferencesLogs.edit().putBoolean(key, value).apply()
    }

    /**
     * Type: Method.
     * Parent: PreferenceHelperLogs.
     * Name: getInt.
     *
     * @param key @PsiType:String.
     * @return the int
     * @Description.
     * @EndDescription.
     */
    fun getInt(key: String): Int {
        return sharedPreferencesLogs.getInt(key, -1)
    }

    /**
     * Type: Method.
     * Parent: PreferenceHelperLogs.
     * Name: removePreference.
     *
     * @param key @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    fun removePreference(key: String) {
        sharedPreferencesLogs.edit().remove(key).apply()
    }

    /**
     * Type: Method.
     * Parent: PreferenceHelperLogs.
     * Name: clear.
     *
     * @Description.
     * @EndDescription.
     */
    fun clear() {
        sharedPreferencesLogs.edit().clear().apply()
    }
}