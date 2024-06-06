/*
 * *
 *  * Created by Gerardo Ruiz on 11/11/20 5:25 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 11/11/20 5:25 PM
 *
 */
package com.View.Dialogs

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.DI.BaseApplication
import com.Utilities.Curl
import com.Utilities.PreferenceHelper
import com.Utilities.Utils
import com.Verifone.VerifoneTaskManager
import com.View.Fragments.ContentFragment
import com.View.UserInteraction
import com.innovacion.eks.beerws.R
import com.innovacion.eks.beerws.databinding.InputDialogLayoutBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.security.GeneralSecurityException
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.CoroutineContext

/**
 * Type: Class.
 * Access: Public.
 * Name: DialogTokenInput.
 *
 * @Description.
 * @EndDescription.
 */
class DialogTokenInput : DialogFragment(), CoroutineScope {

    private lateinit var binding: InputDialogLayoutBinding

    private var r: Runnable? = null
    private var handler: Handler? = null

    private var bandera = ""
    private var estafeta = ""

    private var mFragmentManager: FragmentManager? = null

    private val job = Job()

    private var token2 : String? = null

    companion object {

        private const val ARG_BANDERA = "bandera"
        private const val ARG_ESTAFETA = "estafeta"

        fun newInstance(bandera: String, estafeta: String): DialogTokenInput {
            val args = Bundle()
            args.putString(ARG_BANDERA, bandera)
            args.putString(ARG_ESTAFETA, estafeta)

            val fragment = DialogTokenInput()
            fragment.arguments = args

            return fragment
        }

    }

    @Named("Preferencias")
    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var preferenceHelper: PreferenceHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { arguments ->
            bandera = arguments.getString(ARG_BANDERA, "")
            estafeta = arguments.getString(ARG_ESTAFETA, "")
        } ?: run {
            Toast.makeText(context, "Error to instatiate DialogTokenInput", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = InputDialogLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context.applicationContext as BaseApplication).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mFragmentManager = fragmentManager
        binding.dialogMessage.text = "Token"
        binding.dialogTitle.text = "Ingrese Token"
        if(bandera!="77")
            executeToken()
        binding.aceptarButton.setOnClickListener {
            if (binding.inputEditText.text.toString() != "") {
                if (binding.inputEditText.text.toString() == getToken())
                 {
                    when (bandera) {
                        "0", "5" -> UserInteraction.showInputDialogReferencia(
                            fragmentManager,
                            ContentFragment.getDescuento!!.textReferencia,
                            bandera
                        )
                        "1" -> {
                            Utils.hideSoftKeyboard(binding.root, activity)
                            ContentFragment.contentFragment!!.cancelaDescuento()
                        }
                        "3" -> {
                            Utils.hideSoftKeyboard(binding.root, activity)
                            ContentFragment.contentFragment!!.cancelarPago()
                        }
                        "4" -> {
                            Utils.hideSoftKeyboard(binding.root, activity)
                            DialogMultipleDiscounts.dialogMultipleDiscounts.executeAplicaDescuentos()
                        }
                        "6" -> {
                            Utils.hideSoftKeyboard(binding.root, activity)
                            ContentFragment.contentFragment!!.iniciaProcesoDePago()
                        }

                        "7" -> {
                            Utils.hideSoftKeyboard(binding.root, activity)
                            UserInteraction.getDialogInputNipAComer.acceptByToken()
                        }

                        "77" -> {
                            Utils.hideSoftKeyboard(binding.root, activity)
                            UserInteraction.getSettingd.setTraining()
                        }
                    }
                    dismiss()
                    r?.let { runnable ->
                        handler?.removeCallbacks(runnable)
                    }
                } else {
                    Toast.makeText(activity, "Token expirado o incorrecto", Toast.LENGTH_LONG)
                        .show()
                }

            }
        }
        binding.cancelarButton.setOnClickListener {
            Utils.hideSoftKeyboard(binding.root, activity)
            if (bandera == "6") launch { VerifoneTaskManager.limpiarTerminal() }
            dismiss()
            r?.let { runnable ->
                handler?.removeCallbacks(runnable)
            }

        }

    }
    private fun executeToken() {
        handler = Handler(Looper.getMainLooper())
        r = Runnable {
            handler?.postDelayed({
                try {
                    val tokenGenerado = getToken()
                    Log.i("TOKEN", tokenGenerado)
                } catch (e: GeneralSecurityException) {
                    e.printStackTrace()
                }
            }, 0)
            r?.let { runnable ->
                handler?.postDelayed(runnable, 0)
            }

        }

        r?.let { runnable ->
            handler?.postDelayed(runnable, 0)
        }

    }


    fun getToken(): String{
        var params = HashMap<String, String>()
        params["Llave"] = "e936c17f7b54f02d32@@5463e0c3f749080b9"
        params["Usuario"] = arguments!!.getString(ARG_ESTAFETA).toString()
        var reponse = Curl.sendPostRequest(params, "Consulta_Token", preferenceHelper, 7)
        val jsonArray = JSONArray(reponse)
        val token = jsonArray.getJSONObject(0).getString("TOKEN")
        token2 = token
        Log.i("token2", token2.toString())
        Log.i("Response2", jsonArray.toString())
        return token2.toString()

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.let { dialog ->
            dialog.setCancelable(false)

            dialog.window?.let { window ->
                window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                window.attributes.windowAnimations = R.style.DialogTheme
            }

        }

    }
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

}