/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Utilities.Seguridad

import com.Constants.ConstantsPreferences
import com.Utilities.PreferenceHelper
import java.security.GeneralSecurityException
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.experimental.and

/**
 * Type: Class.
 * Access: Public.
 * Name: TOTP.
 *
 * @Description.
 * @EndDescription.
 */
object TOTP {
    /**
     * The constant Tiempo.
     */
    const val Tiempo = 30
    private var blockOfZeros: String? = null

    /**
     * Type: Method.
     * Parent: TOTP.
     * Name: generaNumeroString.
     *
     * @param base32Secret     @PsiType:String.
     * @param preferenceHelper @PsiType:PreferenceHelper.
     * @return string
     * @throws GeneralSecurityException the general security exception
     * @Description.
     * @EndDescription. string.
     */
    @JvmStatic
    @Throws(GeneralSecurityException::class)
    fun generaNumeroString(base32Secret: String, preferenceHelper: PreferenceHelper): String {
        val a = generaNumero(base32Secret, System.currentTimeMillis(), Tiempo)
        val ret = zeroPrepend(a)
        preferenceHelper.putString(ConstantsPreferences.PREF_TOKEN, ret)

        //String ret = String.valueOf(a);
        return ret
    }

    /**
     * Type: Method.
     * Parent: TOTP.
     * Name: zeroPrepend.
     *
     * @param num @PsiType:long.
     * @return string
     * @Description.
     * @EndDescription. string.
     */
    fun zeroPrepend(num: Long): String {
        val numStr = java.lang.Long.toString(num)
        return if (numStr.length >= 4) {
            numStr
        } else {
            val sb = StringBuilder(4)
            val zeroCount = 4 - numStr.length
            sb.append(blockOfZeros, 0, zeroCount)
            sb.append(numStr)
            sb.toString()
        }
    }

    /**
     * Type: Method.
     * Parent: TOTP.
     * Name: generaNumero.
     *
     * @param secret          @PsiType:String.
     * @param timeMillis      @PsiType:long.
     * @param timeStepSeconds @PsiType:int.
     * @return long
     * @throws GeneralSecurityException the general security exception
     * @Description.
     * @EndDescription. long.
     */
    @Throws(GeneralSecurityException::class)
    fun generaNumero(secret: String, timeMillis: Long, timeStepSeconds: Int): Long {
        val key = secret.toByteArray()
        val data = ByteArray(8)
        var value = timeMillis / 1000 / timeStepSeconds
        run {
            var i = 7
            while (value > 0) {
                data[i] = (value and 0xFF).toByte()
                value = value shr 8
                i--
            }
        }
        val signKey = SecretKeySpec(key, "HmacSHA1")
        val mac = Mac.getInstance("HmacSHA1")
        mac.init(signKey)
        println()
        val hash = mac.doFinal(data)
        val offset: Byte = hash[hash.size - 1] and 0xF
        var truncatedHash: Long = 0
        for (i in offset until offset + 4) {
            truncatedHash = truncatedHash shl 8
            truncatedHash = truncatedHash or ((hash[i] and 0xFF.toByte()).toLong())
        }
        truncatedHash = truncatedHash and 0x7FFFFF
        truncatedHash %= 10000
        return truncatedHash
    }

    init {
        var chars = CharArray(4)

        for (i in chars.indices) {
            chars[i] = '0'
        }
        blockOfZeros = String(chars)
    }
}