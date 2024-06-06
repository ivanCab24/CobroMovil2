package com.View.Dialogs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.DialogFragment
import com.DI.BaseApplication
import com.Interfaces.SeleccionImpresoraMVP
import com.View.Fragments.ContentFragment
import com.View.UserInteraction
import com.github.ybq.android.spinkit.style.DoubleBounce
import com.innovacion.eks.beerws.R
import com.innovacion.eks.beerws.databinding.SeleccionDeImpresoraBinding
import org.json.JSONArray
import javax.inject.Inject

class DialogImpresora : DialogFragment(), SeleccionImpresoraMVP.View{

    @Inject
    lateinit var presenter: SeleccionImpresoraMVP.Presenter
    private val TAG = "DialogImpresora"
    var binding: SeleccionDeImpresoraBinding? = null
    private var spinnerPrinter : Spinner? = null;
    private var botonAceptar : Button? = null;
    private var botonCancelar : Button? = null
    private var botonEnviar : Button? = null;
    private var doubleBounce: DoubleBounce? = null
    lateinit var jsonArrayImpresora : JSONArray
    private var textoImpresion = ""
    private var bandera = 0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = SeleccionDeImpresoraBinding.inflate(inflater, container, false) //SettingsDialogLayoutBinding
        spinnerPrinter = binding!!.spinnerImpresora
        initPresenter()
        botonAceptar = binding!!.cancelarButton
        botonEnviar = binding!!.enviarButton
        botonEnviar?.setOnClickListener {
            UserInteraction.showEnviaCorreoDialog(fragmentManager, bandera)
        }
        botonAceptar?.setOnClickListener{
            onSelectPrint(spinnerPrinter?.selectedItem.toString());
            // onResultPrint()
        }
        botonCancelar = binding!!.cancelar2
        botonCancelar?.setOnClickListener {
            this.dismiss()
        }


        presenter!!.excutegetPrinters()
        return binding?.root
    }

    fun initPresenter(){
        presenter!!.setDialogV(this)
        presenter!!.setView(this)
        presenter!!.getV()
        presenter!!.setActivityV(activity!!)
        presenter!!.setBindingV(binding!!)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context.applicationContext as BaseApplication).appComponent.inject(this)
        doubleBounce = DoubleBounce()
    }

    companion object{
        fun newInstance(textoImpresion: String, bandera:Int): DialogImpresora = DialogImpresora().apply {
            this.textoImpresion = textoImpresion
            this.bandera = bandera
        }
    }

  override  fun onSelectPrint(id_impresora : String) {
      var id = "";
      for (i in 0 until jsonArrayImpresora.length()){
           val imp = jsonArrayImpresora.getJSONObject(i)
          if(imp.getString("PRINTER")==id_impresora)
              id = imp.getString("ID_PRINTER")
      }
      presenter!!.excuteSetPrinte(id, textoImpresion, bandera)
    }

    override fun onSuccesGetPrint(impresoras: JSONArray) {
        spinnerPrinter = binding!!.spinnerImpresora
        this.jsonArrayImpresora = impresoras
        val adapter = ArrayAdapter<String>(requireContext(), R.layout.text1)
        for (i in 0 until impresoras.length()) {
            val jsonObject = impresoras.getJSONObject(i)
            val printer = jsonObject.getString("PRINTER")
            val id_printer = jsonObject.getString("ID_PRINTER")
            //adapter.add(id_printer)
            adapter.add(printer)
        }
        adapter.setDropDownViewResource(R.layout.text1)
        spinnerPrinter!!.setAdapter(adapter)

    }

    override fun dismiss() {
       super.dismiss()
        if(!ContentFragment.contentFragment!!.copiaprinted) {
            ContentFragment.contentFragment!!.conteoImpresiones = 0
            ContentFragment.contentFragment?.presenter?.buscarMesa()
        }
    }

    override fun onResultPrint(result: String) {
        UserInteraction.snackyWarning(activity, null,  result)
        UserInteraction.getImpresora.dismiss()
        UserInteraction.getDialogImmprimiendo?.dismiss()

        val texto = ArrayList<String>()
        texto.add(textoImpresion)


        if (ContentFragment.contentFragment!!.conteoImpresiones < 2) {
            ContentFragment.contentFragment!!.copiaprinted = true
            ContentFragment.contentFragment!!.conteoImpresiones++
            UserInteraction.showImprimeCopiaClienteDialog(fragmentManager, "99", texto, bandera)

        }

    }



}