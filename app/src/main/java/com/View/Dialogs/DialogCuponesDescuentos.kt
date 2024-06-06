
package com.View.Dialogs

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.Controller.Adapters.*
import com.DI.BaseApplication
import com.DataModel.Miembro
import com.DataModel.Producto
import com.Interfaces.CuponSelectionListner
import com.Interfaces.CuponesDescuentosMVP
import com.View.Fragments.ContentFragment
import com.View.UserInteraction
import com.innovacion.eks.beerws.R
import com.innovacion.eks.beerws.databinding.DialogCuponesDescuentosBinding
import com.orhanobut.hawk.Hawk
import javax.inject.Inject

/**
 * Type: Class.
 * Access: Public.
 * Name: DialogPaymentMethods.
 *
 * @Description.
 * @EndDescription.
 */
class DialogCuponesDescuentos:CuponesDescuentosMVP.View, DialogFragment(), CuponSelectionListner {
    @JvmField
    @Inject
    var presenter: CuponesDescuentosMVP.Presenter? = null
    var binding: DialogCuponesDescuentosBinding? = null
    var cuenta = ContentFragment.cuenta
    private var fragmentManager2: FragmentManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments == null)
            Toast.makeText(context, "Error instantiating DialogPaymentMethods", Toast.LENGTH_SHORT)
                .show()
    }

    /**
     * Type: Method.
     * Parent: DialogPaymentMethods.
     * Name: onCreateView.
     *
     * @param inflater           @PsiType:LayoutInflater.
     * @param container          @PsiType:ViewGroup.
     * @param savedInstanceState @PsiType:Bundle.
     * @return view
     * @Description.
     * @EndDescription. view.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogCuponesDescuentosBinding.inflate(inflater, container, false)
        dialog!!.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        sort(cuponesArray!!)
        sort(descuentosArray!!)
        initPresenter()

        for (i in descuentosAplicadpsArray!!){
            if(presenter!!.validaCupon(i)){
                if(i!!.restricciones.tipoCupon=="LOY_TDP_PRODUCTO")
                    cuponesArray!!.remove(i)
                else
                    descuentosArray!!.remove(i)
                presenter!!.generaDescuento(i)
                //items.add(cupon)
                presenter!!.procesaSeleccion()
            }
        }

        presenter!!.procesaSeleccion()

        return binding!!.root

    }

    /**
     * Type: Method.
     * Parent: DialogPaymentMethods.
     * Name: onAttach.
     *
     * @param context @PsiType:Context.
     * @Description.
     * @EndDescription.
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context.applicationContext as BaseApplication).plusAcomerClubSubComponent().inject(this)
    }

    /**
     * Type: Method.
     * Parent: DialogPaymentMethods.
     * Name: onViewCreated.
     *
     * @param view               @PsiType:View.
     * @param savedInstanceState @PsiType:Bundle.
     * @Description.
     * @EndDescription.
     */
    @SuppressLint("SuspiciousIndentation")
    //validacion de cupones
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentManager2 = fragmentManager
        binding!!.cancelarButton.setOnClickListener {
            dismiss()
            if(cuenta.saldo2>0&& ContentFragment.miembro!!.response.membresia!!.puntosNoCalificados>0&&false)
                UserInteraction.showRedencionDePuntosDialog(fragmentManager2)
        }
        binding!!.enviarButton.setOnClickListener {
            if(ContentFragment.nipA=="0000"){
                UserInteraction.showNipAComerDialog(fragmentManager2,ContentFragment.miembro!!.response.membresia!!.numeroMembresia,
                    object : DialogInputNipAComer.DialogButtonClickListener {
                        override fun onPositiveButton(nip: String?) {
                            run {
                                presenter!!.enviaCupones()
                                ContentFragment.nipA=nip!!
                                Hawk.put("membresias",ContentFragment.miembroCuentaA)
                            }
                        }

                    })
            }else
            presenter!!.enviaCupones()
        }


    }

    fun initPresenter(){
        presenter!!.setViewV(binding!!.root)
        presenter!!.setDialogV(this)
        presenter!!.setBindingV(binding!!)
        presenter!!.setActivityV(activity!!)
    }

    /**
     * Type: Method.
     * Parent: DialogPaymentMethods.
     * Name: onActivityCreated.
     *
     * @param savedInstanceState @PsiType:Bundle.
     * @Description.
     * @EndDescription.
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.window!!.attributes.windowAnimations = R.style.DialogTheme
        dialog!!.setCancelable(false)
    }

    fun sort(list: ArrayList<Miembro.Response.Cupones?>) {
        list.sortWith(Comparator { o1, o2 ->
            (o2!!.descuentoM).compareTo(
                o1!!.descuentoM
            )
        })
    }

    /**
     * Type: Method.
     * Parent: DialogPaymentMethods.
     * Name: onSelectedItem.
     *
     * @param formasDePago @PsiType:FormasDePago.
     * @Description.
     * @EndDescription.
     */


    companion object {
        var descuentosArray: ArrayList<Miembro.Response.Cupones?>? = null
        var descuentosAplicadpsArray: ArrayList<Miembro.Response.Cupones?>? = ArrayList()
        var cuponesArray: ArrayList<Miembro.Response.Cupones?>? = null
        var items= ArrayList<Miembro.Response.Cupones>()
        var cuponesAplicados = HashMap<Miembro.Response.Cupones,Producto>()
        /**
         * The constant TAG.
         */
        private const val TAG = "DialogPaymentMethods"

        /**
         * The constant ARG_FORMAS.
         */
        private const val ARG_FORMAS = "arrayDescuentos"

        /**
         * Type: Method.
         * Parent: DialogPaymentMethods.
         * Name: newInstance.
         *
         * @param arrayListFormasDePago @PsiType:ArrayList<FormasDePago>.
         * @return dialog payment methods
         * @Description.
         * @EndDescription. dialog payment methods.
        </FormasDePago> */
        fun newInstance(arrayDescuentos: ArrayList<Miembro.Response.Cupones?>?,arrayCupones: ArrayList<Miembro.Response.Cupones?>?,arrayCuponesAplicados: ArrayList<Miembro.Response.Cupones?>?): DialogCuponesDescuentos {
            val args = Bundle()
            descuentosArray = arrayDescuentos
            cuponesArray = arrayCupones
            items.removeAll(items)
            cuponesAplicados.clear()
            descuentosAplicadpsArray = arrayCuponesAplicados

            //args.putParcelableArrayList(ARG_FORMAS, arrayDescuentos)
            val fragment = DialogCuponesDescuentos()
            fragment.arguments = args

            return fragment
        }
    }

    fun nipRequired():Boolean{
        if(items.size>1)
            return  true
        for(cup in items){
            if(cup.numeroCupon!= "corporativo" && cup.numeroCupon!= "inapam")
                return true
        }
        return false
    }

    override fun onSelectedItem(cupon: Miembro.Response.Cupones?, bandera: String?) {
        if(presenter!!.validaCupon(cupon)){
            if(cupon!!.restricciones.tipoCupon=="LOY_TDP_PRODUCTO")
                cuponesArray!!.remove(cupon)
            else
                descuentosArray!!.remove(cupon)
            presenter!!.generaDescuento(cupon)
            //items.add(cupon)
            presenter!!.procesaSeleccion()
        }
    }

    fun Double.round(decimals: Int = 2): Double = "%.${decimals}f".format(this).toDouble()

    override fun executeFavorito(cupon: Miembro.Response.Cupones?) {
        Log.i("Descuento",cupon.toString())
    }
}