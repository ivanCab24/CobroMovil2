package com.View.Dialogs

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import com.Constants.ConstantsPreferences
import com.DI.BaseApplication
import com.Interfaces.CorreoMVP
import com.Utilities.Curl
import com.Utilities.PreferenceHelper
import com.View.Fragments.ContentFragment
import com.View.UserInteraction
import com.github.ybq.android.spinkit.style.DoubleBounce
import com.innovacion.eks.beerws.databinding.EnviaCorreoBinding
import com.webservicestasks.ToksWebServicesConnection
import java.util.regex.Pattern
import javax.inject.Inject


class DialogEnviarCorreo : DialogFragment(), CorreoMVP.View  {

    @Inject
    lateinit var presenter: CorreoMVP.Presenter
    private val TAG = "DialogCorreo"
    var binding: EnviaCorreoBinding? = null
    private var botonEnviar : Button? = null;
    private var editText : EditText?=null;
    private var botonCancelar : Button? = null;
    private var doubleBounce: DoubleBounce? = null
    private var layoud : ConstraintLayout? = null
    private var bandera = 0
    private var textoImpresion = ""

    @Inject
    lateinit var preferenceHelper: PreferenceHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = EnviaCorreoBinding.inflate(inflater, container, false)
        botonEnviar = binding!!.enviarButton
        layoud = binding!!.layoud
        botonCancelar = binding!!.cancelarButton
        editText = binding!!.montoEditText
        initPresenter()
        botonEnviar?.setOnClickListener {
            EnviaTicket(bandera)
            UserInteraction.getImpresora.dismiss()
           // presenter!!.excuteSetTicket(bandera)
        }
        botonCancelar?.setOnClickListener {
            this.dismiss()
        }
        return binding?.root
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context.applicationContext as BaseApplication).appComponent.inject(this)
        doubleBounce = DoubleBounce()
    }
    companion object{
        fun newInstance(bandera : Int): DialogEnviarCorreo = DialogEnviarCorreo().apply {
        this.bandera  = bandera
        }
    }


     @SuppressLint("SuspiciousIndentation")
     fun EnviaTicket(bandera:Int){
       val texto = binding!!.montoEditText.text?.toString()
         editText?.setText(texto)
        var folio = ""
         Log.i("BANDERA--->", bandera.toString())
        if(bandera==1){//TICKET
            folio = ContentFragment?.cuenta?.folio
        }else if(bandera==2){ //HISTORICO
            folio = ContentFragment.folioHistorico
        }else if(bandera==0){ //ERROR
            folio = ""
        }
         if(validarEmail(texto.toString()) == true){
            presenter.executeEnviaTikcet(7,folio,texto!!)
         }else{
             mostrarToast(requireContext(),"Formato de correo invalido")
         }

    }

    fun initPresenter(){
        presenter!!.setDialogV(this)
        presenter!!.setView(this)//inicializando la vista en el presenter
        presenter!!.getV()
        presenter!!.setActivityV(activity!!)
        presenter!!.setBindingV(binding!!)
    }
  override  fun mostrarToast(contexto: Context, mensaje: String) {
        Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
    }

    override fun onSuccessSend(text: String) {
        mostrarToast(requireContext(),"Correo enviado correctamente")
        this.dismiss()
        UserInteraction.getImpresora.dismiss()

    }

    override fun onFailSend(text: String) {
        UserInteraction.snackyException(activity,null,text)
       // mostrarToast(requireContext(),text)
        this.dismiss()
        UserInteraction.getImpresora.dismiss()
    }

    private fun validarEmail(email: String): Boolean {  //validacion de que el formato de correo ingresado es correcto
        val pattern: Pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }
    //funciones para ocultar el teclado
    fun View.hideKeyboard(){
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
























}