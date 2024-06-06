package com.View.Dialogs

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.View.Fragments.ContentFragment
import com.github.ybq.android.spinkit.style.DoubleBounce
import com.innovacion.eks.beerws.R
import com.innovacion.eks.beerws.databinding.VisualizadorDialogBinding

/**
 * Type: Class.
 * Access: Public.
 * Name: DialogSettings.
 *
 * @Description.
 * @EndDescription.
 */
class DialogVisualizador : DialogFragment(){

    private val TAG = "DialogSettings"

    private var binding: VisualizadorDialogBinding? = null
    private var doubleBounce: DoubleBounce? = null
    private var mListener: View.OnClickListener? = null
    private var banderaActivity: String = ""

    companion object {
        var registrosL: ArrayList<ArrayList<String>>?=null
        const val ARG_BANDERA_ACTIVITY = "banderaActivity"
        fun newInstance(registros: ArrayList<ArrayList<String>>): DialogVisualizador {
            registrosL = registros
            val args = Bundle()
            val dialog = DialogVisualizador()
            dialog.arguments = args
            return dialog
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = VisualizadorDialogBinding.inflate(inflater, container, false)
        ContentFragment.contentFragment!!.presenterVisualizador!!.setBinding(binding!!)
        ContentFragment.contentFragment!!.presenterVisualizador!!.agregarCabecera()
        ContentFragment.contentFragment!!.presenterVisualizador!!.agregarContenido(registrosL!!)
        return binding?.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        doubleBounce = DoubleBounce()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { args ->
            this.banderaActivity = args.getString(ARG_BANDERA_ACTIVITY, "")
        } ?: run {
            Toast.makeText(context, "Error instatiating DialogSettings", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        dialog?.let { it ->
            it.window?.let {
                it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                it.attributes.windowAnimations = R.style.DialogTheme
                it.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
            }
        }
    }

    override fun onDestroyView() {
        mListener = null
        doubleBounce = null
        binding = null
        super.onDestroyView()
    }

}