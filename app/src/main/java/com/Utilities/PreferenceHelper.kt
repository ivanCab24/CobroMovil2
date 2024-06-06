package com.Utilities

import android.content.SharedPreferences
import com.DI.Scopes.AppComponentScope
import javax.inject.Inject
import javax.inject.Named

/**
 * Type: Class.
 * Access: Public.
 * Name: PreferenceHelper.
 *
 * @Description.
 * @EndDescription.
 */
@AppComponentScope
class PreferenceHelper
/**
 * Type: Method.
 * Parent: PreferenceHelper.
 * Name: PreferenceHelper.
 *
 * @param sharedPreferences @PsiType:SharedPreferences.
 * @Description.
 * @EndDescription.
 */
@Inject internal constructor(@Named("Preferencias") private val sharedPreferences: SharedPreferences) {
    /**
     * Type: Method.
     * Parent: PreferenceHelper.
     * Name: putString.
     *
     * @param key   @PsiType:String.
     * @param value @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    fun putString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    /**
     * Type: Method.
     * Parent: PreferenceHelper.
     * Name: getString.
     *
     * @param key @PsiType:String.
     * @return the string
     * @Description.
     * @EndDescription.
     */
    fun getString(key: String, nothing: Nothing?): String {
        return sharedPreferences.getString(key, "")!!
    }

    /**
     * Type: Method.
     * Parent: PreferenceHelper.
     * Name: putBoolean.
     *
     * @param key   @PsiType:String.
     * @param value @PsiType:boolean.
     * @Description.
     * @EndDescription.
     */
    fun putBoolean(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    /**
     * Type: Method.
     * Parent: PreferenceHelper.
     * Name: getBoolean.
     *
     * @param key @PsiType:String.
     * @return the boolean
     * @Description.
     * @EndDescription.
     */
    fun getBoolean(key: String): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

    /**
     * Type: Method.
     * Parent: PreferenceHelper.
     * Name: putInt.
     *
     * @param key   @PsiType:String.
     * @param value @PsiType:boolean.
     * @Description.
     * @EndDescription.
     */
    fun putInt(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    /**
     * Type: Method.
     * Parent: PreferenceHelper.
     * Name: getInt.
     *
     * @param key @PsiType:String.
     * @return the int
     * @Description.
     * @EndDescription.
     */
    fun getInt(key: String): Int {
        return sharedPreferences.getInt(key, -1)
    }

    /**
     * Type: Method.
     * Parent: PreferenceHelper.
     * Name: removePreference.
     *
     * @param key @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    fun removePreference(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }

    /**
     * Type: Method.
     * Parent: PreferenceHelper.
     * Name: clear.
     *
     * @Description.
     * @EndDescription.
     */
    fun clear() {
        sharedPreferences.edit().clear().apply()
    }
}