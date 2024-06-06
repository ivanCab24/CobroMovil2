package com.Utilities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Handler
import android.util.Log
import android.view.Window

import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import com.Constants.ConstantsMarcas
import com.View.Fragments.ContentFragment
import com.innovacion.eks.beerws.BuildConfig
import com.innovacion.eks.beerws.R
import com.orhanobut.hawk.Hawk
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.Days
import java.util.*


object Utilities {
    lateinit var preferenceHelper:PreferenceHelper
    private const val TAG = "Utilities"

    var btnDis: ImageButton? = null

    fun getJulianDate():String{
        val c = Calendar.getInstance()
        val today = DateTime(Calendar.getInstance().time)
        val begining = DateTime("1900-01-01T10:23:20+0000", DateTimeZone.UTC)
        val days = Days.daysBetween(begining, today).days+2
        println(days)
        return days.toString()
    }
    fun setUpTheme(marca: String): Int {

        var theme = 0

        if (marca.isNotEmpty()) {

            when (marca) {
                ConstantsMarcas.MARCA_BEER_FACTORY -> theme = R.style.TemaBeer
                ConstantsMarcas.MARCA_TOKS -> theme = R.style.TemaToks
                ConstantsMarcas.MARCA_EL_FAROLITO -> theme = R.style.TemaFarolito
            }

        } else {

            theme = R.style.TemaToks

        }

        return theme

    }

    fun startHandler(runnable: Runnable?, handler: Handler?, timeOut: Long) {
        runnable?.let { handler?.postDelayed(it, timeOut) }
    }

    fun stopHandler(runnable: Runnable?, handler: Handler?) {
        runnable?.let { handler?.removeCallbacks(it) }
    }

    fun fullScreenActivity(window: Window) {

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

    }

    fun setUpDoubleBounce(context: Context, marca: String): Int {

        var color: Int = ContextCompat.getColor(context, R.color.whiteToks)

        when (marca) {
            ConstantsMarcas.MARCA_BEER_FACTORY -> color =
                ContextCompat.getColor(context, R.color.progressbar_color_beer)
            ConstantsMarcas.MARCA_TOKS, ConstantsMarcas.MARCA_EL_FAROLITO -> color =
                ContextCompat.getColor(context, R.color.whiteToks)
        }

        return color
    }

    fun checkScreenInfo(resources: Resources) {

        when {
            resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK == Configuration.SCREENLAYOUT_SIZE_LARGE -> {
                Log.d(TAG, "checkScreenInfo: LARGE")
            }
            resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK == Configuration.SCREENLAYOUT_SIZE_NORMAL -> {
                Log.d(TAG, "checkScreenInfo: NORMAL")
            }
            resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK == Configuration.SCREENLAYOUT_SIZE_SMALL -> {
                Log.d(TAG, "checkScreenInfo: SMALL")
            }
            resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK == Configuration.SCREENLAYOUT_SIZE_XLARGE -> {
                Log.d(TAG, "checkScreenInfo: XLARGE")
            }
            else -> {
                Log.d(TAG, "checkScreenInfo: UNKNOWN_CATEGORY_SCREEN_SIZE")
            }
        }

        val screenSize = resources.configuration.screenLayout and
                Configuration.SCREENLAYOUT_SIZE_MASK
        Log.i("Screen", screenSize.toString())

        val metrics = resources.displayMetrics

        Log.i("Screen Density", metrics.toString())

    }

    fun getVersionName(): String = BuildConfig.VERSION_NAME

    @SuppressLint("StaticFieldLeak")
    @JvmStatic
    fun disableButon(btn: ImageButton, activity: Activity): kotlinx.coroutines.Deferred<Unit> =
        GlobalScope.async {
            btn.isClickable = false
            val buttonTimer = Timer()
            buttonTimer.schedule(object : TimerTask() {
                override fun run() {
                    activity.runOnUiThread(Runnable { btn.isClickable = true })
                }
            }, 1000)
        }
}